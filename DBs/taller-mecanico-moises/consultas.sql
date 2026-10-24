-- 1. Lista los clientes ordenados por nombre.
SELECT id_cliente, nombre, email, telefono FROM clientes ORDER BY nombre DESC;
-- 2. Consulta matrícula, marca, modelo y año de los vehículos de una marca elegida.
SELECT matricula, marca, modelo, anio FROM vehiculos WHERE marca = ?;
-- 3. Obtén los vehículos de un cliente utilizando su identificador.
SELECT matricula, marca, modeo, anio FROM vehiculos WHERE id_cliente = ?;
-- 4. Lista los mecánicos de una especialidad y ordénalos por tarifa.
SELECT nombre, especialidad, tarifa_hora FROM mecanicos ORDER BY tarifa_hora DESC;
-- 5. Obtén las reparaciones pendientes, ordenadas por fecha.
SELECT matricula, descripcion, fecha, horas_trabajo, estado WHERE estado = "pendiente" ORDER BY fecha DESC;
-- 6. Muestra la matrícula de cada vehículo y el nombre de su propietario. Resuélvelo una vez con `WHERE` y otra con `INNER JOIN`.
SELECT v.matricula, c.nombre FROM vehiculos v INNER JOIN clientes c ON v.id_cliente = c.id_cliente
-- 7. Muestra las reparaciones con el nombre del cliente, la matrícula y el nombre del mecánico.
-- 8. Cuenta los vehículos de cada cliente, incluyendo clientes sin vehículos.

-- 9. Calcula el coste de piezas por reparación, incluyendo las reparaciones sin piezas con coste cero.

-- 10. Obtén las reparaciones que no tienen piezas asociadas.