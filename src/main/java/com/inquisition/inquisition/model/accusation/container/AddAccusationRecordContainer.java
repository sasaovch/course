package com.inquisition.inquisition.model.accusation.container;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddAccusationRecordContainer {
    @NotNull
    private Integer informer;
    @NotNull
    private Integer bishop;
    @NotNull
    private Integer accused;
    @NotNull
    private String violationPlace;
    @NotNull
    private LocalDate dateTime;
    @NotNull
    private String description;
    @NotNull
    private Integer accusationId;
}
