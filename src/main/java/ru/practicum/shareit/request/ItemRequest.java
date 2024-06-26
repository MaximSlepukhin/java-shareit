package ru.practicum.shareit.request;

import lombok.Data;
import lombok.ToString;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Entity
@Table(name = "requests")
public class ItemRequest {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    @ToString.Exclude
    private User requester;

    @Column(name = "created")
    @FutureOrPresent
    private LocalDateTime created;

}
