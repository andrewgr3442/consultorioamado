
package sistemadeturnos;


public class Paciente {
    private String dni;
    private String nombreCompleto;
    private String telefono;
    private String obraSocial;

    public Paciente(String dni, String nombreCompleto, String telefono, String obraSocial) {
        this.dni = dni;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.obraSocial = obraSocial;
    }

    public String getDni() { return dni; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getTelefono() { return telefono; }
    public String getObraSocial() { return obraSocial; }

    @Override
    public String toString() {
        return nombreCompleto + " (DNI: " + dni + " | Tel: " + telefono + " | OS: " + obraSocial + ")";
    }
}
