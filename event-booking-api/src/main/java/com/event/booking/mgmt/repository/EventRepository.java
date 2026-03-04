package com.event.booking.mgmt.repository;

import com.event.booking.mgmt.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Page<Event> findByEventDateAfter(LocalDateTime now, Pageable pageable);
}
