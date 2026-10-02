package com.ev.stationservice.Controller;

import com.ev.stationservice.DTO.*;
import com.ev.stationservice.Entity.Station;
import com.ev.stationservice.Service.StationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StationControllerTest {

    @Mock
    private StationService stationService;

    @InjectMocks
    private StationController stationController;


    @Test
    void stationCreated_shouldReturnCreatedStatus() {

        StationRequest request = new StationRequest();

        when(stationService.stationCreate(request))
                .thenReturn("station created successfully");

        ResponseEntity<String> response =
                stationController.stationCreated(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(
                "station created successfully",
                response.getBody()
        );

        verify(stationService).stationCreate(request);
    }


    @Test
    void getByStation_shouldReturnStation() {

        StationResponse stationResponse = new StationResponse();
        stationResponse.setStationId("ST001");
        stationResponse.setStationName("Bhubaneswar Station");

        when(stationService.getByStationId("ST001"))
                .thenReturn(stationResponse);

        ResponseEntity<StationResponse> response =
                stationController.getByStation("ST001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ST001", response.getBody().getStationId());
        assertEquals(
                "Bhubaneswar Station",
                response.getBody().getStationName()
        );

        verify(stationService).getByStationId("ST001");
    }


    @Test
    void getAllStation_shouldReturnAllStations() {

        StationResponse station1 = new StationResponse();
        station1.setStationId("ST001");

        StationResponse station2 = new StationResponse();
        station2.setStationId("ST002");

        when(stationService.getAllStation())
                .thenReturn(List.of(station1, station2));

        ResponseEntity<List<StationResponse>> response =
                stationController.getAllStation();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(
                "ST001",
                response.getBody().get(0).getStationId()
        );

        verify(stationService).getAllStation();
    }


    @Test
    void updateStationDetails_shouldReturnUpdatedStation() {

        StationUpdateRequest request = new StationUpdateRequest();

        Station station = new Station();
        station.setStationId("ST001");
        station.setStationName("Updated Station");

        when(stationService.updateStationDetails("ST001", request))
                .thenReturn(station);

        ResponseEntity<Station> response =
                stationController.updateStationDetails(
                        "ST001",
                        request
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(
                "ST001",
                response.getBody().getStationId()
        );
        assertEquals(
                "Updated Station",
                response.getBody().getStationName()
        );

        verify(stationService)
                .updateStationDetails("ST001", request);
    }


    @Test
    void deleteStation_shouldReturnSuccessMessage() {

        when(stationService.deleteStation("ST001"))
                .thenReturn(" deleted successfully");

        ResponseEntity<String> response =
                stationController.deleteStation("ST001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(
                " deleted successfully",
                response.getBody()
        );

        verify(stationService).deleteStation("ST001");
    }


    @Test
    void addChargerToStation_shouldReturnCreatedStatus() {

        ChargerRequest request = new ChargerRequest();

        when(stationService.addChargerToStation(request, "ST001"))
                .thenReturn(
                        "charger details added to charger db successfully"
                );

        ResponseEntity<String> response =
                stationController.addChargerToStation(
                        request,
                        "ST001"
                );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(
                "charger details added to charger db successfully",
                response.getBody()
        );

        verify(stationService)
                .addChargerToStation(request, "ST001");
    }


    @Test
    void getChargerByStationId_shouldReturnChargers() {

        ChargerResponse charger1 = new ChargerResponse();
        charger1.setChargerId("CH001");

        ChargerResponse charger2 = new ChargerResponse();
        charger2.setChargerId("CH002");

        when(stationService.getChargerByStationId("ST001"))
                .thenReturn(List.of(charger1, charger2));

        ResponseEntity<List<ChargerResponse>> response =
                stationController.getChargerByStationId("ST001");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(
                "CH001",
                response.getBody().get(0).getChargerId()
        );

        verify(stationService)
                .getChargerByStationId("ST001");
    }


    @Test
    void updateCharger_shouldReturnSuccessMessage() {

        ChargerUpdateRequest request = new ChargerUpdateRequest();

        when(stationService.updateCharger(request, "CH001"))
                .thenReturn("charger is updated successfully");

        ResponseEntity<String> response =
                stationController.updateCharger(
                        request,
                        "CH001"
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(
                "charger is updated successfully",
                response.getBody()
        );

        verify(stationService)
                .updateCharger(request, "CH001");
    }
}