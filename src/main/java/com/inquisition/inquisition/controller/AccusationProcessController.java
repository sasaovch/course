package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.model.accusation.AccusationProcessWithIdContainer;
import com.inquisition.inquisition.model.accusation.AccusationProcessWithInqProcessId;
import com.inquisition.inquisition.model.accusation.AddAccusationRecordContainer;
import com.inquisition.inquisition.model.accusation.ConnectCommandmentContainer;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.service.AccusationProcessService;
import jakarta.validation.Valid;
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
        value = "/accusations",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@PreAuthorize("hasAnyAuthority('INQUISITOR')")
public class AccusationProcessController {
    private final AccusationProcessService accusationProcessService;

    public AccusationProcessController(AccusationProcessService accusationProcessService) {
        this.accusationProcessService = accusationProcessService;
    }

    @PostMapping("/start")
    public ResponseEntity<Payload> startProcess(
            @Valid @RequestBody AccusationProcessWithInqProcessId container
    ) {
        Payload payload = accusationProcessService.startProcess(container);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/finish")
    public ResponseEntity<Payload> finishProcess(
            @Valid @RequestBody AccusationProcessWithIdContainer container
    ) {
        Payload payload = accusationProcessService.finishProcess(container);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/generateCases")
    public ResponseEntity<Payload> generateCases(
            @Valid @RequestBody AccusationProcessWithIdContainer container
    ) {
        Payload payload = accusationProcessService.generateCases(container);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/accusationRecords/{process_id}")
    @PreAuthorize("hasAnyAuthority('INQUISITOR')")
    public ResponseEntity<Payload> getAccusationRecords(@PathVariable("process_id") Integer processId) {
        Payload payload = accusationProcessService.getAllAccusationRecords(processId);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/notResolvedRecords/{process_id}")
    public ResponseEntity<Payload> getNotResolvedRecords(@PathVariable("process_id") Integer processId) {
        Payload payload = accusationProcessService.getNotResolvedAccusationRecord(processId);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('INQUISITOR', 'BISHOP')")
    public ResponseEntity<Payload> add(@Valid @RequestBody AddAccusationRecordContainer container) {
        Payload payload = accusationProcessService.addRecord(container);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/connectCommandment/{record_id}")
    public ResponseEntity<Payload> connectCommandment(
            @Valid @RequestBody ConnectCommandmentContainer container,
            @PathVariable("record_id") Integer recordId) {
        Payload payload = accusationProcessService.connectCommandment(container, recordId);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }
}
