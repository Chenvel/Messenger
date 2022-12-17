package ru.pasha.messenger.message.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.pasha.messenger.chat.domain.Chat;
import ru.pasha.messenger.chat.repository.ChatRepository;
import ru.pasha.messenger.exception.MessageNotFoundException;
import ru.pasha.messenger.exception.ValidationException;
import ru.pasha.messenger.message.domain.Message;
import ru.pasha.messenger.message.repository.MessageRepository;
import ru.pasha.messenger.message.requestEntity.RequestUpdateEntity;
import ru.pasha.messenger.person.domain.Person;
import ru.pasha.messenger.person.repository.PersonRepository;
import ru.pasha.messenger.utils.date.DateParser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageServiceImplTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private DateParser dateParser;

    @BeforeEach
    void init() {
        Person person1 = new Person(1, "Pasha", null, "Pussia", (byte) 13, null, "+12345678901", null);
        Person person2 = new Person(2, "Igor", "ker", "Igor", (byte) 24, null, "+12345678902", null);

        personRepository.saveAndFlush(person1);
        personRepository.saveAndFlush(person2);

        Chat chat1 = new Chat(1, 1, 2, null, new ArrayList<>(List.of(person1, person2)));

        chatRepository.save(chat1);
    }

    @Test
    void saveMessage() {
        Chat existedChat = chatRepository.findAll().stream().findAny().get();
        Message actual = new Message(
                0,
                "Message1",
                "2042-05-28T21:12:01.000Z",
                null,
                null,
                1,
                existedChat.getId()
        );
        Message expected = new Message(
                1,
                "Message1",
                "2042-05-28T21:12:01.000Z",
                dateParser.parse("2042-05-28T21:12:01.000Z"),
                existedChat,
                1,
                existedChat.getId()
        );

        assertEquals(expected, messageService.saveMessage(actual));
    }

    @Test
    void saveMessageWithWrongDate() {
        Chat existedChat = chatRepository.findAll().stream().findAny().get();
        Message expected = new Message(
                0,
                "Message1",
                "2042-05-28",
                null,
                null,
                1,
                existedChat.getId()
        );

        assertThrows(ValidationException.class, () -> {
            messageService.saveMessage(expected);
        }, "Date format is wrong");
    }

    @Test
    void saveMessageWithoutMessage() {
        Message expected = new Message(
                0,
                null,
                "2042-05-28T21:12:01.000Z",
                null,
                null,
                1,
                1
        );

        assertThrows(ValidationException.class, () -> {
            messageService.saveMessage(expected);
        }, "Validation Failed");
    }

    @Test
    void saveMessageWithWrongPersonId() {
        Message expected = new Message(
                0,
                "Message",
                "2042-05-28T21:12:01.000Z",
                null,
                null,
                -1,
                1
        );

        assertThrows(ValidationException.class, () -> {
            messageService.saveMessage(expected);
        }, "Person with id -1 not found");
    }

    @Test
    void saveMessageWithWrongChatId() {
        Message expected = new Message(
                0,
                "Message",
                "2042-05-28T21:12:01.000Z",
                null,
                null,
                2,
                -1
        );

        assertThrows(ValidationException.class, () -> {
            messageService.saveMessage(expected);
        }, "Chat with id -1 not found");
    }

    @Test
    void getMessagesFromChat() {
        messageRepository.deleteAll();
        Chat existedChat = chatRepository.findAll().stream().findAny().get();

        Message message1 = messageService.saveMessage(new Message(
                0,
                "Message1",
                "2042-05-28T21:12:01.000Z",
                dateParser.parse("2042-05-28T21:12:01.000Z"),
                null,
                2,
                existedChat.getId()
        ));

        Message message2 = messageService.saveMessage(new Message(
                0,
                "Message2",
                "2042-05-28T21:12:01.000Z",
                dateParser.parse("2042-05-28T21:12:01.000Z"),
                null,
                2,
                existedChat.getId()
        ));

        int expectedSize = 2;
        List<Message> expectedResult = new ArrayList<>(List.of(message1, message2));


        assertEquals(expectedSize, messageService.getMessagesFromChat(existedChat.getId()).size());
        assertEquals(expectedResult, messageService.getMessagesFromChat(existedChat.getId()));
    }

    @Test
    void getMessageById() {
        messageRepository.deleteAll();

        Message message = messageRepository.saveAndFlush(new Message(
                0,
                "Message",
                "2042-05-28T21:12:01.000Z",
                dateParser.parse("2042-05-28T21:12:01.000Z"),
                null,
                2,
                2
        ));

        assertEquals(message, messageService.getMessageById(message.getId()));
    }

    @Test
    void getMessageByIdWithWrongId() {
        assertThrows(MessageNotFoundException.class, () -> {
            messageService.getMessageById(1000L);
        }, "Message with id 1000 not found");
    }

    @Test
    void deleteMessage() {
        // Before deleting

        Message message = messageRepository.saveAndFlush(new Message(
                0,
                "Delete this message",
                "2042-05-28T21:12:01.000Z",
                dateParser.parse("2042-05-28T21:12:01.000Z"),
                null,
                2,
                2
        ));

        assertEquals(message, messageService.deleteMessage(message.getId()));

        // After deleting

        assertThrows(MessageNotFoundException.class, () -> {
            messageService.getMessageById(message.getId());
        }, String.format("Message with id %s not found", message.getId()));
    }

    @Test
    void deleteMessageWithError() {
        assertThrows(MessageNotFoundException.class, () -> {
            messageService.deleteMessage(1000L);
        }, "Message with id 1000 not found");
    }

    @Test
    void updateMessageById() {
        Message message = messageRepository.saveAndFlush(new Message(
                0,
                "Message",
                "2042-05-28T21:12:01.000Z",
                dateParser.parse("2042-05-28T21:12:01.000Z"),
                null,
                2,
                2
        ));

        Message expected = new Message(
                0,
                "New Message",
                "2022-05-28T21:12:01.000Z",
                dateParser.parse("2022-05-28T21:12:01.000Z"),
                null,
                2,
                2
        );

        assertEquals(expected, messageService.updateMessageById(
                new RequestUpdateEntity("New Message", "2022-05-28T21:12:01.000Z"),
                message.getId()
        ));
    }

    @Test
    void updateMessageByIdWithoutMessage() {
        Message message = messageRepository.saveAndFlush(new Message(
                0,
                "Message",
                "2042-05-28T21:12:01.000Z",
                dateParser.parse("2042-05-28T21:12:01.000Z"),
                null,
                2,
                2
        ));

        assertThrows(ValidationException.class, () -> {
            messageService.updateMessageById(
                    new RequestUpdateEntity(null, "2022-05-28T21:12:01.000Z"),
                    message.getId()
            );
        }, "Validation Failed");
    }

    @Test
    void updateMessageByIdWithWrongDate() {
        Message message = messageRepository.saveAndFlush(new Message(
                0,
                "Message",
                "2042-05-28T21:12:01.000Z",
                dateParser.parse("2042-05-28T21:12:01.000Z"),
                null,
                2,
                2
        ));

        assertThrows(ValidationException.class, () -> {
            messageService.updateMessageById(
                    new RequestUpdateEntity("new message", "2022-05-28"),
                    message.getId()
            );
        }, "Date format is wrong");
    }

    @Test
    void updateMessageByIdWithWrongId() {
        assertThrows(MessageNotFoundException.class, () -> {
            messageService.updateMessageById(
                    new RequestUpdateEntity("new message", "2042-05-28T21:12:01.000Z"),
                    1000L
            );
        }, "Message with id 1000 not found");
    }

//    @AfterEach
//    void cleanUp() {
//        chatRepository.deleteAll();
//        personRepository.deleteAll();
//        messageRepository.deleteAll();
//    }
}