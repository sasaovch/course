package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.service.impl.LocalityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/localities")
public class LocalityController {
    @Autowired
    private LocalityServiceImpl localityService;

    @GetMapping("/")
    public ResponseEntity<?> getAllLocality() {
        return ResponseEntity.ok(localityService.getAllLocality());
    }
}
