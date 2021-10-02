package se331.lab.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se331.lab.rest.entity.Organizer;
import se331.lab.rest.repository.OrganizerRepository;
import se331.lab.rest.security.entity.Authority;
import se331.lab.rest.security.entity.AuthorityName;
import se331.lab.rest.security.entity.User;
import se331.lab.rest.security.repository.UserRepository;
import se331.lab.rest.service.UserService;
import se331.lab.rest.util.LabMapper;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrganizerRepository organizerRepository;
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    List<Authority> userAuth = new ArrayList<>();
    Authority authority = new Authority();
    @PostMapping("/signup")
    public ResponseEntity<?> saveUser(@RequestBody User user){
        authority.setName(AuthorityName.ROLE_USER);
        userAuth.add(authority);
        Organizer organizer =organizerRepository.save(Organizer.builder().name(user.getUsername()).id(Long.parseLong(String.valueOf(organizerRepository.findAll().size()+1))).build());
        organizer.setUser(user);
        String password = encoder.encode(user.getPassword());
        user.setPassword(password);
        user.setEnabled(true);
//        user.getAuthorities().add(authority);
        user.setOrganizer(organizer);
//       User newUser = User.builder().username(user.getUsername()).email(user.getEmail()).password(user.getPassword())
//               .enabled(true).authorities(userAuth)
//               .build();

//        newUser.setAuthorities(userAuth);
//        newUser.setOrganizer(a);
//        a.setUser(newUser);

        User u = userRepository.save(user);
       return ResponseEntity.ok(LabMapper.INSTANCE.getUserDTO(u));
    }
}
