package com.hospitality.platform.repository;

import com.hospitality.platform.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {
    List<Hotel> findByActiveTrue();
    List<Hotel> findByCityContainingIgnoreCaseAndActiveTrue(String city);
    List<Hotel> findByHotelAdminId(Long adminId);

    @Query("SELECT DISTINCT h.city FROM Hotel h WHERE h.active = true")
    List<String> findDistinctCities();

    @Query("SELECT h FROM Hotel h WHERE h.active = true AND " +
           "(LOWER(h.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(h.city) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(h.state) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Hotel> findActiveHotelsBySearch(@Param("query") String query);
}
