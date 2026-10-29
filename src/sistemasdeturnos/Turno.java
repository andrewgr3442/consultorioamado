
package sistemasdeturnos;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Turno implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int contadorId = 1;

    private int id;
    private Paciente paciente;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivoConsulta;

    // Constructor
    public Turno(Paciente paciente, LocalDate fecha, LocalTime hora, String motivoConsulta) {
        this.id = contadorId++;
        this.paciente = paciente;
        this.fecha = fecha;
        this.hora = hora;
        this.motivoConsulta = motivoConsulta;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    // Métodos estáticos para gestionar la secuencia de autoincremento
    public static void setContadorId(int nuevoContador) {
        contadorId = nuevoContador;
    }

    public static int getContadorId() {
        return contadorId;
    }
}