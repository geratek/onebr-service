package br.com.onebr.service;

import br.com.onebr.exception.NotFoundApiException;
import br.com.onebr.model.SCCMecElement;
import br.com.onebr.model.Sequencer;
import br.com.onebr.repository.SCCMecElementRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class SCCMecElementService {

    @Autowired
    private SCCMecElementRepository sccMecElementRepository;

    public List<SCCMecElement> findAll() {
        final List<SCCMecElement> sccMecElements = sccMecElementRepository.findAll();

        if (CollectionUtils.isEmpty(sccMecElements)) {
            log.warn("message=No SCC mec element found on database.");
            throw new NotFoundApiException("scc.mec.element.not.found");
        }

        return sccMecElements;
    }
}
