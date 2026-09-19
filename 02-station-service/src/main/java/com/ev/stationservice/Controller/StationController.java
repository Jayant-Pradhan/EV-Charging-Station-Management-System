package com.ev.stationservice.Controller;

import com.ev.stationservice.DTO.*;
import com.ev.stationservice.Entity.Station;
import com.ev.stationservice.Service.StationService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/station")
public class StationController {

    private final StationService stationService;

    //    createStation()
    @PostMapping("/create")
    public ResponseEntity<String> stationCreated(@RequestBody StationRequest stationRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(stationService.stationCreate(stationRequest));
    }

    // getStationById()
    @GetMapping("/getById/{stationId}")
    public ResponseEntity<StationResponse> getByStation(@PathVariable String stationId) {
        return ResponseEntity.ok(stationService.getByStationId(stationId));
    }

    //    getAllStations()
    @GetMapping("/getAllStation")
    public ResponseEntity<List<StationResponse>> getAllStation(){
        return ResponseEntity.ok(stationService.getAllStation());
    }

    // updateStation()
    @PutMapping("/update/{stationId}")
    public ResponseEntity<Station> updateStationDetails(@PathVariable String stationId , @RequestBody StationUpdateRequest stationUpdateRequest){
        return ResponseEntity.ok(stationService.updateStationDetails(stationId , stationUpdateRequest));
    }

    //  deleteStation()
    @DeleteMapping("/delete/{stationId}")
    public ResponseEntity<String> deleteStation(@PathVariable String stationId){
        return ResponseEntity.ok(stationService.deleteStation(stationId));
    }

    //  addChargerToStation()
    @PostMapping("/{stationId}/charger")
    public ResponseEntity<String> addChargerToStation(@RequestBody ChargerRequest chargerRequest , @PathVariable String stationId){

        return ResponseEntity.status(HttpStatus.CREATED).body(stationService.addChargerToStation(chargerRequest,stationId));
    }

    // getChargersByStation()
    @GetMapping("getCharger/{stationId}")
    public ResponseEntity<List<ChargerResponse>> getChargerByStationId(@PathVariable String stationId){
        return ResponseEntity.ok(stationService.getChargerByStationId(stationId));
    }

    //  updateCharger()
    @PutMapping("update/charger/{chargerId}")
    public ResponseEntity<String> updateCharger(@RequestBody ChargerUpdateRequest chargerUpdateRequest , @PathVariable String chargerId){
        return ResponseEntity.ok(stationService.updateCharger(chargerUpdateRequest,chargerId));
    }
}
