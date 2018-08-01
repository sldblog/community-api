package uk.gov.justice.digital.delius.data.api;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CourtReport {
    private Long courtReportId;
    private LocalDateTime dateRequested;
    private LocalDateTime dateRequired;
    private LocalDateTime allocationDate;
    private LocalDateTime completedDate;
    private LocalDateTime sentToCourtDate;
    private LocalDateTime receivedByCourtDate;
    private String videoLink;
    private String notes;
    private Boolean punishment;
    private Boolean reductionOfCrime;
    private Boolean reformAndRehabilitation;
    private Boolean publicProtection;
    private Boolean reparation;
    private Boolean recommendationsNotStated;
    private Boolean softDeleted;
    private Long levelOfSeriousnessId;
    private Long deliveredReportReasonId;
    private Boolean section178;
    private LocalDateTime createdDatetime;
    private LocalDateTime lastUpdatedDatetime;
    private Long courtReportTypeId;
    private Long deliveredCourtReportTypeId;
    private Long offenderId;
    private Long requiredByCourtId;
    private Boolean pendingTransfer;
}