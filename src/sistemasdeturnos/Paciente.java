
package sistemasdeturnos;


    import java.io.Serializable;

public class Paciente implements Serializable {
    private static final long serialVersionUID = 1L;

    private String dni;
    private String nombreCompleto;
    private String telefono;
    private String obraSocial;

    // Constructor completo
    public Paciente(String dni, String nombreCompleto, String telefono, String obraSocial) {
        this.dni = dni;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.obraSocial = obraSocial;
    }

    // Getters y Setters
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }
}
