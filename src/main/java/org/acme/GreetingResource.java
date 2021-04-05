package org.acme;

import java.util.List;

import io.quarkus.panache.common.Sort;
import org.acme.entities.Fruit;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("fruits")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class GreetingResource {

    @GET
    public List<Fruit> get() {
        return Fruit.listAll(Sort.by("name"));
    }
}