package com.inquisition.inquisition.service;

import java.util.List;

import com.inquisition.inquisition.model.locality.Locality;
import com.inquisition.inquisition.repository.LocalityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalityServiceImpl implements LocalityService{
    @Autowired
    LocalityRepository localityRepository;
    @Override
    public List<Locality> getAllLocality() {
        return localityRepository.findAll();
    }
}
