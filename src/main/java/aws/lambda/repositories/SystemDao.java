package aws.lambda.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import aws.lambda.entities.ADSystem;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SystemDao implements PanacheRepository<ADSystem> {
    public List<ADSystem> findAllList() { return list("status", 1); }
    public ADSystem findById(Integer id) { return find("id", id).firstResult();}

    public Boolean existEntity(ADSystem entity) {
        ADSystem result = find("name=?1 and status=1", entity.getName()).firstResult();
        return result != null;
    }

    public Boolean existUpdateEntity(Integer id, ADSystem entity) {
        ADSystem result = find("id!=?1 and name=?2 and status=?3", id, entity.getName(), 1).firstResult();
        return result != null;
    }
}
