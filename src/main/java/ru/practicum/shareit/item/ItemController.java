package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.util.Util;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto save(@RequestHeader(Util.USER_HEADER) Long userId,
                        @Valid @RequestBody ItemDto itemDto) {
        log.info("POST запрос на добавление предмета " + itemDto.getName() + " пользователем с id=" + userId + ".");
        return itemService.createItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOut createComment(@RequestHeader(Util.USER_HEADER) Long userId,
                                       @Valid @RequestBody CommentDto commentDto,
                                       @PathVariable Long itemId) {
        log.info("POST запрос на создание комментария");
        return itemService.addComment(userId, commentDto, itemId);
    }

    @RequestMapping(value = "/{itemId}", method = RequestMethod.PATCH, consumes =
            MediaType.APPLICATION_JSON_VALUE)
    public ItemDto update(@RequestBody Map<String, String> itemUpdate,
                          @PathVariable Long itemId,
                          @RequestHeader(Util.USER_HEADER) Long userId) {
        log.info("PATCH запрос на обновлением пользователем с id= " + userId + " предмета с id=" + itemId + ".");
        return itemService.updateItem(itemUpdate, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut getItem(@RequestHeader(Util.USER_HEADER) Long userId,
                              @PathVariable Long itemId) {
        log.info("GET запрос на получение предмета с id=" + itemId + " пользователем с id=" + userId + ".");
        return itemService.findItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoOut> getItemsOfUser(@RequestHeader(Util.USER_HEADER) Long userId) {
        log.info("GET запрос на получение списка предметов пользователя с id=" + userId + ".");
        return itemService.findItemsOfUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader(Util.USER_HEADER) Long userId,
                                     @RequestParam(name = "text") String text) {
        log.info("Get запрос на поиск предметов.");
        return itemService.searchItem(userId, text);
    }
}
