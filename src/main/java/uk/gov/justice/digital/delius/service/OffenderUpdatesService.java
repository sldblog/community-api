package uk.gov.justice.digital.delius.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import uk.gov.justice.digital.delius.controller.NotFoundException;
import uk.gov.justice.digital.delius.data.api.OffenderDelta;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class OffenderUpdatesService {

    private final OffenderDeltaService offenderDeltaService;
    @SuppressWarnings("FieldMayBeFinal")
    private int retries = 10;

    public OffenderUpdatesService(OffenderDeltaService offenderDeltaService) {
        this.offenderDeltaService = offenderDeltaService;
    }

    public Optional<OffenderDelta> getNextUpdate() {
        for(int i=0; i<retries; i++) {
            try {
                return offenderDeltaService.lockNext();
            } catch(ConcurrencyFailureException ex) {
                log.warn("Received ConcurrencyFailureException while trying to getNextUpdate");
            }
        }
        throw new OffenderDeltaLockedException();
    }

    @Transactional
    public void deleteUpdate(Long offenderDeltaId) {
        try {
            offenderDeltaService.deleteDelta(offenderDeltaId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Update not found");
        }
    }
}