package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.entities.ADSystem;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ADSystemRepository implements PanacheRepository<ADSystem> {
    public List<ADSystem> findAllList() {
        return listAll();
    }
    public ADSystem findById(Integer id) { return find("id", id).firstResult();}
}
