package controllers;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import models.Persona;
import services.PersonaService;


@Path("/persona")
@Produces(MediaType.APPLICATION_JSON)
public class PersonaController {
    @Inject
    PersonaService personaService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response enviarDatosUsuario(Persona persona) {
        try {
            personaService.enviarDatosALambda(persona);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
