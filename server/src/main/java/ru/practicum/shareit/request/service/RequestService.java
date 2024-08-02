package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.List;

public interface RequestService {
    ItemRequestDtoOut createRequest(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getRequestsOfUser(Long userId);

    List<ItemRequestDto> getRequests(Long userId, Pageable pageable);

    ItemRequestDto getRequestById(Long userId, Long requestId);
}
