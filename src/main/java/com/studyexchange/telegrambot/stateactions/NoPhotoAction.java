package com.studyexchange.telegrambot.stateactions;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.service.UserService;
import com.studyexchange.telegramapiutils.PhotoUtils;

import static com.studyexchange.service.UserService.checkUserNotNullOrThrow;

public class NoPhotoAction extends BaseStateAction {
    public NoPhotoAction(
            TelegramBot bot,
            UserService userService
    ) {
        super(bot, userService);
    }

    @Override
    public void setupStateAndAskQuestions(long chatId) {
        User user = userService.findUserByChatId(chatId);
        checkUserNotNullOrThrow(user, UserState.NO_PHOTO);
        userService.updateUser(user, u -> u.setUserState(UserState.NO_PHOTO));

        bot.execute(new SendMessage(chatId, "Отправьте фотографию для вашего профиля"));
    }

    @Override
    public UserState processAnswerAndReturnNextStateToSetup(Update update) {
        long chatId = update.message().chat().id();
        PhotoSize[] photos = update.message().photo();
        if (photos == null || photos.length == 0) {
            return null;
        }
        PhotoSize bestPhoto = PhotoUtils.pickBestPhoto(photos);
        User user = userService.findUserByChatId(chatId);
        userService.updateUser(user, u -> u.setPhotoFileId(bestPhoto.fileId()));

        return UserState.ENTERS_QUERY;
    }
}
