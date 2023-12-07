package com.inquisition.inquisition.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.inquisition.inquisition.jwt.JwtUtils;
import com.inquisition.inquisition.model.accusation.AccusationRecord;
import com.inquisition.inquisition.model.inquisition.InquisitionProcess;
import com.inquisition.inquisition.model.inquisition.InquisitionProcessStartContainer;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import com.inquisition.inquisition.model.payload.PayloadWithAccusationRecords;
import com.inquisition.inquisition.model.payload.PayloadWithCollection;
import com.inquisition.inquisition.model.payload.PayloadWithInteger;
import com.inquisition.inquisition.model.person.Person;
import com.inquisition.inquisition.model.user.User;
import com.inquisition.inquisition.model.user.UserRole;
import com.inquisition.inquisition.repository.AccusationRecordRepository;
import com.inquisition.inquisition.repository.InquisitionProcessRepository;
import com.inquisition.inquisition.repository.UserRepository;
import jakarta.persistence.StoredProcedureQuery;
import org.hibernate.procedure.ProcedureCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class InquisitionServiceImpl implements InquisitionService{
    @Autowired
    InquisitionProcessRepository inquisitionProcessRepository;
    @Autowired
    AccusationRecordRepository accusationRecordRepository;
    @Autowired
    UserRepository userRepository;
    //FIXME: get all church
    @Override
    public Payload startProcess(InquisitionProcessStartContainer container) {
        User user = getUser();
        if (user == null) {
            return new BasePayload(400, "User not found.");
        }

        //FIXME: в функции нет проверки, что это фискал

//        Integer processId = inquisitionProcessRepository.startInquisitionProcess(user.getPerson().getId(), container.getLocalityId(), container.getBibleId());
        return new PayloadWithInteger(200, "Inquisition process was created.", processId);
    }
    @Override
    public Payload getCurrentInquisitionProcess(Integer localityId) {
        User user = getUser();
        if (user == null) {
            return new BasePayload(400, "User not found.");
        }

        if (localityId == null) {
            localityId = user.getPerson().getLocality().getId();
        }

        List<InquisitionProcess> processes = inquisitionProcessRepository.findInProgressByLocalityId(localityId);
        InquisitionProcess process = processes.get(0);
        int step;
        if (process.getAccusationProcess() == null) {
            step = 0;
        } else if (process.getAccusationProcess().getFinishTime() == null) {
            step = 1;
        } else if (!accusationRecordRepository.getNotResolvedAccusationRecord(process.getAccusationProcess().getId()).isEmpty()) {
            step = 2;
        } else if (process.getFinishDate() == null) {
            step = 3;
        } else {
            step = 4;
        }
        return new PayloadWithInteger(200, "", step);
    }

    @Override
    public Payload getAllInquisitionProcess(Integer localityId) {
        List<InquisitionProcess> processes;
        if (localityId == null) {
            processes = inquisitionProcessRepository.findAll();
        } else {
            processes = inquisitionProcessRepository.findInquisitionProcessesByLocalityId(localityId);
        }
        return new PayloadWithCollection<InquisitionProcess>(200, "", processes);
    }

    @Override
    //FIXME: нужно написать конвертеры в ожидаемые объекты
    public Payload getAllAccusationRecords(Long accusationProcessId) {
        if (accusationProcessId == null) {
            return new BasePayload(200, "Nothing was found");
        }
        List<AccusationRecord> records = accusationRecordRepository.findAccusationRecordsByAccusationProcessId(accusationProcessId);
        return new PayloadWithAccusationRecords(200, "", records);
    }

    private User getUser() {
        User user = new User(
                1L,
                "sasa",
                "123",
                UserRole.Inquisitor,
                ""
        );
        Person p = new Person();
        p.setId(1);
        user.setPerson(p);
        return user;
//        String username = JwtUtils.getUserName((JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication());
//        Optional<User> optUser = userRepository.findByUsername(username);
//        return optUser.orElse(null);
    }
}
