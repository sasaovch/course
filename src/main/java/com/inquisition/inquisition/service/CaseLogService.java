package com.inquisition.inquisition.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.inquisition.inquisition.model.accusation.entity.AccusationRecordComplex;
import com.inquisition.inquisition.model.cases.container.CaseIdContainer;
import com.inquisition.inquisition.model.cases.container.CaseWithResultContainer;
import com.inquisition.inquisition.model.cases.container.CaseWithStepContainer;
import com.inquisition.inquisition.model.cases.entity.CaseLog;
import com.inquisition.inquisition.model.cases.entity.InquisitionCaseLog;
import com.inquisition.inquisition.model.cases.payload.CaseLogForProcessPayload;
import com.inquisition.inquisition.model.cases.payload.CaseLogForPunishmentPayload;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithCollection;
import com.inquisition.inquisition.model.person.entity.Person;
import com.inquisition.inquisition.model.prison.entity.Prison;
import com.inquisition.inquisition.repository.AccusationRecordRepository;
import com.inquisition.inquisition.repository.CaseLogRepository;
import com.inquisition.inquisition.repository.InquisitionProcessRepository;
import com.inquisition.inquisition.repository.PersonRepository;
import com.inquisition.inquisition.repository.PrisonRepository;
import com.inquisition.inquisition.repository.helper.QueryFetchHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.inquisition.inquisition.utils.Messages.ERROR_WHILE_HANDLE_REQUEST;

@Service
public class CaseLogService {
    private final CaseLogRepository caseLogRepository;
    private final InquisitionProcessRepository inquisitionProcessRepository;
    @Autowired
    private AccusationRecordRepository accusationRecordRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PrisonRepository prisonRepository;

    public CaseLogService(CaseLogRepository caseLogRepository,
                          InquisitionProcessRepository inquisitionProcessRepository) {
        this.caseLogRepository = caseLogRepository;
        this.inquisitionProcessRepository = inquisitionProcessRepository;
    }

    public Payload sendToDiscussion(CaseIdContainer input) {
        caseLogRepository.sendToDiscussion(input.getId());
        return new BasePayload(200, "Sent");
    }

    public Payload finishDiscussion(CaseWithResultContainer input) {
        CaseLog caseLog = caseLogRepository.findCurrentByCaseId(input.getId());
        caseLogRepository.finishCaseLogProcess(caseLog.getId(), input.getResult(), input.getDescription());
        return new BasePayload(200, "Finished");
    }

    public Payload finishTorture(CaseWithResultContainer input) {
        CaseLog caseLog = caseLogRepository.findCurrentByCaseId(input.getId());
        caseLogRepository.finishCaseLogProcess(caseLog.getId(), input.getResult(), input.getDescription());
        return new BasePayload(200, "Finished");
    }

    public Payload sendToTorture(CaseIdContainer input) {
        caseLogRepository.sendToTorture(input.getId());
        return new BasePayload(200, "Sent");
    }

    public Payload makeTortureStep(CaseWithStepContainer input) {
        caseLogRepository.makeTortureStep(input.getId(), input.getStep());
        return new BasePayload(200, "Made");
    }

    public Payload getCasesForDiscussion(Integer inquisitionId) {
        return getCasesFor(inquisitionId, inquisitionProcessRepository::getCasesForDiscussion);
    }

    public Payload getCasesForTorture(Integer inquisitionId) {
        return getCasesFor(inquisitionId, inquisitionProcessRepository::getCasesForTorture);
    }

    public Payload getCasesForPunishment(Integer inquisitionId) {
//        return getCasesFor(inquisitionId, inquisitionProcessRepository::getCasesForPunishment);
        QueryFetchHelper<Integer, List<InquisitionCaseLog>> helper = new QueryFetchHelper<>(
                inquisitionId,
                inquisitionProcessRepository::getCasesForPunishment
        );
        List<InquisitionCaseLog> caseLogs = helper.fetch().getFirst();
        if (caseLogs == null) {
            return new BasePayload(400, ERROR_WHILE_HANDLE_REQUEST);
        }

        List<Integer> recordIds = caseLogs.stream().map(c -> c.getAccusationRecord().getId()).toList();
        QueryFetchHelper<List<Integer>, List<AccusationRecordComplex>> helperRecord = new QueryFetchHelper<>(
                recordIds,
                accusationRecordRepository::findById
        );

        List<AccusationRecordComplex> recordComplexes = helperRecord.fetch().getFirst();
        if (recordComplexes == null) {
            return new BasePayload(400, ERROR_WHILE_HANDLE_REQUEST);
        }

        Map<Integer, Person> punishmentByCaseId = caseLogs.stream().collect(Collectors.toMap(
                caseLog -> caseLog.getCaseLog().getCaseId(), c -> personRepository.find(c.getCaseLog().getPunishmentId())
        ));

        Map<Integer, Prison> prisonMap = caseLogs.stream().collect(Collectors.toMap(
                caseLog -> caseLog.getCaseLog().getCaseId(), c -> prisonRepository.find(c.getCaseLog().getPrisonId())
        ));

        Map<Integer, AccusationRecordComplex> recordById = recordComplexes.stream()
                .collect(Collectors.toMap(AccusationRecordComplex::getId, Function.identity()));
        List<CaseLogForPunishmentPayload> payload = caseLogs.stream()
                .map(c -> {
                            AccusationRecordComplex record = recordById.get(c.getAccusationRecord().getId());
                            Person person = punishmentByCaseId.get(c.getCaseLog().getCaseId());
                            Prison prison = prisonMap.get(c.getCaseLog().getCaseId());
                            return convertToPunishmentPayload(record, person, prison, c.getCaseLog());
                        }
                )
                .toList();
        return new PayloadWithCollection<>(200, payload);
    }

