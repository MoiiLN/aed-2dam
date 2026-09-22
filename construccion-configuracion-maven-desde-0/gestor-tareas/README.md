# Práctica 00. Construcción y configuración de maven desde 0.

1. Instalar openjdk 21, mvn y comprobar version de cada uno.

<img width="658" height="414" alt="Captura de pantalla 2026-09-22 103005" src="https://github.com/user-attachments/assets/a2fc6398-2bc2-4ce5-ae76-0c688e673475" />
<img width="643" height="408" alt="Captura de pantalla 2026-09-22 103041" src="https://github.com/user-attachments/assets/4626d637-5cef-425d-8177-c0a86bb7e044" />

2. Creamos el directorio de gestor-tareas con su estructura, un Main.java y el propio pom.xml para maven en la raíz del proyecto.

<img width="642" height="411" alt="Captura de pantalla 2026-09-22 103310" src="https://github.com/user-attachments/assets/cde4df1e-c32f-475b-9e66-238595149708" />

3. Añadimos el contenido al archivo de configuración de Maven (pom.xml) y el código correspondiente al Main.java.

<img width="879" height="833" alt="Captura de pantalla 2026-09-22 103509" src="https://github.com/user-attachments/assets/9cb6b365-bcd6-4fcd-9fd9-c1a07d10e442" />

4. Ejecutamos un mvn validate para asegurarnos de que está todo correcto.

<img width="641" height="209" alt="Captura de pantalla 2026-09-22 103542" src="https://github.com/user-attachments/assets/7cec3997-2453-4bcb-83fc-eb8c77d8a006" />

5. Cambiamos el ArtifactID "gestor-tareas" a "gestor-tareas-prueba" y posteriormente a ello, validamos.

<img width="1241" height="787" alt="Captura de pantalla 2026-09-22 104105" src="https://github.com/user-attachments/assets/522470e3-ad1d-4e25-96c6-bedd6e118cd2" />
<img width="948" height="882" alt="Captura de pantalla 2026-09-22 103947" src="https://github.com/user-attachments/assets/30a17852-b0b9-4261-bce1-845e761bbaae" />

6. Ejecutamos un bloque de comandos para ientificar el usuario y el entorno.

<img width="1168" height="343" alt="Captura de pantalla 2026-09-22 105515" src="https://github.com/user-attachments/assets/1b8b2c4f-9a36-4b59-a2e7-384ae74af7c6" />

7. Instalamos el OpenJDK17 y comprobamos.
   
<img width="1092" height="457" alt="Captura de pantalla 2026-09-22 105555" src="https://github.com/user-attachments/assets/f3a985cd-2b59-4f24-9a5f-c9d4d820cb4d" />

<img width="451" height="105" alt="Captura de pantalla 2026-09-22 105624" src="https://github.com/user-attachments/assets/25e9c480-09eb-4c07-8116-3b2d73b5bf60" />

8. Seleccionamos el JDK17 activo, tanto para java como para javac.

<img width="694" height="341" alt="Captura de pantalla 2026-09-22 113600" src="https://github.com/user-attachments/assets/fd66e8d2-69fd-4130-98a7-1affe5c7cb6d" />

9. Configuramos variables de entorno.

<img width="616" height="39" alt="Captura de pantalla 2026-09-22 105950" src="https://github.com/user-attachments/assets/e9d80c3a-b4dc-4ca5-8da3-bba1475a3d2a" />

10. Bloque de comprobaciones para el cambio realizado.

<img width="670" height="238" alt="Captura de pantalla 2026-09-22 110020" src="https://github.com/user-attachments/assets/ddb83df9-d585-4b19-9dac-89cd3282cc48" />

11. Si tratamos de ejecutar un verify nos devolverá error ya que seguimos teniendo en el pom.xml configurado para java 21.

<img width="1703" height="510" alt="Captura de pantalla 2026-09-22 110124" src="https://github.com/user-attachments/assets/d5891c2e-7377-414d-b272-0f3726fef896" />

12. Volvemos al JDK21.

<img width="1063" height="649" alt="Captura de pantalla 2026-09-22 110342" src="https://github.com/user-attachments/assets/ce905efd-bb58-406d-bbbd-33b3bd54706e" />

13. Finalmente, si ejecutamos mvn clean verify el build será exitoso.

<img width="788" height="616" alt="Captura de pantalla 2026-09-22 110358" src="https://github.com/user-attachments/assets/4d48921c-42f7-4099-bffe-33568f4ecb07" />

Pom.xml hasta el momento:

<img width="894" height="909" alt="Captura de pantalla 2026-09-22 110922" src="https://github.com/user-attachments/assets/51de495f-b789-44a0-890f-57ca22a5a652" />

14. Añadiendo la dependencia de Gson en el pom.xml y compilando.

<img width="880" height="555" alt="image" src="https://github.com/user-attachments/assets/25897c00-cc8f-49ae-8843-91e45340ce73" />

<img width="1360" height="536" alt="image" src="https://github.com/user-attachments/assets/01201414-f63c-47d3-bb93-badd7a334a3b" />

15. mvn dependency:tree.

<img width="1421" height="291" alt="image" src="https://github.com/user-attachments/assets/cef7f0bb-663d-41e0-ae9d-361c1b376257" />

16. Si quitamos la dependencia de gson e intentamos compilar, lo cual da error y si volvemos a añadir la dependencia y compilamos, será exitoso:

<img width="1462" height="367" alt="image" src="https://github.com/user-attachments/assets/3d54830f-be47-4954-8222-6a2ecba3662f" />

17. Realizamos mvn install.

<img width="744" height="152" alt="image" src="https://github.com/user-attachments/assets/82049368-7482-47d8-afee-7e664b9813bc" />

18. Vemos que se ha descargado correctamente.

<img width="1121" height="76" alt="image" src="https://github.com/user-attachments/assets/30bd754b-b3fa-4a22-ba00-6d4cdf342473" />

19. mvn -o package

<img width="900" height="274" alt="image" src="https://github.com/user-attachments/assets/208a25db-b0ad-4383-afed-83bc63490ef6" />

20. Creando la carpeta config y guardando settings-publico.xml.

<img width="970" height="345" alt="image" src="https://github.com/user-attachments/assets/edcd6c73-2235-406e-aacb-6d49544517ae" />

21. Configuramos los perfiles-

<img width="1222" height="704" alt="image" src="https://github.com/user-attachments/assets/e241b534-a3e2-453f-be14-a095afc96784" />

22. Creamos el .xml de settings-empresa fuera de Git.

<img width="584" height="272" alt="image" src="https://github.com/user-attachments/assets/871ab16e-12a8-4f72-8413-da7979b39832" />

23. Si intentamos ejecutar el comando, dará error.

<img width="2030" height="263" alt="image" src="https://github.com/user-attachments/assets/fa40c506-553d-449d-96e4-3f77f6e95a57" />
