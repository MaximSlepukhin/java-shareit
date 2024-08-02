package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceImplTest {
    @Mock
    UserRepository userRepository;

    @Mock
    RequestRepository requestRepository;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    RequestServiceImpl requestServiceImpl;

    static LocalDateTime created = LocalDateTime.now();

    User owner = User.builder().id(2L).name("User2").email("user2@mail.ru").build();

    User anotherUser = User.builder().id(3L).name("User3").email("user3@mail.ru").build();
    User requester = User.builder().id(1L).name("User1").email("user1@mail.ru").build();


    ItemRequestDto itemRequestDtoFirst = ItemRequestDto.builder()
            .description("Хотел бы воспользоваться дрелью").build();

    ItemRequest itemRequestFirstFromRepository = ItemRequest.builder().id(1L).
            description("Хотел бы воспользоваться дрелью").requester(requester).created(created).build();

    ItemRequestDtoOut itemRequestDtoFirstOut = ItemRequestMapper.mapToItemRequestDtoOut(itemRequestFirstFromRepository);
    ItemDto itemDto = ItemDto.builder().id(1L).name("Дрель").description("Аккумулятораня дрель").available(true).
            requestId(1L).build();
    Item item = Item.builder().id(1L).name("Дрель").description("Аккумулятораня дрель").available(true)
            .owner(owner).request(itemRequestFirstFromRepository).build();

    List<ItemDto> listOfItemsDto = List.of(itemDto);
    List<Item> listOfItems = List.of(item);

    ItemRequestDto itemRequestDtoForList = ItemRequestDto.builder().id(1L)
            .description("Хотел бы воспользоваться дрелью").created(created).items(listOfItemsDto).build();
    ItemRequest itemRequestForList = ItemRequest.builder().id(1L)
            .description("Хотел бы воспользоваться дрелью").requester(requester).created(created).build();

    List<ItemRequestDto> listOfItemRequestDto = List.of(itemRequestDtoForList);
    List<ItemRequest> listOfItemRequest = List.of(itemRequestForList);
    Pageable pageable = PageRequest.of(1, 20);

    @Test
    void shouldCreateRequestTest() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(requester));

        when(requestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequestFirstFromRepository);

        ItemRequestDtoOut result = requestServiceImpl.createRequest(1L, itemRequestDtoFirst);

        Assertions.assertEquals(result, itemRequestDtoFirstOut);
    }

    @Test
    void shouldGetRequestsOfUser() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(requester));
        when(requestRepository.findByRequesterId(1L))
                .thenReturn(listOfItemRequest);
        when(itemRepository.findByRequestIdIn(anyList()))
                .thenReturn(listOfItems);

        List<ItemRequestDto> result = requestServiceImpl.getRequestsOfUser(1L);

        Assertions.assertEquals(result, listOfItemRequestDto);
    }

    @Test
    void shouldGetRequests() {
        when(userRepository.findById(3L))
                .thenReturn(Optional.ofNullable(anotherUser));
        Page<ItemRequest> requestsPage = new PageImpl<>(listOfItemRequest);
        when(requestRepository.findByRequesterIdNot(3L, pageable))
                .thenReturn(requestsPage);
        when(itemRepository.findByRequestIdIn(anyList()))
                .thenReturn(listOfItems);

        List<ItemRequestDto> result = requestServiceImpl.getRequests(3L, pageable);

        Assertions.assertEquals(result, listOfItemRequestDto);
    }

    @Test
    void shouldGetRequestById() {
        when(userRepository.findById(3L))
                .thenReturn(Optional.ofNullable(anotherUser));
        when(requestRepository.findById(1L))
                .thenReturn(Optional.ofNullable(itemRequestFirstFromRepository));
        when(itemRepository.findByRequestId(1L))
                .thenReturn(listOfItems);

        ItemRequestDto result = requestServiceImpl.getRequestById(3L, 1L);

        Assertions.assertEquals(result, itemRequestDtoForList );
    }
}
