package ru.pasha.messenger.message.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pasha.messenger.message.domain.Message;
import ru.pasha.messenger.utils.validator.Validator;

@Component
@AllArgsConstructor
public class MessageValidator implements Validator<Message> {

    @Override
    public boolean validate(Message message) {
        if (
                message.getMessage() == null || message.getDate() == null ||
                message.getPersonId() <= 0 || message.getChatId() <= 0
        ) return false;

        return true;
    }
}
