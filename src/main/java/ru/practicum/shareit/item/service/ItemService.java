package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ItemService {
    Item createItem(Long userId, Item item);

    Item findItemById(Long itemId);

    Collection<Item> getAllItems();

    Collection<Item> findItemsOfUser(Long userId);

    Item updateItem(Map<String, String> userUpdate, Long userId, Long itemId);

    List<Item> searchItem(Long userId, String text);
}
