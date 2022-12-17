package ru.pasha.messenger.chat.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pasha.messenger.chat.domain.Chat;
import ru.pasha.messenger.chat.repository.ChatRepository;
import ru.pasha.messenger.chat.validator.ChatValidator;
import ru.pasha.messenger.exception.ChatNotFoundException;
import ru.pasha.messenger.exception.PersonNotFoundException;
import ru.pasha.messenger.exception.ValidationException;
import ru.pasha.messenger.person.domain.Person;
import ru.pasha.messenger.person.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final PersonRepository personRepository;
    private final ChatValidator chatValidator;

    @Override
    @Transactional
    public Chat saveChat(Chat chat) {
        if (!chatValidator.validate(chat)) throw new ValidationException("Validation Failed");

        Optional<Person> first = personRepository.findById(chat.getFirstId());
        if (first.isEmpty())
            throw new PersonNotFoundException(String.format("Person with id %s not found", chat.getFirstId()));

        Optional<Person> second = personRepository.findById(chat.getSecondId());
        if (second.isEmpty())
            throw new PersonNotFoundException(String.format("Person with id %s not found", chat.getSecondId()));

        first.get().addChat(second.get(), chat);
        return chatRepository.saveAndFlush(chat);
    }

    @Override
    public Chat getChatById(Long id) {
        Optional<Chat> chat = chatRepository.findById(id);

        if (chat.isEmpty())
            throw new ChatNotFoundException(String.format("Chat with id %s not found", id));

        System.out.println(chat.get().getMessages());


        return chat.get();
    }

    @Override
    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    @Override
    @Transactional
    public Chat deleteChatById(Long id) {
        Optional<Chat> chat = chatRepository.findById(id);

        if (chat.isEmpty())
            throw new ChatNotFoundException(String.format("Chat with id %s not found", id));

        chatRepository.delete(chat.get());

        return chat.get();
    }
}
