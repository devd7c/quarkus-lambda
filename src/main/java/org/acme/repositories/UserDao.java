package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.entities.ADUser;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserDao implements PanacheRepository<ADUser> {
    public ADUser findById(Integer id) { return find("id", id).firstResult();}
}
