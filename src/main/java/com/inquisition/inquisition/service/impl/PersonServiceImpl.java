package com.inquisition.inquisition.service.impl;

import java.util.List;

import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithCollection;
import com.inquisition.inquisition.model.person.Person;
import com.inquisition.inquisition.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl {
    @Autowired
    PersonRepository personRepository;

    public Payload getPersonByLocality(Integer localityId) {
        List<Person> persons = personRepository.findAll(
                PersonRepository.byLocalityId(localityId));
        return new PayloadWithCollection<>(200, "", persons);
    }
}
