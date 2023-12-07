package com.inquisition.inquisition.service;

import java.util.List;

import com.inquisition.inquisition.model.accusation.AccusationRecord;
import com.inquisition.inquisition.model.inquisition.InquisitionProcess;
import com.inquisition.inquisition.model.inquisition.InquisitionProcessStartContainer;
import com.inquisition.inquisition.model.payload.Payload;

public interface InquisitionService {
    Payload startProcess(InquisitionProcessStartContainer container);
    Payload getCurrentInquisitionProcess(Integer localityId);
    Payload getAllInquisitionProcess(Integer localityId);
    Payload getAllAccusationRecords(Long inquisitionId);
}
