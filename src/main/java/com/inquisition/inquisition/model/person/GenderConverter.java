//package com.inquisition.inquisition.model.person;
//
//import jakarta.persistence.AttributeConverter;
//import jakarta.persistence.Converter;
//
//@Converter(autoApply = true)
//public class GenderConverter implements AttributeConverter<Gender, String> {
//    @Override
//    public String convertToDatabaseColumn(Gender color) {
//        if (color == null) {
//            return null;
//        }
//        return color.name().toLowerCase();
//    }
//
//    @Override
//    public Gender convertToEntityAttribute(String value) {
//        if (value == null) {
//            return null;
//        }
//        return Gender.valueOf(value.toUpperCase());
//    }
//}
