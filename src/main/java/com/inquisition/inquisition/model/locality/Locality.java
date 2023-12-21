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

@Data
public class Locality {
    private Integer id;
    private String name;
    private LocalDate foundationDate;
}
