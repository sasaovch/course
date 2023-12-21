package com.inquisition.inquisition.model.cases;

import lombok.Data;

@Data
public class CaseWithResultInput {
    private Integer id;
    private Integer result;
    private String description;
}
