package com.studyexchange.telegrambot.stateactions.educational;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import com.studyexchange.core.HelpRequest;
import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.service.HelpRequestService;
import com.studyexchange.service.UserService;
import com.studyexchange.telegrambot.stateactions.BaseStateAction;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.studyexchange.service.HelpRequestService.checkHelpRequestNotNullOrThrow;
import static com.studyexchange.service.UserService.checkUserNotNullOrThrow;
import static com.studyexchange.telegramapiutils.MessagingUtils.NEXT_LINE;
import static com.studyexchange.telegramapiutils.PhotoUtils.pickBestPhoto;

public class FillHelpPhotosEducational extends BaseStateAction {
    private static final String ASK_PHOTO_TEXT = ""
        + "Можешь прикрепить фотографию, полезную для твоей просьбы о помощи";

    private static final String LEAVE_WITHOUT_PHOTO = ""
        + "Оставить без фото";

    private static final String EMPTY_PHOTO_TEXT = ""
        + "Прикрепи фото, полезное для твоей задачи или нажми кнопку "
        + "\"" + LEAVE_WITHOUT_PHOTO + "\"";

    private static final String FORM_COMPLETED_NOTIFY_TEXT = ""
        + "Ура! Мы составили нашу первую просьбу о помощи:)"
        + NEXT_LINE
        + "Твоя просьба о помощи сейчас выглядит так:";

    private static final ReplyKeyboardMarkup NO_PHOTO_KEYBOARD =
        new ReplyKeyboardMarkup(LEAVE_WITHOUT_PHOTO)
            .oneTimeKeyboard(true)
            .resizeKeyboard(true);


    private final HelpRequestService helpRequestService;

    public FillHelpPhotosEducational(
        TelegramBot bot,
        UserService userService,
        HelpRequestService helpRequestService
    ) {
        super(bot, userService);
        this.helpRequestService = helpRequestService;
    }

    @Override
    public int setupStateAndAskQuestions(long chatId) {
        User user = userService.findUserByChatId(chatId);
        checkUserNotNullOrThrow(user, UserState.FILL_HELP_PHOTOS_EDUCATIONAL);
        userService.updateUser(user, u -> u.setUserState(UserState.FILL_HELP_PHOTOS_EDUCATIONAL));

        HelpRequest lastHelpRequest = helpRequestService.findLastHelpRequestByChatId(chatId);
        checkHelpRequestNotNullOrThrow(lastHelpRequest, UserState.FILL_HELP_PHOTOS_EDUCATIONAL);

        SendResponse questionMessage = bot.execute(
            new SendMessage(chatId, ASK_PHOTO_TEXT)
                .replyMarkup(NO_PHOTO_KEYBOARD)
        );
        return questionMessage.message().date();
    }

    @Override
    public Optional<UserState> processAnswerAndReturnNextStateToSetup(Update update) {
        if (update.message() == null) {
            return Optional.empty();
        }
        long chatId = update.message().chat().id();
        String answer = update.message().text();

        HelpRequest lastHelpRequest = helpRequestService.findLastHelpRequestByChatId(chatId);
        checkHelpRequestNotNullOrThrow(lastHelpRequest, UserState.FILL_HELP_PHOTOS_EDUCATIONAL);

        String photoId = null;

        if (!Objects.equals(answer, LEAVE_WITHOUT_PHOTO)) {
            PhotoSize[] photo = update.message().photo();
            if (photo == null || photo.length == 0) {
                bot.execute(new SendMessage(chatId, EMPTY_PHOTO_TEXT)
                    .replyMarkup(NO_PHOTO_KEYBOARD)
                );
                return Optional.empty();
            }
            PhotoSize bestPhoto = pickBestPhoto(photo);
            photoId = bestPhoto.fileId();

            helpRequestService.updateHelpRequest(lastHelpRequest, r -> r.setPhotoFileIds(
                List.of(bestPhoto.fileId())
            ));
        }

        bot.execute(new SendMessage(chatId, FORM_COMPLETED_NOTIFY_TEXT));

        User user = userService.findUserByChatId(chatId);
        checkUserNotNullOrThrow(user, UserState.FILL_HELP_PHOTOS_EDUCATIONAL);

        String helpRequestFormText = lastHelpRequest.helpRequestFormText(user.getName(), user.getGrade());
        if (photoId == null) {
            bot.execute(
                new SendMessage(chatId, helpRequestFormText)
                    .parseMode(ParseMode.MarkdownV2)
            );
        } else {
            bot.execute(
                new SendPhoto(chatId, photoId)
                    .caption(helpRequestFormText)
                    .parseMode(ParseMode.MarkdownV2)
            );
        }

        return Optional.of(UserState.READY_TO_SEARCH_EDUCATIONAL);
    }
}
