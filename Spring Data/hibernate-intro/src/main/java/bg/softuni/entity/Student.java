package bg.softuni.entity;


import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "registrationDate")
    private LocalDate registrationDate;

    @Column(name = "gpa")
    private double gpa;


    public Student(String name, int age, String email, LocalDate registrationDate, double gpa) {
        setName(name);
        setAge(age);
        setEmail(email);
        setRegistrationDate(registrationDate);
        setGpa(gpa);
    }

    public Student() {}

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (name.length() > 50) {
            throw new IllegalArgumentException("Name length cannot exceed 50 characters");
        }
        this.name = name;
    }

    public void setAge(int age) {
        if (age < 7 || age > 120) { // Logic: must be at least school age
            throw new IllegalArgumentException("Age must be between 7 and 120");
        }
        this.age = age;
    }

    public void setEmail(String email) {
        if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        if (registrationDate != null && registrationDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Registration date cannot be in the future");
        }
        this.registrationDate = registrationDate;
    }

    public void setGpa(double gpa) {
        if (gpa < 2.0 || gpa > 6.0) { // Based on the Bulgarian grading system (2-6)
            throw new IllegalArgumentException("GPA must be between 2.0 and 6.0");
        }
        this.gpa = gpa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public double getGpa() {
        return gpa;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                ", gpa=" + gpa +
                '}';
    }
}