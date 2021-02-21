package org.example;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Merging {

    public static void main(String[] args) {

        List<Person> people = List.of(1, 2, 3).parallelStream()
                .flatMap(id -> List.of(PersonProvider.of(id, Merging::ageProvider), PersonProvider.of(id, Merging::nameProvider)).parallelStream())
                .map(Supplier::get)
                .collect(Collectors.collectingAndThen(Collectors.groupingBy(Person::getId), Merging::mergePerson));
        System.out.println("people = " + people);
    }

    private static List<Person> mergePerson(Map<Integer, List<Person>> map) {
        return map.values().stream()
                .map(Merging::mergePerson)
                .collect(Collectors.toList());
    }

    private static Person mergePerson(List<Person> list) {
        return list.stream()
                .reduce(new Person(), Person::merge);
    }

    static Person ageProvider(int id) {
        System.out.println("ageProvider - id = " + id);
        sleep(1000);
        switch (id) {
            case 1:
                return new Person(id, 34);
            case 2:
                return new Person(id, 33);
            case 3:
                return new Person(id, 28);
            default:
                throw new InvalidParameterException();
        }
    }

    static Person nameProvider(int id) {
        System.out.println("nameProvider - id = " + id);
        sleep(1000);
        switch (id) {
            case 1:
                return new Person(id, "Mike");
            case 2:
                return new Person(id, "Bob");
            case 3:
                return new Person(id, "Zack");
            default:
                throw new InvalidParameterException();
        }
    }

    static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }

    private static class Person {
        private int id = -1;
        private String name;
        private int age = -1;

        public Person() {
        }

        private Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public Person(int id, int age) {
            this.id = id;
            this.age = age;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }

        public Person merge(Person other) {
            if (id < 0) id = other.id;
            if (name == null) name = other.name;
            if (age < 0) age = other.age;
            return this;
        }
    }

    public interface PersonProvider extends Supplier<Person> {
        static PersonProvider of(Integer id, Function<Integer, Person> supplier) {
            return () -> supplier.apply(id);
        }
    }

}
