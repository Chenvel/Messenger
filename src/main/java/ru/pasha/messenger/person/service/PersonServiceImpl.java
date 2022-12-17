package ru.pasha.messenger.person.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pasha.messenger.exception.AlreadyExistsException;
import ru.pasha.messenger.exception.PersonNotFoundException;
import ru.pasha.messenger.exception.ValidationException;
import ru.pasha.messenger.person.domain.Person;
import ru.pasha.messenger.person.repository.PersonRepository;
import ru.pasha.messenger.person.validator.PersonValidator;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonValidator personValidator;

    @Override
    @Transactional
    public Person savePerson(Person person) {
        if (!personValidator.validate(person))
            throw new ValidationException("Validation Failed");

        if (personRepository.findPersonByUsername(person.getUsername()).isPresent())
            throw new AlreadyExistsException(String.format("Person with username %s already exists", person.getUsername()));

        if (personRepository.findPersonByPhoneNumber(person.getPhoneNumber()).isPresent())
            throw new AlreadyExistsException(String.format("Person with phone number %s already exists", person.getPhoneNumber()));

        return personRepository.saveAndFlush(person);
    }

    @Override
    public Person getPersonByField(String field) {
        try {
            return getPersonById(Long.parseLong(field));
        } catch (NumberFormatException e) {
            return getPersonByUsername(field);
        }
    }

    @Override
    public Person getPersonByUsername(String username) {
        Optional<Person> person = personRepository.findPersonByUsername(username);
        if (person.isEmpty())
            throw new PersonNotFoundException(String.format("Person with username %s not found", username));

        return person.get();
    }

    @Override
    public Person getPersonById(Long id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isEmpty())
            throw new PersonNotFoundException(String.format("Person with id %s not found", id));

        return person.get();
    }

    @Override
    @Transactional
    public Person deletePersonById(Long id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isEmpty())
            throw new PersonNotFoundException(String.format("Person with id %s not found", id));

        personRepository.delete(person.get());
        return person.get();
    }

    @Override
    @Transactional
    public Person updatePerson(Person person, Long id) {
        Optional<Person> optPerson = personRepository.findById(id);
        if (optPerson.isEmpty())
            throw new PersonNotFoundException(String.format("Person with id %s not found", id));

        if (!personValidator.validate(person))
            throw new ValidationException("Validation Failed");

        Person oldPerson = optPerson.get();

        Optional<Person> personWithSameUsername = personRepository.findPersonByUsername(person.getUsername());
        if (personWithSameUsername.isPresent() && !personWithSameUsername.get().getUsername().equals(oldPerson.getUsername()))
            throw new AlreadyExistsException(String.format("Person with username %s already exists", person.getUsername()));

        Optional<Person> personWithSamePhone = personRepository.findPersonByPhoneNumber(person.getPhoneNumber());
        if (personWithSamePhone.isPresent() && !personWithSamePhone.get().getPhoneNumber().equals(oldPerson.getPhoneNumber()))
            throw new AlreadyExistsException(String.format("Person with phone number %s already exists", person.getPhoneNumber()));

        oldPerson.setAge(person.getAge());
        oldPerson.setDescription(person.getDescription());
        oldPerson.setLastname(person.getLastname());
        oldPerson.setFirstname(person.getFirstname());
        oldPerson.setUsername(person.getUsername());
        oldPerson.setPhoneNumber(person.getPhoneNumber());

        personRepository.flush();

        return oldPerson;
    }


}
