package ru.pasha.messenger.chat.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.pasha.messenger.chat.domain.Chat;
import ru.pasha.messenger.exception.ChatNotFoundException;
import ru.pasha.messenger.exception.PersonNotFoundException;
import ru.pasha.messenger.chat.repository.ChatRepository;
import ru.pasha.messenger.person.domain.Person;
import ru.pasha.messenger.person.repository.PersonRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatServiceImplTest {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private ChatService chatService;

    @BeforeEach
    void init() {
        Person person1 = new Person(1, "Pasha", null, "Pussia", (byte) 13, null, "+12345678901", null);
        Person person2 = new Person(2, "Igor", "ker", "Igor", (byte) 24, null, "+12345678902", null);

        personRepository.save(person1);
        personRepository.save(person2);
    }

    void cleanChat() {
        chatRepository.deleteAll();
    }

    @Test
    void saveChat() {
        Chat chat = new Chat(0, 1, 2, null, null);
        Chat expected = new Chat(1, 1, 2, null, null);

        assertEquals(expected, chatService.saveChat(chat));
    }

    @Test
    void saveChatWithError() {
        Chat chat1 = new Chat(0, 500, 2, null, null);
        Chat chat2 = new Chat(0, 1, 700, null, null);

        assertThrows(PersonNotFoundException.class, () -> chatService.saveChat(chat1), "Person with id 500 not found");
        assertThrows(PersonNotFoundException.class, () -> chatService.saveChat(chat2), "Person with id 700 not found");
    }

    @Test
    void getChatById() {
        cleanChat();

        Chat expected = chatRepository.saveAndFlush(new Chat(1, 1, 2, null, null));
        Chat actual = chatService.getChatById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    void getChatByIdWithError() {
        cleanChat();
        assertThrows(ChatNotFoundException.class, () -> chatService.getChatById(2L), "Chat with id 2 not found");
    }

    @Test
    void getAllChats() {
        cleanChat();

        Chat chat = new Chat(1, 1, 2, null, null);
        chatRepository.saveAndFlush(chat);

        List<Chat> allChats = chatService.getAllChats();
        List<Chat> expected = new ArrayList<>();
        expected.add(chat);

        assertEquals(expected, allChats);
    }

    @Test
    void deleteChatById() {
        // Before deleting
        Chat actual = chatRepository.saveAndFlush(new Chat(1, 1, 2, null, null));

        assertEquals(actual, chatService.deleteChatById(actual.getId()));

        // After deleting

        assertThrows(ChatNotFoundException.class, () -> chatService.getChatById(1L), "Chat with id 1 not found");
    }

//    @AfterEach
//    void cleanUp() {
//        chatRepository.deleteAll();
//        personRepository.deleteAll();
//    }
}