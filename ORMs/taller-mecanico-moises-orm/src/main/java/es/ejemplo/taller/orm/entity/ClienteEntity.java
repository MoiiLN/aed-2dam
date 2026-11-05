package es.ejemplo.taller.orm.entity;

import es.ejemplo.taller.orm.repository.IRepository;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La base de datos generará el id_cliente automaticamente.
    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "telefono")
    private String telefono;

    protected ClienteEntity() {} // Necesario para JPA

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<VehiculoEntity> vehiculos = new ArrayList<>();

    public ClienteEntity(Integer idCliente, String nombre,
                         String email, String telefono) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<VehiculoEntity> getVehiculos() {
        return vehiculos;
    }
// Genera getters y setters de los cuatro atributos con el IDE.
}