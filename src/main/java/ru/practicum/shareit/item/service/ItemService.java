package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.util.List;
import java.util.Map;


public interface ItemService {

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDtoOut findItemById(Long itemId, Long userId);

    List<ItemDtoOut> findItemsOfUser(Long userId);

    ItemDto updateItem(Map<String, String> itemUpdate, Long userId, Long itemId);

    List<ItemDto> searchItem(Long userId, String text);

    CommentDtoOut addComment(Long userId, CommentDto commentDto, Long itemId);
}
