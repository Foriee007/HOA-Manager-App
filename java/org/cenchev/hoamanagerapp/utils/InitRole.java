package org.cenchev.hoamanagerapp.utils;
import org.cenchev.hoamanagerapp.model.entities.Admin;
import org.cenchev.hoamanagerapp.model.entities.User;
import org.cenchev.hoamanagerapp.model.entities.Role;
import org.cenchev.hoamanagerapp.model.enums.RoleType;
import org.cenchev.hoamanagerapp.repository.AdminRepository;
import org.cenchev.hoamanagerapp.repository.RoleRepository;
import org.cenchev.hoamanagerapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitRole implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private  final BCryptPasswordEncoder passwordEncoder;

    public InitRole(RoleRepository roleRepository, UserRepository userRepository, AdminRepository adminRepository, BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            if (roleRepository.count() == 0 && userRepository.count() == 0){
                Role adminRole = new Role(RoleType.ADMIN);
                Role residentRole = new Role(RoleType.RESIDENT);
                Role propertyManagerRole = new Role(RoleType.PROPERTY_MANAGER);

            roleRepository.save(adminRole);
            roleRepository.save(residentRole);
            roleRepository.save(propertyManagerRole);
                User user1 = new User();
                user1.setUsername("admin@hoamanager.com");
                user1.setPassword(passwordEncoder.encode("123456"));
                user1.setFirstName("Admin");
                user1.setLastName("User");
                user1.setRole(adminRole);
                userRepository.save(user1);
                adminRepository.saveAdmin(user1.getId()); // Associate the admin table with the newly created user

            }
        }
        catch (DataAccessException e){
            String message = ("Error occurred during data initializing : " + e.getMessage());
            System.out.println(message);
        }
    }
}
