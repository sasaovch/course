package com.inquisition.inquisition.model.bible.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
@Data
public class Bible {
    private Integer version;
    private LocalDate publicationDate;
    private String name;
}
