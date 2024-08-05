package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final RequestService requestService;


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDtoOut save(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestBody ItemRequestDto itemRequestDto) {
        log.info("POST запрос на добавление запроса от пользователя с id=" + userId + ".");
        return requestService.createRequest(userId, itemRequestDto);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getRequestsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET запрос на получение списка запросов с данными об ответах на них");
        return requestService.getRequestsOfUser(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable Long requestId) {
        log.info("GET запрос на получение списка запросов с данными об ответах на них");
        return requestService.getRequestById(userId, requestId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(value = "from", required = false,
                                                        defaultValue = "0") Integer from,
                                                @RequestParam(value = "size", required = false,
                                                        defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        return requestService.getRequests(userId, pageable);
    }
}