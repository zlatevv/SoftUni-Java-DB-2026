package com.zlatev.studentmanager;

import com.zlatev.studentmanager.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("student-manager");
        EntityManager em = emf.createEntityManager();

        deleteStudent(em);

        emf.close();
        em.close();
    }

    private static void deleteStudent(EntityManager em) {
        try {
            em.getTransaction().begin();

            Student toDelete = em.find(Student.class, 5);
            if (toDelete != null) {
                em.remove(toDelete);
                em.getTransaction().commit();
                System.out.println(toDelete.getName() + " has been deleted");
            } else {
                System.out.println("Student with id 5 not found");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    private static void updateStudent(EntityManager em) {
        try {
            em.getTransaction().begin();

            Student toUpdate = em.find(Student.class, 1);

            if (toUpdate != null){
                toUpdate.setName("Tseko");
                toUpdate.setAge(18);
                em.getTransaction().commit();

                System.out.println("Student updated");
            } else {
                System.out.println("Student not found");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    private static void readStudent(EntityManager em) {
        try {
            Student student = em.find(Student.class, 1L);

            if (student != null){
                System.out.println(student);
            } else {
                System.out.println("No student found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createStudent(EntityManager em) {
        try {
            em.getTransaction().begin();
            Student student = new Student("Limon", 30);

            em.persist(student);
            em.getTransaction().commit();

            System.out.println("Student saved successfully!");
            System.out.println("Generated ID: " + student.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }
}
