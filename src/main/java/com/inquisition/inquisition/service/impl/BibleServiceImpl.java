package com.inquisition.inquisition.service.impl;

import java.util.List;

import com.inquisition.inquisition.model.bible.Bible;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithCollection;
import com.inquisition.inquisition.repository.BibleRepository;
import com.inquisition.inquisition.service.intr.BibleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BibleServiceImpl implements BibleService {
    private final BibleRepository bibleRepository;

    @Autowired
    public BibleServiceImpl(BibleRepository bibleRepository) {
        this.bibleRepository = bibleRepository;
    }

    @Override
    public Payload getAllBible() {
        return new PayloadWithCollection<>(200, "", bibleRepository.findAll());
    }

    public Payload getCommandments(Integer bibleId) {
        return new PayloadWithCollection<>(200, "", bibleRepository.getCommandment(bibleId));
    }
}
