package bg.softuni.accountsystem.service;

import bg.softuni.accountsystem.models.Account;
import bg.softuni.accountsystem.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public void withdrawMoney(BigDecimal money, Long id) {
        Optional<Account> byID = accountRepository.findAccountById(id);

        if (byID.isEmpty()){
            System.out.println("No such account!");
            return;
        }
        Account account = byID.get();

        if (account.getBalance().compareTo(money) < 0){
            System.out.println("Account is poor!");
            return;
        }

        account.setBalance(account.getBalance().subtract(money));
    }

    @Override
    public void transferMoney(BigDecimal money, Long id) {
        Optional<Account> byID = accountRepository.findAccountById(id);

        if (byID.isEmpty()){
            System.out.println("No such account!");
            return;
        }

        if (money.compareTo(BigDecimal.ZERO) < 0){
            System.out.println("Cannot transfer negative amount");
            return;
        }


        Account account = byID.get();
        account.setBalance(account.getBalance().add(money));
    }
}
