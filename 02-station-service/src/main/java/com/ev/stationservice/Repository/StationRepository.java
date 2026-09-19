package com.ev.stationservice.Repository;

import com.ev.stationservice.Entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station,String> {
}
