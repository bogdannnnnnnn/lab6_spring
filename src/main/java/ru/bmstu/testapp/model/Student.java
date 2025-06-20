package ru.bmstu.testapp.model;

/**
 * Модель «Студент»: фамилия, имя, количество жетонов.
 */
public class Student {
    private String lastName;
    private String firstName;
    private int tokens;

    public Student() { }

    public Student(String lastName, String firstName, int tokens) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.tokens = tokens;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getTokens() {
        return tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        return "Student{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", tokens=" + tokens +
                '}';
    }
} 