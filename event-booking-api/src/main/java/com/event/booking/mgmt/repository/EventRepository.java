package com.event.booking.mgmt.repository;

import com.event.booking.mgmt.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer>, JpaSpecificationExecutor<Event> {
    Page<Event> findByEventDateAfter(LocalDateTime now, Pageable pageable);
    Optional<Event> findByIdEventAndIsActiveTrue(Integer idEvent);
}
