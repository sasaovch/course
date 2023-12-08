package com.inquisition.inquisition.model.inquisition;

import com.inquisition.inquisition.model.payload.Payload;
import lombok.Data;
import lombok.Getter;

@Data
public class CurrentInquisitionProcessPayload {
    private Integer id;
    private String bible;
    private String locality;
    private Integer step;
}
