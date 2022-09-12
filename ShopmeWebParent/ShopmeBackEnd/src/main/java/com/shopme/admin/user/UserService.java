package com.shopme.admin.user;

import com.shopme.common.entity.User;
import com.shopme.common.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listAll(){
        return (List<User>) userRepo.findAll();
    }

    public List<Role> listRoles(){
        return (List<Role>) roleRepo.findAll();
    }

    public User save(User user){
        boolean isUpdatingUser = (user.getId() != null);

        if(isUpdatingUser){
            User existingUser = userRepo.findById(user.getId()).get();
            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else{
                encodePassword(user);
            }
        } else {
            encodePassword(user);
        }
        return userRepo.save(user);
    }

    private void encodePassword(User user){
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
    }

    public boolean isEmailUnique(Integer id, String email){
        User userByEmail = userRepo.getUserByEmail(email);

        if (userByEmail == null) return true;

        // New Account Mode
        if (id == null){
            if (userByEmail != null) return false;
        // User Edit Mode
        } else{
            if (userByEmail.getId() != id){
                return false;
            }
        }
        return true;
    }

    public User get(Integer id) throws UsernameNotFoundException {
        try{
            return userRepo.findById(id).get();
        } catch (NoSuchElementException ex){
            throw new UsernameNotFoundException("Could not find user with ID " + id);
        }
    }

    public void delete(Integer id) throws UsernameNotFoundException{
        Long countById =  userRepo.countById(id);

        if(countById == null || countById == 0){
            throw new UsernameNotFoundException("Could not find user with ID " + id);
        }

        userRepo.deleteById(id);
    }

    public void updateEnabledStatus(Integer id, boolean enabled){
        userRepo.updateEnabledStatus(id, enabled);
    }
}
