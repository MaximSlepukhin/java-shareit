package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestDtoTest {

    private final JacksonTester<ItemRequestDto> json;

    @Test
    void testSerialize() throws Exception {

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1l);
        itemRequestDto.setDescription("Description");
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setItems(new ArrayList<>());

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(itemRequestDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.description")
                .satisfies(description -> assertThat(description).isEqualTo(itemRequestDto.getDescription()));
        assertThat(result).extractingJsonPathStringValue("$.created")
                .satisfies(created -> assertThat(created).isNotNull());
        assertThat(result).extractingJsonPathArrayValue("$.items")
                .satisfies(items -> assertThat(items).isEqualTo(itemRequestDto.getItems()));
    }
}
