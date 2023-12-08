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
//public record InquisitionProcess(
//        Integer id,
//        LocalDate startDate,
//        LocalDate finishDate,
//        Integer officialId,
//        Integer churchId,
//        Integer bibleId,
//        AccusationProcess accusationProcess
//) {
//}
//
@Data
//@Entity(name = "inquisition_process")
public class InquisitionProcess {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//
//    @Column(name = "start_data", nullable = false)
    private LocalDate startDate;
//
//    @Column(name = "finish_data")
    private LocalDate finishDate;
//
//    @ManyToOne
//    @JoinColumn(name = "official_id", nullable = false)
    private Official official;

//    @ManyToOne
//    @JoinColumn(name = "church_id", nullable = false)
    private Church church;

//    @ManyToOne
//    @JoinColumn(name = "bible_id", nullable = false)
    private Bible bible;

//    @OneToOne(mappedBy = "inquisitionProcess")
    private AccusationProcess accusationProcess;
}
