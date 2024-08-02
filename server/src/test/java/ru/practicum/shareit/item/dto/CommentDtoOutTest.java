package ru.practicum.shareit.item.dto;

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
public class CommentDtoOutTest {

    private final JacksonTester<CommentDtoOut> json;

    @Test
    void testSerialize() throws Exception {
        CommentDtoOut commentDtoOut = new CommentDtoOut();
        commentDtoOut.setId(1L);
        commentDtoOut.setText("Comment");
        commentDtoOut.setCreated(LocalDateTime.now());
        commentDtoOut.setAuthorName("Name");

        JsonContent<CommentDtoOut> result = json.write(commentDtoOut);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(commentDtoOut.getId()));
        assertThat(result).extractingJsonPathStringValue("$.authorName")
                .satisfies(author -> assertThat(author).isEqualTo(commentDtoOut.getAuthorName()));
        assertThat(result).extractingJsonPathStringValue("$.created")
                .satisfies(creationDate -> assertThat(creationDate).isNotNull());
        assertThat(result).extractingJsonPathStringValue("$.text")
                .satisfies(text -> assertThat(text).isEqualTo(commentDtoOut.getText()));
    }
}
