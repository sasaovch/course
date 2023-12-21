package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.service.BibleService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        value = "/bibles",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class BibleController {
    private final BibleService bibleService;

    public BibleController(BibleService bibleService) {
        this.bibleService = bibleService;
    }

    @GetMapping(value = "/")
    public ResponseEntity<Payload> getAllBibles() {
        return ResponseEntity.ok(bibleService.getAllBible());
    }

    @GetMapping(value = "/commandments/{bible_id}")
    public ResponseEntity<Payload> getCommandments(@PathVariable("bible_id") Integer bibleId) {
        return ResponseEntity.ok(bibleService.getCommandments(bibleId));
    }
}
