package models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Pais {
    public static final int NEW_ID = -1;

    private int id;
    private String nombre;
    private int fundacion;
    private Long poblacion;
    private String continente;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Pais() {
        this(NEW_ID, "", 0, 0l, "", LocalDateTime.now(), LocalDateTime.now());
    }

    public Pais(int id, String nombre, int fundacion, Long poblacion,
                      String continente, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nombre = nombre;
        this.fundacion = fundacion;
        this.poblacion = poblacion;
        this.continente = continente;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    // Constructor sin id, createdAt y updatedAt (opcional, si quieres facilidad de uso)
    public Pais(String nombre, int fundacion, Long poblacion, String continente) {
        this(NEW_ID, nombre, fundacion, poblacion, continente, LocalDateTime.now(), LocalDateTime.now());
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFundacion() {
        return fundacion;
    }

    public void setFundacion(int fundacion) {
        this.fundacion = fundacion;
    }

    public Long getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(Long poblacion) {
        this.poblacion = poblacion;
    }

    public String getContinente() {
        return continente;
    }

    public void setContinente(String continente) {
        this.continente = continente;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // equals, hashCode, toString (opcional pero recomendado)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pais)) return false;
        Pais that = (Pais) o;
        return id == that.id &&
                Long.compare(that.poblacion, poblacion) == 0 &&
                Integer.compare(fundacion, that.fundacion) == 0 &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(continente, that.continente) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, fundacion, poblacion, continente, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fundacion=" + fundacion +
                ", poblacion=" + poblacion +
                ", continente=" + continente +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
