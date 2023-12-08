package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.model.inquisition.InquisitionProcessStartContainer;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.service.impl.InquisitionServiceImpl;
import jakarta.validation.Valid;
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
@RequestMapping(value = "/inquisitions",
//        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class InquisitionProcessController {
    @Autowired
    InquisitionServiceImpl inquisitionService;

    @PostMapping("/start")
//    @PreAuthorize("hasAnyAuthority('Fiscal') or hasAnyAuthority('Inquisitor')")
    public ResponseEntity<Payload> startProcess(
            @Valid @RequestBody InquisitionProcessStartContainer container
    ) {
        return ResponseEntity.ok(inquisitionService.startProcess(container));
    }

    @GetMapping("/getCurrent/{official_id}")
//    @PreAuthorize("hasAnyAuthority('Inquisitor')")
    public ResponseEntity<Payload> getCurrentProcess(@PathVariable("official_id") Integer officialId) {
        return ResponseEntity.ok(inquisitionService.getCurrentInquisitionProcess(officialId));
    }

//    @GetMapping("/getAccusationRecords/{process_id}")
////    @PreAuthorize("hasAnyAuthority('Inquisitor')")
//    public ResponseEntity<?> getAccusationRecords(@PathVariable("process_id") Integer processId) {
//        return ResponseEntity.ok(inquisitionService.getAllAccusationRecords(processId));
//    }

    @GetMapping("/getAllInquisitions")
//    @PreAuthorize("hasAnyAuthority('Inquisitor')")
    public ResponseEntity<Payload> getAllInquisitions() {
        return ResponseEntity.ok(inquisitionService.getAllInquisitionProcess());
    }


//    @GetMapping("/getQueueForDiscussion/{process_id}")
//    public ResponseEntity<Payload> getQueueForDiscussion(@PathVariable("process_id") Integer processId) {
//        inquisitionService.getQueueForDiscussion(processId);
//    }
}
