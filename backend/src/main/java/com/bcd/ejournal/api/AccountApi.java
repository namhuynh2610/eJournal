package com.bcd.ejournal.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bcd.ejournal.configuration.jwt.payload.AccountJWTPayload;
import com.bcd.ejournal.domain.dto.request.AccountChangeForgotPassword;
import com.bcd.ejournal.domain.dto.request.AccountChangePasswordRequest;
import com.bcd.ejournal.domain.dto.request.AccountEmailVerify;
import com.bcd.ejournal.domain.dto.request.AccountUpdateProfileRequest;
import com.bcd.ejournal.domain.dto.response.AccountProfileResponse;
import com.bcd.ejournal.domain.dto.response.EducationResponse;
import com.bcd.ejournal.domain.dto.response.InvoiceResponse;
import com.bcd.ejournal.service.AccountService;
import com.bcd.ejournal.service.EmailService;
import com.bcd.ejournal.service.InvoiceService;

@RestController
@RequestMapping("/account")
public class AccountApi {
    private final AccountService accountService;
    private final EmailService emailService;
    private final InvoiceService invoiceService;
    @Autowired
    public AccountApi(AccountService accountService, EmailService emailService, InvoiceService invoiceService) {
        this.accountService = accountService;
        this.emailService = emailService;
        this.invoiceService =invoiceService;
    }

    @GetMapping("/profile")
    public ResponseEntity<AccountProfileResponse> getProfile(@AuthenticationPrincipal AccountJWTPayload jwt) {
        AccountProfileResponse response = accountService.getProfile(jwt.getAccountId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<AccountProfileResponse> updateProfile(@AuthenticationPrincipal AccountJWTPayload jwt,
            @Valid @RequestBody AccountUpdateProfileRequest request) {
        AccountProfileResponse response = accountService.updateProfile(jwt.getAccountId(), request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/education")
    public ResponseEntity<EducationResponse> getEducation(@AuthenticationPrincipal AccountJWTPayload payload) {
        Integer accountId = payload.getAccountId();
        EducationResponse response = accountService.getEducation(accountId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/email")
    public void verifyEmail(@RequestBody AccountEmailVerify req) {
        emailService.sendEmailForgetPassword(req);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal AccountJWTPayload jwt,
            @Valid @RequestBody AccountChangePasswordRequest request) {
        accountService.changePassword(jwt.getAccountId(), request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PutMapping("/forgot")
    public ResponseEntity<Void> forgotPassowrd(@RequestParam String token,@RequestBody AccountChangeForgotPassword req){
    	accountService.forgotPassword(token, req);
    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/payment")
    public ResponseEntity<List<InvoiceResponse>> historyPayment(@AuthenticationPrincipal AccountJWTPayload payload){
    	Integer accountId = payload.getAccountId();
    	List<InvoiceResponse> response= invoiceService.getInvoicebyId(accountId);
    	return new ResponseEntity<>(response ,HttpStatus.OK);
    }
    @DeleteMapping
    public ResponseEntity<Void> archiveAccount(@AuthenticationPrincipal AccountJWTPayload jwt) {
        accountService.archiveAccount(jwt.getAccountId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
