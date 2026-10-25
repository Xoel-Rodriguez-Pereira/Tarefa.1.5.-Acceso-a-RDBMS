

import java.util.List;

import models.Pais;
import repository.PaisesRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        PaisesRepositoryImpl repository = new PaisesRepositoryImpl();

        // Mostrar todos los estudiantes
        repository.findAll().forEach(System.out::println);

        // Insertar nuevo estudiante
        Pais pais = new Pais(
                "España", 
                1492, 
                49801559l, 
                "Europa"
        );
        Pais res = repository.save(pais);
        System.out.println(res);

        repository.findAll().forEach(System.out::println);

        // Buscar por ID
        Pais pais2 = repository.findById(1);
        System.out.println(pais2 != null ? pais2 : "No encontrado");

        Pais pais3 = repository.findById(2);
        System.out.println(pais3 != null ? pais3 : "No encontrado");

        Pais pais5 = repository.findById(99);
        System.out.println(pais5 != null ? pais5 : "No encontrado");

        // Actualizar estudiante
        Pais pais4 = new Pais(
                99,
                "Demo_Update",
                pais.getFundacion(),
                5l,
                pais.getContinente(),
                pais.getCreatedAt(),
                pais.getUpdatedAt()
        );
        Pais res2 = repository.update(1, pais4);
        System.out.println(res2 != null ? res2 : "No encontrado");

        // Eliminar estudiante
        Pais res3 = repository.delete(2);
        System.out.println(res3 != null ? res3 : "No encontrado");

        // Mostrar todos
        repository.findAll().forEach(System.out::println);

        // Guardar más estudiantes
        Pais e2 = new Pais(
                "Francia",
                987,
                68882600l,
                "Europa"
        );
        Pais r2 = repository.save(e2);
        System.out.println(r2);

        Pais e3 = new Pais(
                "Alemania",
                1326,
                58282600l,
                "Europa"
        );
        Pais r3 = repository.save(e3);

        repository.findAll().forEach(System.out::println);

        // Buscar por nombre
        List<Pais> byNombre = repository.findByNombre("Pepe");
        byNombre.forEach(System.out::println);

        // Buscar por calificación
        repository.findByPoblacion(58282600l).forEach(System.out::println);

        // Buscar por repetidor
        repository.findByContinente("Europa").forEach(System.out::println);

        // Estadísticas
        System.out.println(repository.mediaPoblaciones());
        System.out.println(repository.maximaPoblacion());
        System.out.println(repository.minimaPoblacion());

        // Todos los estudiantes
        repository.findAll().forEach(System.out::println);

        // Paginación
        repository.findAllPaginated(1, 2).forEach(System.out::println);
        repository.findAllPaginated(1, 100).forEach(System.out::println);
    }
}
