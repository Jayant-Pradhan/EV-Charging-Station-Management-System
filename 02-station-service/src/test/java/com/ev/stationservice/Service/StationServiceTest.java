package com.ev.stationservice.Service;

import com.ev.stationservice.DTO.*;
import com.ev.stationservice.Entity.Charger;
import com.ev.stationservice.Entity.Station;
import com.ev.stationservice.Kafka.Event.ChargerAddedEvent;
import com.ev.stationservice.Kafka.Event.StationCreatedEvent;
import com.ev.stationservice.Kafka.Producer.ChargerEventProducer;
import com.ev.stationservice.Kafka.Producer.StationEventProducer;
import com.ev.stationservice.Repository.ChargerRepository;
import com.ev.stationservice.Repository.StationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @Mock
    private StationRepository stationRepository;

    @Mock
    private ChargerRepository chargerRepository;

    @Mock
    private StationEventProducer stationEventProducer;

    @Mock
    private ChargerEventProducer chargerEventProducer;

    @InjectMocks
    private StationService stationService;


    @Test
    void stationCreate_shouldCreateStationSuccessfully() {

        StationRequest request = new StationRequest();
        request.setStationId("ST001");
        request.setStationName("Bhubaneswar Station");
        request.setLatitude(20.2961);
        request.setLongitude(85.8245);
        request.setStatus("AVAILABLE");

        when(stationRepository.findById("ST001"))
                .thenReturn(Optional.empty());

        String result = stationService.stationCreate(request);

        assertEquals(" station details added to db successfully", result);

        verify(stationRepository).save(any(Station.class));
        verify(stationEventProducer).producingStationEvent(
                any(StationCreatedEvent.class)
        );
    }


    @Test
    void stationCreate_shouldReturnMessage_whenStationAlreadyExists() {

        Station existingStation = new Station();

        when(stationRepository.findById("ST001"))
                .thenReturn(Optional.of(existingStation));

        StationRequest request = new StationRequest();
        request.setStationId("ST001");

        String result = stationService.stationCreate(request);

        assertEquals(
                "station details is already available or already existed",
                result
        );

        verify(stationRepository, never()).save(any(Station.class));
        verify(stationEventProducer, never())
                .producingStationEvent(any(StationCreatedEvent.class));
    }


    @Test
    void getByStationId_shouldReturnStation_whenStationExists() {

        Station station = new Station();
        station.setStationId("ST001");
        station.setStationName("Bhubaneswar Station");
        station.setLatitude(20.2961);
        station.setLongitude(85.8245);
        station.setStatus("AVAILABLE");

        when(stationRepository.findById("ST001"))
                .thenReturn(Optional.of(station));

        StationResponse response =
                stationService.getByStationId("ST001");

        assertNotNull(response);
        assertEquals("ST001", response.getStationId());
        assertEquals("Bhubaneswar Station", response.getStationName());
        assertEquals(20.2961, response.getLatitude());
        assertEquals(85.8245, response.getLongitude());
        assertEquals("AVAILABLE", response.getStatus());
    }


    @Test
    void getByStationId_shouldThrowException_whenStationDoesNotExist() {

        when(stationRepository.findById("ST999"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> stationService.getByStationId("ST999")
        );

        assertEquals("Station not found", exception.getMessage());
    }


    @Test
    void getAllStation_shouldReturnAllStations() {

        Station station1 = new Station();
        station1.setStationId("ST001");
        station1.setStationName("Station One");
        station1.setLatitude(20.1);
        station1.setLongitude(85.1);
        station1.setStatus("AVAILABLE");

        Station station2 = new Station();
        station2.setStationId("ST002");
        station2.setStationName("Station Two");
        station2.setLatitude(21.1);
        station2.setLongitude(86.1);
        station2.setStatus("BUSY");

        when(stationRepository.findAll())
                .thenReturn(List.of(station1, station2));

        List<StationResponse> result =
                stationService.getAllStation();

        assertEquals(2, result.size());
        assertEquals("ST001", result.get(0).getStationId());
        assertEquals("ST002", result.get(1).getStationId());
    }


    @Test
    void getAllStation_shouldReturnEmptyList_whenNoStationsExist() {

        when(stationRepository.findAll())
                .thenReturn(new ArrayList<>());

        List<StationResponse> result =
                stationService.getAllStation();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void updateStationDetails_shouldUpdateStationSuccessfully() {

        Station station = new Station();
        station.setStationId("ST001");

        StationUpdateRequest request = new StationUpdateRequest();
        request.setStationName("Updated Station");
        request.setLatitude(22.1);
        request.setLongitude(87.1);
        request.setStatus("BUSY");

        when(stationRepository.findById("ST001"))
                .thenReturn(Optional.of(station));

        when(stationRepository.save(station))
                .thenReturn(station);

        Station result =
                stationService.updateStationDetails("ST001", request);

        assertNotNull(result);
        assertEquals("Updated Station", result.getStationName());
        assertEquals(22.1, result.getLatitude());
        assertEquals(87.1, result.getLongitude());
        assertEquals("BUSY", result.getStatus());

        verify(stationRepository).save(station);
    }


    @Test
    void updateStationDetails_shouldThrowException_whenStationDoesNotExist() {

        StationUpdateRequest request = new StationUpdateRequest();

        when(stationRepository.findById("ST999"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> stationService.updateStationDetails("ST999", request)
        );

        assertEquals("station not found for update", exception.getMessage());
    }


    @Test
    void deleteStation_shouldDeleteStationSuccessfully() {

        Station station = new Station();

        when(stationRepository.findById("ST001"))
                .thenReturn(Optional.of(station));

        String result =
                stationService.deleteStation("ST001");

        assertEquals(" deleted successfully", result);

        verify(stationRepository).deleteById("ST001");
    }


    @Test
    void deleteStation_shouldThrowException_whenStationDoesNotExist() {

        when(stationRepository.findById("ST999"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> stationService.deleteStation("ST999")
        );

        assertEquals(
                "station is not found for delete",
                exception.getMessage()
        );

        verify(stationRepository, never()).deleteById(anyString());
    }


    @Test
    void addChargerToStation_shouldAddChargerSuccessfully() {

        Station station = new Station();

        ChargerRequest request = new ChargerRequest();
        request.setChargerId("CH001");
        request.setChargerNumber("C001");
        request.setChargerType("DC_FAST");
        request.setPower(50.0);
        request.setStatus("AVAILABLE");

        when(stationRepository.findById("ST001"))
                .thenReturn(Optional.of(station));

        String result =
                stationService.addChargerToStation(request, "ST001");

        assertEquals(
                "charger details added to charger db successfully",
                result
        );

        verify(chargerRepository).save(any(Charger.class));
        verify(chargerEventProducer).producingChargerEvent(
                any(ChargerAddedEvent.class)
        );
    }


    @Test
    void addChargerToStation_shouldThrowException_whenStationDoesNotExist() {

        ChargerRequest request = new ChargerRequest();

        when(stationRepository.findById("ST999"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> stationService.addChargerToStation(
                        request, "ST999"
                )
        );

        assertEquals("station is not found", exception.getMessage());

        verify(chargerRepository, never())
                .save(any(Charger.class));
    }


    @Test
    void getChargerByStationId_shouldReturnChargers_whenStationExists() {

        Station station = new Station();

        Charger charger1 = new Charger();
        charger1.setChargerId("CH001");
        charger1.setChargerNumber("C001");
        charger1.setChargerType("DC_FAST");
        charger1.setPower(50.0);
        charger1.setStatus("AVAILABLE");

        Charger charger2 = new Charger();
        charger2.setChargerId("CH002");
        charger2.setChargerNumber("C002");
        charger2.setChargerType("AC");
        charger2.setPower(22.0);
        charger2.setStatus("BUSY");

        station.setChargers(List.of(charger1, charger2));

        when(stationRepository.findById("ST001"))
                .thenReturn(Optional.of(station));

        List<ChargerResponse> result =
                stationService.getChargerByStationId("ST001");

        assertEquals(2, result.size());
        assertEquals("CH001", result.get(0).getChargerId());
        assertEquals("CH002", result.get(1).getChargerId());
    }


    @Test
    void getChargerByStationId_shouldReturnEmptyList_whenStationHasNoChargers() {

        Station station = new Station();
        station.setChargers(new ArrayList<>());

        when(stationRepository.findById("ST001"))
                .thenReturn(Optional.of(station));

        List<ChargerResponse> result =
                stationService.getChargerByStationId("ST001");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void getChargerByStationId_shouldThrowException_whenStationDoesNotExist() {

        when(stationRepository.findById("ST999"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> stationService.getChargerByStationId("ST999")
        );

        assertEquals("Station not found", exception.getMessage());
    }


    @Test
    void updateCharger_shouldUpdateChargerSuccessfully() {

        Charger charger = new Charger();

        ChargerUpdateRequest request = new ChargerUpdateRequest();
        request.setChargerNumber("C999");
        request.setChargerType("DC_FAST");
        request.setPower(100.0);
        request.setStatus("AVAILABLE");

        when(chargerRepository.findById("CH001"))
                .thenReturn(Optional.of(charger));

        String result =
                stationService.updateCharger(request, "CH001");

        assertEquals(
                "charger is updated successfully",
                result
        );

        verify(chargerRepository).save(charger);
    }


    @Test
    void updateCharger_shouldThrowException_whenChargerDoesNotExist() {

        ChargerUpdateRequest request = new ChargerUpdateRequest();

        when(chargerRepository.findById("CH999"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> stationService.updateCharger(request, "CH999")
        );

        assertEquals(
                "charger is not found",
                exception.getMessage()
        );

        verify(chargerRepository, never())
                .save(any(Charger.class));
    }
}