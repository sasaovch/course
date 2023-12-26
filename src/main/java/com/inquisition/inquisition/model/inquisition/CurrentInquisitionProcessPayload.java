package com.inquisition.inquisition.model.inquisition;

import lombok.Data;

@Data
public class CurrentInquisitionProcessPayload {
    private Integer id;
    private String bible;
    private String locality;
    private Integer step;
    private Integer currentAccusationProcess;
}
