
import com.example.hamacasbackend.entidades.Alumno;
import com.example.hamacasbackend.entidades.Libro;
import com.example.hamacasbackend.repositorios.AlumnoRepositorio;
import com.example.hamacasbackend.repositorios.LibroRepositorio;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.*;


/*Con RestController indicamos que esta clase es el controlador para las peticiones HTTP o HTTPS de tipo REST, esto
* es, enviaremos información al servidor en formato JSON y recibiremos información desde el servidor en formato JSON*/
@RestController
/*En RequestMapping indicaremos en la url que todas las direcciones que incluyan "userapi" serám respondidas por este
* controlador*/
@RequestMapping("/userapi")
public class AlumnoController {
    private final AlumnoRepositorio alumnoRepositorio;//Aquí vamos a referenciar nuestro repositorio
    private final LibroRepositorio libroRepositorio;

    @Autowired
    public AlumnoController(AlumnoRepositorio alumnoRepositorio, LibroRepositorio libroRepositorio) {
        this.alumnoRepositorio = alumnoRepositorio;
        this.libroRepositorio = libroRepositorio;
    }
    /*Con GetMapping estamos indicando que el tipo de petición HTTP o HTTPS debe ser GET y también indicamos qué
    * ruta dentro de la url (listaAlumnos) debemos usar para acceder a este método en el controlador*/
    @GetMapping("/listaAlumnos")
    public List<Alumno> getAlumnos(){
        /*Con un findAll accedemos a todos los Alumnos que haya en la base de datos. La lista de Alumnos devuelta
        * será transformada a formato JSON automáticamente por SpringBoot usando las reglas que hayamos especificado
        * en el application.properties y en la entidad que corresponda, en este caso Alumno*/
        Iterable<Alumno> iterar=alumnoRepositorio.findAll();
        Iterator<Alumno> iterus=iterar.iterator();
        List<Alumno> resultado=new ArrayList<>();
        while(iterus.hasNext()){
            resultado.add(iterus.next());
        }
        return resultado;
    }

    /*Con PostMapping estamos indicando que el tipo de petición HTTP o HTTPS debe ser POST y también indicamos qué
     * ruta dentro de la url (nuevoAlumno) debemos usar para acceder a este método en el controlador*/
    @PostMapping("/nuevoAlumno")
    /*En este caso el método va a devolver al cliente una entidad de tipo Long (la clave primaria del nuevo Alumno
    creado en la base de datos). Además estamos incluyendo como parámetro de este método un objeto de la clase Alumno
    escrito en formato JSON (esto lo estamos indicando con la anotación RequestBody). Desde el cliente controlaremos
    que hacemos una petición de tipo POST y que en el RequestBody incluiremos el Alumno en formato JSON*/
    public ResponseEntity<Long> nuevo(@RequestBody Alumno Alumno)
            throws URISyntaxException {
        //Hacemos un save usando el repositorio para guardar el nuevo Alumno, que nos devolverá el Alumno creado
        Alumno AlumnoCreado = alumnoRepositorio.save(Alumno);
        if (AlumnoCreado == null) {/*Si el Alumno creado es nulo es porque ha habido un error y lo indicaremos en
        la respuesta que enviaremos al cliente con un "not found" en el cuerpo*/
            return ResponseEntity.notFound().build();
        } else {/*Si hemos tenido éxito al crear el nuevo Alumno devolveremos en el cuerpo de la respuesta su clave
        primaria correspondiente. También indicamos que hemos tenido éxito al crear el Alumno mediante el estado
        HTTP "created"*/
            return ResponseEntity.status(HttpStatus.CREATED).body(AlumnoCreado.getIdAlumno());
        }
    }

    /*En este GetMapping vamos a incluir en la url una variable que será la clave primaria del Alumno que queremos
    * obtener. En la url la ruta que usaremos para accerder a este método del controlador será getAlumno y entre
    * llaves indicaremos el nombre de la variable que posteriormente usaremos como parámetro*/
    @GetMapping("/getAlumno{id}")
    /*En este caso el método va a devolver al cliente una entidad de tipo Alumno. Además indicamos con la anotación
    * PathVariable que la url va a incluir un parámetro cuyo nombre será "id"*/
    public ResponseEntity<Alumno> getAlumno(@PathVariable("id") Long id) {
        //Obtenemos un único Alumno usando el método findById del repositorio que ya conocemos
        Optional<Alumno> alumnoEncontrado = alumnoRepositorio.findById(id);
        if (alumnoEncontrado.isEmpty()) {/*Si no se ha encontrado al Alumno con la clave primaria correspondiente
        devolveremos un "not found"*/
            System.out.println("Alumno con id: " + id + "NO encontrado");
            return ResponseEntity.notFound().build();
        } else {/*Si se ha encontrado al Alumno devolveremos en HTTP OK y el Alumno correspondiente en formato JSON
        (esto lo hace SpringBoot automáticamente)*/
            System.out.println("Alumno con id: " + id + "SI encontrado");
            return ResponseEntity.ok(alumnoEncontrado.get());
        }
    }

    /*Este método es muy parecido al de crear nuevo Alumno. La primera diferencia es que usamos una petición HTTP
    * o HTTPS PUT en lugar de POST. La segunda diferencia es que la ruta de la url cambia a "actualizarAlumno". El
    * resto del método es igual (mismos ResponseEntity, RequestBody y método del repositorio de la base de datos).*/
    @PutMapping("/actualizarAlumno")
    public ResponseEntity<Long> modificar(@RequestBody Alumno Alumno)
            throws URISyntaxException {
        Alumno alumnoCreado = alumnoRepositorio.save(Alumno);
        if (alumnoCreado == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(alumnoCreado.getIdAlumno());
        }
    }

