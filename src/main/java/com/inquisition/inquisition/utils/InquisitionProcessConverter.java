package com.inquisition.inquisition.utils;

import com.inquisition.inquisition.model.inquisition.CurrentInquisitionProcessPayload;
import com.inquisition.inquisition.model.inquisition.InquisitionProcess;
import com.inquisition.inquisition.model.inquisition.InquisitionProcessPayload;
import com.inquisition.inquisition.model.person.Person;

public final class InquisitionProcessConverter {
    private InquisitionProcessConverter() {}
    public static CurrentInquisitionProcessPayload convertToCurrentPayload(InquisitionProcess process, Integer step) {
        CurrentInquisitionProcessPayload payload = new CurrentInquisitionProcessPayload();
        payload.setId(process.getId());
        payload.setLocality(process.getChurch().getLocality().getName());
        payload.setBible(process.getBible().getName());
        payload.setStep(step);
        payload.setCurrentAccusationProcess(process.getAccusationProcess().getId());
        return payload;
    }

    public static InquisitionProcessPayload convertToPayload(InquisitionProcess process, Integer countCases) {
        InquisitionProcessPayload payload = new InquisitionProcessPayload();
        payload.setStartTime(process.getStartData());
        payload.setEndTime(process.getFinishData());
        Person person = process.getOfficial().getPerson();
        if (person != null) {
            payload.setInquisitor(person.getName() + " " + person.getSurname());
        } else {
            payload.setInquisitor("");
        }
        payload.setCaseCount(countCases);
        payload.setLocality(process.getChurch().getLocality().getName());
        return payload;
    }
}
