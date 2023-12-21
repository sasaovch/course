package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.model.accusation.AccusationProcessWithIdContainer;
import com.inquisition.inquisition.model.accusation.AccusationProcessWithInqProcessId;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.service.impl.AccusationProcessServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(
        value = "/accusations",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@PreAuthorize("hasAnyAuthority('INQUISITOR')")
public class AccusationProcessController {
    private final AccusationProcessServiceImpl accusationProcessService;

    @Autowired
    public AccusationProcessController(AccusationProcessServiceImpl accusationProcessService) {
        this.accusationProcessService = accusationProcessService;
    }

    @PostMapping("/start")
    public ResponseEntity<Payload> startProcess(
            @RequestBody AccusationProcessWithInqProcessId container
    ) {
        Payload payload = accusationProcessService.startProcess(container);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/finish")
    public ResponseEntity<Payload> finishProcess(
            @RequestBody AccusationProcessWithIdContainer container
    ) {
        Payload payload = accusationProcessService.finishProcess(container);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/generateCases")
    public ResponseEntity<Payload> generateCases(
            @RequestBody AccusationProcessWithIdContainer container
    ) {
        Payload payload = accusationProcessService.generateCases(container);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }
}
