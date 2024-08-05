package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;


@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> save(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("POST запрос на добавление запроса от пользователя с id=" + userId + ".");
        return itemRequestClient.createRequest(userId, itemRequestDto);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getRequestsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET запрос на получение списка запросов с данными об ответах на них");
        return itemRequestClient.getRequestsOfUser(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long requestId) {
        log.info("GET запрос на получение списка запросов с данными об ответах на них");
        return itemRequestClient.getRequestById(userId, requestId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(value = "from", required = false,
                                                          defaultValue = "0") Integer from,
                                                  @RequestParam(value = "size", required = false,
                                                          defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        return itemRequestClient.getRequests(userId, from, size);
    }
}