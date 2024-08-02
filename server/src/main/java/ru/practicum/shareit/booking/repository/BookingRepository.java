package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByBookerIdOrderByStartDesc(Long userId, Pageable pageable);

    Page<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId,
                                                                              LocalDateTime now,
                                                                              LocalDateTime current,
                                                                              Pageable pageable);

    Page<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long userId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByBookerIdAndStatusOrderByStartDesc(Long userId, BookingStatus status, Pageable pageable);

    List<Booking> findByBookerIdAndStatusAndEndIsBeforeOrderByStartDesc(Long userId, BookingStatus status,
                                                                        LocalDateTime now);

    Page<Booking> findByItemOwnerIdOrderByStartDesc(Long userId, Pageable pageable);


    Page<Booking> findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long userId,
                                                                                 LocalDateTime now,
                                                                                 LocalDateTime current,
                                                                                 Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStartIsAfterOrderByStart(Long userId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long userId, BookingStatus status, Pageable pageable);
}