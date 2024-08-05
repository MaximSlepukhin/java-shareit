package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;
@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDtoOutTest {

    private final JacksonTester<UserDtoOut> json;

    @Test
    void testSerialize() throws Exception {

        UserDtoOut userDtoOut = new UserDtoOut(1L, "User1");

        JsonContent<UserDtoOut> result = json.write(userDtoOut);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.name");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(userDtoOut.getId()));
        assertThat(result).extractingJsonPathStringValue("$.name")
                .satisfies(name -> assertThat(name).isEqualTo(userDtoOut.getName()));
    }
}
