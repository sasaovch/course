package com.inquisition.inquisition.model.accusation;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AddAccusationRecordContainer {
    /*
    cur_informer integer, cur_bishop integer, cur_accused integer, cur_violation_place varchar(255), cur_date_time timestamp, cur_description text, cur_accusation_id integer
     */
    private Integer informer;
    private Integer bishop;
    private Integer accused;
    private String violationPlace;
    private LocalDateTime dateTime;
    private String description;
    private Integer accusationId;
}
