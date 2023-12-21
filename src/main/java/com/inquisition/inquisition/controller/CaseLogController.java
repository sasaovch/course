package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.model.cases.CaseInput;
import com.inquisition.inquisition.model.cases.CaseWithResultInput;
import com.inquisition.inquisition.model.cases.CaseWithStepInput;
import com.inquisition.inquisition.model.inquisition.InquisitionProcessIdContainer;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.service.impl.CaseLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//FIXME
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(
        value = "/case",
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
        return ResponseEntity.ok(caseLogService.finishProcess(input));
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
        return ResponseEntity.ok(caseLogService.finishProcess(input));
    }

    @PostMapping("/forDiscussion")
    public ResponseEntity<Payload> getCaseForDiscussion(@RequestBody InquisitionProcessIdContainer input) {
        return ResponseEntity.ok(caseLogService.getCasesForDiscussion(input));
    }
}
