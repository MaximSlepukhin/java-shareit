package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.util.List;
import java.util.Map;

public interface ItemService {
    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDtoOut findItemById(Long itemId, Long userId, Pageable pageable);

    List<ItemDtoOut> findItemsOfUser(Long userId, Pageable pageable);

    ItemDto updateItem(Map<String, String> itemUpdate, Long userId, Long itemId);

    List<ItemDto> searchItem(Long userId, String text, Pageable pageable);

    CommentDtoOut addComment(Long userId, CommentDto commentDto, Long itemId);
}
