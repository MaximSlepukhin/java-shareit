package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest, List<ItemDto> items, Long userId) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .items(items)
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequestDtoOut mapToItemRequestDtoOut(ItemRequest itemRequest) {
        return ItemRequestDtoOut.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto, User user, LocalDateTime now) {
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .requester(user)
                .created(now)
                .build();
    }
}