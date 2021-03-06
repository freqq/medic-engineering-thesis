package com.pwit.messagesservice.controller;

import com.pwit.common.utils.Logger;
import com.pwit.messagesservice.entity.ChatMessage;
import com.pwit.messagesservice.entity.requests.ChatMessageItem;
import com.pwit.messagesservice.entity.requests.ChatMessageRequest;
import com.pwit.messagesservice.service.MessagesService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.pwit.common.security.Authorities.ROLE_USER;
import static com.pwit.common.security.SecurityUtils.getCurrentUserId;

@AllArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class MessagesController {
    private static final Logger LOGGER = new Logger();

    private final MessagesService messagesService;

    /**
     * Sending private message user-to-user
     *
     * @param chatMessageRequest Body of message to send
     */
    @MessageMapping("/private.chat.{channelId}")
    @SendTo("/topic/private.chat.{channelId}")
    public ChatMessage sendPrivateMessage(@Payload ChatMessageRequest chatMessageRequest) {
        LOGGER.info("Sending private message with content: {}.", chatMessageRequest.getContent());
        return messagesService.sendPrivateMessage(chatMessageRequest);
    }

    /**
     * Getting all messages saved for current user
     */
    @GetMapping(value="/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(ROLE_USER)
    public List<ChatMessageItem> getMessagesList() {
        LOGGER.info("Getting messages list for user {}.", getCurrentUserId());
        return messagesService.getMessagesList( getCurrentUserId());
    }

    /**
     * Getting all messages saved for current user with user of given id
     */
    @GetMapping(value="/history", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(ROLE_USER)
    public List<ChatMessage> getChatHistoryWithUser(@RequestParam(value = "userId") String userId) {
        LOGGER.info("Getting chat history for users {} and {}.", userId, getCurrentUserId());
        return messagesService.getChatHistoryWithUser(getCurrentUserId(), userId);
    }

    /**
     * Getting count of unread messages for current user
     */
    @GetMapping(value="/count", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(ROLE_USER)
    public Integer countUnreadMessages() {
        LOGGER.info("Counting unread messages by user {}.", getCurrentUserId());
        return messagesService.countUnreadMessages(getCurrentUserId());
    }

    /**
     * Marking all messages with given user as read
     *
     * @param userId Id of user to mark all messages as read
     */
    @GetMapping(value="/read", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(ROLE_USER)
    public List<ChatMessage> markMessagesWithUserAsRead(@RequestParam(value = "userId") String userId) {
        LOGGER.info("Marking messages as read by user {} with user {}.", getCurrentUserId(), userId);
        return messagesService.markMessagesWithUserAsRead(getCurrentUserId(), userId);
    }
}
