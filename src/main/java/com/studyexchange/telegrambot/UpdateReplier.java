package com.studyexchange.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.studyexchange.core.User;
import com.studyexchange.core.UserState;
import com.studyexchange.service.UserService;
import com.studyexchange.telegrambot.stateactions.StateActionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UpdateReplier {
    private static final Logger log = LogManager.getLogger(UpdateReplier.class);
    private static final String ERROR_MESSAGE = ""
        + "К сожалению, во время обработки сообщения возникла ошибка. "
        + "Попробуй отправить сообщение еще раз";

    private static final String TOO_FAST_ANSWER = ""
        + "Ответ пришел почти одновременно с новым вопросом, пожалуйста, введи ответ еще раз:)";

    private final UserService userService;
    private final StateActionFactory stateActionFactory;
    private final TelegramBot bot;

    public UpdateReplier(
        UserService userService,
        StateActionFactory stateActionFactory,
        TelegramBot bot
    ) {
        this.userService = userService;
        this.stateActionFactory = stateActionFactory;
        this.bot = bot;
    }

    public void replyUpdate(Update update) {
        log.info(update);

        Long chatId = retrieveChatId(update);
        if (chatId == null) {
            return;
        }
        try {
            User user = userService.findUserByChatId(chatId);
            if (user == null) {
                int botReplyDate = stateActionFactory.stateActionFrom(UserState.REQUEST_NAME)
                    .setupStateAndAskQuestions(chatId);
                userService.updateUser(chatId, u -> u.setLastBotAnswerDate(botReplyDate));
                return;
            }

            if (update.message() != null && user.getLastBotAnswerDate() >= update.message().date()) {
                bot.execute(new SendMessage(chatId, TOO_FAST_ANSWER));

                log.warn("UpdateId {} is ignored", update.updateId());
                return;
            }

            Optional<UserState> nextUserStateToSetup = stateActionFactory.stateActionFrom(user.getUserState())
                .processAnswerAndReturnNextStateToSetup(update);
            if (nextUserStateToSetup.isPresent()) {
                int botReplyDate = stateActionFactory.stateActionFrom(nextUserStateToSetup.get())
                    .setupStateAndAskQuestions(chatId);
                userService.updateUser(chatId, u -> u.setLastBotAnswerDate(botReplyDate));
            }
        } catch (Exception ex) {
            log.error("chatId: {}, ex: {}", chatId, ex.getMessage());
            bot.execute(new SendMessage(chatId, ERROR_MESSAGE));
        }
    }

    private Long retrieveChatId(Update update) {
        if (update.message() != null) {
            return update.message().chat().id();
        }
        if (update.callbackQuery() != null) {
            return update.callbackQuery().message().chat().id();
        }
        return null;
    }
}
