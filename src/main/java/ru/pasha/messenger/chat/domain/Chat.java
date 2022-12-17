package ru.pasha.messenger.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ru.pasha.messenger.message.domain.Message;
import ru.pasha.messenger.person.domain.Person;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = { "messages", "people", "id" })
@ToString(exclude = { "messages", "people" })
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "first_person_id", nullable = false)
    private long firstId;

    @Column(name = "second_person_id", nullable = false)
    private long secondId;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<Message> messages;

    @ManyToMany
    @JoinTable(
            name = "people_id",
            joinColumns = @JoinColumn(name = "person_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id", referencedColumnName = "id")
    )
    @JsonIgnore
    private List<Person> people;

    public void addMessage(Message message) {
        if (this.messages == null) this.messages = new ArrayList<>();
        this.messages.add(message);
        message.setChat(this);
    }
}
