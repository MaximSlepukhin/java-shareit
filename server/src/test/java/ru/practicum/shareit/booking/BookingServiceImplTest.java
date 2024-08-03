package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.exceptions.BookingDateException;
import ru.practicum.shareit.booking.exceptions.BookingNotFoundException;
import ru.practicum.shareit.booking.exceptions.BookingStatusException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.itemExceptions.ItemIsNotAvailableException;
import ru.practicum.shareit.item.itemExceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    BookingRepository bookingRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    BookingServiceImpl bookingServiceImpl;

    static LocalDateTime start = LocalDateTime.now().plusDays(1);
    static LocalDateTime end = LocalDateTime.now().plusDays(2);
    static LocalDateTime created = LocalDateTime.now();

    @BeforeAll
    static void createStartTime() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
    }

    @BeforeAll
    static void createEndTime() {
        LocalDateTime end = LocalDateTime.now().plusDays(2);
    }

    @BeforeAll
    static void createTime() {
        LocalDateTime created = LocalDateTime.now();
    }

    User owner = User.builder().id(1L).name("User1").email("user1mail.ru").build();
    User booker = User.builder().id(2L).name("User2").email("user2@mail.ru").build();
    User requester = User.builder().id(3L).name("User1").email("user1@mail.ru").build();

    ItemRequest itemRequest = ItemRequest.builder().id(1L).description("Нужна дрель").requester(requester)
            .created(created).build();
    Item item = Item.builder().id(1L).name("Дрель").description("Простая дрель").available(true).owner(owner)
            .request(itemRequest).build();
    Item itemWithFalseStatus = Item.builder().id(1L).name("Дрель").description("Простая дрель").available(false)
            .owner(owner).request(itemRequest).build();

    BookingDtoRequest bookingDtoRequest = BookingDtoRequest.builder().itemId(1L).start(start).end(end).build();
    BookingDtoRequest bookingWithWrongDates = BookingDtoRequest.builder().itemId(1L)
            .start(LocalDateTime.now().plusDays(3)).end(LocalDateTime.now().plusDays(1)).build();
    Booking bookingToRepository = Booking.builder().start(start).end(end).item(item).booker(booker)
            .status(BookingStatus.WAITING).build();
    Booking bookingFromRepository = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker)
            .status(BookingStatus.WAITING).build();

    Booking bookingWithApprovedStatus = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker)
            .status(BookingStatus.APPROVED).build();

    Booking bookingWithCanceledStatus = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker)
            .status(BookingStatus.CANCELED).build();
    Booking bookingApproved = Booking.builder().id(1L).start(start).end(end).item(item).booker(booker)
            .status(BookingStatus.APPROVED).build();
    BookingDto bookingDto = BookingMapper.toBookingDto(bookingFromRepository);
    BookingDto bookingApprovedDto = BookingMapper.toBookingDto(bookingApproved);

    List<BookingDto> listOfBookingsDto = List.of(bookingDto);
    List<Booking> listOfBookings = List.of(bookingFromRepository);
    Pageable pageable = PageRequest.of(1, 20);

    @Test
    void shouldAddBooking() {
        when(userRepository.findById(2L))
                .thenReturn(Optional.ofNullable(booker));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(bookingToRepository))
                .thenReturn(bookingFromRepository);

        BookingDto result = bookingServiceImpl.add(2L, bookingDtoRequest);

        Assertions.assertEquals(result, bookingDto);

    }

    @Test
    void shouldNotAddBookingIfOwnerIdIsEquilUserId() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(owner));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(item));

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingServiceImpl.add(1L, bookingDtoRequest));
        Assertions.assertEquals(exception.getMessage(), "Бронирование не возможно.");
    }

    @Test
    void shouldNotAddBookingIfDatesAreWrong() {
        BookingDateException exception = Assertions.assertThrows(
                BookingDateException.class,
                () -> bookingServiceImpl.add(1L, bookingWithWrongDates));
        Assertions.assertEquals(exception.getMessage(), "Некорректно заданы даты бронирования.");
    }

    @Test
    void shouldNotAddBookingIfStatusOfBookingIsFalse() {
        when(userRepository.findById(2L))
                .thenReturn(Optional.ofNullable(booker));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(itemWithFalseStatus));

        ItemIsNotAvailableException exception = Assertions.assertThrows(
                ItemIsNotAvailableException.class,
                () -> bookingServiceImpl.add(2L, bookingDtoRequest));
        Assertions.assertEquals(exception.getMessage(), "Бронирование не возможно.");
    }

    @Test
    void shouldUpdateStatusOfBooking() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(owner));
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.ofNullable(bookingFromRepository));
        when(bookingRepository.save(bookingFromRepository))
                .thenReturn(bookingApproved);

        BookingDto result = bookingServiceImpl.updateStatus(1L, 1L, true);

        Assertions.assertEquals(result, bookingApprovedDto);
    }

    @Test
    void shouldNotUpdateStatusOfBookingIfStatusApproved() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(owner));
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.ofNullable(bookingWithApprovedStatus));

        BookingStatusException exception = Assertions.assertThrows(
                BookingStatusException.class,
                () -> bookingServiceImpl.updateStatus(1L, 1L,true));
        Assertions.assertEquals(exception.getMessage(), "Бронирование имеет статус.");
    }

    void shouldNotUpdateStatusOfBookingIfStatusCanceled() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(owner));
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.ofNullable(bookingWithCanceledStatus));

        BookingStatusException exception = Assertions.assertThrows(
                BookingStatusException.class,
                () -> bookingServiceImpl.updateStatus(1L, 1L,true));
        Assertions.assertEquals(exception.getMessage(), "Бронирование имеет статус.");
    }

    @Test
    void shouldFindBookingById() {
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.ofNullable(bookingFromRepository));

        BookingDto result = bookingServiceImpl.findBookingById(1L, 1L);

        Assertions.assertEquals(result, bookingDto);
    }

    @Test
    void shouldGetAllBookingsOfOwner() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(owner));
        Page<Booking> bookingPage = new PageImpl<>(listOfBookings);
        when(bookingRepository.findByBookerIdOrderByStartDesc(1L, pageable))
                .thenReturn(bookingPage);

        List<BookingDto> result = bookingServiceImpl.getAllBookingsOfUser(1L,
                "ALL", pageable);

        Assertions.assertEquals(result, listOfBookingsDto);
    }

    @Test
    void shouldFindBookingsOfOwnerById() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(owner));
        Page<Booking> bookingPage = new PageImpl<>(listOfBookings);
        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(1L, pageable))
                .thenReturn(bookingPage);

        List<BookingDto> result = bookingServiceImpl.findBookingsOfOwnerById(1L,"ALL",pageable);

        Assertions.assertEquals(result, listOfBookingsDto);
    }

    @Test
    void shouldThrowExceptionWhenBookingNotFound() {
        BookingNotFoundException exception = Assertions.assertThrows(
                BookingNotFoundException.class,
                () -> bookingServiceImpl.findBookingById(1L,11L));
        Assertions.assertEquals(exception.getMessage(), "Бронирование с id: 11 не существует.");
    }
}
