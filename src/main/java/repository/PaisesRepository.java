package repository;

import java.util.List;

import models.Pais;

public interface PaisesRepository extends CrudRepository<Pais> {
    List<Pais> findAllPaginated(int page, int size);
    List<Pais> findByNombre(String nombre);
    List<Pais> findByPoblacion(Long poblacion);
    List<Pais> findByContinente(String continente);
    Long mediaPoblaciones();
    Long maximaPoblacion();
    Long minimaPoblacion();
}
