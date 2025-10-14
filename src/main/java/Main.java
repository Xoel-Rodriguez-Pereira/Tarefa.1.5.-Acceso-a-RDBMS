

import models.Estudiante;
import repository.EstudiantesRepositoryImpl;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EstudiantesRepositoryImpl repository = new EstudiantesRepositoryImpl();

        // Mostrar todos los estudiantes
        for (Estudiante e : repository.findAll()) {
            System.out.println(e);
        }

        // Insertar nuevo estudiante
        Estudiante estudiante = new Estudiante(
                -1,
                "Pepe",
                LocalDate.parse("1990-01-01"),
                10.0,
                false
        );
        Estudiante res = repository.save(estudiante);
        System.out.println(res);

        repository.findAll().forEach(System.out::println);

        // Buscar por ID
        Estudiante estudiante2 = repository.findById(1);
        System.out.println(estudiante2 != null ? estudiante2 : "No encontrado");

        Estudiante estudiante3 = repository.findById(2);
        System.out.println(estudiante3 != null ? estudiante3 : "No encontrado");

        Estudiante estudiante5 = repository.findById(99);
        System.out.println(estudiante5 != null ? estudiante5 : "No encontrado");

        // Actualizar estudiante
        Estudiante estudiante4 = new Estudiante(
                99,
                "Demo_Update",
                estudiante.getFechaNacimiento(),
                5.0,
                estudiante.isRepetidor(),
                estudiante.getCreatedAt(),
                estudiante.getUpdatedAt()
        );
        Estudiante res2 = repository.update(1, estudiante4);
        System.out.println(res2 != null ? res2 : "No encontrado");

        // Eliminar estudiante
        Estudiante res3 = repository.delete(2);
        System.out.println(res3 != null ? res3 : "No encontrado");

        // Mostrar todos
        repository.findAll().forEach(System.out::println);

        // Guardar más estudiantes
        Estudiante e2 = new Estudiante(
                -1,
                "Eva",
                LocalDate.parse("1990-01-01"),
                10.0,
                false
        );
        Estudiante r2 = repository.save(e2);
        System.out.println(r2);

        Estudiante e3 = new Estudiante(
                -1,
                "Pepe",
                LocalDate.parse("1990-01-01"),
                4.0,
                true
        );
        Estudiante r3 = repository.save(e3);

        repository.findAll().forEach(System.out::println);

        // Buscar por nombre
        List<Estudiante> byNombre = repository.findByNombre("Pepe");
        byNombre.forEach(System.out::println);

        // Buscar por calificación
        repository.findByCalificacion(10.0).forEach(System.out::println);

        // Buscar por repetidor
        repository.findByRepetidor(false).forEach(System.out::println);

        // Estadísticas
        System.out.println(repository.mediaCalificaciones());
        System.out.println(repository.maximaCalificacion());
        System.out.println(repository.minimaCalificacion());
        System.out.println(repository.numAprobados());
        System.out.println(repository.numSuspensos());

        // Todos los estudiantes
        repository.findAll().forEach(System.out::println);

        // Paginación
        repository.findAllPaginated(1, 2).forEach(System.out::println);
        repository.findAllPaginated(1, 100).forEach(System.out::println);
    }
}
