package com.inquisition.inquisition.model.church;

import java.time.LocalDate;
import java.util.Date;

import com.inquisition.inquisition.model.locality.Locality;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
//public record Church(
//        Integer id,
//        String name,
//        LocalDate foundationDate,
//        Integer localityId
//) {
//}
//
//@Entity(name = "church")
@Data
public class Church {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//
//    @Column(nullable = false)
    private String name;
//
//    @Column(name = "foundation_date")
    private LocalDate foundationDate;
//
//    @ManyToOne
//    @JoinColumn(name = "locality_id", nullable = false)
    private Locality locality;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Date getFoundationDate() {
//        return foundationDate;
//    }
//
//    public void setFoundationDate(Date foundationDate) {
//        this.foundationDate = foundationDate;
//    }
//
//    public Locality getLocality() {
//        return locality;
//    }
//
//    public void setLocality(Locality locality) {
//        this.locality = locality;
//    }
}
