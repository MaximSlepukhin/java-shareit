package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.error.ErrorHandler;
import ru.practicum.shareit.util.Util;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@Import(ErrorHandler.class)
public class BookingControllerTest {

    @MockBean
    BookingService bookingService;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mockMvc;

    int from = 0;
    int size = 20;

    BookingDtoRequest bookingDtoRequest = BookingDtoRequest.builder().itemId(2L)
            .start(LocalDateTime.now().plusDays(1))
            .end(LocalDateTime.now().plusDays(4))
            .build();

    BookingDto bookingDto = BookingDto.builder().id(1L).start(LocalDateTime.now())
            .end(LocalDateTime.now().plusDays(3)).status(BookingStatus.WAITING).build();

    BookingDto bookingDtoSecond = BookingDto.builder().id(2L).start(LocalDateTime.now())
            .end(LocalDateTime.now().plusDays(3)).status(BookingStatus.WAITING).build();

    List<BookingDto> listOfBookings = new ArrayList<>(Arrays.asList(bookingDto, bookingDtoSecond));

    BookingDto updateBooking = BookingDto.builder().id(1L).start(LocalDateTime.now())
            .end(LocalDateTime.now().plusDays(3)).status(BookingStatus.APPROVED).build();

    Pageable pageable = PageRequest.of(from, size);

    @Test
    void addBookingTest() throws Exception {
        when(bookingService.add(1L, bookingDtoRequest))
                .thenReturn(bookingDto);
        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Util.USER_HEADER, 1))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void updateStatusOfBookingTest() throws Exception {
        when(bookingService.updateStatus(1L, 1L, true))
                .thenReturn(updateBooking);
        mockMvc.perform(patch("/bookings/" + bookingDto.getId())
                        .header(Util.USER_HEADER, 1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(updateBooking)));
    }

    @Test
    void findBookingByIdTest() throws Exception {
        when(bookingService.findBookingById(1L,bookingDto.getId()))
                .thenReturn(bookingDto);
        mockMvc.perform(get("/bookings/" + bookingDto.getId())
                        .header(Util.USER_HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void getAllBookings() throws Exception {
        when(bookingService.getAllBookingsOfUser(1L,"ALL",pageable))
                .thenReturn(listOfBookings);
        mockMvc.perform(get("/bookings")
                        .header(Util.USER_HEADER, 1)
                        .param("from", "0")
                        .param("size", "20")
                        .param("state","ALL"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(listOfBookings)));
    }

    @Test
    void findAllBookingsOfOwner() throws Exception {
        when(bookingService.findBookingsOfOwnerById(1L, "ALL", pageable))
                .thenReturn(listOfBookings);
        mockMvc.perform(get("/bookings/owner")
                        .header(Util.USER_HEADER, 1)
                        .param("from", "0")
                        .param("size", "20")
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(listOfBookings)));
    }
}
