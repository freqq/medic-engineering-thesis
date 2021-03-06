package com.pwit.messagesservice.entity.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;
import com.pwit.messagesservice.entity.ChatType;
import com.pwit.messagesservice.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageRequest {
    @Nullable
    @JsonProperty("type")
    private ChatType type;

    @Nullable
    @JsonProperty("content")
    private String content;

    @Nullable
    @JsonProperty("sender")
    private User sender;

    @Nullable
    @JsonProperty("receiver")
    private User receiver;

    @Nullable
    @JsonProperty("read")
    private boolean read;

    @Nullable
    @JsonProperty("dateTime")
    private LocalDateTime dateTime;
}
