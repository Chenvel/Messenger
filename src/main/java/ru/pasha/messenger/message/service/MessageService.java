package ru.pasha.messenger.message.service;

import ru.pasha.messenger.message.domain.Message;
import ru.pasha.messenger.message.requestEntity.RequestUpdateEntity;

import java.util.List;

public interface MessageService {
    Message saveMessage(Message message);
    List<Message> getMessagesFromChat(Long id);
    Message getMessageById(Long id);
    Message deleteMessage(Long id);
    Message updateMessageById(RequestUpdateEntity newMessage, Long id);
}
