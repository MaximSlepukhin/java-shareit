package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.itemExceptions.NotFoundException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    RequestRepository requestRepository;

    @InjectMocks
    ItemServiceImpl itemServiceImpl;

    User owner = User.builder().id(1L).name("User1").email("user1@mail.ru").build();
    User firstBooker = User.builder().id(2L).name("User2").email("user2@mail.ru").build();
    User secondBooker = User.builder().id(3L).name("User3").email("user3@mail.ru").build();
    User requester = User.builder().id(3L).name("User2").email("user2@mail.ru").build();

    ItemRequest itemRequest = ItemRequest.builder().id(1L).description("Ищу дрель").requester(requester).build();
    Item firstItemFromRepository = Item.builder().id(1L).name("Дрель").description("Простая дрель")
            .available(true).owner(owner).build();
    Item secondItemToRepository = Item.builder().name("Дрель").description("Простая дрель")
            .available(true).owner(owner).request(itemRequest).build();
    Item secondItemFromRepository = Item.builder().id(1L).name("Дрель").description("Простая дрель")
            .available(true).owner(owner).request(itemRequest).build();
    ItemDto firstItemDto = ItemMapper.mapToItemDto(firstItemFromRepository);
    ItemDto secondItemDto = ItemMapper.mapToItemDto(secondItemFromRepository);
    Item firstItemToRepository = Item.builder().name("Дрель").description("Простая дрель")
            .available(true).owner(owner).build();
    Item itemAfterUpdate = Item.builder().id(1L).name("Дрель+").description("Аккумуляторная дрель")
            .available(false).owner(owner).build();
    ItemDto itemDtoAfterUpdate = ItemDto.builder().id(1L).name("Дрель+").description("Аккумуляторная дрель")
            .available(false).build();

    Booking lastBooking = Booking.builder().id(1L).start(LocalDateTime.now().minusDays(10))
            .end(LocalDateTime.now().minusDays(9)).item(firstItemFromRepository).booker(firstBooker)
            .status(BookingStatus.APPROVED).build();
    Booking nextBooking = Booking.builder().id(2L).start(LocalDateTime.now().minusDays(4))
            .end(LocalDateTime.now().minusDays(2)).item(firstItemFromRepository).booker(secondBooker)
            .status(BookingStatus.APPROVED).build();
    BookingForItemDto lastBookingDto = BookingMapper.toBookingForItemDto(lastBooking);
    BookingForItemDto nextBookingDto = BookingMapper.toBookingForItemDto(nextBooking);

    Comment commentOfFirstUser = Comment.builder().id(1L).text("First comment").item(firstItemFromRepository)
            .user(firstBooker).created(LocalDateTime.now().minusDays(2)).build();
    Comment commentOfSecondUser = Comment.builder().id(1L).text("Second comment").item(firstItemFromRepository)
            .user(secondBooker).created(LocalDateTime.now().minusDays(1)).build();
    CommentDto commentDto = CommentDto.builder().text("First comment").build();
    CommentDtoOut commentDtoOutOfFirstUser = CommentMapper.mapToCommentDtoOut(commentOfFirstUser);
    CommentDtoOut commentDtoOutOfSecondUser = CommentMapper.mapToCommentDtoOut(commentOfSecondUser);

    List<CommentDtoOut> listOfCommentsDto = List.of(commentDtoOutOfFirstUser, commentDtoOutOfSecondUser);
    List<Comment> listOfComments = List.of(commentOfFirstUser, commentOfSecondUser);
    ItemDtoOut itemDtoOut = ItemDtoOut.builder().id(1L).name("Дрель").description("Простая дрель").available(true)
            .lastBooking(lastBookingDto).nextBooking(nextBookingDto).comments(listOfCommentsDto).build();

    List<Item> listOfItemsByRequest = List.of(firstItemFromRepository);
    Collection<Item> listOfItems = List.of(firstItemFromRepository);
    List<ItemDto> listOfItemsDto = List.of(firstItemDto);
    List<ItemDtoOut> listOfItemsDtoOut = List.of(itemDtoOut);

    List<Booking> listOfBookings = List.of(nextBooking, lastBooking);
    Collection<Booking> listBookings = List.of(nextBooking, lastBooking);

    Pageable pageable = PageRequest.of(1, 20);



    static Map<String, String> mapForItemUpdate = new HashMap<>();

    @BeforeAll
    static void createMapWithEmail() {
        mapForItemUpdate.put("name", "Дрель+");
        mapForItemUpdate.put("description", "Аккумуляторная дрель");
        mapForItemUpdate.put("available", "false");
    }
    //+
    @Test
    void shouldCreateItemWithoutRequest() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(owner));
        when(itemRepository.save(firstItemToRepository))
                .thenReturn(firstItemFromRepository);

        ItemDto result = itemServiceImpl.createItem(1L, firstItemDto);

        Assertions.assertEquals(result, ItemMapper.mapToItemDto(firstItemFromRepository));
    }
    //+
    @Test
    void createItemWithItemRequest() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(owner));
        when(requestRepository.findById(1L))
                .thenReturn(Optional.ofNullable(itemRequest));
        when(itemRepository.save(secondItemToRepository))
                .thenReturn(secondItemFromRepository);

        ItemDto result = itemServiceImpl.createItem(1L, secondItemDto);

        Assertions.assertEquals(result, ItemMapper.mapToItemDto(secondItemFromRepository));
    }
    //+
    @Test
    void shouldCreateItemShouldThrowExceptionWithNotValidRequest() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(owner));
        when(requestRepository.findById(1L))
                .thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemServiceImpl.createItem(1L, secondItemDto));
        Assertions.assertEquals(exception.getMessage(), "Запрос с id=1 не найден.");
    }
    //+-
    @Test
    void shouldFindItemById() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(owner));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(firstItemFromRepository));
        Page<Booking> bookingPage = new PageImpl<>(listOfBookings);
        when(bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(1L, BookingStatus.APPROVED, pageable))
                .thenReturn(bookingPage);
        when(commentRepository.findByItemId(1L))
                .thenReturn(listOfComments);

        ItemDtoOut result = itemServiceImpl.findItemById(1L, 1L, pageable);

        Assertions.assertEquals(result.getId(), itemDtoOut.getId());
        Assertions.assertEquals(result.getName(), itemDtoOut.getName());
        Assertions.assertEquals(result.getDescription(), itemDtoOut.getDescription());
    }
    //+
    @Test
    void shouldUpdateItem() {
        when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(firstItemFromRepository));
        when(itemRepository.save(itemAfterUpdate))
                .thenReturn(itemAfterUpdate);

        ItemDto result = itemServiceImpl.updateItem(mapForItemUpdate, 1L, 1L);

        Assertions.assertEquals(result, itemDtoAfterUpdate);
    }
    //+
    @Test
    void shouldSearchItem() {
        Page<Item> itemsPage = new PageImpl<>(listOfItemsByRequest);
        when(itemRepository
                .findByNameOrDescriptionContainingIgnoreCaseAndAvailable(
                        "дрель", "дрель", true, pageable))
                .thenReturn(itemsPage);

        List<ItemDto> result = itemServiceImpl.searchItem(2L, "дрель", pageable);

        Assertions.assertEquals(result, listOfItemsDto);
    }
    //+
    @Test
    void shouldNotSearchItemWhenTextIsEmpty() {
        List<ItemDto> result = itemServiceImpl.searchItem(2L, "", pageable);

        Assertions.assertEquals(result.size(), 0);
    }
    //+-
    @Test
    void shouldAddComment() {
        when(userRepository.findById(2L))
                .thenReturn(Optional.ofNullable(firstBooker));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.ofNullable(firstItemFromRepository));
        when(bookingRepository.findByBookerIdAndStatusAndEndIsBeforeOrderByStartDesc(eq(2L),
                eq(BookingStatus.APPROVED), any(LocalDateTime.class)))
                .thenReturn((List<Booking>) listBookings);
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(commentOfFirstUser);

        CommentDtoOut result = itemServiceImpl.addComment(2L, commentDto, 1L);

        Assertions.assertEquals(result.getText(), commentDtoOutOfFirstUser.getText());

    }
    //+-
    @Test
    void shouldFindItemsOfUser() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(owner));
        when(itemRepository.findByOwnerId(1L))
                .thenReturn(listOfItems);
        Page<Booking> bookingPage = new PageImpl<>(listOfBookings);
        when(bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(1L, BookingStatus.APPROVED, pageable))
                .thenReturn(bookingPage);
        when(commentRepository.findByItemIdIn(anyList()))
                .thenReturn(listOfComments);

        List<ItemDtoOut> result = itemServiceImpl.findItemsOfUser(1L, pageable);

        Assertions.assertEquals(result.size(), listOfItemsDtoOut.size());
    }
}
