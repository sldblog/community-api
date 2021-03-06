package uk.gov.justice.digital.delius.service;

import com.google.common.collect.ImmutableList;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.justice.digital.delius.jpa.standard.entity.Event;
import uk.gov.justice.digital.delius.jpa.standard.repository.EventRepository;
import uk.gov.justice.digital.delius.jpa.standard.repository.OffenderRepository;
import uk.gov.justice.digital.delius.service.ConvictionService.SingleActiveCustodyConvictionNotFoundException;
import uk.gov.justice.digital.delius.entitybuilders.EventEntityBuilder;
import uk.gov.justice.digital.delius.entitybuilders.KeyDateEntityBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.justice.digital.delius.util.EntityHelper.aCustodyEvent;
import static uk.gov.justice.digital.delius.util.EntityHelper.aKeyDate;

@RunWith(MockitoJUnitRunner.class)
public class ConvictionService_DeleteCustodyKeyDateTest {

    private ConvictionService convictionService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private OffenderRepository offenderRepository;

    @Mock
    private EventEntityBuilder eventEntityBuilder;

    @Mock
    private SpgNotificationService spgNotificationService;

    @Mock
    private LookupSupplier lookupSupplier;

    @Captor
    private ArgumentCaptor<Event> eventArgumentCaptor;

    @Mock
    private IAPSNotificationService iapsNotificationService;

    @Mock
    private ContactService contactService;


    @Before
    public void setUp() {
        convictionService = new ConvictionService(true, eventRepository, offenderRepository, eventEntityBuilder, spgNotificationService, lookupSupplier, new KeyDateEntityBuilder(lookupSupplier), iapsNotificationService, contactService);
    }


    @Test
    public void deletedCustodyKeyDateByOffenderIdIsRemoved() throws SingleActiveCustodyConvictionNotFoundException {
        val event = aCustodyEvent(1L, new ArrayList<>());
        event.getDisposal().getCustody().getKeyDates().add(aKeyDate("POM1", "POM Handover expected start date", LocalDate.now()));
        event.getDisposal().getCustody().getKeyDates().add(aKeyDate("POM2", "Some other key date", LocalDate.now()));
        assertThat(event.getDisposal().getCustody().getKeyDates()).hasSize(2);

        when(eventRepository.findByOffenderIdWithCustody(999L)).thenReturn(ImmutableList.of(event));

        convictionService.deleteCustodyKeyDateByOffenderId(
                999L,
                "POM1");

        verify(eventRepository).save(eventArgumentCaptor.capture());

        assertThat(eventArgumentCaptor.getValue().getDisposal().getCustody().getKeyDates()).hasSize(1);
        assertThat(eventArgumentCaptor.getValue().getDisposal().getCustody().getKeyDates().get(0).getKeyDateType().getCodeValue()).isEqualTo("POM2");
    }

    @Test
    public void spgIsNotifiedOfDeletedCustodyKeyDateByOffenderId() throws SingleActiveCustodyConvictionNotFoundException {
        val event = aCustodyEvent(1L, new ArrayList<>());
        val keyDateToBeRemoved = aKeyDate("POM1", "POM Handover expected start date", LocalDate.now());
        event.getDisposal().getCustody().getKeyDates().add(keyDateToBeRemoved);

        when(eventRepository.findByOffenderIdWithCustody(999L)).thenReturn(ImmutableList.of(event));

        convictionService.deleteCustodyKeyDateByOffenderId(
                999L,
                "POM1");

        verify(spgNotificationService).notifyDeletedCustodyKeyDate(keyDateToBeRemoved, event);
    }

    @Test
    public void iapsIsNotNotifiedOfDeleteByOffenderIdIdAndWhenSentenceExpiryNotAffected() throws SingleActiveCustodyConvictionNotFoundException {
        val event = aCustodyEvent(
                1L,
                listOf(aKeyDate("POM1", "POM Handover expected start date", LocalDate.now().minusDays(1))));

        when(eventRepository.findByOffenderIdWithCustody(999L)).thenReturn(ImmutableList.of(event));

        convictionService.deleteCustodyKeyDateByOffenderId(
                999L,
                "POM1");

        verify(iapsNotificationService, never()).notifyEventUpdated(any());
    }

