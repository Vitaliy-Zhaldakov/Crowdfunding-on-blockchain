package com.blockchain.project.service;

import com.blockchain.project.UserRepository;
import com.blockchain.project.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Создание записи
    public User createUser(User user) {
        if (user.getWalletAddress() != null && walletAddressExists(user.getWalletAddress())) {
            throw new IllegalArgumentException("Этот кошелек уже привязан к другому аккаунту");
        }
        return userRepository.save(user);
    }

    // Получение всех записей
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Поиск по имени
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean walletAddressExists(String address) {
        return address != null && userRepository.findByWalletAddress(address).isPresent();
    }
}
