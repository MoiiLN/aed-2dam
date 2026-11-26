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
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/csv/tareas")
@Tag(name = "Tareas Csv", description = "Operaciones CRUD de tareas en csv.")
public class TareaControllerCsv {
    private final ITareaService servicio;

    public TareaControllerCsv(@Qualifier("tareaServiceCsv") ITareaService servicio) {
        this.servicio = servicio;
    }

    // TODO: Definir la ruta base /api/csv/tareas.

    @GetMapping
    @Operation(summary = "Obtener todas las tareas desde el fichero csv.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se obtiene el listado de tareas."),
    })
    public List<TareaResponse> listar() {
        return TareaMapperApi.toResponses(servicio.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una tarea a través de su id en el fichero csv.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se obtiene la tarea."),
            @ApiResponse(responseCode = "404", description = "No existe la tareas que buscas.")
    })
    public ResponseEntity<TareaResponse> buscar(@PathVariable long id) {
        Optional<Tarea> result = servicio.buscar(id);
        if (result.isPresent()) {
            TareaResponse response = TareaMapperApi.toResponse(result.get());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Crear una tarea en el fichero csv.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Se ha creado la tarea correctamente."),
            @ApiResponse(responseCode = "400", description = "El título es inválido.")
    })
    public ResponseEntity<TareaResponse> crear(@RequestBody TareaRequest request) {
        Tarea tarea = servicio.crear(request);
        if (tarea != null) {
            TareaResponse response = TareaMapperApi.toResponse(tarea);
            return ResponseEntity.created(URI.create("/api/csv/tareas/" + response.id())).body(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una tarea en el fichero csv.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se ha actualizado la tarea correctamente."),
            @ApiResponse(responseCode = "400", description = "El título es inválido."),
            @ApiResponse(responseCode = "404", description = "La tarea no existe.")
    })
    public ResponseEntity<TareaResponse> actualizar(@PathVariable long id, @RequestBody TareaRequest request) {
        Optional<Tarea> result = servicio.actualizar(id, request);
        if (result.isPresent()) {
            TareaResponse response = TareaMapperApi.toResponse(result.get());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una tarea de fichero csv.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No tiene cuerpo."),
            @ApiResponse(responseCode = "404", description = "La tarea no existe.")
    })
    public ResponseEntity<Void> eliminar(@PathVariable long id) {
        boolean deleted = servicio.eliminar(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
