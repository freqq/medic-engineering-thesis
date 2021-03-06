package com.pwit.appointmentsservice.feign.account;

import com.pwit.appointmentsservice.dto.user.User;
import com.pwit.appointmentsservice.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(
    name = "account-service-svc",
    url = "https://account-service-svc:8445",
    configuration = FeignConfiguration.class,
    fallback = AccountServiceFallback.class
)
public interface AccountService {
    @PutMapping("/api/users/diagnose")
    ResponseEntity<?> setInitialDiagnoseDone();

    @GetMapping("/api/users/details/{userId}")
    User getDetailsOfUserWithGivenId(@PathVariable("userId") String userId);
}
