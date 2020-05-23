package uk.gov.justice.digital.delius.transformers;

import org.junit.Test;
import uk.gov.justice.digital.delius.jpa.standard.entity.Deregistration;
import uk.gov.justice.digital.delius.jpa.standard.entity.ProbationArea;
import uk.gov.justice.digital.delius.jpa.standard.entity.RegisterType;
import uk.gov.justice.digital.delius.jpa.standard.entity.Registration;
import uk.gov.justice.digital.delius.jpa.standard.entity.Staff;
import uk.gov.justice.digital.delius.jpa.standard.entity.StandardReference;
import uk.gov.justice.digital.delius.jpa.standard.entity.Team;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationTransformerTest {

    @Test
    public void registerMappedFromRegisterTypeFlagReferenceData() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .registerType(
                                aRegistration()
                                        .getRegisterType()
                                        .toBuilder()
                                        .registerTypeFlag(
                                                StandardReference
                                                        .builder()
                                                        .codeValue("1")
                                                        .codeDescription("RoSH")
                                                        .build())
                                        .build())
                        .build())
                .getRegister())
                .isNotNull()
                .hasFieldOrPropertyWithValue("code", "1")
                .hasFieldOrPropertyWithValue("description", "RoSH");

    }

    @Test
    public void typeMappedFromRegisterType() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .registerType(
                                aRegistration()
                                        .getRegisterType()
                                        .toBuilder()
                                        .code("RVHR")
                                        .description("Very High RoSH")
                                        .build())
                        .build())
                .getType())
                .isNotNull()
                .hasFieldOrPropertyWithValue("code", "RVHR")
                .hasFieldOrPropertyWithValue("description", "Very High RoSH");

    }

    @Test
    public void registeringOfficerMappedFromStaff() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .registeringStaff(
                                Staff
                                .builder()
                                .forename("John")
                                .surname("Smith")
                                .build()
                        )
                        .build())
                .getRegisteringOfficer())
                .isNotNull()
                .hasFieldOrPropertyWithValue("forenames", "John")
                .hasFieldOrPropertyWithValue("surname", "Smith");

    }

    @Test
    public void registeringTeamMappedFromTeam() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .registeringTeam(
                                aRegistration()
                                        .getRegisteringTeam()
                                        .toBuilder()
                                        .code("AA")
                                        .description("Sheffield")
                                        .build()
                        )
                        .build())
                .getRegisteringTeam())
                .isNotNull()
                .hasFieldOrPropertyWithValue("code", "AA")
                .hasFieldOrPropertyWithValue("description", "Sheffield");

    }

    @Test
    public void registeringProbationAreaMappedFromTeamsProbationArea() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .registeringTeam(
                                aRegistration()
                                .getRegisteringTeam()
                                .toBuilder()
                                .probationArea(ProbationArea.builder().code("NWNPS").description("North West").build())
                                .build()
                        )
                        .build())
                .getRegisteringProbationArea())
                .isNotNull()
                .hasFieldOrPropertyWithValue("code", "NWNPS")
                .hasFieldOrPropertyWithValue("description", "North West");

    }

    @Test
    public void reviewPeriodMonthsMappedFromRegisterTypeReviewPeriod() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .registerType(
                                aRegistration()
                                        .getRegisterType()
                                        .toBuilder()
                                        .registerReviewPeriod(6L)
                                        .build())
                        .build())
                .getReviewPeriodMonths())
                .isNotNull()
                .isEqualTo(6L);

    }

    @Test
    public void registerLevelMappedFromRegisterLevelWhenPresent() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .registerLevel(StandardReference
                                .builder()
                                .codeValue("1031")
                                .codeDescription("MAPPA Level 2")
                                .build())
                        .build())
                .getRegisterLevel())
                .isNotNull()
                .hasFieldOrPropertyWithValue("code", "1031")
                .hasFieldOrPropertyWithValue("description", "MAPPA Level 2");

    }

    @Test
    public void registerLevelNotMappedFromRegisterLevelWhenAbsent() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .registerLevel(null)
                        .build())
                .getRegisterLevel())
                .isNull();

    }

    @Test
    public void registerCategoryMappedFromRegisterLevelWhenPresent() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .registerCategory(StandardReference
                                .builder()
                                .codeValue("1026")
                                .codeDescription("MAPPA Cat 1")
                                .build())
                        .build())
                .getRegisterCategory())
                .isNotNull()
                .hasFieldOrPropertyWithValue("code", "1026")
                .hasFieldOrPropertyWithValue("description", "MAPPA Cat 1");

    }

    @Test
    public void registerCategoryNotMappedFromRegisterLevelWhenAbsent() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .registerCategory(null)
                        .build())
                .getRegisterCategory())
                .isNull();

    }

    @Test
    public void warnUserMappedFromRegisterTypeAlertMessage_Yes() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .registerType(
                                aRegistration()
                                        .getRegisterType()
                                        .toBuilder()
                                        .alertMessage("Y")
                                        .build())
                        .build())
                .isWarnUser())
                .isTrue();
    }

    @Test
    public void warnUserMappedFromRegisterTypeAlertMessage_No() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .registerType(
                                aRegistration()
                                        .getRegisterType()
                                        .toBuilder()
                                        .alertMessage("N")
                                        .build())
                        .build())
                .isWarnUser())
                .isFalse();
    }

    @Test
    public void warnUserMappedFromRegisterTypeAlertMessage_Absent() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .registerType(
                                aRegistration()
                                        .getRegisterType()
                                        .toBuilder()
                                        .alertMessage(null)
                                        .build())
                        .build())
                .isWarnUser())
                .isFalse();
    }

    @Test
    public void activeMappedFromDeregisteredReversed_1() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .deregistered(1L)
                        .build())
                .isActive())
                .isFalse();
    }

    @Test
    public void activeMappedFromDeregisteredReversed_0() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .deregistered(0L)
                        .build())
                .isActive())
                .isTrue();
    }

    @Test
    public void activeMappedFromDeregisteredReversed_null() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .deregistered(null)
                        .build())
                .isActive())
                .isTrue();
    }


    @Test
    public void endDateMappedFromDeregistrationWhenDeregistered() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .deregistered(1L)
                        .deregistration(
                                aDeregistration()
                                        .toBuilder()
                                        .deregistrationDate(LocalDate.now())
                                        .build()
                        )
                        .build())
                .getEndDate())
                .isEqualTo(LocalDate.now());
    }

    @Test
    public void endDateWillBeBullWhenNotDeregistered() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .deregistered(0L)
                        .deregistration(
                                aDeregistration()
                                        .toBuilder()
                                        .deregistrationDate(LocalDate.now())
                                        .build()
                        )
                        .build())
                .getEndDate())
                .isNull();
    }



    @Test
    public void deregisteringOfficerMappedFromStaff() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .deregistered(1L)
                        .deregistration(
                                aDeregistration()
                                        .toBuilder()
                                        .deregisteringStaff(
                                                aDeregistration()
                                                        .getDeregisteringStaff()
                                                        .toBuilder()
                                                        .forename("John")
                                                        .surname("Smith")
                                                .build()

                                        )
                                        .build()
                        )
                        .build())
                .getDeregisteringOfficer())
                .isNotNull()
                .hasFieldOrPropertyWithValue("forenames", "John")
                .hasFieldOrPropertyWithValue("surname", "Smith");

    }

    @Test
    public void deregisteringTeamMappedFromTeam() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .deregistered(1L)
                        .deregistration(
                                aDeregistration()
                                        .toBuilder()
                                        .deregisteringTeam(
                                                aDeregistration()
                                                .getDeregisteringTeam()
                                                .toBuilder()
                                                .code("AA")
                                                .description("Sheffield")
                                                .build()

                                        )
                                        .build()
                        )
                        .build())
                .getDeregisteringTeam())
                .isNotNull()
                .hasFieldOrPropertyWithValue("code", "AA")
                .hasFieldOrPropertyWithValue("description", "Sheffield");

    }
    @Test
    public void deregisteringProbationAreaMappedFromTeamArea() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .deregistered(1L)
                        .deregistration(
                                aDeregistration()
                                        .toBuilder()
                                        .deregisteringTeam(
                                                aDeregistration()
                                                .getDeregisteringTeam()
                                                .toBuilder()
                                                .probationArea(ProbationArea.builder().code("NWNPS").description("North West").build())
                                                .build()

                                        )
                                        .build()
                        )
                        .build())
                .getDeregisteringProbationArea())
                .isNotNull()
                .hasFieldOrPropertyWithValue("code", "NWNPS")
                .hasFieldOrPropertyWithValue("description", "North West");

    }
    @Test
    public void deregisteringNotesMappedFromDeregistration() {
        assertThat(RegistrationTransformer.registrationOf(
                aRegistration()
                        .toBuilder()
                        .deregistered(1L)
                        .deregistration(
                                aDeregistration()
                                        .toBuilder()
                                        .deregisteringNotes("No longer an issue")
                                        .build()
                        )
                        .build())
                .getDeregisteringNotes())
                .isEqualTo("No longer an issue");

    }

    private Registration aRegistration() {
        return Registration.builder()
                .registerType(RegisterType
                        .builder()
                        .registerTypeFlag(StandardReference
                                .builder()
                                .codeValue("A")
                                .codeDescription("Alerts")
                                .build())
                        .code("AV2S")
                        .description("Risk to staff")
                        .build())
                .registeringTeam(Team
                        .builder()
                        .probationArea(
                                ProbationArea
                                        .builder()
                                        .description("North West")
                                        .code("NWNPS")
                                        .build())
                        .build())
                .build();
    }

    private Deregistration aDeregistration() {
        return Deregistration
                .builder()
                .deregisteringStaff(
                        Staff
                                .builder()
                                .forename("John")
                                .surname("Smith")
                                .build()
                )
                .deregisteringTeam(
                        Team
                                .builder()
                                .probationArea(
                                        ProbationArea
                                                .builder()
                                                .description("North West")
                                                .code("NWNPS")
                                                .build())
                                .build()
                )
                .build();

    }

}
