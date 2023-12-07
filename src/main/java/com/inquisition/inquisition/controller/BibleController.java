package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.service.BibleService;
import com.inquisition.inquisition.service.BibleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bibles")
public class BibleController {
    @Autowired
    private BibleServiceImpl bibleService;

    @GetMapping("/")
//    @PreAuthorize("hasAnyAuthority('Inquisitor')")
    public ResponseEntity<?> getAllBibles() {
        return ResponseEntity.ok(bibleService.getAllBible());
    }
}
