package com.inquisition.inquisition.model.accusation;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConnectCommandmentContainer {
    @NotNull
    @NotEmpty
    private List<Integer> commandments;
}