    @Test
    public void iapsIsNotifiedOfDeleteWhenByOffenderIdAndWhenSentenceExpiryIsAffected() throws SingleActiveCustodyConvictionNotFoundException {
        val event = aCustodyEvent(
                1L,
                listOf(aKeyDate("SED", "Sentence Expiry Date", LocalDate.now().minusDays(1))));

        when(eventRepository.findByOffenderIdWithCustody(999L)).thenReturn(ImmutableList.of(event));

        convictionService.deleteCustodyKeyDateByOffenderId(
                999L,
                "SED");

        verify(iapsNotificationService).notifyEventUpdated(event);
    }


    @Test
    public void deletedCustodyKeyDateByConvictionIdIsRemoved() {
        val event = aCustodyEvent(1L, new ArrayList<>());
        event.getDisposal().getCustody().getKeyDates().add(aKeyDate("POM1", "POM Handover expected start date", LocalDate.now()));
        event.getDisposal().getCustody().getKeyDates().add(aKeyDate("POM2", "Some other key date", LocalDate.now()));
        assertThat(event.getDisposal().getCustody().getKeyDates()).hasSize(2);

        when(eventRepository.getOne(999L)).thenReturn(event);

        convictionService.deleteCustodyKeyDateByConvictionId(
                999L,
                "POM1");

        verify(eventRepository).save(eventArgumentCaptor.capture());

        assertThat(eventArgumentCaptor.getValue().getDisposal().getCustody().getKeyDates()).hasSize(1);
        assertThat(eventArgumentCaptor.getValue().getDisposal().getCustody().getKeyDates().get(0).getKeyDateType().getCodeValue()).isEqualTo("POM2");
    }

    @Test
    public void spgIsNotifiedOfDeletedCustodyKeyDateByConvictionId() {
        val event = aCustodyEvent(1L, new ArrayList<>());
        val keyDateToBeRemoved = aKeyDate("POM1", "POM Handover expected start date", LocalDate.now());
        event.getDisposal().getCustody().getKeyDates().add(keyDateToBeRemoved);

        when(eventRepository.getOne(999L)).thenReturn(event);

        convictionService.deleteCustodyKeyDateByConvictionId(
                999L,
                "POM1");

        verify(spgNotificationService).notifyDeletedCustodyKeyDate(keyDateToBeRemoved, event);
    }

    @Test
    public void iapsIsNotNotifiedOfDeleteByConvictionIdAndWhenSentenceExpiryNotAffected() {
        val event = aCustodyEvent(
                1L,
                listOf(aKeyDate("POM1", "POM Handover expected start date", LocalDate.now().minusDays(1))));

        when(eventRepository.getOne(999L)).thenReturn(event);

        convictionService.deleteCustodyKeyDateByConvictionId(
                999L,
                "POM1");

        verify(iapsNotificationService, never()).notifyEventUpdated(any());
    }

    @Test
    public void iapsIsNotifiedOfDeleteWhenByConvictionIdAndWhenSentenceExpiryIsAffected() {
        val event = aCustodyEvent(
                1L,
                listOf(aKeyDate("SED", "Sentence Expiry Date", LocalDate.now().minusDays(1))));

        when(eventRepository.getOne(999L)).thenReturn(event);

        convictionService.deleteCustodyKeyDateByConvictionId(
                999L,
                "SED");

        verify(iapsNotificationService).notifyEventUpdated(event);
    }


    @Test
    public void nothingIsSavedWhenCustodyKeyDateByOffenderIdIsNotFound() throws SingleActiveCustodyConvictionNotFoundException {
        val event = aCustodyEvent(1L, new ArrayList<>());
        event.getDisposal().getCustody().getKeyDates().add(aKeyDate("POM2", "Some other key date", LocalDate.now()));

        when(eventRepository.findByOffenderIdWithCustody(999L)).thenReturn(ImmutableList.of(event));

        convictionService.deleteCustodyKeyDateByOffenderId(
                999L,
                "POM1");

        verify(eventRepository, never()).save(event);

    }

