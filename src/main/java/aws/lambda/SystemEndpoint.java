package aws.lambda;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import aws.lambda.entities.ADSystem;

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

import aws.lambda.entities.ADUser;
import aws.lambda.repositories.SystemDao;
import aws.lambda.repositories.UserDao;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("system")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class SystemEndpoint {

    private static final Logger LOGGER = Logger.getLogger(SystemEndpoint.class.getName());

    @Inject
    SystemDao systemDao;

    @Inject
    UserDao userDao;

    @GET
    @Path("/find_all/{auth}/{control}")
    public Response get() {
        List<ADSystem> entityLs = systemDao.findAllList();
        return Response.ok(entityLs)
                .status(200)
                .build();
    }

    @GET
    @Path("/read/{id}/{auth}")
    public ADSystem getSingle(@PathParam Integer id) {
        ADSystem entity = systemDao.findById(id);
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
            Boolean exist = systemDao.existEntity(entity);
            if(!exist) {
                systemDao.persistAndFlush(entity);
                return Response.ok("OK").status(200).build();
            } else return Response.ok("EXIST").status(200).build();
        }
        catch(PersistenceException pe){
            throw new WebApplicationException(pe.getMessage(), 500);
        }
    }

    @POST
    @Path("/update/{id}/{auth}")
    @Transactional
    public Response update(@PathParam Integer id, ADSystem updateEntity) {
        try {
            ADSystem entity = systemDao.findById(id);

            if (entity == null) {
                throw new WebApplicationException("Entity with id of " + id + " does not exist.", 404);
            }
            Boolean exist = systemDao.existUpdateEntity(entity.getId(), updateEntity);
            if(!exist) {
                entity.setName(updateEntity.getName());
                entity.setDescription(updateEntity.getDescription());
                entity.setAddress(updateEntity.getAddress());
                entity.setMessage(updateEntity.getMessage());
                entity.setEmail(updateEntity.getEmail());
                entity.setUserAdmin(updateEntity.getUserAdmin());
                return Response.ok("OK").status(200).build();
            } else return Response.ok("EXIST").status(200).build();
        }
        catch(PersistenceException pe){
            throw new WebApplicationException(pe.getMessage(), 500);
        }
    }

    @POST
    @Path("/delete/{id}/{auth}")
    @Transactional
    public Response delete(@PathParam Integer id, @PathParam Integer auth) {
        try {
            ADSystem entity = systemDao.findById(id);
            ADUser user = userDao.findById(auth);

            if (entity == null) {
                throw new WebApplicationException("Entity with id of " + id + " does not exist.", 404);
            }
            entity.setStatus(0);
            entity.setUserAdmin(user.getUsername());
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
                           ContainerResponseContext CRC) throws IOException {
            CRC.getHeaders().add("Access-Control-Allow-Origin", "*");
            CRC.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
            CRC.getHeaders().add("Access-Control-Allow-Credentials", "true");
            CRC.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            CRC.getHeaders().add("Access-Control-Expose-Headers", "application/json");
        }

    }
}