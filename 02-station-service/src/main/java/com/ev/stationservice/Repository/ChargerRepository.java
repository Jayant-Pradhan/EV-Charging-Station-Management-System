package com.ev.stationservice.Repository;

import com.ev.stationservice.Entity.Charger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargerRepository extends JpaRepository<Charger,String> {
}
