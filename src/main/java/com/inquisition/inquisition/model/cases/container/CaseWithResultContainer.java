package com.inquisition.inquisition.model.cases.container;

import lombok.Data;

@Data
public class CaseWithResultContainer {
    private Integer id;
    private Integer result;
    private String description;
}
