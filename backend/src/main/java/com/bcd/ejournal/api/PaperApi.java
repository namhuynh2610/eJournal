package com.bcd.ejournal.api;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcd.ejournal.configuration.jwt.payload.AccountJWTPayload;
import com.bcd.ejournal.domain.dto.request.PaperStatusBulkUpdateRequest;
import com.bcd.ejournal.domain.dto.request.PaperStatusUpdateRequest;
import com.bcd.ejournal.domain.dto.request.PaperSubmitRequest;
import com.bcd.ejournal.domain.dto.request.PaperUpdateRequest;
import com.bcd.ejournal.domain.dto.response.InvitationPaperResponse;
import com.bcd.ejournal.domain.dto.response.PaperDetailResponse;
import com.bcd.ejournal.domain.enums.PaperStatus;
import com.bcd.ejournal.service.InvitationService;
import com.bcd.ejournal.service.PaperService;


@RestController
@RequestMapping(path = "/paper")
public class PaperApi {
    private final PaperService paperService;
    private final InvitationService invitationService;

    @Autowired
    public PaperApi(PaperService paperService, InvitationService invitationService) {
        this.paperService = paperService;
        this.invitationService = invitationService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> submitPaper(@AuthenticationPrincipal AccountJWTPayload payload,
            @ModelAttribute PaperSubmitRequest request) throws InvalidPasswordException, IOException {
        paperService.submitPaper(payload.getAccountId(), request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<Void> updatePaper(@AuthenticationPrincipal AccountJWTPayload payload, @PathVariable(name = "id") Integer paperId, @ModelAttribute PaperUpdateRequest request) {
        paperService.updatePaper(payload.getAccountId(), paperId, request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaperById(@PathVariable(name = "id") Integer paperId) {
        // TODO: verify right account
        paperService.deleteById(paperId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> managerUpdatePaperStatus(@AuthenticationPrincipal AccountJWTPayload payload, @PathVariable(name = "id") Integer paperId, @RequestBody PaperStatusUpdateRequest request) {
        paperService.managerUpdatePaperStatus(payload.getAccountId(), paperId, request.getStatus());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/status")
    public ResponseEntity<Void> managerBulkUpdatePaperStatus(@AuthenticationPrincipal AccountJWTPayload payload, @RequestBody PaperStatusBulkUpdateRequest request) {
        paperService.managerBulkUpdatePaperStatus(payload.getAccountId(), request.getPaperIds(), request.getStatus());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaperDetailResponse> getPaper(@PathVariable(name = "id") Integer paperId) {
        // TODO: author or reviewer or journal
        PaperDetailResponse response = paperService.getPaper(paperId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> getFile(@PathVariable(name = "id") Integer paperId) throws IOException {
        // TODO: verify reviewer can download
        Resource rs = paperService.downloadFile(paperId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + rs.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf").body(rs);
    }

    @GetMapping("/{id}/invitation")
    public ResponseEntity<List<InvitationPaperResponse>> getInvitation(
            @AuthenticationPrincipal AccountJWTPayload payload, @PathVariable(name = "id") Integer paperId) {
        Integer accountId = payload.getAccountId();
        List<InvitationPaperResponse> responses = invitationService.listInvitationFromPaper(accountId, paperId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
