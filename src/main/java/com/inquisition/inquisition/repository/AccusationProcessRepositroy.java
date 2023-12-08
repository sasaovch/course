package com.inquisition.inquisition.repository;

import java.util.List;

import com.inquisition.inquisition.model.accusation.AccusationProcess;
import com.inquisition.inquisition.models.routines.FinishAccusationProcess;
import com.inquisition.inquisition.models.routines.GenerateCases;
import com.inquisition.inquisition.models.routines.StartAccusationProcess;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class AccusationProcessRepositroy implements CrudRepository<AccusationProcess> {
    @Autowired
    private DSLContext dsl;

    @Override
    public AccusationProcess insert(AccusationProcess accusationProcess) {
        return null;
    }

    @Override
    public AccusationProcess update(AccusationProcess accusationProcess) {
        return null;
    }

    @Override
    public AccusationProcess find(Integer id) {
        return null;
    }

    @Override
    public List<AccusationProcess> findAll(Condition condition) {
        return null;
    }

    @Override
    public Boolean delete(Integer id) {
        return null;
    }

    public Integer startAccusationProcess(Integer inquisitionProcessId) {
        StartAccusationProcess startAccusationProcess = new StartAccusationProcess();
        startAccusationProcess.setCurInquisitionProcessId(inquisitionProcessId);
        //FIXME: проверить точно ли value возвращается
        startAccusationProcess.execute(dsl.configuration());
//                .execute();
        return startAccusationProcess.getReturnValue();
    }

    public int finishAccusationProcess(Integer accusationProcessId) {
        FinishAccusationProcess finishAccusationProcess = new FinishAccusationProcess();
        finishAccusationProcess.setCurAccusationId(accusationProcessId);
        return finishAccusationProcess.execute(dsl.configuration());
    }

    public int generateCases(Integer accusationProcessId) {
        GenerateCases generateCases = new GenerateCases();
        generateCases.setAccusationProcess(accusationProcessId);
        return generateCases.execute(dsl.configuration());
    }
}
