package com.inquisition.inquisition.utils;

import com.inquisition.inquisition.model.accusation.entity.AccusationRecordComplex;
import com.inquisition.inquisition.model.accusation.payload.AccusationRecordPayload;

public class AccusationRecordConverter {
    public static AccusationRecordPayload convertToPayload(AccusationRecordComplex record) {
        AccusationRecordPayload payload = new AccusationRecordPayload();

        payload.setId(record.getId());
        payload.setInformer(record.getInformer().getName() + " " + record.getInformer().getSurname());
        payload.setBishop(record.getBishop().getName() + " " + record.getBishop().getSurname());
        payload.setAccused(record.getAccused().getName() + " " + record.getAccused().getSurname());
        payload.setViolationPlace(record.getViolationPlace());
        payload.setDateTime(record.getViolationTime());
        payload.setDescription(record.getDescription());
        return payload;
    }
}
