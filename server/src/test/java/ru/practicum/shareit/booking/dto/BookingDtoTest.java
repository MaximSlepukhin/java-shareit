package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingDtoTest {

    private final JacksonTester<BookingDto> json;

    @Test
    void testSerialize() throws Exception {

        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(3))
                .status(BookingStatus.APPROVED)
                .booker(new User())
                .item(new Item())
                .build();

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(bookingDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.start")
                .satisfies(start -> assertThat(start).isNotNull());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .satisfies(end -> assertThat(end).isNotNull());
    }
}
