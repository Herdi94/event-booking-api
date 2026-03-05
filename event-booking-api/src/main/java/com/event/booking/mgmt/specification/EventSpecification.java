package com.event.booking.mgmt.specification;

import com.event.booking.mgmt.model.Event;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventSpecification {
    public static Specification<Event> filterEvents(
            String title,
            String location,
            LocalDateTime startDate,
            LocalDateTime endDate
    ){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.isTrue(root.get("isActive")));
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), LocalDateTime.now()));

            if(title != null)
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));

            if(location != null)
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), "%" + location.toLowerCase() + "%"));

            if(startDate != null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), startDate));

            if(endDate != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), endDate));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
