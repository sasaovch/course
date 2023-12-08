package com.inquisition.inquisition.utils;

import com.inquisition.inquisition.model.accusation.AccusationRecord;
import com.inquisition.inquisition.model.accusation.AccusationRecordFull;
import com.inquisition.inquisition.model.accusation.AccusationRecordPayload;
import com.inquisition.inquisition.model.accusation.AccusationRecordWithCasePayload;

public class AccusationRecordConverter {
    public static AccusationRecordPayload convertToPayload(AccusationRecordFull record) {
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
