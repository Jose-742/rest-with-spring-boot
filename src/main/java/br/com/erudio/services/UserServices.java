package br.com.erudio.services;


import br.com.erudio.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServices implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: " + username);
        var user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else{
            throw new UsernameNotFoundException("Username "+username+" not found!");
        }
    }
}
