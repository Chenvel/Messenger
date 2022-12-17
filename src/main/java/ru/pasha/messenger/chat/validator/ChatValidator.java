package ru.pasha.messenger.chat.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pasha.messenger.chat.domain.Chat;
import ru.pasha.messenger.person.repository.PersonRepository;
import ru.pasha.messenger.utils.validator.Validator;

@Component
@AllArgsConstructor
public class ChatValidator implements Validator<Chat> {

    @Override
    public boolean validate(Chat chat) {
        // firstId and secondId must be bigger than 0
        if (chat.getFirstId() <= 0 || chat.getSecondId() <= 0) return false;

        return true;
    }
}
