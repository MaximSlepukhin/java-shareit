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
        log.debug("Предмет успешно добавлен.");
        return newItem;
    }

    @Override
    public Item findItemById(Long itemId) {
        var itemById = repository.findItem(itemId);
        log.debug("Предмет с id=" + itemId + " найден.");
        return itemById;
    }

    @Override
    public Collection<Item> getAllItems() {
        var listOfItems =  repository.findAll();
        log.debug("Список со всеми предметами сформирован.");
        return listOfItems;
    }

    @Override
    public Collection<Item> findItemsOfUser(Long userId) {
        var listOfItems =  repository.findItems(userId);
        log.debug("Список с предметами пользователя с id=" + userId + " сформирован.");
        return listOfItems;
    }

    @Override
    public Item updateItem(Map<String, String> userUpdate, Long userId, Long itemId) {
        var newItem = repository.update(userUpdate,userId, itemId);
        log.debug("Данные предмета с id=" + itemId + " обновлены.");
        return newItem;
    }

    @Override
    public List<Item> searchItem(Long userId, String text) {
        var listOfItems = (List<Item>) repository.search(userId,text);
        log.debug("Предмет по запросу найден.");
        return listOfItems;
    }
}
