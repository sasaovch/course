package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.model.inquisition.container.InquisitionProcessIdContainer;
import com.inquisition.inquisition.model.inquisition.container.InquisitionProcessStartContainer;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.service.InquisitionProcessService;
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
        value = "/inquisitions",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@PreAuthorize("hasAnyAuthority('INQUISITOR')")
public class InquisitionProcessController {
    private final InquisitionProcessService inquisitionProcessService;

    public InquisitionProcessController(InquisitionProcessService inquisitionProcessService) {
        this.inquisitionProcessService = inquisitionProcessService;
    }

    @PostMapping("/start")
    public ResponseEntity<Payload> startProcess(@Valid @RequestBody InquisitionProcessStartContainer container) {
        Payload payload = inquisitionProcessService.startProcess(container);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @PostMapping("/finish")
    public ResponseEntity<Payload> finishProcess(@Valid @RequestBody InquisitionProcessIdContainer container) {
        Payload payload = inquisitionProcessService.finishProcess(container);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/getCurrent/{official_id}")
    @PreAuthorize("hasAnyAuthority('INQUISITOR')")
    public ResponseEntity<Payload> getCurrentProcess(@PathVariable("official_id") Integer officialId) {
        Payload payload = inquisitionProcessService.getCurrentInquisitionProcessForInquisitor(officialId);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/getCurrentForPerson")
    @PreAuthorize("hasAnyAuthority('BISHOP', 'INQUISITOR', 'USER', 'SECULAR_AUTHORITY', 'FISCAL')")
    public ResponseEntity<Payload> getCurrentProcessForBishop() {
        Payload payload = inquisitionProcessService.getCurrentInquisitionProcessForBishop();
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/getAllInquisitions")
    public ResponseEntity<Payload> getAllInquisitions() {
        Payload payload = inquisitionProcessService.getAllInquisitionProcess();
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/allCases/{process_id}")
    @PreAuthorize("hasAnyAuthority('BISHOP', 'INQUISITOR')")
    public ResponseEntity<Payload> getAllCases(@PathVariable("process_id") Integer processId) {
        Payload payload = inquisitionProcessService.getAllCases(processId);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }
}
