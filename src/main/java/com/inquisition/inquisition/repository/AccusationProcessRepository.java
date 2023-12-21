package com.inquisition.inquisition.repository;

import com.inquisition.inquisition.model.accusation.AccusationProcess;
import com.inquisition.inquisition.models.routines.FinishAccusationProcess;
import com.inquisition.inquisition.models.routines.GenerateCases;
import com.inquisition.inquisition.models.routines.StartAccusationProcess;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class AccusationProcessRepository {
    private final DSLContext dsl;

    private static final com.inquisition.inquisition.models.tables.AccusationProcess ACCUSATION_PROCESS =
            com.inquisition.inquisition.models.tables.AccusationProcess.ACCUSATION_PROCESS;

    public AccusationProcessRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Transactional(readOnly = true)
    public AccusationProcess find(Integer id) {
        return dsl.selectFrom(ACCUSATION_PROCESS)
                .where(ACCUSATION_PROCESS.ID.eq(id))
                .fetchOptional()
                .map(r -> r.into(AccusationProcess.class))
                .orElse(null);
    }

    @Transactional
    public Integer startAccusationProcess(Integer inquisitionProcessId) {
        StartAccusationProcess startAccusationProcess = new StartAccusationProcess();
        startAccusationProcess.setCurInquisitionProcessId(inquisitionProcessId);
        startAccusationProcess.execute(dsl.configuration());
        return startAccusationProcess.getReturnValue();
    }

    @Transactional
    public void finishAccusationProcess(Integer accusationProcessId) {
        FinishAccusationProcess finishAccusationProcess = new FinishAccusationProcess();
        finishAccusationProcess.setCurAccusationId(accusationProcessId);
        finishAccusationProcess.execute(dsl.configuration());
    }

    @Transactional
    public void generateCases(Integer accusationProcessId) {
        GenerateCases generateCases = new GenerateCases();
        generateCases.setAccusationProcess(accusationProcessId);
        generateCases.execute(dsl.configuration());
    }
}
