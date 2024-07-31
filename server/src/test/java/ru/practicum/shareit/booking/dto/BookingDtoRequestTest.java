package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingDtoRequestTest {

    private final JacksonTester<BookingDtoRequest> json;

    @Test
    void testSerialize() throws Exception {
        BookingDtoRequest bookingDtoRequest = BookingDtoRequest.builder()
                .itemId(1l)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(3))
                .build();

        JsonContent<BookingDtoRequest> result = json.write(bookingDtoRequest);

        assertThat(result).extractingJsonPathNumberValue("$.itemId")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(bookingDtoRequest.getItemId()));
        assertThat(result).extractingJsonPathStringValue("$.start")
                .satisfies(start -> assertThat(start).isNotNull());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .satisfies(end -> assertThat(end).isNotNull());
    }
}
