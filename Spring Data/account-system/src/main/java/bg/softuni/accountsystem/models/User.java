package bg.softuni.accountsystem.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private int age;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Account> accounts;

    private User() {
        this.accounts = new HashSet<>();
    }

    public User(String username, int age) {
        this.username = username;
        this.age = age;
        this.accounts = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(HashSet<Account> accounts) {
        this.accounts = accounts;
    }
}
