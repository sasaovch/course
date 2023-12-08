package com.inquisition.inquisition.mapper.inquisitionprocess;

import com.inquisition.inquisition.mapper.official.OfficialRecordMapper;
import com.inquisition.inquisition.model.accusation.AccusationProcess;
import com.inquisition.inquisition.model.bible.Bible;
import com.inquisition.inquisition.model.church.Church;
import com.inquisition.inquisition.model.inquisition.InquisitionProcess;
import com.inquisition.inquisition.model.locality.Locality;
import com.inquisition.inquisition.model.official.Official;
import com.inquisition.inquisition.model.person.Person;
import com.inquisition.inquisition.models.tables.records.InquisitionProcessRecord;
import com.inquisition.inquisition.models.tables.records.OfficialRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InquisitionProcessRecordMapper implements RecordMapper<InquisitionProcessRecord, InquisitionProcess> {
//    @Autowired
//    private final AccusationProcessRepository;
    @Autowired
    private final OfficialRecordMapper officialRecordMapper;
    @Override
    public InquisitionProcess map(InquisitionProcessRecord record) {
        return null;
    }

    public InquisitionProcess mapFull(Record record) {
        InquisitionProcess process = record.into(InquisitionProcess.class);
        Integer id = record.get(com.inquisition.inquisition.models.tables.InquisitionProcess.INQUISITION_PROCESS.ID);
        process.setId(id);
        AccusationProcess accusationProcess = record.into(AccusationProcess.class);
        Integer accusationProcessId = record.get(com.inquisition.inquisition.models.tables.AccusationProcess.ACCUSATION_PROCESS.ID);
        accusationProcess.setId(accusationProcessId);

        Church church = record.into(Church.class);
        Locality locality = record.into(Locality.class);
        Official official = record.into(Official.class);
        Bible bible = record.into(Bible.class);

        process.setAccusationProcess(accusationProcess);
        church.setLocality(locality);
        process.setChurch(church);
        process.setBible(bible);

        if (record.get(com.inquisition.inquisition.models.tables.Person.PERSON.ID) != null) {
            Person person = record.into(Person.class);
            official.setPerson(person);
        }
        process.setOfficial(official);
        return process;
    }
}
