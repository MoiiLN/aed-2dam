package ies.puesto.peliculas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) // Obliga a Spring a no utilizar su propio DataSource y en este caso, utilizará DriverManager
public class PeliculasApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeliculasApplication.class, args);
    }
}
