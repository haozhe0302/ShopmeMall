package com.shopme.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRole(){
        Role roleAdmin = entityManager.find(Role.class, 1);  // id 1 refer to admin
        User userHaozheW = new User("haozhe.wang.chn@outlook.com", "19990302", "Haozhe", "Wang");
        userHaozheW.addRole(roleAdmin);

        User savedUser = repo.save(userHaozheW);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewUserWithTwoRole() {
        User userHarukaK = new User("haruka.kakki.nogizaka@outlook.com", "20010808", "Haruka", "Kaki");

        Role roleEditor = new Role(3);  // id 3 refer to editor
        Role roleAssistant = new Role(5);  // id 5 refer to assitant
        userHarukaK.addRole(roleEditor);
        userHarukaK.addRole(roleAssistant);

        User savedUser = repo.save(userHarukaK);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers(){
        Iterable<User> listUsers = repo.findAll();
        listUsers.forEach(System.out::println);
    }

    @Test
    public void testGetUserId(){
        User userHaozheW = repo.findById(1).get();
        System.out.println(userHaozheW);
        assertThat(userHaozheW).isNotNull();
    }

    @Test
    public void testUpdateUserDetails(){
        User userHarukaK = repo.findById(8).get();
        userHarukaK.setEnabled(true);

        repo.save(userHarukaK);
    }

    @Test
    public void testUpdateUserRoles(){
        User userHarukaK = repo.findById(8).get();
        Role roleSalesperson = new Role(2);  // id 3 refer to salesperson
        Role roleEditor = new Role(3);

        userHarukaK.getRoles().remove(roleEditor);
        userHarukaK.addRole(roleSalesperson);

        repo.save(userHarukaK);
    }

    @Test
    public void testDeleteUser(){
        Integer userId = 2;
        repo.deleteById(2);
    }
}
