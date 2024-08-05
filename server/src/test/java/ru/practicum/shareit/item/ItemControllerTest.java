package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @MockBean
    ItemService itemService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    ItemDto itemDtoFirstIn = ItemDto.builder().id(null).name("Дрель").description("Простая дрель")
            .available(true).requestId(3L).build();

    ItemDto itemDtoFirstUpdateOut = ItemDto.builder().id(1L).name("Дрель").description("Простая дрель")
            .available(false).build();

    ItemDto itemDtoFirstOut = ItemDto.builder().id(1L).name("Дрель").description("Простая дрель").available(true)
            .requestId(3L).build();

    ItemDtoOut itemDtoOutGetById = ItemDtoOut.builder().id(1L).name("Дрель").description("Простая дрель")
            .available(false).lastBooking(null).nextBooking(null).comments(null).build();

    CommentDto commentDto = CommentDto.builder().text("Comment for item 1").build();

    CommentDtoOut commentDtoOut = CommentDtoOut.builder()
            .id(1L)
            .text("Comment for item 1")
            .authorName("Maxim")
            .created(LocalDateTime.now())
            .build();

    List<ItemDtoOut> listItemsOfUser = new ArrayList<>();

    List<ItemDto> listOfItems = new ArrayList<>();

    Map<String, String> map = new HashMap<>();

    @Test
    void addItem() throws Exception {
        when(itemService.createItem(1L, itemDtoFirstIn))
                .thenReturn(itemDtoFirstOut);
        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDtoFirstIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDtoFirstOut)));
    }

    @Test
    void createComment() throws Exception {
        when(itemService.addComment(1L, commentDto, 1L))
                .thenReturn(commentDtoOut);
        mockMvc.perform(post("/items/" + itemDtoFirstOut.getId() + "/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("itemId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentDtoOut)));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(anyMap(), anyLong(), anyLong()))
                .thenReturn(itemDtoFirstUpdateOut);
        mockMvc.perform(patch("/items/" + itemDtoFirstOut.getId())
                        .content(mapper.writeValueAsString(map))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("itemId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDtoFirstUpdateOut)));
    }

    @Test
    void getItem() throws Exception {
        when(itemService.findItemById(anyLong(), anyLong(), any(Pageable.class)))
                .thenReturn(itemDtoOutGetById);
        mockMvc.perform(get("/items/" + itemDtoOutGetById.getId())
                        .param("itemId", "1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDtoOutGetById)));
    }

    @Test
    void getItemsOfUser() throws Exception {
        when(itemService.findItemsOfUser(anyLong(), any(Pageable.class)))
                .thenReturn(listItemsOfUser);
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(listItemsOfUser)));
    }

    @Test
    void searchItems() throws Exception {
        when(itemService.searchItem(anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(listOfItems);
        mockMvc.perform(get("/items/search")
                        .param("text", "text")
                        .param("from", "0")
                        .param("size", "10")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(listOfItems)));
    }
}
