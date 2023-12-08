package com.inquisition.inquisition.model.locality;


import java.time.LocalDate;
import java.util.List;

import com.inquisition.inquisition.model.church.Church;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

//public record Locality(
//        Integer id,
//        String name,
//        LocalDate foundationDate
//) {
//}
//
//@Entity(name = "locality")
@Data
public class Locality {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//
//    @Column(nullable = false)
    private String name;
//
//    @Column(name = "foundation_date")
    private LocalDate foundationDate;
//
//    @OneToMany(mappedBy = "locality", cascade = CascadeType.REMOVE)
//    private List<Church> churches;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
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
//    public LocalDate getFoundationDate() {
//        return foundationDate;
//    }
//
//    public void setFoundationDate(LocalDate foundationDate) {
//        this.foundationDate = foundationDate;
//    }
//
//    public List<Church> getChurches() {
//        return churches;
//    }
//
//    public void setChurches(List<Church> churches) {
//        this.churches = churches;
//    }
}
