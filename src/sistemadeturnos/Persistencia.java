
package sistemadeturnos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Guarda y carga pacientes y turnos en archivos CSV (separador ';').
 * Los archivos quedan en la carpeta desde donde se ejecuta el programa.
 */
public class Persistencia {

    private static final Path ARCHIVO_PACIENTES = Paths.get("pacientes.csv");
    private static final Path ARCHIVO_TURNOS = Paths.get("turnos.csv");

    // Los textos libres no pueden tener ';' ni saltos de línea porque romperían el CSV
    private static String limpiar(String s) {
        return s.replace(";", ",").replace("\n", " ").replace("\r", " ");
    }

    public static void guardar(Map<String, Paciente> pacientes, List<Turno> turnos) {
        List<String> lp = new ArrayList<>();
        for (Paciente p : pacientes.values()) {
            lp.add(String.join(";", p.getDni(), limpiar(p.getNombreCompleto()),
                    limpiar(p.getTelefono()), limpiar(p.getObraSocial())));
        }
        List<String> lt = new ArrayList<>();
        for (Turno t : turnos) {
            lt.add(String.join(";", String.valueOf(t.getId()), t.getPaciente().getDni(),
                    t.getFecha().toString(), t.getHora().toString(), limpiar(t.getMotivoConsulta())));
        }
        try {
            Files.write(ARCHIVO_PACIENTES, lp, StandardCharsets.UTF_8);
            Files.write(ARCHIVO_TURNOS, lt, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("[ERROR] No se pudieron guardar los datos: " + e.getMessage());
        }
    }

    public static void cargar(Map<String, Paciente> pacientes, List<Turno> turnos) {
        try {
            if (Files.exists(ARCHIVO_PACIENTES)) {
                for (String l : Files.readAllLines(ARCHIVO_PACIENTES, StandardCharsets.UTF_8)) {
                    if (l.isBlank()) continue;
                    String[] p = l.split(";", -1);
                    pacientes.put(p[0], new Paciente(p[0], p[1], p[2], p[3]));
                }
            }
            if (Files.exists(ARCHIVO_TURNOS)) {
                for (String l : Files.readAllLines(ARCHIVO_TURNOS, StandardCharsets.UTF_8)) {
                    if (l.isBlank()) continue;
                    String[] t = l.split(";", -1);
                    Paciente pac = pacientes.get(t[1]);
                    if (pac == null) continue; // turno huérfano: se ignora
                    turnos.add(new Turno(Integer.parseInt(t[0]), pac,
                            LocalDate.parse(t[2]), LocalTime.parse(t[3]), t[4]));
                }
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("[ERROR] No se pudieron leer los datos guardados: " + e.getMessage());
        }
    }
}
