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
@Entity(name = "inquisition_process")
public class InquisitionProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "start_data", nullable = false)
    private LocalDate startDate;

    @Column(name = "finish_data")
    private LocalDate finishDate;

    @ManyToOne
    @JoinColumn(name = "official_id", nullable = false)
    private Official official;

    @ManyToOne
    @JoinColumn(name = "church_id", nullable = false)
    private Church church;

    @ManyToOne
    @JoinColumn(name = "bible_id", nullable = false)
    private Bible bible;

    @OneToOne(mappedBy = "inquisitionProcess")
    private AccusationProcess accusationProcess;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public Official getOfficial() {
        return official;
    }

    public void setOfficial(Official official) {
        this.official = official;
    }

    public Church getChurch() {
        return church;
    }

    public void setChurch(Church church) {
        this.church = church;
    }

    public Bible getBible() {
        return bible;
    }

    public void setBible(Bible bible) {
        this.bible = bible;
    }

    public AccusationProcess getAccusationProcess() {
        return accusationProcess;
    }

    public void setAccusationProcess(AccusationProcess accusationProcess) {
        this.accusationProcess = accusationProcess;
    }
}
