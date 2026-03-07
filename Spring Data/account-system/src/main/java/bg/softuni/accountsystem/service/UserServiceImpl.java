package bg.softuni.accountsystem.service;

import bg.softuni.accountsystem.models.User;
import bg.softuni.accountsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(User user) {
        Optional<User> foundUser = userRepository.getByUsername(user.getUsername());

        if (foundUser.isPresent()){
            System.out.println("User already exists!");
            return;
        }
        userRepository.save(user);
    }
}
