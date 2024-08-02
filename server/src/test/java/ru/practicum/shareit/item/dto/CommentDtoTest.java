package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentDtoTest {
    private final JacksonTester<CommentDto> json;

    @Test
    void testSerialize() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Comment");

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathStringValue("$.text")
                .satisfies(text -> assertThat(text).isEqualTo(commentDto.getText()));
    }
}