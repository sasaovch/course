package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.model.cases.CaseInput;
import com.inquisition.inquisition.model.cases.CaseWithResultInput;
import com.inquisition.inquisition.model.cases.CaseWithStepInput;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.service.CaseLogService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(
        value = "/cases",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@PreAuthorize("hasAnyAuthority('INQUISITOR')")
public class CaseLogController {
    private final CaseLogService caseLogService;

    public CaseLogController(CaseLogService caseLogService) {
        this.caseLogService = caseLogService;
    }

    @PostMapping("/sendToDiscussion")
    public ResponseEntity<Payload> sendToDiscussion(@RequestBody CaseInput input) {
        return ResponseEntity.ok(caseLogService.sendToDiscussion(input));
    }

    @PostMapping("/finishDiscussion")
    public ResponseEntity<Payload> finishDiscussion(@RequestBody CaseWithResultInput input) {
        return ResponseEntity.ok(caseLogService.finishDiscussion(input));
    }

    @PostMapping("/sendToTorture")
    public ResponseEntity<Payload> sendToTorture(@RequestBody CaseInput input) {
        return ResponseEntity.ok(caseLogService.sendToTorture(input));
    }

    @PostMapping("/makeTortureStep")
    public ResponseEntity<Payload> makeTortureStep(@RequestBody CaseWithStepInput input) {
        return ResponseEntity.ok(caseLogService.makeTortureStep(input));
    }

    @PostMapping("/finishTorture")
    public ResponseEntity<Payload> finishTorture(@RequestBody CaseWithResultInput input) {
        return ResponseEntity.ok(caseLogService.finishTorture(input));
    }

    @GetMapping("/forDiscussion/{inquisition_id}")
    public ResponseEntity<Payload> getCasesForDiscussion(@PathVariable("inquisition_id") Integer inquisitionId) {
        return ResponseEntity.ok(caseLogService.getCasesForDiscussion(inquisitionId));
    }

    @GetMapping("/forTorture/{inquisition_id}")
    public ResponseEntity<Payload> getCasesForTorture(@PathVariable("inquisition_id") Integer inquisitionId) {
        return ResponseEntity.ok(caseLogService.getCasesForTorture(inquisitionId));
    }

    @GetMapping("/forPunishment/{inquisition_id}")
    public ResponseEntity<Payload> getCasesForPunishment(@PathVariable("inquisition_id") Integer inquisitionId) {
        return ResponseEntity.ok(caseLogService.getCasesForPunishment(inquisitionId));
    }
}
