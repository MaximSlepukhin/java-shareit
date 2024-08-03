package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingForItemDtoTest {
    private final JacksonTester<BookingForItemDto> json;

    @Test
    void testSerialize() throws Exception {

        BookingForItemDto bookingForItemDto = BookingForItemDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(3))
                .status(BookingStatus.APPROVED)
                .bookerId(2L)
                .build();

        JsonContent<BookingForItemDto> result = json.write(bookingForItemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(bookingForItemDto.getId()));
        assertThat(result).extractingJsonPathNumberValue("$.bookerId")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(bookingForItemDto.getBookerId()));
        assertThat(result).extractingJsonPathStringValue("$.start")
                .satisfies(start -> assertThat(start).isNotNull());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .satisfies(end -> assertThat(end).isNotNull());
    }
}
