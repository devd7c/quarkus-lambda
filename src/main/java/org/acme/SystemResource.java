package org.acme;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.acme.entities.ADSystem;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.ext.Provider;

import org.acme.repositories.ADSystemRepository;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("system")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class SystemResource {

    private static final Logger LOGGER = Logger.getLogger(SystemResource.class.getName());

    @Inject
    ADSystemRepository adSystemRepository;

    @GET
    @Path("/find_all/{auth}/{control}")
    public Response get() {
        List<ADSystem> entityLs = adSystemRepository.findAllList();
        return Response.ok(entityLs)
                .status(200)
                .build();
        //return adSystemRepository.findAllList();
    }

    @GET
    @Path("/read/{id}/{auth}")
    public ADSystem getSingle(@PathParam Integer id) {
        ADSystem entity = adSystemRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Entity with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Path("/create/{auth}")
    @Transactional
    public Response create(ADSystem entity) {
        try {
            //Here I use the persistAndFlush() shorthand method on a Panache repository to persist to database then flush the changes.
            if (entity.getId() != null) {
                throw new WebApplicationException("Id was invalidly set on request.", 422);
            }
            adSystemRepository.persistAndFlush(entity);
            return Response.ok("OK").status(200).build();
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
    @Path("/update/{id}/{auth}")
    @Transactional
    public Response update(@PathParam Integer id, ADSystem updateEntity) {
        try {
            ADSystem entity = adSystemRepository.findById(id);

            if (entity == null) {
                throw new WebApplicationException("Entity with id of " + id + " does not exist.", 404);
            }
            entity.setName(updateEntity.getName());
            entity.setDescription(updateEntity.getDescription());
            entity.setAddress(updateEntity.getAddress());
            entity.setMessage(updateEntity.getMessage());
            entity.setEmail(updateEntity.getEmail());
            entity.setStatus(updateEntity.getStatus());
            entity.setUserAdmin(updateEntity.getUserAdmin());
            entity.setSocietyId(updateEntity.getSocietyId());
            return Response.ok("OK").status(200).build();
        }
        catch(PersistenceException pe){
            throw new WebApplicationException(pe.getMessage(), 500);
        }
    }

    @DELETE
    @Path("/delete/{id}/{auth}")
    @Transactional
    public Response delete(@PathParam Integer id) {
        try {
            ADSystem entity = adSystemRepository.findById(id);
            if (entity == null) {
                throw new WebApplicationException("Entity with id of " + id + " does not exist.", 404);
            }
            adSystemRepository.delete(entity);
            return Response.ok("OK").status(200).build();
        }
        catch(PersistenceException pe){
            throw new WebApplicationException(pe.getMessage(), 500);
        }
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

    @Provider
    public static class CORSFilter implements ContainerResponseFilter {

        @Override
        public void filter(ContainerRequestContext requestContext,
                           ContainerResponseContext cres) throws IOException {
            cres.getHeaders().add("Access-Control-Allow-Origin", "*");
            cres.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
            cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
            cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            cres.getHeaders().add("Access-Control-Expose-Headers", "application/json");
        }

    }
}