package com.inquisition.inquisition.model.payload;

import java.util.Collection;
import java.util.List;

import com.inquisition.inquisition.model.accusation.AccusationRecord;
import lombok.Getter;

@Getter
public class PayloadWithAccusationRecords extends PayloadWithCollection<AccusationRecord> {
    public PayloadWithAccusationRecords(Integer code, String message, Collection<AccusationRecord> collection) {
        super(code, message, collection);
    }
}
