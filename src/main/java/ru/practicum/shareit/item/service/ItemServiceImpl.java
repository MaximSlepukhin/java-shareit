package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    @Override
    public Item createItem(Long userId, Item item) {
        item.setOwner(userId);
        var newItem = repository.save(item);
        log.debug("Добавлен предмет.");
        return newItem;
    }

    @Override
    public Item findItemById(Long itemId) {
        return repository.findItem(itemId);
    }

    @Override
    public Collection<Item> getAllItems() {
        return repository.findAll();
    }

    @Override
    public Collection<Item> findItemsOfUser(Long userId) {
        return repository.findItems(userId);
    }

    @Override
    public Item updateItem(Map<String, String> userUpdate, Long userId, Long itemId) {
        return repository.update(userUpdate,userId, itemId);
    }

    @Override
    public List<Item> searchItem(Long userId, String text) {
        return (List<Item>) repository.search(userId,text);
    }
}
