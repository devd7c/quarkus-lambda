package org.acme;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.panache.common.Sort;
import org.acme.entities.ADSystem;
import org.acme.entities.Fruit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.ext.Provider;

import org.acme.repositories.ADSystemRepository;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("systems")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class GreetingResource {

    private static final Logger LOGGER = Logger.getLogger(GreetingResource.class.getName());

    @Inject
    ADSystemRepository adSystemRepository;

    @GET
    public List<ADSystem> get() {
        return adSystemRepository.findAllList();
        //return Fruit.listAll();
    }

    @GET
    @Path("{id}")
    public ADSystem getSingle(@PathParam Integer id) {
        ADSystem entity = adSystemRepository.findById(id);
        //Fruit entity = Fruit.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Entity with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(ADSystem entity) {
        try {
            //Here I use the persistAndFlush() shorthand method on a Panache repository to persist to database then flush the changes.
            if (entity.getId() != null) {
                throw new WebApplicationException("Id was invalidly set on request.", 422);
            }

            adSystemRepository.persistAndFlush(entity);

            return Response.ok(entity).status(201).build();
        }
        catch(PersistenceException pe){
            //LOG.error("Unable to create the parameter", pe);
            //in case of error, I save it to disk
            //diskPersister.save(parameter);
            throw new WebApplicationException(pe.getMessage(), 500);
        }
        //entity.persist();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Fruit update(@PathParam Long id, Fruit fruit) {
        if (fruit.name == null) {
            throw new WebApplicationException("Entity Name was not set on request.", 422);
        }
        Fruit entity = Fruit.findById(id);

        if (entity == null) {
            throw new WebApplicationException("Entity with id of " + id + " does not exist.", 404);
        }
        entity.name = fruit.name;
        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam Long id) {
        Fruit entity = Fruit.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Entity with id of " + id + " does not exist.", 404);
        }
        entity.delete();
        return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}