package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.util.Util;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @MockBean
    RequestService requestService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    UserDto userDtoInFirst = UserDto.builder().name("Maxim").email("user1@mail.ru").build();

    ItemRequestDto itemRequestDtoIn = ItemRequestDto.builder().description("Дрель").build();

    ItemRequestDto itemRequestDtoInSecond = ItemRequestDto.builder().description("Велосипед").build();

    ItemRequestDtoOut itemRequestDtoOut = new ItemRequestDtoOut(1L, "Дрель", LocalDateTime.now());

    ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("Дрель").items(Collections.EMPTY_LIST)
            .build();

    List<ItemRequestDto> itemRequestDtos = new ArrayList<>(Arrays.asList(itemRequestDto, itemRequestDtoInSecond));

    @Test
    void createRequestTest() throws Exception {
        when(requestService.createRequest(anyLong(), any()))
                .thenReturn(itemRequestDtoOut);
        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Util.USER_HEADER, "1"))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDtoOut)));
    }

    @Test
    void getRequestsOfUserTest() throws Exception {
        when(requestService.getRequestsOfUser(anyLong()))
                .thenReturn(itemRequestDtos);
        mockMvc.perform(get("/requests")
                        .header(Util.USER_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDtos)));
    }

    @Test
    void getRequestByIdTest() throws Exception {
        when(requestService.getRequestById(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);
        mockMvc.perform(get("/requests/" + 1)
                        .header(Util.USER_HEADER, "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDto)));
    }

    @Test
    void getItemRequestsTest() throws Exception {
        when(requestService.getRequests(anyLong(), any(Pageable.class)))
                .thenReturn(itemRequestDtos);
        mockMvc.perform(get("/requests/all")
                        .header(Util.USER_HEADER, "1")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDtos)));
    }
}

