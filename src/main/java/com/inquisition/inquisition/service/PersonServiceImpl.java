package com.inquisition.inquisition.service;

import java.util.List;

import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithCollection;
import com.inquisition.inquisition.model.person.Person;
import com.inquisition.inquisition.repository.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl {
    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Payload getPersonByLocality(Integer localityId) {
        List<Person> persons = personRepository.findAll(
                PersonRepository.byLocalityId(localityId));
        return new PayloadWithCollection<>(200, "", persons);
    }
}
