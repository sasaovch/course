package com.inquisition.inquisition.repository;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InquisitionCaseRepository {
    @Autowired
    private DSLContext dsl;

//    public List<InquisitionCase> findAll() {
//
//    }
}
