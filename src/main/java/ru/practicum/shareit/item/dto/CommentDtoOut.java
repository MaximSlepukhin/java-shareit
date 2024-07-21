package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoOut {

    private Long id;

    @NotNull
    private String text;

    @NotNull
    private String authorName;

    @NotNull
    private LocalDateTime created;

}