package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;


public class CommentMapper {

    public static CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .text(comment.getText()).build();
    }

    public static CommentDtoOut mapToCommentDtoOut(Comment comment) {
        return CommentDtoOut.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getUser().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment mapToComment(CommentDto commentDto, User user, Item item) {
        return Comment.builder()
                .text(commentDto.getText())
                .item(item)
                .user(user)
                .created(LocalDateTime.now())
                .build();
    }
}