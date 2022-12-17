package ru.pasha.messenger.person.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.pasha.messenger.person.domain.Person;
import ru.pasha.messenger.utils.validator.Validator;

@Component
@AllArgsConstructor
public class PersonValidator implements Validator<Person> {

    @Override
    public boolean validate(Person person) {
        // Check non-nullable fields
        if (
                person.getFirstname() == null || person.getUsername() == null ||
                person.getPhoneNumber() == null
        ) return false;

        // Check length limit

        if (
                person.getFirstname().length() > 127 || person.getUsername().length() > 63
        ) return false;

        if (person.getLastname() != null) {
            if (person.getLastname().length() > 127) return false;
        }

        if (person.getDescription() != null) {
            if (person.getDescription().length() > 2047) return false;
        }

        // Check age

        if (person.getAge() <= 0 || person.getAge() >= 120) return false;

        return true;
    }
}
