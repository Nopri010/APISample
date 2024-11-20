package br.edu.atitus.apisample.services;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import br.edu.atitus.apisample.entities.UserEntity;
import br.edu.atitus.apisample.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{2,})$";

    public List<UserEntity> findAll() throws Exception {
        return repository.findAll();
    }

    public UserEntity save(UserEntity newUser) throws Exception {
        if (newUser == null) throw new Exception("Objeto nulo!");
        if (newUser.getName() == null || newUser.getName().trim().isEmpty())
            throw new Exception("Nome inválido!");
        if (newUser.getEmail() == null || newUser.getEmail().trim().isEmpty())
            throw new Exception("Email inválido!");
        if (!isValidEmail(newUser.getEmail()))
            throw new Exception("Formato de email inválido!");
        if (newUser.getPassword() == null || newUser.getPassword().trim().isEmpty())
            throw new Exception("Senha inválida!");
        if (repository.existsByEmail(newUser.getEmail()))
            throw new Exception("Já existe um usuário com esse email!");

        return repository.save(newUser);
    }

    private boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }
}