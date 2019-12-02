package org.sda.model;

public enum GenderEnum {
    M,F;

    public static GenderEnum getGenderByValue(String value) {
        for(GenderEnum genderEnum : values()) {
            if(genderEnum.name().equalsIgnoreCase(value)) {
                return genderEnum;
            }
        }

        return null;
    }
}
