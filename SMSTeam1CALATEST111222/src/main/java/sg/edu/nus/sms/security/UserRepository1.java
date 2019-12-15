package sg.edu.nus.sms.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.sms.model.User;

public interface UserRepository1 extends JpaRepository<User, Integer> {
	Optional<User> findByUserName(String userName);
}
