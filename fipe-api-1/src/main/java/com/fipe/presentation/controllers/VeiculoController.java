package com.fipe.presentation.controllers;

import com.fipe.application.usecases.AtualizacaoVeiculoUseCase;
import com.fipe.application.usecases.CargaInicialUseCase;
import com.fipe.application.usecases.ConsultaVeiculosUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import java.util.List;

@Path("/api/veiculos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Veículos", description = "Operações com veículos FIPE")
public class VeiculoController {

    private static final Logger LOG = Logger.getLogger(VeiculoController.class);

    @Inject
    CargaInicialUseCase cargaInicialUseCase;

    @Inject
    ConsultaVeiculosUseCase consultaVeiculosUseCase;

    @Inject
    AtualizacaoVeiculoUseCase atualizacaoVeiculoUseCase;

    @POST
    @Path("/carga-inicial")
    @Operation(summary = "Inicia carga de dados da FIPE",
            description = "Busca marcas na API FIPE e envia para processamento assíncrono")
    @APIResponse(responseCode = "202", description = "Carga iniciada com sucesso")
    @APIResponse(responseCode = "500", description = "Erro ao processar carga")
    public Response iniciarCargaInicial() {
        LOG.info("Requisição recebida: POST /api/veiculos/carga-inicial");

        try {
            int totalMarcas = cargaInicialUseCase.executar();

            return Response.accepted()
                    .entity(new CargaInicialResponse(
                            "Carga iniciada com sucesso",
                            totalMarcas
                    ))
                    .build();

        } catch (Exception e) {
            LOG.error("Erro ao iniciar carga inicial", e);
            return Response.serverError()
                    .entity(new ErrorResponse("Erro ao processar carga inicial: " + e.getMessage()))
                    .build();
        }
    }


    @GET
    @Path("/marcas")
    @Operation(summary = "Lista todas as marcas",
            description = "Retorna todas as marcas cadastradas no banco de dados")
    @APIResponse(responseCode = "200", description = "Lista de marcas")
    public Response buscarMarcas() {
        LOG.info("Requisição recebida: GET /api/veiculos/marcas");

        List<String> marcas = consultaVeiculosUseCase.buscarMarcas();
        return Response.ok(marcas).build();
    }

    @GET
    @Path("/marca/{marca}")
    @Operation(summary = "Lista veículos por marca",
            description = "Retorna código, modelo e observações dos veículos de uma marca")
    @APIResponse(responseCode = "200", description = "Lista de veículos")
    public Response buscarVeiculosPorMarca(@PathParam("marca") String marca) {
        LOG.infof("Requisição recebida: GET /api/veiculos/marca/%s", marca);

        List<ConsultaVeiculosUseCase.VeiculoDTO> veiculos =
                consultaVeiculosUseCase.buscarVeiculosPorMarca(marca);

        return Response.ok(veiculos).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualiza dados do veículo",
            description = "Atualiza modelo e observações de um veículo")
    @APIResponse(responseCode = "200", description = "Veículo atualizado")
    @APIResponse(responseCode = "404", description = "Veículo não encontrado")
    @APIResponse(responseCode = "400", description = "Dados inválidos")
    public Response atualizarVeiculo(
            @PathParam("id") Long id,
            AtualizacaoVeiculoUseCase.AtualizacaoVeiculoRequest request) {

        LOG.infof("Requisição recebida: PUT /api/veiculos/%d", id);

        try {
            request.validar();
            atualizacaoVeiculoUseCase.atualizar(id, request.modelo(), request.observacoes());

            return Response.ok()
                    .entity(new SuccessResponse("Veículo atualizado com sucesso"))
                    .build();

        } catch (AtualizacaoVeiculoUseCase.VeiculoNaoEncontradoException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    record CargaInicialResponse(String mensagem, int totalMarcas) {}
    record SuccessResponse(String mensagem) {}
    record ErrorResponse(String erro) {}
}