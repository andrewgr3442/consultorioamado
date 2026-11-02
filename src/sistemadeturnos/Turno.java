
package sistemadeturnos;

import java.time.LocalDate;
import java.time.LocalTime;


public class Turno {
    private static int contadorId = 1;
    private int id;
    private Paciente paciente;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivoConsulta;

    // Turno nuevo: toma el próximo ID disponible
    public Turno(Paciente paciente, LocalDate fecha, LocalTime hora, String motivoConsulta) {
        this.id = contadorId++;
        this.paciente = paciente;
        this.fecha = fecha;
        this.hora = hora;
        this.motivoConsulta = motivoConsulta;
    }

    // Turno cargado desde archivo: conserva su ID y ajusta el contador para no repetir IDs
    public Turno(int id, Paciente paciente, LocalDate fecha, LocalTime hora, String motivoConsulta) {
        this.id = id;
        this.paciente = paciente;
        this.fecha = fecha;
        this.hora = hora;
        this.motivoConsulta = motivoConsulta;
        if (id >= contadorId) contadorId = id + 1;
    }

    public int getId() { return id; }
    public Paciente getPaciente() { return paciente; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
    public String getMotivoConsulta() { return motivoConsulta; }

    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setHora(LocalTime hora) { this.hora = hora; }
}
