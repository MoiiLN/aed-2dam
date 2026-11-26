package ies.puesto.peliculas.controller.api;

import ies.puesto.peliculas.mapper.ActorMapper;
import ies.puesto.peliculas.model.Actor;
import ies.puesto.peliculas.model.DatosActor;
import ies.puesto.peliculas.service.ActorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Actores - V2")
@RestController
@RequestMapping("/api/v2/actores")
@Tag(name = "Actores Jpa - Escritura", description = "Operaciones CRUD de actores en la bdd con Jpa.")
public class ActorV2Controller {
    private final ActorService servicio;

    public ActorV2Controller(@Qualifier("actorOrmService") ActorService servicio) {
        this.servicio = servicio;
    }
    @Operation(summary = "Eliminar actor")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Se ha eliminado el actor correctamente."),
            @ApiResponse(responseCode = "400", description = "La Id del actor es inválido."),
            @ApiResponse(responseCode = "404", description = "No existe el actor.")
    })
    @DeleteMapping("/{idActor}")
    public void eliminar(@PathVariable @Pattern(regexp = "[A-Za-z0-9-]{1,32}") String idActor) {
        servicio.eliminar(idActor);
    }
    @Operation(summary = "Editar actor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actor editado correctamente."),
            @ApiResponse(responseCode = "400", description = "La Id del actor es inválido."),
            @ApiResponse(responseCode = "404", description = "No existe el actor.")
    })
    @PutMapping("/{idActor}")
    public ActorResponse editar(@PathVariable @Pattern(regexp = "[A-Za-z0-9-]{1,32}") String idActor, @RequestBody ActorEdicionRequest peticion) {
        DatosActor datos = ActorMapper.toDatosActor(peticion);
        Actor actor = servicio.editar(idActor, datos);
        return ActorMapper.toApi(actor);
    }

    @Operation(summary = "Crear actor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Actor creado correctamente."),
            @ApiResponse(responseCode = "400", description = "La Id del actor es inválido."),
            @ApiResponse(responseCode = "404", description = "No existe el actor.")
    })
    @PutMapping
    public ActorResponse crear(@RequestBody ActorRequest peticion) {
        DatosActor datos = ActorMapper.RequestToDatos(peticion);
        Actor actor = servicio.crear(peticion.idActor(), datos);
        return ActorMapper.toApi(actor);
    }

}
