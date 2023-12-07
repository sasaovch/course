package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.model.inquisition.InquisitionProcess;
import com.inquisition.inquisition.model.inquisition.InquisitionProcessStartContainer;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.service.InquisitionService;
import com.inquisition.inquisition.service.InquisitionServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/inquisitions")
public class InquisitionController {
    @Autowired
    InquisitionServiceImpl inquisitionService;

    @PostMapping("/start")
//    @PreAuthorize("hasAnyAuthority('Fiscal') or hasAnyAuthority('Inquisitor')")
    public ResponseEntity<Payload> startProcess(
            @Valid @RequestBody InquisitionProcessStartContainer container
    ) {
        return ResponseEntity.ok(inquisitionService.startProcess(container));
    }

    @GetMapping("/getCurrent/{locality_id}")
//    @PreAuthorize("hasAnyAuthority('Inquisitor')")
    public ResponseEntity<Payload> getCurrentProcess(@PathVariable("locality_id") Integer localityId) {
        return ResponseEntity.ok(inquisitionService.getCurrentInquisitionProcess(localityId));
    }

    @GetMapping("/getAccusationRecords/{process_id}")
//    @PreAuthorize("hasAnyAuthority('Inquisitor')")
    public ResponseEntity<?> getAccusationRecords(@PathVariable("process_id") Long processId) {
        return ResponseEntity.ok(inquisitionService.getAllAccusationRecords(processId));
    }

    @GetMapping("/getInquisitions/{locality_id}")
//    @PreAuthorize("hasAnyAuthority('Inquisitor')")
    public ResponseEntity<?> getInquisitions(@PathVariable("locality_id") Integer localityId) {
        return ResponseEntity.ok(inquisitionService.getAllInquisitionProcess(localityId));
    }
}
