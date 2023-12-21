package com.inquisition.inquisition.mapper.inquisitionprocess;

import com.inquisition.inquisition.mapper.accusationrecord.AccusationRecordRecordMapper;
import com.inquisition.inquisition.mapper.bible.BibleRecordMapper;
import com.inquisition.inquisition.mapper.official.OfficialRecordMapper;
import com.inquisition.inquisition.model.accusation.AccusationProcess;
import com.inquisition.inquisition.model.bible.Bible;
import com.inquisition.inquisition.model.church.Church;
import com.inquisition.inquisition.model.inquisition.InquisitionProcess;
import com.inquisition.inquisition.model.locality.Locality;
import com.inquisition.inquisition.model.official.Official;
import com.inquisition.inquisition.model.official.OfficialName;
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
        AccusationProcess accusationProcess = mapAccusationProcess("accusation", record);
        Integer accusationProcessId = record.get(com.inquisition.inquisition.models.tables.AccusationProcess.ACCUSATION_PROCESS.ID);
        accusationProcess.setId(accusationProcessId);

        Church church = mapChurch("church", record);
        Locality locality = mapLocality("locality", record);
        Official official = mapOfficial("official", record);
        Bible bible = BibleRecordMapper.mapWithAlias("bible", record);

        process.setAccusationProcess(accusationProcess);
        church.setLocality(locality);
        process.setChurch(church);
        process.setBible(bible);

        if (record.get(com.inquisition.inquisition.models.tables.Person.PERSON.ID) != null) {
            Person person = AccusationRecordRecordMapper.convertToPerson("person", record);
            official.setPerson(person);
        }
        process.setOfficial(official);
        return process;
    }

    public AccusationProcess mapAccusationProcess(String alias, Record record) {
        AccusationProcess process = new AccusationProcess();
        process.setId(record.get(com.inquisition.inquisition.models.tables.AccusationProcess.ACCUSATION_PROCESS.as(alias).ID));
        process.setStartTime(record.get(com.inquisition.inquisition.models.tables.AccusationProcess.ACCUSATION_PROCESS.as(alias).START_TIME));
        process.setFinishTime(record.get(com.inquisition.inquisition.models.tables.AccusationProcess.ACCUSATION_PROCESS.as(alias).FINISH_TIME));
        process.setInquisitionProcessId(record.get(com.inquisition.inquisition.models.tables.AccusationProcess.ACCUSATION_PROCESS.as(alias).INQUISITION_PROCESS_ID));
        return process;
    }

    public Church mapChurch(String alias, Record record) {
        Church church = new Church();
        church.setId(record.get(com.inquisition.inquisition.models.tables.Church.CHURCH.as(alias).ID).longValue());
        church.setName(record.get(com.inquisition.inquisition.models.tables.Church.CHURCH.as(alias).NAME));
        church.setFoundationDate(record.get(com.inquisition.inquisition.models.tables.Church.CHURCH.as(alias).FOUNDATION_DATE));

        return church;
    }

    public Locality mapLocality(String alias, Record record) {
        Locality locality = new Locality();
        locality.setId(record.get(com.inquisition.inquisition.models.tables.Locality.LOCALITY.as(alias).ID));
        locality.setName(record.get(com.inquisition.inquisition.models.tables.Locality.LOCALITY.as(alias).NAME));
        locality.setFoundationDate(record.get(com.inquisition.inquisition.models.tables.Locality.LOCALITY.as(alias).FOUNDATION_DATE));

        return locality;
    }

    public Official mapOfficial(String alias, Record record) {
        Official official = new Official();
        official.setId(record.get(com.inquisition.inquisition.models.tables.Official.OFFICIAL.as(alias).ID));
        official.setPersonId(record.get(com.inquisition.inquisition.models.tables.Official.OFFICIAL.as(alias).PERSON_ID));
        official.setOfficialName(OfficialName.valueOfSql(record.get(com.inquisition.inquisition.models.tables.Official.OFFICIAL.as(alias).OFFICIAL_NAME).name()));
        official.setEmploymentDate(record.get(com.inquisition.inquisition.models.tables.Official.OFFICIAL.as(alias).EMPLOYMENT_DATE));
        official.setFiredDate(record.get(com.inquisition.inquisition.models.tables.Official.OFFICIAL.as(alias).FIRED_DATE));

        return official;
    }


}
