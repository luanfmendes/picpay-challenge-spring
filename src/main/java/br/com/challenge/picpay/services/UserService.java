package br.com.challenge.picpay.services;

import br.com.challenge.picpay.domain.user.User;
import br.com.challenge.picpay.domain.user.UserType;
import br.com.challenge.picpay.dtos.UserDTO;
import br.com.challenge.picpay.exceptions.TransactionAuthorizationException;
import br.com.challenge.picpay.exceptions.UserNotFoundException;
import br.com.challenge.picpay.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    public User createUser(UserDTO userDTO){
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return repository.save(user);
    }
    public User findUserById(Long id) throws UserNotFoundException {
        return repository.findUserById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }

    public List<User> getAllUsers(){
        return this.repository.findAll();
    }

    public void validateTransactionAuthorization(User sender, BigDecimal amount) throws TransactionAuthorizationException {
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new TransactionAuthorizationException("Usuário do tipo lojista não está autorizado a realizar transação");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new TransactionAuthorizationException("Saldo insuficiente");
        }
    }

    public void save(User user) {
        this.repository.save(user);
    }
}
