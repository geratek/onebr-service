package br.com.onebr.service;

import br.com.onebr.exception.NotFoundApiException;
import br.com.onebr.model.EffluxPump;
import br.com.onebr.repository.EffluxPumpRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class EffluxPumpService {

    @Autowired
    private EffluxPumpRepository effluxPumpRepository;

    public List<EffluxPump> findAll() {
        final List<EffluxPump> effluxPumps = effluxPumpRepository.findAll();

        if (CollectionUtils.isEmpty(effluxPumps)) {
            log.warn("message=No efflux pumps found on database.");
            throw new NotFoundApiException("efflux.pump.not.found");
        }

        return effluxPumps;
    }
}
