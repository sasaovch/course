package com.inquisition.inquisition.model.accusation;

public enum AccusationStatus {
    FALSE("Ложный"),
    TRUE("Правдивый");

    private final String displayName;

    AccusationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
