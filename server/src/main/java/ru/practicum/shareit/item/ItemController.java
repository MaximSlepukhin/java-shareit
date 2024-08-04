package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.util.Util;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @RequestBody ItemDto itemDto) {
        log.info("POST запрос на добавление предмета " + itemDto.getName() + " пользователем с id=" + userId + ".");
        return itemService.createItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDtoOut createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestBody CommentDto commentDto,
                                       @PathVariable Long itemId) {
        log.info("POST запрос на создание комментария");
        return itemService.addComment(userId, commentDto, itemId);
    }

    @RequestMapping(value = "/{itemId}", method = RequestMethod.PATCH, consumes =
            MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ItemDto update(@RequestBody Map<String, String> itemUpdate,
                          @PathVariable Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("PATCH запрос на обновлением пользователем с id= " + userId + " предмета с id=" + itemId + ".");
        return itemService.updateItem(itemUpdate, userId, itemId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoOut getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId,
                              @RequestParam(value = "from", required = false,
                                      defaultValue = "0") Integer from,
                              @RequestParam(value = "size", required = false,
                                      defaultValue = "10") Integer size) {
        log.info("GET запрос на получение предмета с id=" + itemId + " пользователем с id=" + userId + ".");
        Pageable pageable = PageRequest.of(from, size);
        return itemService.findItemById(itemId, userId, pageable);
    }

    @GetMapping
    public List<ItemDtoOut> getItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(value = "from", required = false,
                                                   defaultValue = "0") Integer from,
                                           @RequestParam(value = "size", required = false,
                                                   defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        log.info("GET запрос на получение списка предметов пользователя с id=" + userId + ".");
        return itemService.findItemsOfUser(userId, pageable);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestParam(name = "text") String text,
                                     @RequestParam(value = "from", required = false,
                                             defaultValue = "0") Integer from,
                                     @RequestParam(value = "size", required = false,
                                             defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        log.info("Get запрос на поиск предметов.");
        return itemService.searchItem(userId, text, pageable);
    }
}
