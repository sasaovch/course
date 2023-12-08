package com.inquisition.inquisition.model.official;

import com.inquisition.inquisition.model.person.Gender;

public enum OfficialName {
    BISHOP("Bishop", 0),
    SECULAR_AUTHORITY("SecularAuthority", 1),
    INQUISITOR("Inquisitor", 2),
    FISCAL("Fiscal", 3);
//'Епископ', 'Светская власть', 'Инквизитор', 'Фискал')
    private final String displayName;
    private final Integer id;

    OfficialName(String displayName, Integer id) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static OfficialName valueOf(int id) {
        for (OfficialName value : OfficialName.values()) {
            if (value.id == id) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid MyEnum id: " + id);
    }
}
