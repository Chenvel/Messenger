package ru.pasha.messenger.message.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ru.pasha.messenger.chat.domain.Chat;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "msg")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(exclude = {"chat"})
@EqualsAndHashCode(exclude = {"id", "chat"})
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "parsed_date")
    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date parsedDate;

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    @JsonIgnore
    private Chat chat;

    @Column(name = "person_id", nullable = false)
    private long personId;

    @Column(name = "chat_link", nullable = false)
    private long chatId;
}