    private Payload getCasesFor(Integer inquisitionId, Function<Integer, List<InquisitionCaseLog>> sqlRequest) {
        QueryFetchHelper<Integer, List<InquisitionCaseLog>> helper = new QueryFetchHelper<>(
                inquisitionId,
                sqlRequest
        );
        List<InquisitionCaseLog> caseLogs = helper.fetch().getFirst();
        if (caseLogs == null) {
            return new BasePayload(400, ERROR_WHILE_HANDLE_REQUEST);
        }

        List<Integer> recordIds = caseLogs.stream().map(c -> c.getAccusationRecord().getId()).toList();
        QueryFetchHelper<List<Integer>, List<AccusationRecordComplex>> helperRecord = new QueryFetchHelper<>(
                recordIds,
                accusationRecordRepository::findById
        );

        List<AccusationRecordComplex> recordComplexes = helperRecord.fetch().getFirst();
        if (recordComplexes == null) {
            return new BasePayload(400, ERROR_WHILE_HANDLE_REQUEST);
        }

        Map<Integer, List<InquisitionCaseLog>> caseLogsById = caseLogs.stream().collect(Collectors.groupingBy(c -> c.getCaseLog().getCaseId()));

        Map<Integer, AccusationRecordComplex> recordById = recordComplexes.stream()
                .collect(Collectors.toMap(AccusationRecordComplex::getId, Function.identity()));
        List<CaseLogForProcessPayload> payload = caseLogsById.entrySet().stream()
                .map(c -> {
                    var value = c.getValue().get(0);
                            AccusationRecordComplex record = recordById.get(value.getAccusationRecord().getId());
                            return convertToPayload(record, value.getCaseLog());
                        }
                )
                .toList();
        return new PayloadWithCollection<>(200, payload);
    }

    private CaseLogForProcessPayload convertToPayload(AccusationRecordComplex recordComplex, CaseLog caseLog) {
        String informer = recordComplex.getInformer().getName() + " " + recordComplex.getInformer().getSurname();
        String bishop = recordComplex.getBishop().getName() + " " + recordComplex.getBishop().getSurname();
        String accused = recordComplex.getAccused().getName() + " " + recordComplex.getAccused().getSurname();
        String violationPlace = recordComplex.getViolationPlace();
        LocalDate dateTime = recordComplex.getViolationTime();
        String description = recordComplex.getDescription();

        CaseLogForProcessPayload payload = new CaseLogForProcessPayload();
        payload.setId(caseLog.getCaseId());
        payload.setInformer(informer);
        payload.setBishop(bishop);
        payload.setAccused(accused);
        payload.setViolationPlace(violationPlace);
        payload.setDateTime(dateTime);
        payload.setDescription(description);

        return payload;
    }

    private CaseLogForPunishmentPayload convertToPunishmentPayload(AccusationRecordComplex recordComplex, Person punishment, Prison prison, CaseLog caseLog) {
        String accused = recordComplex.getAccused().getName() + " " + recordComplex.getAccused().getSurname();
        String prisonName = prison.getName();
        String punishmentName = punishment.getName() + " " + punishment.getSurname();
        String violationDescription = recordComplex.getDescription();
        LocalDate creationDate = caseLog.getStartTime().toLocalDate();
        String description = caseLog.getDescription();

        CaseLogForPunishmentPayload payload = new CaseLogForPunishmentPayload();
        payload.setAccused(accused);
        payload.setPunishment(punishmentName);
        payload.setPrisonName(prisonName);
        payload.setCreationDate(creationDate);
        payload.setViolationDescription(violationDescription);
        payload.setDescription(description);

        return payload;
    }
}
