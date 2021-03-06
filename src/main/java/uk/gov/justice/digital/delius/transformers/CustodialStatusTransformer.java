package uk.gov.justice.digital.delius.transformers;

import uk.gov.justice.digital.delius.data.api.CustodialStatus;
import uk.gov.justice.digital.delius.data.api.KeyValue;
import uk.gov.justice.digital.delius.jpa.standard.entity.Disposal;
import uk.gov.justice.digital.delius.jpa.standard.entity.Release;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CustodialStatusTransformer {
    public static CustodialStatus custodialStatusOf(Disposal disposal) {
        return CustodialStatus.builder()
                .sentenceId(disposal.getDisposalId())
                .custodialType(custodialTypeOf(disposal))
                .sentence(sentenceOf(disposal))
                .mainOffence(mainOffenceOf(disposal))
                .sentenceDate(disposal.getStartDate())
                .actualReleaseDate(actualReleaseDateOf(disposal))
                .licenceExpiryDate(pssStartDateOf(disposal))
                .length(disposal.getLength())
                .lengthUnit("Months")
        .build();
    }

    private static LocalDate pssStartDateOf(Disposal disposal) {
        return disposal.getCustody().getPssStartDate();
    }

    private static LocalDate actualReleaseDateOf(Disposal disposal) {
        return disposal.getCustody()
                .getReleases()
                .stream()
                .map(Release::getActualReleaseDate)
                .max(LocalDateTime::compareTo)
                .map(LocalDateTime::toLocalDate)
                .orElse(null);
    }

    private static KeyValue mainOffenceOf(Disposal disposal) {
        return KeyValue.builder()
                .description(disposal.getEvent().getMainOffence().getOffence().getDescription()).build();
    }

    private static KeyValue sentenceOf(Disposal disposal) {
        return KeyValue.builder()
                .description(disposal.getDisposalType().getDescription())
                .build();
    }

    private static KeyValue custodialTypeOf(Disposal disposal) {
        return KeyValue.builder()
                .code(disposal.getCustody().getCustodialStatus().getCodeValue())
                .description(disposal.getCustody().getCustodialStatus().getCodeDescription())
                .build();
    }
}
