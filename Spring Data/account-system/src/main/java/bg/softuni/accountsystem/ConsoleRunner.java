package bg.softuni.accountsystem;

import bg.softuni.accountsystem.models.Account;
import bg.softuni.accountsystem.models.User;
import bg.softuni.accountsystem.service.AccountService;
import bg.softuni.accountsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public ConsoleRunner(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @Override
    public void run(String... args) throws Exception {
        User user = new User("Pesho", 20);

        Account account = new Account(new BigDecimal("25000"));
        account.setUser(user);

        user.setAccounts(new HashSet<>() {{
            add(account);
        }});

        userService.registerUser(user);

        accountService.transferMoney(new BigDecimal("20000"), account.getId());
        accountService.withdrawMoney(new BigDecimal("30000"), account.getId());
    }
}
