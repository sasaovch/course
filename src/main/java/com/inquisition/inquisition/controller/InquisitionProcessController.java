package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.model.inquisition.InquisitionProcessStartContainer;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.service.impl.InquisitionServiceImpl;
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
    private final InquisitionServiceImpl inquisitionService;

    public InquisitionProcessController(InquisitionServiceImpl inquisitionService) {
        this.inquisitionService = inquisitionService;
    }

    @PostMapping("/start")
    public ResponseEntity<Payload> startProcess(@Valid @RequestBody InquisitionProcessStartContainer container) {
        Payload payload = inquisitionService.startProcess(container);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/getCurrent/{official_id}")
    public ResponseEntity<Payload> getCurrentProcess(@PathVariable("official_id") Integer officialId) {
        Payload payload = inquisitionService.getCurrentInquisitionProcess(officialId);
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }

    @GetMapping("/getAllInquisitions")
    public ResponseEntity<Payload> getAllInquisitions() {
        Payload payload = inquisitionService.getAllInquisitionProcess();
        if (payload.code() != 200) {
            return ResponseEntity.badRequest().body(payload);
        }
        return ResponseEntity.ok(payload);
    }
}
