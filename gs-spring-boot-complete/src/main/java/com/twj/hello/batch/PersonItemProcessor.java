package com.twj.hello.batch;

import org.springframework.batch.item.ItemProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(final Person person) throws Exception {
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();

        final Person transformedPerson = new Person(firstName, lastName, person.getAge());

        log.info("Converting (" + person + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }

}
