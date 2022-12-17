package ru.pasha.messenger.person.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.pasha.messenger.exception.AlreadyExistsException;
import ru.pasha.messenger.exception.PersonNotFoundException;
import ru.pasha.messenger.exception.ValidationException;
import ru.pasha.messenger.person.domain.Person;
import ru.pasha.messenger.person.repository.PersonRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersonServiceImplTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    @Test
    void savePerson() {
        Person person = new Person(
                0L,
                "Pasha",
                "Levchenko",
                "Pussia001",
                (byte) 23,
                "something here",
                "+1234567890",
                null
        );

        Person expected = new Person(
                1L,
                "Pasha",
                "Levchenko",
                "Pussia001",
                (byte) 23,
                "something here",
                "+1234567890",
                null
        );

        assertEquals(expected, personService.savePerson(person));
    }

    @Test
    void savePersonWithoutFirstname() {
        Person person = new Person(
                0L,
                null,
                "Levchenko",
                "Pussia",
                (byte) 23,
                "something here",
                "+1234567890",
                null
        );

        assertThrows(ValidationException.class, () -> {
            personService.savePerson(person);
        }, "Validation Failed");
    }

    @Test
    void savePersonWithoutLastname() {
        Person person = new Person(
                0L,
                "Pasha",
                null,
                "Pussia0",
                (byte) 23,
                "something here",
                "+1234567890111",
                null
        );

        Person expected = new Person(
                2L,
                "Pasha",
                null,
                "Pussia0",
                (byte) 23,
                "something here",
                "+1234567890111",
                null
        );

        assertEquals(expected, personService.savePerson(person));
    }

    @Test
    void savePersonWithoutUsername() {
        Person person = new Person(
                0L,
                "Pasha",
                "Levchenko",
                null,
                (byte) 23,
                "something here",
                "+1234567890",
                null
        );

        assertThrows(ValidationException.class, () -> {
            personService.savePerson(person);
        }, "Validation Failed");
    }

    @Test
    void savePersonWithExistedUsername() {
        Person person = new Person(
                0L,
                "Pasha",
                "Levchenko",
                "Pussia0",
                (byte) 23,
                "something here",
                "+1234567890222",
                null
        );

        assertThrows(AlreadyExistsException.class, () -> {
            personService.savePerson(person);
        }, "Person with username Pussia0 already exists");
    }

    @Test
    void savePersonWithWrongAge() {
        Person person1 = new Person(
                0L,
                "Pasha",
                "Levchenko",
                "Pussia1",
                (byte) -1,
                "something here",
                "+12345678902",
                null
        );

        assertThrows(ValidationException.class, () -> {
            personService.savePerson(person1);
        }, "Validation Failed");

        Person person2 = new Person(
                0L,
                "Pasha",
                "Levchenko",
                "Pussia1",
                (byte) 140,
                "something here",
                "+12345678902",
                null
        );

        assertThrows(ValidationException.class, () -> {
            personService.savePerson(person2);
        }, "Validation Failed");
    }

    @Test
    void savePersonWithoutDescription() {
        Person person = new Person(
                0L,
                "Pasha",
                "Levchenko",
                "Pussia1",
                (byte) 23,
                null,
                "+1234567890223",
                null
        );

        Person expected = new Person(
                0L,
                "Pasha",
                "Levchenko",
                "Pussia1",
                (byte) 23,
                null,
                "+1234567890223",
                null
        );

        assertEquals(expected, personService.savePerson(person));
    }

    @Test
    void savePersonWithoutPhoneNumber() {
        Person person = new Person(
                0L,
                "Pasha",
                "Levchenko",
                "Pussia2",
                (byte) 23,
                "something here",
                null,
                null
        );

        assertThrows(ValidationException.class, () -> {
            personService.savePerson(person);
        }, "Validation Failed");
    }

    @Test
    void getPersonById() {
        Person expected = new Person(
                0L,
                "Pasha",
                "Levchenko",
                "Pussia12",
                (byte) 12,
                "Something here",
                "+123456789012",
                null
        );

        personRepository.saveAndFlush(expected);

        assertEquals(expected, personService.getPersonByField(Long.toString(expected.getId())));
    }

    @Test
    void getPersonByUsername() {
        Person expected = new Person(
                0L,
                "Pasha",
                "Levchenko",
                "Pussia13",
                (byte) 12,
                "Something here",
                "+123456789013",
                null
        );

        personRepository.saveAndFlush(expected);

        assertEquals(expected, personService.getPersonByField(expected.getUsername()));
    }

    @Test
    void getPersonByUsernameWithError() {
        assertThrows(PersonNotFoundException.class, () -> {
            personService.getPersonByUsername("NonExistedMan");
        }, "Person with username NonExistedMan not found");
    }

    @Test
    void getPersonByIdWithError() {
        assertThrows(PersonNotFoundException.class, () -> {
            personService.getPersonByUsername("999");
        }, "Person with id 999 not found");
    }

    @Test
    void deletePerson() {
        // Before deleting
        Person expected = new Person(
                0L,
                "Pasha",
                "Levchenko",
                "Pussia14",
                (byte) 12,
                "Something here",
                "+123456789014",
                null
        );

        personRepository.saveAndFlush(expected);

        assertEquals(expected, personService.deletePersonById(expected.getId()));

        // After deleting

        assertThrows(PersonNotFoundException.class, () -> {
            personService.getPersonByField(Long.toString(expected.getId()));
        }, String.format("Person with id %s not found", expected.getId()));
    }

    @Test
    void deletePersonWithError() {
        assertThrows(PersonNotFoundException.class, () -> {
            personService.deletePersonById(10000L);
        }, "Person with id 10000 not found");
    }

    @Test
    void updatePerson() {
        Person actual = new Person(
                0L,
                "Pasha",
                "Levchenko",
                "Pussia15",
                (byte) 12,
                "Something here",
                "+123456789015",
                null
        );

        personRepository.saveAndFlush(actual);

        Person expected = new Person(
                0L,
                "Pasha1",
                "Levchenko1",
                "Pussia20",
                (byte) 34,
                "Something",
                "+123456789020",
                null
        );

        assertEquals(expected, personService.updatePerson(expected, actual.getId()));
    }

    @Test
    void updatePersonWithError() {
        Person person = new Person(
                0L,
                "Pasha",
                "Levchenko",
                "Pussia25",
                (byte) 12,
                "Something here",
                "+123456789025",
                null
        );

        Person person2 = new Person(
                0L,
                "Pasha",
                "Levchenko",
                "Pussia123",
                (byte) 12,
                "Something here",
                "+1234567890123",
                null
        );

        personRepository.saveAndFlush(person);
        personRepository.saveAndFlush(person2);

        Person newPerson1 = new Person(
                0L,
                "Pasha",
                "Levchenko",
                null,
                (byte) 12,
                "Something here",
                "+123456789025",
                null
        );

        assertThrows(ValidationException.class, () -> {
            personService.updatePerson(newPerson1, person.getId());
        }, "Validation Failed");

        Person newPerson2 = new Person(
                0L,
                "Pasha",
                "Levchenko",
                "Pussia26",
                (byte) 12,
                "Something here",
                "+1234567890123",
                null
        );

        assertThrows(AlreadyExistsException.class, () -> {
            personService.updatePerson(newPerson2, person.getId());
        }, "Person with phone number +1234567890123 already exists");

        Person newPerson3 = new Person(
                0L,
                "Pasha",
                "Levchenko",
                "Pussia123",
                (byte) 12,
                "Something here",
                "+12345678901230",
                null
        );

        assertThrows(AlreadyExistsException.class, () -> {
            personService.updatePerson(newPerson3, person.getId());
        }, "Person with username Pussia123 already exists");
    }

//    @AfterEach
//    void cleanUp() {
//        personRepository.deleteAll();
//    }
}