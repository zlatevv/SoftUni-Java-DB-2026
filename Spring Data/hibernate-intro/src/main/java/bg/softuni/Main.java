package bg.softuni;

import bg.softuni.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("school");
        EntityManager em = emf.createEntityManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== Student Management System ===\n" +
                    "1. Add a new student\n" +
                    "2. Find student by ID\n" +
                    "3. List all students\n" +
                    "4. Update a student\n" +
                    "5. Delete a student\n" +
                    "6. Count all students\n" +
                    "7. Search by name pattern\n" +
                    "0. Exit\n" +
                    "=================================\n");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addStudent(scanner, em);
                    break;
                case 2:
                    findStudent(scanner, em);
                    break;
                case 3:
                    listStudents(scanner, em);
                    break;
                case 4:
                    updateStudent(scanner, em);
                    break;
                case 5:
                    deleteStudent(scanner, em);
                    break;
                case 6:
                    countAllStudents(scanner, em);
                    break;
                case 7:
                    searchByNamePattern(scanner, em);
                    break;
                case 0:
                    em.close();
                    emf.close();
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice! ");
            }
        }

    }

    private static void searchByNamePattern(Scanner scanner, EntityManager em) {
        System.out.print("Enter name pattern (use % for wildcards): ");
        String pattern = scanner.nextLine();

        TypedQuery<Student> query = em.createQuery(
                "select s from Student s where s.name like :pattern",
                Student.class
        );

        query.setParameter("pattern", pattern);

        List<Student> students = query.getResultList();

        if (students.isEmpty()) {
            System.out.println("No students found matching that pattern.");
        } else {
            students.forEach(s -> System.out.println(s.getName()));
        }

    }

    private static void countAllStudents(Scanner scanner, EntityManager em) {
        TypedQuery<Student> query = em.createQuery(
                "select count(s) from Student s",
                Student.class
        );

        System.out.println("Student count: " + query.getSingleResult());
    }

    private static void deleteStudent(Scanner scanner, EntityManager em) {
        int id = Integer.parseInt(scanner.nextLine());
        em.getTransaction().begin();

        Student student = em.find(Student.class, id);
        if (student != null) {
            em.remove(student);
            System.out.printf("Deleted student with ID %d%n.", id);
        }

        em.getTransaction().commit();

    }

    private static void updateStudent(Scanner scanner, EntityManager em) {
        System.out.print("Enter Student ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        em.getTransaction().begin();

        Student student = em.find(Student.class, id);

        if (student != null) {
            System.out.print("Enter new name: ");
            String newName = scanner.nextLine();

            System.out.print("Enter new age: ");
            int newAge = Integer.parseInt(scanner.nextLine());

            student.setName(newName);
            student.setAge(newAge);

            em.getTransaction().commit();
            System.out.println("Update successful: " + student);
        } else {
            em.getTransaction().rollback();
            System.out.println("Error: Student with ID " + id + " not found.");
        }
    }

    private static void listStudents(Scanner scanner, EntityManager em) {
        TypedQuery<Student> query = em.createQuery(
                "select s from Student s",
                Student.class
        );

        List<Student> allStudents = query.getResultList();
        allStudents.forEach(System.out::println);
    }

    private static void findStudent(Scanner scanner, EntityManager em) {
        int id = Integer.parseInt(scanner.nextLine());

        Student lostStudent = em.find(Student.class, id);

        if (lostStudent != null){
            System.out.println("Found this donny: " + lostStudent);
        }else {
            System.out.println("No such donneh lah! ");
        }
    }

    private static void addStudent(Scanner scanner, EntityManager em) {
        System.out.println("Enter: Name, Age, Email, GPA (separated by commas)");
        String[] tokens = scanner.nextLine().split("\\s*,\\s*");

        if (tokens.length < 4) {
            System.out.println("Invalid input format.");
            return;
        }

        Student student = new Student(
                tokens[0],                   // Name (can now contain spaces)
                Integer.parseInt(tokens[1]), // Age
                tokens[2],                   // Email
                LocalDate.now(),
                Double.parseDouble(tokens[3]) // GPA
        );

        em.getTransaction().begin();
        em.persist(student);
        em.getTransaction().commit();
        System.out.printf("Student %s added!%n", student.getName());
    }
}
