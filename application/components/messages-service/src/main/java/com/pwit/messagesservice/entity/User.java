package com.pwit.messagesservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pwit.messagesservice.entity.enumeration.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("birthDate")
    private LocalDate birthDate;

    @JsonProperty("gender")
    private Gender gender;

    @JsonProperty("langKey")
    private String langKey;
}
