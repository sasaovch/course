package com.inquisition.inquisition.model.person;

public enum Gender {
    M("M", 0),
    F("F", 1);

    public int getId() {
        return id;
    }

    private final Integer id;
    private final String displayName;

    Gender(String displayName, Integer id) {
        this.id = id; this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

//    public static Gender valueOf(String id) {
//        for (Gender value : Gender.values()) {
//            if (value.id == id) {
//                return value;
//            }
//        }
//        throw new IllegalArgumentException("Invalid MyEnum id: " + id);
//    }
}
