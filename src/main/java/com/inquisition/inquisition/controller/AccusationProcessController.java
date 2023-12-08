package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.model.accusation.AccusationProcessWithIdContainer;
import com.inquisition.inquisition.model.accusation.AccusationProcessStartContainer;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.service.impl.AccusationProcessServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/accusations",
//        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AccusationProcessController {
    //FIXME: what if database is failed
    @Autowired
    AccusationProcessServiceImpl accusationProcessService;

    @PostMapping("/start")
    //    @PreAuthorize("hasAnyAuthority('Fiscal') or hasAnyAuthority('Inquisitor')")
    public ResponseEntity<Payload> startProcess(
            @RequestBody AccusationProcessStartContainer container
    ) {
        return ResponseEntity.ok(accusationProcessService.startProcess(container));
    }

    @PostMapping("/finish")
    //    @PreAuthorize("hasAnyAuthority('Fiscal') or hasAnyAuthority('Inquisitor')")
    public ResponseEntity<Payload> startProcess(
            @RequestBody AccusationProcessWithIdContainer container
    ) {
        return ResponseEntity.ok(accusationProcessService.finishProcess(container));
    }

    @PostMapping("/generateCases")
    public ResponseEntity<Payload> generateCases(
            @RequestBody AccusationProcessWithIdContainer container
    ) {
        return ResponseEntity.ok(accusationProcessService.generateCases(container));
    }
}
