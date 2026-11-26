
package ies.puesto.peliculas.controller;

import ies.puesto.peliculas.controller.api.PeliculaEdicionRequest;
import ies.puesto.peliculas.controller.api.PeliculaRequest;
import ies.puesto.peliculas.controller.api.PeliculaResponse;
import ies.puesto.peliculas.controller.mapper.PeliculaApiMapper;
import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.service.PeliculaService;
import ies.puesto.peliculas.service.modelo.DatosCreacionPelicula;
import ies.puesto.peliculas.service.modelo.DatosEdicionPelicula;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@Profile("jdbc")
@RequestMapping("/api/v1/jdbc/peliculas")
@Tag(name = "Peliculas Jdbc", description = "Operaciones CRUD de peliculas en Jdbc.")
public class PeliculaJdbcController {

    private final PeliculaService peliculaService;

    public PeliculaJdbcController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las peliculas desde la base de datos (Jdbc).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Se obtiene el listado de peliculas.")
    })
    public List<PeliculaResponse> listar() {
        return PeliculaApiMapper.aRespuestas(peliculaService.listar());
    }

    @GetMapping("/{codigo}")
    @Operation(summary = "Obtener una pelicula a traves de su codigo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pelicula encontrada correctamente."),
            @ApiResponse(responseCode = "400", description = "El codigo proporcionado no tiene un formato valido."),
            @ApiResponse(responseCode = "404", description = "Pelicula no encontrada.")
    })
    public ResponseEntity<?> obtener(@PathVariable @Pattern(regexp = "^PEL[0-9]{3}$") String codigo) {
        Optional<Pelicula> result = peliculaService.obtener(codigo);
        if (result.isPresent()) {
            PeliculaResponse response = PeliculaApiMapper.aRespuesta(result.get());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Crear una pelicula en Jdbc.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pelicula creada correctamente."),
            @ApiResponse(responseCode = "400", description = "La peticion contiene datos invalidos o no se ha podido crear la pelicula."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<?> crear(@Valid @RequestBody PeliculaRequest peticion) {
        DatosCreacionPelicula datos = PeliculaApiMapper.aDatosCreacion(peticion);
        Optional<Pelicula> result = peliculaService.crear(datos);
        if (result.isPresent()) {
            PeliculaResponse response = PeliculaApiMapper.aRespuesta(result.get());
            return ResponseEntity.created(
                    URI.create("/api/v1/jdbc/peliculas/" + response.codigo())
            ).body(response);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{codigo}")
    @Operation(summary = "Actualizar una pelicula en Jdbc.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pelicula actualizada correctamente."),
            @ApiResponse(responseCode = "400", description = "La peticion contiene datos invalidos o el codigo no tiene un formato valido."),
            @ApiResponse(responseCode = "404", description = "La pelicula que se intenta actualizar no existe."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<?> actualizar(
            @PathVariable @Pattern(regexp = "^PEL[0-9]{3}$") String codigo,
            @Valid @RequestBody PeliculaEdicionRequest peticion) {
        DatosEdicionPelicula datos = PeliculaApiMapper.aDatosEdicion(peticion);
        Optional<Pelicula> result = peliculaService.actualizar(codigo, datos);
        if (result.isPresent()) {
            PeliculaResponse response = PeliculaApiMapper.aRespuesta(result.get());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{codigo}")
    @Operation(summary = "Eliminar una pelicula en Jdbc.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pelicula eliminada correctamente. Sin cuerpo de respuesta."),
            @ApiResponse(responseCode = "400", description = "El codigo proporcionado no tiene un formato valido."),
            @ApiResponse(responseCode = "404", description = "La pelicula que se intenta eliminar no existe."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<?> eliminar(
            @PathVariable @Pattern(regexp = "^PEL[0-9]{3}$") String codigo) {
        boolean deleted = peliculaService.eliminar(codigo);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
