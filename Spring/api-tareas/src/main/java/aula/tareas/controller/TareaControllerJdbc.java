package aula.tareas.controller;

import aula.tareas.api.TareaRequest;
import aula.tareas.service.ITareaService;
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
// api/v1/
// api/public
@RequestMapping("/api/jdbc/tareas")
@Tag(name="Tareas Jdbc", description = "Operaciones CRUD de tareas en Jdbc.")
public class TareaControllerJdbc {

    private final ITareaService serviceJson;

    public TareaControllerJdbc(@Qualifier("tareaServiceJdbc") ITareaService serviceJson) {
        this.serviceJson = serviceJson;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las tareas desde el fichero Jdbc.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Se obtiene el listado de tareas."))
    List<TareaRequest> listar() {
        return serviceJson.listar();
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener la tarea a través de su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarea encontrada correctamente."),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada.")
})
    ResponseEntity<TareaRequest> buscar(@PathVariable long id) {
        // return ResponseEntity.ofNullable(servicio.buscar(id));
        return null;
    }

    @PostMapping
    @Operation(summary = "Obtener la tarea a través de su id")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarea creada correctamente."),
            @ApiResponse(responseCode = "400", description = "Tarea ya existe.")
    })
    ResponseEntity<TareaRequest> crear(@Valid @RequestBody TareaControllerJdbc.NuevaTarea peticion) {
        TareaRequest tareaRequest = serviceJson.crear(peticion.titulo());
        return ResponseEntity.created(URI.create("/api/jdbc/tareas/" + tareaRequest.id())).body(tareaRequest);
    }

    @PutMapping("/{id}")
    ResponseEntity<TareaRequest> update(@PathVariable long id, @Valid @RequestBody TareaControllerCsv.NuevaTarea peticion) {
        return null;
    }
    public record NuevaTarea(@NotBlank String titulo) {}
}
