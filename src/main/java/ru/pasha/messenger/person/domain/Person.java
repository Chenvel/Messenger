package ru.pasha.messenger.person.domain;

import jakarta.persistence.*;
import lombok.*;
import ru.pasha.messenger.chat.domain.Chat;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = "chats")
@EqualsAndHashCode(exclude = { "id", "chats" })
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "firstname", length = 127, nullable = false)
    private String firstname;

    @Column(name = "lastname", length = 127)
    private String lastname;

    @Column(name = "username", unique = true, length = 63, nullable = false)
    private String username;

    @Column(name = "age", nullable = false)
    private byte age;

    @Column(name = "description", length = 2047)
    private String description;

    @Column(name = "phoneNumber", unique = true, nullable = false)
    private String phoneNumber;

    @ManyToMany(mappedBy = "people")
    private List<Chat> chats;

    public void addChat(Person person, Chat chat) {
        if (this.chats == null) this.chats = new ArrayList<>();
        if (person.chats == null) person.chats = new ArrayList<>();

        this.chats.add(chat);
        person.chats.add(chat);
    }
}
