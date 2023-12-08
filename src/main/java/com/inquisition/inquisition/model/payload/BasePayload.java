package com.inquisition.inquisition.model.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public record BasePayload(@NotNull Integer code, @NotNull String message) implements Payload {
}
