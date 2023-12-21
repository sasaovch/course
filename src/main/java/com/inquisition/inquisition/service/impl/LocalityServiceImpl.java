package com.inquisition.inquisition.service.impl;

import java.util.List;

import com.inquisition.inquisition.model.locality.Locality;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithCollection;
import com.inquisition.inquisition.repository.LocalityRepository;
import com.inquisition.inquisition.service.intr.LocalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalityServiceImpl implements LocalityService {
    private final LocalityRepository localityRepository;
    @Autowired
    public LocalityServiceImpl(LocalityRepository localityRepository) {
        this.localityRepository = localityRepository;
    }

    @Override
    public Payload getAllLocality() {
        return new PayloadWithCollection<>(200, localityRepository.findAll());
    }
}
