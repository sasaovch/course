package com.inquisition.inquisition.model.inquisition;


import java.time.LocalDate;

import com.inquisition.inquisition.model.accusation.AccusationProcess;
import com.inquisition.inquisition.model.bible.Bible;
import com.inquisition.inquisition.model.church.Church;
import com.inquisition.inquisition.model.official.Official;
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
