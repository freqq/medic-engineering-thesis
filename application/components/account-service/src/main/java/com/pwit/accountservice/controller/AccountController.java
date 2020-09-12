package com.pwit.accountservice.controller;

import com.pwit.accountservice.dto.UserDetailsChangeDTO;
import com.pwit.accountservice.dto.request.RegisterRequest;
import com.pwit.accountservice.service.AccountService;
import com.pwit.accountservice.utils.Logger;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.pwit.accountservice.security.Authorities.ROLE_USER;
import static com.pwit.accountservice.security.SecurityUtils.getCurrentUserEmail;
import static com.pwit.accountservice.security.SecurityUtils.getCurrentUsername;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class AccountController {
    private static final Logger LOGGER = new Logger();

    private final AccountService accountService;

    /**
     * Creates a new user account.
     *
     * @param registerRequest      Model of a new user request
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createAccount(@Valid @RequestBody RegisterRequest registerRequest) {
        LOGGER.info("Creating new user with username {}.", getCurrentUsername());
        return accountService.createAccount(registerRequest);
    }

    /**
     * Gets info about currently logged in user.
     */
    @Secured(ROLE_USER)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getCurrentUserData() {
        LOGGER.info("Getting info of user with email '{}'.", getCurrentUserEmail());
        return accountService.getCurrentUserData();
    }

    /**
     * Update current user data.
     *
     * @param userDetailsChangeDTO      Update user request
     */
    @Secured(ROLE_USER)
    @PutMapping()
    ResponseEntity<?> updateCurrentUserDetails(@Valid @RequestBody UserDetailsChangeDTO userDetailsChangeDTO){
        LOGGER.info("Updating details of user with email '{}'", getCurrentUserEmail());
        return accountService.updateCurrentUserDetails(userDetailsChangeDTO);
    }
}