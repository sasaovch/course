package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.model.payload.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//FIXME
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/inquisitions",
//        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class InquisitionCaseController {
//    @Autowired
//    InquisitionCaseServiceImpl inquisitionCaseService;

//    @GetMapping("/allCases/{process_id}")
    //    @PreAuthorize("hasAnyAuthority('Fiscal') or hasAnyAuthority('Inquisitor')")
//    public ResponseEntity<Payload> getAllCases(@PathVariable("process_id") Integer processId) {
//        return ResponseEntity.ok(inquisitionCaseService.getAllCases(processId));
//    }
}
