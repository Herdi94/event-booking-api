package com.event.booking.mgmt.repository;

import com.event.booking.mgmt.model.Booking;
import com.event.booking.mgmt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT COALESCE(SUM(a.numberTickets), 0) FROM Booking a WHERE a.event.idEvent = :idEvent")
    Integer countTicketBookedByIdEvent(@Param("idEvent") Integer idEvent);

    List<Booking> findByUser(User user);
    Optional<Booking> findByUserIdUserAndEventIdEvent(Integer idUser, Integer idEvent);
}
