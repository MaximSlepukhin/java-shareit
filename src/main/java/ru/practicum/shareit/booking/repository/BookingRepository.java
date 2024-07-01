package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findByBookerIdOrderByStart(Long userId);

    Collection<Booking> findByBookerIdAndEndIsBeforeOrderByStart(Long userId, LocalDateTime now);

    Collection<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStart(Long userId,
                                                                                LocalDateTime now, LocalDateTime current);

    Collection<Booking> findByBookerIdAndStartIsAfterOrderByStart(Long userId, LocalDateTime now);

    Collection<Booking> findByBookerIdAndStatusOrderByStart(Long userId, BookingStatus status);

    Collection<Booking> findByBookerIdAndStatusAndEndIsBeforeOrderByStart(Long userId, BookingStatus status,
                                                                          LocalDateTime now);

    Collection<Booking> findByItemOwnerIdOrderByStart(Long userId);

    Collection<Booking> findByItemOwnerIdAndEndIsBeforeOrderByStart(Long userId, LocalDateTime now);

    Collection<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStart(Long userId,
                                                                                LocalDateTime now, LocalDateTime current);

    Collection<Booking> findByItemOwnerIdAndStartIsAfterOrderByStart(Long userId, LocalDateTime now);

    Collection<Booking> findByItemOwnerIdAndStatusOrderByStart(Long userId, BookingStatus status);
}