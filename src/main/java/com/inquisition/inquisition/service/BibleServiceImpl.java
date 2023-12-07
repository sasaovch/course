package com.inquisition.inquisition.service;

import java.util.List;

import com.inquisition.inquisition.model.bible.Bible;
import com.inquisition.inquisition.repository.BibleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BibleServiceImpl implements BibleService {
    @Autowired
    private BibleRepository bibleRepository;
    @Override
    public List<Bible> getAllBible() {
        return bibleRepository.findAll();
    }
}
