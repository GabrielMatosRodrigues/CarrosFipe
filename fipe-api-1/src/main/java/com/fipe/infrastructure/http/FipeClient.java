package com.fipe.infrastructure.http;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import java.util.List;

@RegisterRestClient(configKey = "fipe-api")
@Path("/carros")
public interface FipeClient {

    @GET
    @Path("/marcas")
    List<MarcaResponse> buscarMarcas();

    ModeloResponse buscarModelos(@PathParam("codigoMarca") String codigoMarca);

    record MarcaResponse(
            String codigo,
            String nome
    ){}

    record ModelosResponse(
            List<ModeloResponse> modelos
    ) {}

    record ModeloResponse(
            String codigo,
            String nome
    ) {}
}
