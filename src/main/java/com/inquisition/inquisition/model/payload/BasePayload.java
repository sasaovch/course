package com.inquisition.inquisition.model.payload;

import jakarta.validation.constraints.NotNull;

public record BasePayload(@NotNull Integer code, @NotNull String message) implements Payload {
}
