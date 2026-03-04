package com.event.booking.mgmt.repository;

import com.event.booking.mgmt.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
