package ru.pasha.messenger.message.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pasha.messenger.chat.domain.Chat;
import ru.pasha.messenger.exception.ChatNotFoundException;
import ru.pasha.messenger.exception.MessageNotFoundException;
import ru.pasha.messenger.exception.PersonNotFoundException;
import ru.pasha.messenger.chat.repository.ChatRepository;
import ru.pasha.messenger.exception.ValidationException;
import ru.pasha.messenger.message.validator.MessageValidator;
import ru.pasha.messenger.message.domain.Message;
import ru.pasha.messenger.message.repository.MessageRepository;
import ru.pasha.messenger.message.requestEntity.RequestUpdateEntity;
import ru.pasha.messenger.person.domain.Person;
import ru.pasha.messenger.person.repository.PersonRepository;
import ru.pasha.messenger.utils.date.DateParser;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final ChatRepository chatRepository;
    private final PersonRepository personRepository;
    private final MessageRepository messageRepository;
    private final MessageValidator messageValidator;
    private final DateParser dateParser;

    @Override
    @Transactional
    public Message saveMessage(Message message) {
        if (!messageValidator.validate(message))
            throw new ValidationException("Validation Failed");

        long chatId = message.getChatId();
        long personId = message.getPersonId();

        Optional<Person> person = personRepository.findById(personId);
        if (person.isEmpty())
            throw new PersonNotFoundException(String.format("Person with id %s not found", message.getPersonId()));

        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isEmpty())
            throw new ChatNotFoundException(String.format("Chat with id %s not found", message.getChatId()));

        message.setParsedDate(dateParser.parse(message.getDate()));
        chat.get().addMessage(message);

        return messageRepository.saveAndFlush(message);
    }

    @Override
    public List<Message> getMessagesFromChat(Long id) {
        Optional<Chat> chat = chatRepository.findById(id);

        if (chat.isEmpty())
            throw new ChatNotFoundException(String.format("Chat with id %s not found", id));

        return chat.get().getMessages();
    }

    @Override
    public Message getMessageById(Long id) {
        Optional<Message> message = messageRepository.findById(id);

        if (message.isEmpty())
            throw new MessageNotFoundException(String.format("Message with id %s not found", id));

        return message.get();
    }

    @Override
    @Transactional
    public Message deleteMessage(Long id) {
        Optional<Message> message = messageRepository.findById(id);

        if (message.isEmpty())
            throw new MessageNotFoundException(String.format("Message with id %s not found", id));

        messageRepository.delete(message.get());

        return message.get();
    }

    @Override
    @Transactional
    public Message updateMessageById(RequestUpdateEntity newMessage, Long id) {
        Optional<Message> optMessage = messageRepository.findById(id);

        if (optMessage.isEmpty())
            throw new MessageNotFoundException(String.format("Message with id %s not found", id));

        Date date = dateParser.parse(newMessage.getDate());

        if (newMessage.getMessage() == null)
            throw new ValidationException("Validation Failed");

        Message message = optMessage.get();
        message.setMessage(newMessage.getMessage());
        message.setParsedDate(date);
        message.setDate(newMessage.getDate());

        messageRepository.flush();

        return message;
    }
}
