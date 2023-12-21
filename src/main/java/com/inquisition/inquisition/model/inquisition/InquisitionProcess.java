package com.inquisition.inquisition.model.inquisition;


import java.time.LocalDate;
import java.util.Set;

import com.inquisition.inquisition.model.official.Official;
import com.inquisition.inquisition.model.accusation.AccusationProcess;
import com.inquisition.inquisition.model.bible.Bible;
import com.inquisition.inquisition.model.church.Church;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
@Data
public class InquisitionProcess {
    private Integer id;
    private LocalDate startData;
    private LocalDate finishData;
    private Official official;

    private Church church;
    private Bible bible;
    private AccusationProcess accusationProcess;
}
