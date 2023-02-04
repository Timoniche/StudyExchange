package com.studyexchange.telegrambot.stateactions.educational;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.studyexchange.core.HelpRequest;
import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.service.HelpRequestService;
import com.studyexchange.service.UserService;
import com.studyexchange.telegrambot.stateactions.BaseStateAction;

import java.util.List;

import static com.studyexchange.service.HelpRequestService.checkHelpRequestNotNullOrThrow;
import static com.studyexchange.service.UserService.checkUserNotNullOrThrow;
import static com.studyexchange.telegramapiutils.PhotoUtils.pickBestPhoto;

public class FillHelpPhotosEducational extends BaseStateAction {
    private static final String ASK_PHOTO_TEXT = ""
        + "Можешь прикрепить фотографию, полезную для твоей просьбы о помощи";

    private static final String EMPTY_PHOTO_TEXT = ""
        + "Прикрепи фото, полезное для твоей задачи или нажми кнопку...";

    private static String helpRequestForm(HelpRequest helpRequest) {
        return ""
            + helpRequest.getSubject().toHashtag()
            + NEXT_LINE
            + NEXT_LINE
            + helpRequest.getDescription();
    }

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
    public void setupStateAndAskQuestions(long chatId) {
        User user = userService.findUserByChatId(chatId);
        checkUserNotNullOrThrow(user, UserState.FILL_HELP_PHOTOS_EDUCATIONAL);
        userService.updateUser(user, u -> u.setUserState(UserState.FILL_HELP_PHOTOS_EDUCATIONAL));

        HelpRequest lastHelpRequest = helpRequestService.findLastHelpRequestByChatId(chatId);
        checkHelpRequestNotNullOrThrow(lastHelpRequest, UserState.FILL_HELP_PHOTOS_EDUCATIONAL);

        bot.execute(new SendMessage(chatId, ASK_PHOTO_TEXT));
    }

    @Override
    public UserState processAnswerAndReturnNextStateToSetup(Update update) {
        long chatId = update.message().chat().id();
        // sendMediaGroup will crash the code (send as different updates), need to write timestamps
        // to take only 1 update per 1 question
        PhotoSize[] photo = update.message().photo();
        if (photo == null || photo.length == 0) {
            bot.execute(new SendMessage(chatId, EMPTY_PHOTO_TEXT));
            return null;
        }
        PhotoSize bestPhoto = pickBestPhoto(photo);

        HelpRequest lastHelpRequest = helpRequestService.findLastHelpRequestByChatId(chatId);
        checkHelpRequestNotNullOrThrow(lastHelpRequest, UserState.FILL_HELP_PHOTOS_EDUCATIONAL);

        helpRequestService.updateHelpRequest(lastHelpRequest, r -> r.setPhotoFileIds(
            List.of(bestPhoto.fileId())
        ));

        bot.execute(
            new SendPhoto(chatId, bestPhoto.fileId())
                .caption(helpRequestForm(lastHelpRequest))
        );
        return UserState.FILL_HELP_PHOTOS_EDUCATIONAL;
    }
}