    @Test
    public void nothingIsSavedWhenCustodyKeyDateByConvictionIdIsNotFound() {
        val event = aCustodyEvent(1L, new ArrayList<>());
        event.getDisposal().getCustody().getKeyDates().add(aKeyDate("POM2", "Some other key date", LocalDate.now()));

        when(eventRepository.getOne(999L)).thenReturn(event);

        convictionService.deleteCustodyKeyDateByConvictionId(
                999L,
                "POM1");

        verify(eventRepository, never()).save(event);

    }

    @Test
    public void shouldNotAllowKeyDateToBeDeletedWhenMoreThanOneActiveCustodialEvent() {
        val activeCustodyEvent1 = aCustodyEvent(1L, new ArrayList<>())
                .toBuilder()
                .activeFlag(1L)
                .build();
        val activeCustodyEvent2 = aCustodyEvent(2L, new ArrayList<>())
                .toBuilder()
                .activeFlag(1L)
                .build();

        when(eventRepository.findByOffenderIdWithCustody(999L)).thenReturn(ImmutableList.of(activeCustodyEvent1, activeCustodyEvent2));

        assertThatThrownBy(() -> convictionService.deleteCustodyKeyDateByOffenderId(
                999L,
                "POM1")).isInstanceOf(SingleActiveCustodyConvictionNotFoundException.class);

    }

    @Test
    public void shouldAllowKeyDateToBeDeletedByConvictionIdWhenEvenWhenCustodialEventIsNoLongerActive() {
        val inactiveCustodyEvent = aCustodyEvent(2L, new ArrayList<>())
                .toBuilder()
                .activeFlag(0L)
                .build();
        inactiveCustodyEvent.getDisposal().getCustody().getKeyDates().add(aKeyDate("POM1", "POM Handover expected start date", LocalDate.now()));

        when(eventRepository.getOne(999L)).thenReturn(inactiveCustodyEvent);

        convictionService.deleteCustodyKeyDateByConvictionId(
                999L,
                "POM1");

        verify(eventRepository).save(any());
    }

    @Test
    public void keyDateRemovedFromActiveCustodyRecord() throws SingleActiveCustodyConvictionNotFoundException {
        val activeCustodyEvent = aCustodyEvent(1L, new ArrayList<>())
                .toBuilder()
                .activeFlag(1L)
                .build();
        activeCustodyEvent.getDisposal().getCustody().getKeyDates().add(aKeyDate("POM1", "POM Handover expected start date", LocalDate.now()));

        val inactiveCustodyEvent = aCustodyEvent(2L, new ArrayList<>())
                .toBuilder()
                .activeFlag(0L)
                .build();
        inactiveCustodyEvent.getDisposal().getCustody().getKeyDates().add(aKeyDate("POM1", "POM Handover expected start date", LocalDate.now()));
        val event = aCustodyEvent(2L, new ArrayList<>())
                .toBuilder()
                .activeFlag(0L)
                .build();
        val terminatedDisposal = event.getDisposal().toBuilder().terminationDate(LocalDate.now()).build();
        val activeEventButTerminatedCustodyEvent = event.toBuilder().disposal(terminatedDisposal).build();
        activeEventButTerminatedCustodyEvent.getDisposal().getCustody().getKeyDates().add(aKeyDate("POM1", "POM Handover expected start date", LocalDate.now()));

        when(eventRepository.findByOffenderIdWithCustody(999L)).thenReturn(ImmutableList.of(activeCustodyEvent, inactiveCustodyEvent, activeEventButTerminatedCustodyEvent));

        convictionService.deleteCustodyKeyDateByOffenderId(
                999L,
                "POM1");

        assertThat(activeCustodyEvent.getDisposal().getCustody().getKeyDates()).hasSize(0);
        assertThat(inactiveCustodyEvent.getDisposal().getCustody().getKeyDates()).hasSize(1);
        assertThat(activeEventButTerminatedCustodyEvent.getDisposal().getCustody().getKeyDates()).hasSize(1);

    }

    private static <T> List<T> listOf(T item) {
        return new ArrayList<>(singletonList(item));
    }
}
