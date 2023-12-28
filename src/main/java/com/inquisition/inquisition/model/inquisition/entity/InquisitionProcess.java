package com.inquisition.inquisition.model.inquisition.entity;


import java.time.LocalDate;

import com.inquisition.inquisition.model.accusation.entity.AccusationProcess;
import com.inquisition.inquisition.model.bible.entity.Bible;
import com.inquisition.inquisition.model.church.entity.Church;
import com.inquisition.inquisition.model.official.entity.Official;
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
