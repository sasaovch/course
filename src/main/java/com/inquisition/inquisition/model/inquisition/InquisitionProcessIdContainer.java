package com.inquisition.inquisition.model.inquisition;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InquisitionProcessIdContainer {
 @NotNull
 private Integer inquisitionId;
}
