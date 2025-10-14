package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Estudiante {
    public static final int NEW_ID = -1;

    private int id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private double calificacion;
    private boolean repetidor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Estudiante() {
        this(NEW_ID, "", LocalDate.now(), 0.0, false, LocalDateTime.now(), LocalDateTime.now());
    }

    public Estudiante(int id, String nombre, LocalDate fechaNacimiento, double calificacion,
                      boolean repetidor, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.calificacion = calificacion;
        this.repetidor = repetidor;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor sin createdAt y updatedAt (opcional, si quieres facilidad de uso)
    public Estudiante(int id, String nombre, LocalDate fechaNacimiento, double calificacion, boolean repetidor) {
        this(id, nombre, fechaNacimiento, calificacion, repetidor, LocalDateTime.now(), LocalDateTime.now());
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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public boolean isRepetidor() {
        return repetidor;
    }

    public void setRepetidor(boolean repetidor) {
        this.repetidor = repetidor;
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
        if (!(o instanceof Estudiante)) return false;
        Estudiante that = (Estudiante) o;
        return id == that.id &&
                Double.compare(that.calificacion, calificacion) == 0 &&
                repetidor == that.repetidor &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(fechaNacimiento, that.fechaNacimiento) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, fechaNacimiento, calificacion, repetidor, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", calificacion=" + calificacion +
                ", repetidor=" + repetidor +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
