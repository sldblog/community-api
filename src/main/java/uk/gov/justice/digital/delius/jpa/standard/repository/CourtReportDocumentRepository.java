package uk.gov.justice.digital.delius.jpa.standard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.gov.justice.digital.delius.jpa.standard.entity.CourtReportDocument;

import java.util.List;

public interface CourtReportDocumentRepository extends JpaRepository<CourtReportDocument, Long> {
    @Query("select document from CourtReportDocument document, CourtReport entity where document.courtReport = entity and document.offenderId = :offenderId")
    List<CourtReportDocument> findByOffenderId(@Param("offenderId") Long offenderId);
}
