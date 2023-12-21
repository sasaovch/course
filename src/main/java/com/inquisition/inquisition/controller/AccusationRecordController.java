package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.model.accusation.AddAccusationRecordContainer;
import com.inquisition.inquisition.model.accusation.ConnectCommandmentContainer;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.service.impl.AccusationRecordServiceImpl;
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
@RequestMapping(value = "/accusations")
@PreAuthorize("hasAnyAuthority('INQUISITOR')")
public class AccusationRecordController {
    private final AccusationRecordServiceImpl accusationRecordService;

    public AccusationRecordController(AccusationRecordServiceImpl accusationRecordService) {
        this.accusationRecordService = accusationRecordService;
    }

    @GetMapping("/accusationRecords/{process_id}")
    @PreAuthorize("hasAnyAuthority('INQUISITOR')")
    public ResponseEntity<Payload> getAccusationRecords(@PathVariable("process_id") Integer processId) {
        Payload payload = accusationRecordService.getAllAccusationRecords(processId);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/notResolvedRecords/{process_id}")
    public ResponseEntity<Payload> getNotResolvedRecords(@PathVariable("process_id") Integer processId) {
        Payload payload = accusationRecordService.getNotResolvedAccusationRecord(processId);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/allCases/{process_id}")
    public ResponseEntity<Payload> getAllCases(@PathVariable("process_id") Integer processId) {
        Payload payload = accusationRecordService.getAllCases(processId);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('INQUISITOR', 'BISHOP')")
    public ResponseEntity<Payload> add(@RequestBody AddAccusationRecordContainer container) {
        Payload payload = accusationRecordService.addRecord(container);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/connectCommandment/{record_id}")
    public ResponseEntity<Payload> connectCommandment(
            @RequestBody ConnectCommandmentContainer container,
            @PathVariable("record_id") Integer recordId) {
        Payload payload = accusationRecordService.connectCommandment(container, recordId);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }
}
