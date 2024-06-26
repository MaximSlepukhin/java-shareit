package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Map;


public interface ItemRepository {
    Item save(Item item);

    Item findItem(Long itemId);

    Collection<Item> findAll();

    Collection<Item> findItems(Long userId);

    Item update(Map<String, String> itemUpdate, Long userId, Long itemId);

    Collection<Item> search(Long userId, String text);
}
