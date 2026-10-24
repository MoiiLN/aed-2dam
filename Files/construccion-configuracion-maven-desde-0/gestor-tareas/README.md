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

Pom.xml final:

<img width="894" height="909" alt="Captura de pantalla 2026-09-22 110922" src="https://github.com/user-attachments/assets/51de495f-b789-44a0-890f-57ca22a5a652" />

23. Si intentamos ejecutar el comando, dará error.

<img width="2030" height="263" alt="image" src="https://github.com/user-attachments/assets/fa40c506-553d-449d-96e4-3f77f6e95a57" />

24. Añadimos el perfil "informe" al pom.

<img width="1136" height="785" alt="image" src="https://github.com/user-attachments/assets/cefe6a23-2f0c-45cf-aedc-ce4388980e45" />

25. Ejecutamos el bloque de comandos.

<img width="777" height="391" alt="image" src="https://github.com/user-attachments/assets/94efe250-c6f1-4c25-9cc5-888dad215652" />

26. Actualizamos el perfil de informe.

<img width="631" height="359" alt="image" src="https://github.com/user-attachments/assets/5173ab62-8db7-44a8-a381-0aee825172ba" />

27. Activamos el perfil.

<img width="950" height="401" alt="image" src="https://github.com/user-attachments/assets/52c38f86-1cb4-4c8a-888c-681fb44352b7" />

28. Mostramos las dependencias del proyecto y con el -dverbose muestra información adicional.

<img width="838" height="576" alt="image" src="https://github.com/user-attachments/assets/414ec1a3-bbf7-443a-a97e-d3c827c54ab7" />

29. Una vez la dependencia de commons-text y commons-lang3 hayan sido agregadas al pom, ejecutamos los comandos anteriores señalando el apache.commons.

<img width="1215" height="674" alt="image" src="https://github.com/user-attachments/assets/5e555d3d-f9f1-4cc3-a4e6-790ffabf0330" />

30. Eliminamos la dependencia de commons-lang3 y podemos ver que llega de forma transitiva la dependencia que hemos eliminado a través de commons-text.

<img width="1077" height="291" alt="image" src="https://github.com/user-attachments/assets/3d1c3d11-9417-4f1d-9b98-61d3e9808760" />

31. Añadimos a properties el bloque proporcionado y el otro bloque debajo de <project> y compilamos.

<img width="1077" height="291" alt="image" src="https://github.com/user-attachments/assets/2f59e45d-826c-4e80-a508-756108873a83" />

32. Una vez añadidos los bloques nuevos que se proporcionan utilizamos mvn dependency:tree y mvn help:effective-pom -Doutput=target/pom-efectivo.xml:

<img width="1470" height="698" alt="image" src="https://github.com/user-attachments/assets/29d3f902-ba82-43ef-bb5d-60af28f42b0f" />

## Junit

1. Añadimos la dependencia de JUnit.

<img width="1470" height="698" alt="image" src="https://github.com/user-attachments/assets/c8def26a-3cc0-4eca-82b3-05100e9857ca" />

2. Creamos los dos archivos.

<img width="1061" height="340" alt="image" src="https://github.com/user-attachments/assets/1811fb04-9f93-4c79-b264-a85d6989f375" />
<img width="1080" height="491" alt="image" src="https://github.com/user-attachments/assets/2844aa80-d8b4-4687-884e-100943b11189" />

3. Si ejecutamos mvn test ahora nos aparece que hay dos tests creados y que se han ejecutado correctamente.

<img width="1382" height="336" alt="image" src="https://github.com/user-attachments/assets/33008976-d3d9-4994-a71f-5e5e671cba70" />
<img width="1082" height="679" alt="image" src="https://github.com/user-attachments/assets/00e1f64f-0d5e-4291-a120-491e63551061" />

4. Provocamos un fallo modificando el GestorTareasTest.

<img width="520" height="411" alt="image" src="https://github.com/user-attachments/assets/e31dd8aa-93d9-41ed-a6e9-cdbf8447d84c" />
<img width="489" height="69" alt="image" src="https://github.com/user-attachments/assets/a01336bb-7e0a-45d6-aa48-40e6f01ceb71" />

5. Si hacemos un verify después de haber revertido el cambio anterior de archivo test, todo irá bien.

<img width="1039" height="343" alt="image" src="https://github.com/user-attachments/assets/14e9bf2a-24cf-4bcb-b4aa-fe0ec720852e" />

6. Si borramos la línea en GestorTareas.java de titulos.add(titulo) nos dará error.

<img width="1254" height="418" alt="image" src="https://github.com/user-attachments/assets/6a6a96b5-69b5-4119-880e-bb24854eeab7" />
<img width="680" height="43" alt="image" src="https://github.com/user-attachments/assets/5474a2c9-d600-4399-866d-3db08213b86d" />

7. Si revertimos ese último cambio, realizamos un verify y ejecutamos el echo:

<img width="1071" height="315" alt="image" src="https://github.com/user-attachments/assets/9e53fb4c-4a69-434f-920a-b1c53185c20f" />

8. Creamos la carpeta resources en main y el archivo aplicacion.properties con el nombre correspondiente.

<img width="958" height="80" alt="image" src="https://github.com/user-attachments/assets/9a3ca4cc-17a1-4058-b73d-24c30ea3cd98" />

9. Modificamos Main.java

<img width="768" height="586" alt="image" src="https://github.com/user-attachments/assets/0a82c1ea-7a62-479c-a568-423ced3de50c" />

10. Realizamos un mvn clean package, comprobamos si se ha copiado el recurso y ejecutamos el .jar a modo de prueba.
    
<img width="1133" height="562" alt="image" src="https://github.com/user-attachments/assets/24a44c3f-9c1c-467d-82d7-fdf6e6d547ce" />

11. Añadimos en el plugin maven-jar-plugin y ejecutamos el bloque de comandos que nos indican.

<img width="789" height="253" alt="image" src="https://github.com/user-attachments/assets/ef7fa2be-91bc-4336-b9c5-192ed869ac66" />

12. Buscamos el .jar, lo copiamos y lo ejecutamos, el cual, da un error de tipo "NoClassDefFoundError".

<img width="1313" height="166" alt="image" src="https://github.com/user-attachments/assets/dfa86010-12cb-4eda-bd78-8c6309e91350" />

13. Buscamos el .jar correspondiente y lo ejecutamos.

<img width="1022" height="75" alt="image" src="https://github.com/user-attachments/assets/ac59ef85-5283-47cd-be74-9c2d5a647a49" />
