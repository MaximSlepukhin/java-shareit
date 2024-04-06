package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    public final static String USER_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;
    private final UserService userService;

    @PostMapping
    public ItemDto save(@RequestHeader(USER_HEADER) Long userId,
                        @Valid @RequestBody ItemDto itemDto) {
        Item item = ItemMapper.mapToItem(itemDto, userId);
        userService.checkUserExist(userId);
        return ItemMapper.mapToItemDto(itemService.createItem(userId, item));
    }

    @RequestMapping(value = "/{itemId}", method = RequestMethod.PATCH, consumes =
            MediaType.APPLICATION_JSON_VALUE)
    public ItemDto update(@RequestBody Map<String, String> itemUpdate,
                       @PathVariable Long itemId,
                       @RequestHeader(USER_HEADER) Long userId) {
        userService.checkUserExist(userId);
        return ItemMapper.mapToItemDto(itemService.updateItem(itemUpdate, userId, itemId));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@RequestHeader(USER_HEADER) Long userId,
                           @PathVariable Long itemId) {
        return ItemMapper.mapToItemDto(itemService.findItemById(itemId));
    }

    @GetMapping
    public Collection<ItemDto> getItemsOfUser(@RequestHeader(USER_HEADER) Long userId) {
        return itemService.findItemsOfUser(userId).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader(USER_HEADER) Long userId,
                                  @RequestParam(name = "text") String text) {
        userService.checkUserExist(userId);
        return itemService.searchItem(userId, text).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }
}
