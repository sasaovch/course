package com.inquisition.inquisition.utils;

import com.inquisition.inquisition.models.tables.AccusationInvestigativeCase;
import com.inquisition.inquisition.models.tables.AccusationProcess;
import com.inquisition.inquisition.models.tables.AccusationRecord;
import com.inquisition.inquisition.models.tables.Bible;
import com.inquisition.inquisition.models.tables.BibleCommandment;
import com.inquisition.inquisition.models.tables.CaseLog;
import com.inquisition.inquisition.models.tables.Church;
import com.inquisition.inquisition.models.tables.Commandment;
import com.inquisition.inquisition.models.tables.InquisitionProcess;
import com.inquisition.inquisition.models.tables.InvestigativeCase;
import com.inquisition.inquisition.models.tables.Locality;
import com.inquisition.inquisition.models.tables.Official;
import com.inquisition.inquisition.models.tables.Person;
import com.inquisition.inquisition.models.tables.Prison;
import com.inquisition.inquisition.models.tables.Users;
import com.inquisition.inquisition.models.tables.Violation;

import static com.inquisition.inquisition.utils.Aliases.ACCUSATION_INVESTIGATIVE_CASE_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.ACCUSATION_PROCESS_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.ACCUSATION_RECORD_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.ACCUSED_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.BIBLE_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.BIBLE_COMMANDMENT_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.BISHOP_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.CASE_LOG_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.CHURCH_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.COMMANDMENT_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.INFORMER_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.INQUISITION_PROCESS_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.INVESTIGATIVE_CASE_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.LOCALITY_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.OFFICIAL_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.PERSON_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.PRISON_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.USER_ALIAS;
import static com.inquisition.inquisition.utils.Aliases.VIOLATION_ALIAS;

public final class TableAliases {
    private TableAliases() {
    }

    public static final AccusationRecord ACCUSATION_RECORD_TABLE =
            AccusationRecord.ACCUSATION_RECORD.as(ACCUSATION_RECORD_ALIAS);
    public static final Person BISHOP_TABLE = Person.PERSON.as(BISHOP_ALIAS);
    public static final Person INFORMER_TABLE = Person.PERSON.as(INFORMER_ALIAS);
    public static final Person ACCUSED_TABLE = Person.PERSON.as(ACCUSED_ALIAS);
    public static final AccusationInvestigativeCase ACCUSATION_INVESTIGATIVE_CASE_TABLE =
            AccusationInvestigativeCase.ACCUSATION_INVESTIGATIVE_CASE.as(ACCUSATION_INVESTIGATIVE_CASE_ALIAS);
    public static final InvestigativeCase INVESTIGATIVE_CASE_TABLE =
            InvestigativeCase.INVESTIGATIVE_CASE.as(INVESTIGATIVE_CASE_ALIAS);
    public static final AccusationProcess ACCUSATION_PROCESS_TABLE =
            AccusationProcess.ACCUSATION_PROCESS.as(ACCUSATION_PROCESS_ALIAS);
    public static final Person PERSON_TABLE = Person.PERSON.as(PERSON_ALIAS);
    public static final CaseLog CASE_LOG_TABLE = CaseLog.CASE_LOG.as(CASE_LOG_ALIAS);
    public static final Church CHURCH_TABLE = Church.CHURCH.as(CHURCH_ALIAS);
    public static final Locality LOCALITY_TABLE = Locality.LOCALITY.as(LOCALITY_ALIAS);
    public static final Official OFFICIAL_TABLE = Official.OFFICIAL.as(OFFICIAL_ALIAS);
    public static final Bible BIBLE_TABLE = Bible.BIBLE.as(BIBLE_ALIAS);
    public static final InquisitionProcess INQUISITION_PROCESS_TABLE =
            InquisitionProcess.INQUISITION_PROCESS.as(INQUISITION_PROCESS_ALIAS);
    public static final Commandment COMMANDMENT_TABLE = Commandment.COMMANDMENT.as(COMMANDMENT_ALIAS);
    public static final BibleCommandment BIBLE_COMMANDMENT_TABLE =
            BibleCommandment.BIBLE_COMMANDMENT.as(BIBLE_COMMANDMENT_ALIAS);
    public static final Users USER_TABLE = Users.USERS.as(USER_ALIAS);
    public static final Violation VIOLATION_TABLE = Violation.VIOLATION.as(VIOLATION_ALIAS);
    public static final Prison PRISON_TABLE = Prison.PRISON.as(PRISON_ALIAS);
}
