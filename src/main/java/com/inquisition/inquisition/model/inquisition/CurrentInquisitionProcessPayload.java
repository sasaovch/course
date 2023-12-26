package com.inquisition.inquisition.model.inquisition;

import lombok.Data;

@Data
public class CurrentInquisitionProcessPayload {
    private Integer id;
    private Integer bible;
    private Integer locality;
    private Integer step;
    private Integer currentAccusationProcess;
}
