package aula.tareas.controller;

import aula.tareas.api.TareaRequest;
import aula.tareas.api.TareaResponse;
import aula.tareas.mapper.TareaMapperApi;
import aula.tareas.mapper.TareaMapperCsv;
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

import java.util.List;
import java.util.Optional;

@RestController
@Profile("csv")
@RequestMapping("/api/v1/csv/tareas")
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
        //return TareaMapperApi.toResponse(servicio.listar());
        return null;
    }

    @GetMapping
    @Operation(summary = "Obtener una tarea a través de su id en el fichero csv.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se obtiene el listado de tareas."),
            @ApiResponse(responseCode = "404", description = "")
    })
    public ResponseEntity<TareaResponse> buscar(long id) {
        return null;
    }

    @PostMapping
    @Operation(summary = "Crear una tarea en el fichero csv.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Se ha creado la tarea correctamente."),
            @ApiResponse(responseCode = "400", description = "El título es inválido.")
    })
    public ResponseEntity<TareaResponse> crear(TareaRequest request) {
        // TODO: Exponer el alta, validar el cuerpo y componer la respuesta HTTP.
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    @PostMapping
    @Operation(summary = "Actualizar una tarea en el fichero csv.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se ha actualizado la tarea correctamente."),
            @ApiResponse(responseCode = "400", description = "El título es inválido."),
            @ApiResponse(responseCode = "404", description = "La tarea no existe.")
    })
    public ResponseEntity<TareaResponse> actualizar(long id, TareaRequest request) {
        // TODO: Exponer la actualización y validar la petición.
        throw new UnsupportedOperationException("Pendiente de implementar");
    }

    @DeleteMapping
    @Operation(summary = "Eliminar una tarea de fichero csv.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No tiene cuerpo."),
            @ApiResponse(responseCode = "404", description = "La tarea no existe.")
    })
    public ResponseEntity<Void> eliminar(long id) {
        // TODO: Exponer el borrado y comunicar el resultado.
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
