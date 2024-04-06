package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.itemExceptions.ItemNotBelongUserException;
import ru.practicum.shareit.item.itemExceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private Long initialItemId = 1L;
    private final Map<Long, Item> items = new TreeMap<>();

    @Override
    public Item save(Item item) {
        item.setId(initialItemId++);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item findItem(Long itemId) {
        Collection<Item> items = findAll();
        Optional<Item> item = items.stream()
                .filter(item1 -> item1.getId().equals(itemId))
                .findFirst();
        return item.orElse(null);
    }

    @Override
    public Collection<Item> findAll() {
        return items.values();
    }

    @Override
    public Collection<Item> findItems(Long userId) {
        Collection<Item> itemsByOwner = new ArrayList<>();
        for (Item item : items.values()) {
            if (Objects.equals(item.getOwner(), userId)) {
                itemsByOwner.add(item);
            }
        }
        return itemsByOwner;
    }

    @Override
    public Item update(Map<String, String> itemUpdate, Long userId, Long itemId) {
        if (!items.containsKey(itemId)) {
            throw new ItemNotFoundException("Вещь c id=" + itemId + " не найдена.");
        }
        Item item = items.get(itemId);

        if (!userId.equals(item.getOwner())) {
            throw new ItemNotBelongUserException("Редактировать вещь может только владелец.");
        }
        Item itemForUpdate = new Item(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest());

        for (Map.Entry<String, String> entry : itemUpdate.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key) {
                case "name":
                    itemForUpdate.setName(value);
                    break;
                case "description":
                    itemForUpdate.setDescription(value);
                    break;
                case "available":
                    itemForUpdate.setAvailable(Boolean.parseBoolean(value));
                    break;
            }
        }
        items.put(itemId, itemForUpdate);
        return itemForUpdate;
    }

    @Override
    public List<Item> search(Long userId, String text) {
        String searchText = text.toLowerCase();
        if (searchText == null || searchText.isBlank()) {
            return Collections.emptyList();
        }
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(searchText)
                        || item.getDescription().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
    }
}
