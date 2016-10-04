package net.dontdrinkandroot.example.angularrestspringsecurity.rest.resources;

import net.dontdrinkandroot.example.angularrestspringsecurity.JsonViews;
import net.dontdrinkandroot.example.angularrestspringsecurity.dao.newmeasuremententry.NewMeasurementEntryDao;
import net.dontdrinkandroot.example.angularrestspringsecurity.entity.NewMeasurementEntry;
import net.dontdrinkandroot.example.angularrestspringsecurity.entity.Role;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Component
@Path("/new")
public class NewMeasurementEntryResource
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NewMeasurementEntryDao newMeasurementEntryDao;

    @Autowired
    private ObjectMapper mapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String list() throws IOException
    {
        this.logger.info("list()");

        ObjectWriter viewWriter;
        if (this.isAdmin()) {
            viewWriter = this.mapper.writerWithView(JsonViews.Admin.class);
        } else {
            viewWriter = this.mapper.writerWithView(JsonViews.User.class);
        }
        List<NewMeasurementEntry> allEntries = this.newMeasurementEntryDao.findAll();

        return viewWriter.writeValueAsString(allEntries);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public NewMeasurementEntry read(@PathParam("id") Long id)
    {
        this.logger.info("read(id)");

        NewMeasurementEntry newMeasurementEntry = this.newMeasurementEntryDao.find(id);
        if (newMeasurementEntry == null) {
            throw new WebApplicationException(404);
        }
        return newMeasurementEntry;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public NewMeasurementEntry create(NewMeasurementEntry newMeasurementEntry)
    {
        this.logger.info("create(): " + newMeasurementEntry);

        return this.newMeasurementEntryDao.save(newMeasurementEntry);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public NewMeasurementEntry update(@PathParam("id") Long id, NewMeasurementEntry newMeasurementEntry)
    {
        this.logger.info("update(): " + newMeasurementEntry);

        return this.newMeasurementEntryDao.save(newMeasurementEntry);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public void delete(@PathParam("id") Long id)
    {
        this.logger.info("delete(id)");

        this.newMeasurementEntryDao.delete(id);
    }

    private boolean isAdmin()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if ((principal instanceof String) && ((String) principal).equals("anonymousUser")) {
            return false;
        }
        UserDetails userDetails = (UserDetails) principal;

        return userDetails.getAuthorities().contains(Role.ADMIN);
    }
}
