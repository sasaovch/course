package com.inquisition.inquisition.repository;

import com.inquisition.inquisition.model.accusation.AccusationProcess;
import com.inquisition.inquisition.models.routines.FinishAccusationProcess;
import com.inquisition.inquisition.models.routines.GenerateCases;
import com.inquisition.inquisition.models.routines.HandleCasesWithGraveSin;
import com.inquisition.inquisition.models.routines.HandleSimpleCases;
import com.inquisition.inquisition.models.routines.StartAccusationProcess;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.inquisition.inquisition.utils.TableAliases.ACCUSATION_PROCESS_TABLE;


@Repository
public class AccusationProcessRepository {
    private final DSLContext dsl;

    public AccusationProcessRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Transactional(readOnly = true)
    public AccusationProcess find(Integer id) {
        return dsl.selectFrom(ACCUSATION_PROCESS_TABLE)
                .where(ACCUSATION_PROCESS_TABLE.ID.eq(id))
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

    @Transactional
    public void handleSimpleCases(Integer accusationProcessId) {
        HandleSimpleCases handler = new HandleSimpleCases();
        handler.setCurInquisitionProcess(accusationProcessId);
        handler.execute(dsl.configuration());
    }

    public void handleCasesWithGraveSin(Integer accusationProcessId) {
        HandleCasesWithGraveSin handler = new HandleCasesWithGraveSin();
        handler.setCurInquisitionProcess(accusationProcessId);
        handler.execute(dsl.configuration());
    }
}
