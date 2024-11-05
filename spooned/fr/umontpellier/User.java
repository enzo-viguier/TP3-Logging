package fr.umontpellier;
public class User {
    private final int id;

    private java.lang.String name;

    private int age;

    private java.lang.String email;

    private java.lang.String password;

    public User(int id, java.lang.String name, int age, java.lang.String email, java.lang.String password) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public java.lang.String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public java.lang.String getEmail() {
        return email;
    }

    public java.lang.String getPassword() {
        return password;
    }
}