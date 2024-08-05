package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingForItemDto;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemDtoOutTest {

    private final JacksonTester<ItemDtoOut> json;
    BookingForItemDto lastBooking = BookingForItemDto.builder().build();
    BookingForItemDto nextBooking = BookingForItemDto.builder().build();

    @Test
    void testSerialize() throws Exception {
        ItemDtoOut itemDtoOut = new ItemDtoOut();
        itemDtoOut.setId(1L);
        itemDtoOut.setName("Name");
        itemDtoOut.setDescription("Description");
        itemDtoOut.setAvailable(false);
        itemDtoOut.setComments(new ArrayList<>());
        itemDtoOut.setLastBooking(lastBooking);
        itemDtoOut.setNextBooking(nextBooking);

        JsonContent<ItemDtoOut> result = json.write(itemDtoOut);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(itemDtoOut.getId()));
        assertThat(result).extractingJsonPathStringValue("$.name")
                .satisfies(name -> assertThat(name).isEqualTo(itemDtoOut.getName()));
        assertThat(result).extractingJsonPathStringValue("$.description")
                .satisfies(description -> assertThat(description).isEqualTo(itemDtoOut.getDescription()));
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .satisfies(available -> assertThat(available).isNotNull());
        assertThat(result).extractingJsonPathArrayValue("$.comments")
                .satisfies(comments -> assertThat(comments).isEqualTo(itemDtoOut.getComments()));
    }
}
