package com.police.evisitor.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.police.evisitor.entity.Hotel;
import com.police.evisitor.entity.Visitor;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Integer> {

	Optional<Visitor> findById(Long visitorRef);

	@Query("""
			    SELECT v FROM Visitor v
			    WHERE v.hotel = :hotel
			      AND v.checkOutDate IS NULL
			      AND v.recordStatus <> :recordStatus
			      AND (:visitorName IS NULL OR LOWER(v.visitorName) LIKE LOWER(CONCAT(:visitorName, '%')))
				  AND (:visitorMobile IS NULL OR v.visitorMobile LIKE CONCAT(:visitorMobile, '%'))

			""")
	Page<Visitor> searchVisitorsByHotel(@Param("hotel") Hotel hotel, @Param("recordStatus") String recordStatus,
			@Param("visitorName") String visitorName, @Param("visitorMobile") String visitorMobile, Pageable pageable);

	@Query("""
			    SELECT v FROM Visitor v
			    WHERE v.hotel = :hotel
			      AND v.checkOutDate IS NULL
			      AND v.recordStatus <> :recordStatus
			      AND (:visitorName IS NULL OR LOWER(v.visitorName) LIKE LOWER(CONCAT(:visitorName, '%')))
				  AND (:visitorMobile IS NULL OR v.visitorMobile LIKE CONCAT(:visitorMobile, '%'))
				  AND (v.createdOn BETWEEN :startTime AND :endTime)
			""")
	Page<Visitor> searchVisitorsByHotelDateRange(@Param("hotel") Hotel hotel,
			@Param("recordStatus") String recordStatus, @Param("visitorName") String visitorName,
			@Param("visitorMobile") String visitorMobile, @Param("startTime") LocalDateTime startTime,
			@Param("endTime") LocalDateTime endTime, Pageable pageable);

	@Query("""
			SELECT v FROM Visitor v
			WHERE v.checkInDate IS NULL
			AND v.createdOn BETWEEN :startDate AND :endDate
			""")
	List<Visitor> findVisitors(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

	List<Visitor> findByCheckInDateIsNull();
}
