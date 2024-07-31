package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Valid @RequestBody ItemDto itemDto) {
        log.info("POST запрос на добавление предмета " + itemDto.getName() + " пользователем с id=" + userId + ".");
        return itemClient.createItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @Valid @RequestBody CommentDto commentDto,
                                                @PathVariable Long itemId) {
        log.info("POST запрос на создание комментария.");
        return itemClient.addComment(userId, commentDto, itemId);
    }

    @RequestMapping(value = "/{itemId}", method = RequestMethod.PATCH, consumes =
            MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(@Valid @RequestBody Map<String, String> itemUpdate,
                                         @PathVariable Long itemId,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("PATCH запрос на обновлением пользователем с id= " + userId + " предмета с id=" + itemId + ".");
        return itemClient.updateItem(itemUpdate, userId, itemId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long itemId,
                                          @RequestParam(value = "from", required = false,
                                                  defaultValue = "0") Integer from,
                                          @RequestParam(value = "size", required = false,
                                                  defaultValue = "10") Integer size) {
        log.info("GET запрос на получение предмета с id=" + itemId + " пользователем с id=" + userId + ".");
        return itemClient.findItemById(itemId, userId, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(value = "from", required = false,
                                                         defaultValue = "0") Integer from,
                                                 @RequestParam(value = "size", required = false,
                                                         defaultValue = "10") Integer size) {
        log.info("GET запрос на получение списка предметов пользователя с id=" + userId + ".");
        return itemClient.findItemsOfUser(userId, from, size);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(name = "text") String text,
                                              @RequestParam(value = "from", required = false,
                                                      defaultValue = "0") Integer from,
                                              @RequestParam(value = "size", required = false,
                                                      defaultValue = "10") Integer size) {
        log.info("Get запрос на поиск предметов.");
        return itemClient.searchItem(userId, text, from, size);
    }
}