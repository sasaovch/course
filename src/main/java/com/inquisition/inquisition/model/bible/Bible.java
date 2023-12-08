package com.inquisition.inquisition.model.bible;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
//public record Bible(
//        Integer version,
//        LocalDate publicationDate,
//        String name
//) {
//}
//@Entity(name = "bible")
@Data
public class Bible {
//
//    @Id
    private Integer version;
//
//    @Column(name = "publication_date")
    private LocalDate publicationDate;
//
//    @Column(nullable = false)
    private String name;
//
//    public Integer getVersion() {
//        return version;
//    }
//
//    public void setVersion(Integer version) {
//        this.version = version;
//    }
//
//    public LocalDate getPublicationDate() {
//        return publicationDate;
//    }
//
//    public void setPublicationDate(LocalDate publicationDate) {
//        this.publicationDate = publicationDate;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
}
