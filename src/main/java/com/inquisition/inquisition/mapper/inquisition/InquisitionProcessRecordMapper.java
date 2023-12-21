package com.inquisition.inquisition.mapper.inquisition;

import java.time.LocalDate;

import com.inquisition.inquisition.mapper.accusation.AccusationRecordMapper;
import com.inquisition.inquisition.mapper.bible.BibleRecordMapper;
import com.inquisition.inquisition.mapper.church.ChurchRecordMapper;
import com.inquisition.inquisition.mapper.locality.LocalityRecordMapper;
import com.inquisition.inquisition.mapper.official.OfficialRecordMapper;
import com.inquisition.inquisition.mapper.person.PersonRecordMapper;
import com.inquisition.inquisition.model.accusation.AccusationProcess;
import com.inquisition.inquisition.model.bible.Bible;
import com.inquisition.inquisition.model.church.Church;
import com.inquisition.inquisition.model.inquisition.InquisitionProcess;
import com.inquisition.inquisition.model.locality.Locality;
import com.inquisition.inquisition.model.official.Official;
import com.inquisition.inquisition.model.person.Person;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.inquisition.inquisition.utils.TableAliases.INQUISITION_PROCESS_TABLE;
import static com.inquisition.inquisition.utils.TableAliases.PERSON_TABLE;

@Component
public class InquisitionProcessRecordMapper {
    private final AccusationRecordMapper accusationRecordMapper;
    private final ChurchRecordMapper churchRecordMapper;
    private final LocalityRecordMapper localityRecordMapper;
    private final OfficialRecordMapper officialRecordMapper;
    private final BibleRecordMapper bibleRecordMapper;
    private final PersonRecordMapper personRecordMapper;

    public InquisitionProcessRecordMapper(AccusationRecordMapper accusationRecordMapper,
                                          ChurchRecordMapper churchRecordMapper,
                                          LocalityRecordMapper localityRecordMapper,
                                          OfficialRecordMapper officialRecordMapper,
                                          BibleRecordMapper bibleRecordMapper, PersonRecordMapper personRecordMapper) {
        this.accusationRecordMapper = accusationRecordMapper;
        this.churchRecordMapper = churchRecordMapper;
        this.localityRecordMapper = localityRecordMapper;
        this.officialRecordMapper = officialRecordMapper;
        this.bibleRecordMapper = bibleRecordMapper;
        this.personRecordMapper = personRecordMapper;
    }

    public InquisitionProcess mapInquisitionProcess(Record record) {
        InquisitionProcess process = new InquisitionProcess();
        Integer id = record.get(INQUISITION_PROCESS_TABLE.ID);
        LocalDate startDate =
                record.get(INQUISITION_PROCESS_TABLE.START_DATA);
        LocalDate finishDate =
                record.get(INQUISITION_PROCESS_TABLE.FINISH_DATA);

        process.setId(id);
        process.setStartData(startDate);
        process.setFinishData(finishDate);

        AccusationProcess accusationProcess = accusationRecordMapper.mapAccusationProcess(record);
        Church church = churchRecordMapper.mapChurch(record);
        Locality locality = localityRecordMapper.mapLocality(record);
        Official official = officialRecordMapper.mapOfficial(record);
        Bible bible = bibleRecordMapper.mapBible(record);

        process.setAccusationProcess(accusationProcess);
        church.setLocality(locality);
        process.setChurch(church);
        process.setBible(bible);

        if (record.indexOf(PERSON_TABLE.ID) != 0) {
            Person person = personRecordMapper.mapPerson(record);
            official.setPerson(person);
        }
        process.setOfficial(official);
        return process;
    }
}
