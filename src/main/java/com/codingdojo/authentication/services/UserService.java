package com.codingdojo.authentication.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.codingdojo.authentication.models.User;
import com.codingdojo.authentication.repositories.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	//register user and hash password
	public User registerUser(User user) {
		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(hashed);
		return userRepository.save(user);
	}
	
	//find user by email
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	//find user by id
	public User findUserById(Long id) {
		Optional<User> u = userRepository.findById(id);
		if(u.isPresent()) {
			return u.get();
		}
		else {
			return null;
		}
	}
	
	//authenticate user
	public boolean authenticateUser(String email, String password) {
		//find user by email
		User user = userRepository.findByEmail(email);
		//if email doesn't exist, return false
		if(user == null) {
			return false;
		}
		else {
			//return true if passwords match, else return false
			if(BCrypt.checkpw(password,  user.getPassword())) {
				return true;
			}
			else {
				return false;
			}
		}
	}

}
