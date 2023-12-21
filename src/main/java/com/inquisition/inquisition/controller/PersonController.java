package com.inquisition.inquisition.controller;

import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.service.impl.PersonServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(
        value = "/persons",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class PersonController {
    private final PersonServiceImpl personService;

    public PersonController(PersonServiceImpl personService) {
        this.personService = personService;
    }

    @GetMapping("/{locality_id}")
    public ResponseEntity<Payload> getPersonsByLocality(@PathVariable("locality_id") Integer localityId) {
        return ResponseEntity.ok(personService.getPersonByLocality(localityId));
    }
}
