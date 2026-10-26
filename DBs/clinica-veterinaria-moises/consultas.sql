-- 1. Todos los clientes alfabéticamente
SELECT * FROM clientes ORDER BY nombre ASC;
-- 2. Mascotas de una especie (modifica 'Perro' si deseas)
SELECT * FROM mascotas WHERE especie = 'Perro';
-- 3. Mascotas de un cliente
SELECT * FROM mascotas WHERE id_cliente = ?;
-- 4. Veterinarios de una especialidad
SELECT * FROM veterinarios WHERE especialidad = ?;
-- 5. Consultas programadas
SELECT * FROM consultas WHERE estado = 'PROGRAMADA';
-- 6a. Mascota y propietario con WHERE
SELECT m.nombre AS mascota, c.nombre AS propietario FROM mascotas m, clientes c WHERE m.id_cliente = c.id;
-- 6b. Mascota y propietario con INNER JOIN

-- 7. Consultas, mascota, propietario y veterinario

-- 8. Cantidad de mascotas por cliente, incluidos los de cero mascotas

-- 9. Importe tratamientos, incluyendo consultas sin tratamientos

-- 10. Consultas sin tratamientos

-- Soy consciente de que utilizar el asterisco no es eficiente.