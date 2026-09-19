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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StationService {

    private final StationRepository stationRepository;
    private final ChargerRepository chargerRepository;
    private final StationEventProducer stationEventProducer;
    private final ChargerEventProducer chargerEventProducer;

    public String stationCreate(StationRequest stationRequest) {
        Optional<Station> isAvail = stationRepository.findById(stationRequest.getStationId());

        if(isAvail.isPresent()){
            return "station details is already available or already existed";
        }
        else {
            Station station = new Station();
            station.setStationId(stationRequest.getStationId());
            station.setStationName(stationRequest.getStationName());
            station.setLatitude(stationRequest.getLatitude());
            station.setLongitude(stationRequest.getLongitude());
            station.setStatus(stationRequest.getStatus());
            stationRepository.save(station);
            log.info("station details added to db successfully : {} " , stationRequest.getStationId());

            stationEventProducer.producingStationEvent(new StationCreatedEvent(
                    station.getStationId() , station.getStationName() , station.getLatitude() ,
                    station.getLongitude() , station.getStatus()
            ));


            return " station details added to db successfully";
        }
    }
    @Cacheable(value = "stations", key = "#stationId")
    public StationResponse getByStationId(String stationId)  {
        Optional<Station> isAvail = stationRepository.findById(stationId);
        if(isAvail.isPresent()){
            Station station = isAvail.get();
            StationResponse response = new StationResponse();
            response.setStationId(station.getStationId());
            response.setStationName(station.getStationName());
            response.setLatitude(station.getLatitude());
            response.setLongitude(station.getLongitude());
            response.setStatus(station.getStatus());
            return response;
        }
        else{
            throw new RuntimeException("Station not found");
        }
    }
    @Cacheable(value = "allStations")
    public List<StationResponse> getAllStation() {
        List<StationResponse> reaponseList = new ArrayList<>();
        List<Station> avail = stationRepository.findAll();
        for(Station station : avail){
            StationResponse response = new StationResponse();
            response.setStationId(station.getStationId());
            response.setStationName(station.getStationName());
            response.setLatitude(station.getLatitude());
            response.setLongitude(station.getLongitude());
            response.setStatus(station.getStatus());
            reaponseList.add(response);
        }
        return reaponseList;
    }

    @CachePut(value = "stations", key = "#stationId")
    public Station updateStationDetails(String stationId, StationUpdateRequest stationUpdateRequest) {
        Optional<Station> isAvail = stationRepository.findById(stationId);
        if(isAvail.isPresent()){
            Station station = isAvail.get();
            station.setStationName(stationUpdateRequest.getStationName());
            station.setLatitude(stationUpdateRequest.getLatitude());
            station.setLongitude(stationUpdateRequest.getLongitude());
            station.setStatus(stationUpdateRequest.getStatus());
            return stationRepository.save(station);
//            log.info("station details updated successfully : {} " , stationId);
//            return "station details updated successfully";
        }
        else{
            throw new RuntimeException("station not found for update");
        }
    }

    @CacheEvict(value = "stations", key = "#stationId")
    public String deleteStation(String stationId) {
        Optional<Station> isAvail = stationRepository.findById(stationId);
        if(isAvail.isPresent()){
            stationRepository.deleteById(stationId);
            log.warn("station deleted from db successfully");
            return " deleted successfully";
        }
        else{
            log.error("station is not found with this station id : {} " , stationId);
            throw new RuntimeException("station is not found for delete");

        }

    }

    public String addChargerToStation(ChargerRequest chargerRequest, String stationId) {
        Optional<Station> isAvail = stationRepository.findById(stationId);

        if(isAvail.isPresent()){

            Charger charger = new Charger();
            charger.setStation(isAvail.get());
            charger.setChargerId(chargerRequest.getChargerId());
            charger.setChargerNumber(chargerRequest.getChargerNumber());
            charger.setChargerType(chargerRequest.getChargerType());
            charger.setPower(chargerRequest.getPower());
            charger.setStatus(chargerRequest.getStatus());
            chargerRepository.save(charger);
            chargerEventProducer.producingChargerEvent(new ChargerAddedEvent(
                    charger.getChargerId() , charger.getChargerNumber() , charger.getChargerType(),
                    charger.getPower() , charger.getStatus()
            ));
            log.info("charger details added successfully into charger db ");
            return "charger details added to charger db successfully";
        }
        else{
            log.error("station is not found using this station id : {} ",stationId );
            throw new RuntimeException("station is not found");
        }

    }

    public List<ChargerResponse> getChargerByStationId(String stationId) {
        Optional<Station> isAvail = stationRepository.findById(stationId);
        List<ChargerResponse> responseList = new ArrayList<>();
        if(isAvail.isPresent()){
            Station  station = isAvail.get();
            List<Charger> charger = station.getChargers();
            for(Charger ch : charger){
                ChargerResponse response = new ChargerResponse();
                response.setChargerId(ch.getChargerId());
                response.setChargerNumber(ch.getChargerNumber());
                response.setChargerType(ch.getChargerType());
                response.setPower(ch.getPower());
                response.setStatus(ch.getStatus());
                responseList.add(response);
                log.info("charger response is added to response list ");

            }
        }
        else {
            throw new RuntimeException("Station not found");
        }
        return responseList;


    }

    public String updateCharger(ChargerUpdateRequest chargerUpdateRequest, String chargerId) {
        Optional<Charger> isAvail = chargerRepository.findById(chargerId);
        if(isAvail.isPresent()){
            Charger charger = isAvail.get();
            charger.setChargerNumber(chargerUpdateRequest.getChargerNumber());
            charger.setChargerType(chargerUpdateRequest.getChargerType());
            charger.setPower(chargerUpdateRequest.getPower());
            charger.setStatus(chargerUpdateRequest.getStatus());
            chargerRepository.save(charger);
            log.info("charger details updated successfully");
            return "charger is updated successfully";

        }
        else{
            throw new RuntimeException("charger is not found");
        }

    }
}
