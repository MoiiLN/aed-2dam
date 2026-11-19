package aula.tareas.controller;

import aula.tareas.api.TareaRequest;
import aula.tareas.service.TareaServiceXml;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/xml/tareas")
@Tag(name="Tareas Xml", description = "Operaciones CRUD de tareas en Xml.")
public class TareaControllerXml {
    private final TareaServiceXml servicio;

    TareaControllerXml(@Qualifier("tareaServiceXml") TareaServiceXml servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las tareas desde el fichero Xml.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Se obtiene el listado de tareas."))
    List<TareaRequest> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener la tarea a través de su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarea encontrada correctamente."),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada.")
    })
    ResponseEntity<TareaRequest> buscar(@PathVariable long id) {
        return ResponseEntity.ofNullable(servicio.buscar(id));
    }

    @PostMapping
    @Operation(summary = "Obtener la tarea a través de su id")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarea creada correctamente."),
            @ApiResponse(responseCode = "400", description = "Tarea ya existe.")
    })
    ResponseEntity<TareaRequest> crear(@Valid @RequestBody TareaControllerCsv.NuevaTarea peticion) {
        TareaRequest tareaRequest = servicio.crear(peticion.titulo());
        return ResponseEntity.created(URI.create("/api/tareas/" + tareaRequest.id())).body(tareaRequest);
    }

    public record NuevaTarea(@NotBlank String titulo) {}
}
