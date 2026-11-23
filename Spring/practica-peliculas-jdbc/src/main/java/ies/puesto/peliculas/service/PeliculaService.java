package ies.puesto.peliculas.service;


import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.repository.jdbc.IPeliculaRepository;
import ies.puesto.peliculas.service.mapper.PeliculaDominioMapper;
import ies.puesto.peliculas.service.modelo.DatosCreacionPelicula;
import ies.puesto.peliculas.service.modelo.DatosEdicionPelicula;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeliculaService {

    private final PeliculaDominioMapper peliculaDominioMapper;
    private final IPeliculaRepository repository;

    public PeliculaService(PeliculaDominioMapper peliculaDominioMapper, IPeliculaRepository repository) {
        this.peliculaDominioMapper = peliculaDominioMapper;
        this.repository = repository;
    }

    public List<Pelicula> listar() {
        return repository.buscarTodas();
    }

    public Optional<Pelicula> obtener(String codigo) {
        return repository.buscarPorCodigo(codigo);
    }

    public Optional<Pelicula> crear(DatosCreacionPelicula datos) {
        Pelicula pelicula = PeliculaDominioMapper.aPelicula(datos);
        boolean insertada = repository.insertarSiAusente(pelicula);
        if (insertada) {
            return Optional.of(pelicula);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Pelicula> actualizar(String codigo, DatosEdicionPelicula datos) {
        Pelicula pelicula = PeliculaDominioMapper.aPelicula(codigo, datos);
        boolean insertada = repository.actualizarSiExiste(pelicula);
        if (insertada) {
            return Optional.of(pelicula);
        } else {
            return Optional.empty();
        }
    }

    public boolean eliminar(String identificador) {
        return repository.eliminar(identificador);
    }

}
