package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Collection<Item> findByOwnerId(Long userId);

    List<Item> findByNameOrDescriptionContainingIgnoreCaseAndAvailable(String name,
                                                                       String description,
                                                                       Boolean available);

}