package org.example.authentication;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.example.model.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    public User findByUsername(String username) {
        return find("username", username).firstResult();
    }
}