    /*El método que usaremos para eliminar un Alumno deberá utilizar la petición DELETE de HTTP o HTTPS. Icluirá un
    * parámetro con la clave primaria del Alumno que queremos eliminar, como ya hicimos cuando quisimos obtener
    * un único Alumno. La ruta de la url usada en este caso es "eliminarAlumno" con la variable "id" indicada entre
    * llaves*/
    @DeleteMapping("/eliminarAlumno{id}")
    /*Devolvemos un ResponseEntity de tipo Object ya que realmente la respuesta va a ser vacía, bien porque no se haya
    * encontrado al Alumno quq deseamos eliminar o bien porque al eliminarlo indicamos que hemos tenido éxito mediante
    * un "NO CONTENT".*/
    public ResponseEntity<Object> eliminar(@PathVariable Long id) {
        Optional<Alumno> alumnoEliminable = alumnoRepositorio.findById(id);
        if (alumnoEliminable.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            alumnoRepositorio.delete(alumnoEliminable.get());
            return ResponseEntity.noContent().build();
        }
    }
    @PostMapping("/nuevoLibro")
    public ResponseEntity<Long> nuevoLibro(@RequestBody Map<String, String> mLibro) throws URISyntaxException {
        String nombre = mLibro.get("nombre");
        Long idAlumno = Long.parseLong(mLibro.get("idAlumno"));

        // Verifica el ID del alumno recibido (agregar logs para verificar)
        System.out.println("ID del alumno recibido: " + idAlumno);

        Optional<Alumno> alumnoOptional = alumnoRepositorio.findById(idAlumno);
        if (alumnoOptional.isEmpty()) {
            System.out.println("El alumno con ID: " + idAlumno + " no existe.");
            return ResponseEntity.notFound().build();
        }

        Alumno alumno = alumnoOptional.get();
        Libro nuevoLibro = new Libro();
        nuevoLibro.setNombre(nombre);
        nuevoLibro.setAlumno(alumno);

        Libro libroCreado = libroRepositorio.save(nuevoLibro);

        if (libroCreado == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(libroCreado.getIdLibro());
        }
    }


    @DeleteMapping("/eliminarLibro/{id}")
    public ResponseEntity<Object> eliminarLibro(@PathVariable Long id) {
        Optional<Libro> libroEliminable = libroRepositorio.findById(id);
        if (libroEliminable.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            libroRepositorio.delete(libroEliminable.get());
            return ResponseEntity.noContent().build();
        }
    }


    @PutMapping("/actualizarLibro")
    public ResponseEntity<Long> modificar(@RequestBody Libro libro)
            throws URISyntaxException {
        Optional<Libro> libroEncontrado = libroRepositorio.findById(libro.getIdLibro());
        if (libroEncontrado.isEmpty()) {
            System.out.println("Libro no encontrado");
            return ResponseEntity.notFound().build();
        } else {
            Libro libroActualizado = libroEncontrado.get();
            libroActualizado.setNombre(libro.getNombre()); // Actualiza el nombre del libro

            // Actualiza la relación con el alumno manteniendo el ID del alumno del libro original
            Alumno alumnoDelLibroOriginal = libroActualizado.getAlumno();
            libroActualizado.setAlumno(alumnoDelLibroOriginal);

            Libro libroModificado = libroRepositorio.save(libroActualizado);
            //Comprobacion del alumno
            System.out.println("Libro modificado: " + libroModificado.getNombre() + ", idLibro: " + libroModificado.getIdLibro() + ", idAlumno: " + libroModificado.getAlumno().getIdAlumno());
            if (libroModificado == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(libroModificado.getIdLibro());
            }
        }
    }

    @GetMapping("/recogerLibrosPorAlumno/{idAlumno}")
    public ResponseEntity<List<Libro>> recogerLibrosPorAlumno(@PathVariable Long idAlumno) {
        System.out.println("Recogiendo libros para el alumno con ID: " + idAlumno);

        Optional<Alumno> alumnoOptional = alumnoRepositorio.findById(idAlumno);
        if (alumnoOptional.isEmpty()) {
            System.out.println("Alumno con ID: " + idAlumno + " NO encontrado");
            return ResponseEntity.notFound().build();
        }

        Alumno alumno = alumnoOptional.get();
        Set<Libro> libros = alumno.getLibros();

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros para el alumno con ID: " + idAlumno);
            return ResponseEntity.ok(new ArrayList<>()); // Devuelve una lista vacía si no hay libros
        }

        List<Libro> listaLibros = new ArrayList<>(libros);
        System.out.println("Libros encontrados para el alumno con ID: " + idAlumno);

        // Convertir la lista de libros a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String librosJson = objectMapper.writeValueAsString(listaLibros);
            System.out.println("Respuesta JSON: " + librosJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Imprimir todos los libros que tiene el alumno
        System.out.println("Libros del alumno con ID " + idAlumno + ":");
        for (Libro libro : listaLibros) {
            System.out.println(libro.getNombre());
        }

        return ResponseEntity.ok(listaLibros);
    }

    @GetMapping("/getLibro{id}")
    public ResponseEntity<Libro> getLibro(@PathVariable("id") Long id) {

        Optional<Libro> libroEncontrado = libroRepositorio.findById(id);
        if (libroEncontrado.isEmpty()) {
            System.out.println("Libro con id: " + id + "NO encontrado");
            return ResponseEntity.notFound().build();
        } else {
            System.out.println("Libro con id: " + id + "SI encontrado");
            return ResponseEntity.ok(libroEncontrado.get());
        }
    }



}
