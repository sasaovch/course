package com.inquisition.inquisition.service;

import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithCollection;
import com.inquisition.inquisition.repository.BibleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BibleService {
    private final BibleRepository bibleRepository;

    @Autowired
    public BibleService(BibleRepository bibleRepository) {
        this.bibleRepository = bibleRepository;
    }

    public Payload getAllBible() {
        return new PayloadWithCollection<>(200, bibleRepository.findAll());
    }

    public Payload getCommandments(Integer bibleId) {
        return new PayloadWithCollection<>(200, bibleRepository.getCommandment(bibleId));
    }
}
