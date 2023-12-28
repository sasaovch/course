package com.inquisition.inquisition.model.official.entity;

public enum OfficialName {
    BISHOP("Bishop"),
    SECULAR_AUTHORITY("SecularAuthority"),
    INQUISITOR("Inquisitor"),
    FISCAL("Fiscal");
    private final String displayName;

    OfficialName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
