package aula.tareas.controller;

import aula.tareas.api.TareaRequest;
import aula.tareas.api.TareaResponse;
import aula.tareas.mapper.TareaMapperApi;
import aula.tareas.modelo.Tarea;
import aula.tareas.service.ITareaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jdbc/tareas")
@Tag(name = "Tareas Jdbc", description = "Operaciones CRUD de tareas en jdbc.")
public class TareaControllerJdbc {
    private final ITareaService servicio;

    public TareaControllerJdbc(@Qualifier("tareaServiceJdbc") ITareaService servicio) {
        this.servicio = servicio;
    }

    // TODO: Definir la ruta base /api/jdbc/tareas.

    @GetMapping
    @Operation(summary = "Obtener todas las tareas desde el fichero jdbc.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se obtiene el listado de tareas."),
    })
    public List<TareaResponse> listar() {
        // TODO: Exponer el listado y convertir las tareas en respuestas.
        return TareaMapperApi.toResponses(servicio.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una tarea a través de su id en la bdd con jdbc.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se obtiene la tarea."),
            @ApiResponse(responseCode = "404", description = "No existe la tareas que buscas.")
    })
    public ResponseEntity<TareaResponse> buscar(long id) {
        // TODO: Exponer la consulta por ID y gestionar la ausencia de la tarea.
        Optional<Tarea> result = servicio.buscar(id);
        if (result.isPresent()) {
            TareaResponse response = TareaMapperApi.toResponse(result.get());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping
    @Operation(summary = "Crear una tarea en la bdd con jdbc.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Se ha creado la tarea correctamente."),
            @ApiResponse(responseCode = "400", description = "El título es inválido.")
    })
    public ResponseEntity<TareaResponse> crear(TareaRequest request) {
        // TODO: Exponer el alta, validar el cuerpo y componer la respuesta HTTP.
        Tarea tarea = servicio.crear(request);
        if (tarea != null) {
            TareaResponse response = TareaMapperApi.toResponse(tarea);
            return ResponseEntity.created(URI.create("/api/jdbc/tareas/" + response.id())).body(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una tarea en la bdd con jdbc.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se ha actualizado la tarea correctamente."),
            @ApiResponse(responseCode = "400", description = "El título es inválido."),
            @ApiResponse(responseCode = "404", description = "La tarea no existe.")
    })
    public ResponseEntity<TareaResponse> actualizar(long id, TareaRequest request) {
        // TODO: Exponer la actualización y validar la petición.
        Optional<Tarea> result = servicio.actualizar(id, request);
        if (result.isPresent()) {
            TareaResponse response = TareaMapperApi.toResponse(result.get());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una tarea de la bdd con jdbc.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No tiene cuerpo."),
            @ApiResponse(responseCode = "404", description = "La tarea no existe.")
    })
    public  ResponseEntity<Void> eliminar(long id) {
        // TODO: Exponer el borrado y comunicar el resultado.
        boolean deleted = servicio.eliminar(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
