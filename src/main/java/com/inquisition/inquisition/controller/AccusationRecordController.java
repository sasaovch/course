package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.model.accusation.AddAccusationRecordContainer;
import com.inquisition.inquisition.model.accusation.ConnectCommandmentContainer;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.service.impl.AccusationRecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/accusations",
//        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AccusationRecordController {
    @Autowired
    AccusationRecordServiceImpl accusationRecordService;
    @GetMapping("/accusationRecords/{process_id}")
//    @PreAuthorize("hasAnyAuthority('Inquisitor')")
    public ResponseEntity<?> getAccusationRecords(@PathVariable("process_id") Integer processId) {
        return ResponseEntity.ok(accusationRecordService.getAllAccusationRecords(processId));
    }

    @GetMapping("/notResolvedRecords/{process_id}")
    public ResponseEntity<Payload> getNotResolvedRecords(@PathVariable("process_id") Integer processId) {
        return ResponseEntity.ok(accusationRecordService.getNotResolvedAccusationRecord(processId));
    }

    @GetMapping("/allCases/{process_id}")
    public ResponseEntity<Payload> getAllCases(@PathVariable("process_id") Integer processId) {
        return ResponseEntity.ok(accusationRecordService.getAllCases(processId));
    }

    @PostMapping("/add")
    public ResponseEntity<Payload> add(@RequestBody AddAccusationRecordContainer container) {
        Payload payload = accusationRecordService.addRecord(container);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest()
                    .body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/connectCommandment/{record_id}")
    public ResponseEntity<Payload> connectCommandment(@RequestBody ConnectCommandmentContainer container, @PathVariable("record_id") Integer recordId) {
        return ResponseEntity.ok(accusationRecordService.connectCommandment(container, recordId));
    }
}
