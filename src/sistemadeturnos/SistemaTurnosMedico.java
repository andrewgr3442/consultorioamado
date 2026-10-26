
package sistemadeturnos;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class SistemaTurnosMedico {

    private static Map<String, Paciente> pacientesBD = new HashMap<>();
    private static List<Turno> agendaTurnos = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion = -1;
        do {
            System.out.println("\n==============================================");
            System.out.println("  SISTEMA DE GESTIÓN DE TURNOS - CONSULTORIO  ");
            System.out.println("==============================================");
            System.out.println("1. Gestión de Turnos (Agregar, Cancelar, Reprogramar)");
            System.out.println("2. Emisión de Informe de Agenda Diario (Médico)");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            String entrada = scanner.nextLine().trim();
            try {
                opcion = Integer.parseInt(entrada);
                switch (opcion) {
                    case 1:
                        menuGestionTurnos();
                        break;
                    case 2:
                        emitirInformeAgendaDiario();
                        break;
                    case 0:
                        System.out.println("\nCerrando el sistema...");
                        break;
                    default:
                        mostrarAdvertencia("Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                mostrarAdvertencia("Debe ingresar un número entero válido.");
            }
        } while (opcion != 0);
    }

    private static void menuGestionTurnos() {
        System.out.println("\n--- GESTIÓN DE TURNOS ---");
        System.out.println("1. Agregado (Nuevo Turno)");
        System.out.println("2. Cancelación");
        System.out.println("3. Reprogramación");
        System.out.print("Seleccione la operación a realizar: ");

        String entrada = scanner.nextLine().trim();
        switch (entrada) {
            case "1":
                agregarTurno();
                break;
            case "2":
                cancelarTurno();
                break;
            case "3":
                reprogramarTurno();
                break;
            default:
                mostrarAdvertencia("Opción no válida.");
        }
    }

    private static void agregarTurno() {
        System.out.println("\n[PASO 1] Validación de Identidad del Paciente");
        String dni = solicitarEntradaValidada("Ingrese DNI del paciente (numérico, 7-8 dígitos): ", "^\\d{7,8}$", "DNI fuera de formato.");

        Paciente paciente;
        if (pacientesBD.containsKey(dni)) {
            paciente = pacientesBD.get(dni);
            System.out.println("-> Paciente encontrado: " + paciente.getNombreCompleto());
        } else {
            System.out.println("-> Paciente NO registrado. Procediendo a Carga Paciente Nuevo...");
            String nombre = solicitarEntradaValidada("Ingrese Nombre Completo: ", "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{3,50}$", "Nombre fuera de formato.");
            String telefono = solicitarEntradaValidada("Ingrese Teléfono (ej: 1122334455): ", "^\\d{8,15}$", "Teléfono fuera de formato.");
            String obraSocial = solicitarEntradaValidada("Ingrese Obra Social / Prepaga (o 'Particular'): ", "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]{2,30}$", "Dato fuera de formato.");

            paciente = new Paciente(dni, nombre, telefono, obraSocial);
            pacientesBD.put(dni, paciente);
            System.out.println("✔ Paciente registrado exitosamente.");
        }

        System.out.println("\n[PASO 2] Verificación de Agenda y Horario");
        LocalDate fecha = solicitarFecha();
        LocalTime hora = solicitarHora();

        if (existeConflictoHorario(fecha, hora, -1)) {
            mostrarAdvertencia("El médico ya tiene un turno reservado en la fecha " + fecha + " a las " + hora + "hs.");
            return;
        }

        System.out.println("\n[PASO 3] Carga de Motivo (CONFIDENCIAL)");
        System.out.print("Ingrese motivo de consulta (Acceso Restringido): ");
        String motivo = scanner.nextLine().trim();
        if (motivo.isEmpty()) motivo = "Consulta General";

        Turno nuevoTurno = new Turno(paciente, fecha, hora, motivo);
        agendaTurnos.add(nuevoTurno);
        System.out.println("✔ Agenda Actualizada: Turno #" + nuevoTurno.getId() + " registrado con éxito.");
    }

    private static void cancelarTurno() {
        System.out.println("\n--- CANCELACIÓN DE TURNO ---");
        Turno turno = buscarTurnoPorId();
        if (turno == null) return;

        agendaTurnos.remove(turno);
        System.out.println("✔ Cancelación Exitosa: El cupo (" + turno.getFecha() + " " + turno.getHora() + "hs) ha quedado libre inmediatamente.");
    }

    private static void reprogramarTurno() {
        System.out.println("\n--- REPROGRAMACIÓN DE TURNO ---");
        Turno turno = buscarTurnoPorId();
        if (turno == null) return;

        System.out.println("Ingrese la nueva fecha y hora para la reprogramación:");
        LocalDate nuevaFecha = solicitarFecha();
        LocalTime nuevaHora = solicitarHora();

        if (existeConflictoHorario(nuevaFecha, nuevaHora, turno.getId())) {
            mostrarAdvertencia("Conflicto de agenda: El médico ya tiene ocupado ese nuevo horario.");
            return;
        }

        turno.setFecha(nuevaFecha);
        turno.setHora(nuevaHora);
        System.out.println("✔ Agenda Actualizada: El turno #" + turno.getId() + " ha sido reprogramado para el " + nuevaFecha + " a las " + nuevaHora + "hs.");
    }

    private static void emitirInformeAgendaDiario() {
        System.out.println("\n==============================================");
        System.out.println("  EMISIÓN DE INFORME DIARIO PARA EL MÉDICO    ");
        System.out.println("==============================================");
        
        LocalDate fechaConsulta = solicitarFecha();

        List<Turno> turnosDelDia = new ArrayList<>();
        for (Turno t : agendaTurnos) {
            if (t.getFecha().equals(fechaConsulta)) {
                turnosDelDia.add(t);
            }
        }

        turnosDelDia.sort(Comparator.comparing(Turno::getHora));

        System.out.println("\n----------------------------------------------------------------------------------");
        System.out.println(" LISTADO CONSOLIDADO DE PACIENTES - FECHA: " + fechaConsulta);
        System.out.println("----------------------------------------------------------------------------------");
        
        if (turnosDelDia.isEmpty()) {
            System.out.println("No hay turnos agendados para la fecha seleccionada.");
        } else {
            for (Turno t : turnosDelDia) {
                System.out.printf("Hora: %s | Paciente: %-20s | DNI: %-9s | Tel: %-12s | OS: %-12s | Motivo (Confidencial): %s\n",
                        t.getHora(),
                        t.getPaciente().getNombreCompleto(),
                        t.getPaciente().getDni(),
                        t.getPaciente().getTelefono(),
                        t.getPaciente().getObraSocial(),
                        t.getMotivoConsulta());
            }
        }
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("✔ Informe generado exitosamente. Listo para entregar/imprimir para el Doctor.");
    }

    private static String solicitarEntradaValidada(String mensaje, String regex, String mensajeError) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            if (entrada.matches(regex)) {
                return entrada;
            } else {
                mostrarAdvertencia(mensajeError);
            }
        }
    }

    private static LocalDate solicitarFecha() {
        while (true) {
            System.out.print("Ingrese fecha (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                mostrarAdvertencia("Fecha invalida. Debe respetar el formato YYYY-MM-DD.");
            }
        }
    }

    private static LocalTime solicitarHora() {
        while (true) {
            System.out.print("Ingrese hora (HH:MM, ej: 09:30, 14:00): ");
            String input = scanner.nextLine().trim();
            try {
                return LocalTime.parse(input);
            } catch (DateTimeParseException e) {
                mostrarAdvertencia("Hora invalida. Debe respetar el formato HH:MM en 24hs.");
            }
        }
    }

    private static boolean existeConflictoHorario(LocalDate fecha, LocalTime hora, int idTurnoExcluir) {
        for (Turno t : agendaTurnos) {
            if (t.getId() != idTurnoExcluir && t.getFecha().equals(fecha) && t.getHora().equals(hora)) {
                return true;
            }
        }
        return false;
    }

    private static Turno buscarTurnoPorId() {
        System.out.print("Ingrese el Número ID del Turno: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            for (Turno t : agendaTurnos) {
                if (t.getId() == id) {
                    return t;
                }
            }
            mostrarAdvertencia("No se encontró ningún turno con el ID proporcionado.");
        } catch (NumberFormatException e) {
            mostrarAdvertencia("ID de turno no válido.");
        }
        return null;
    }

    private static void mostrarAdvertencia(String mensaje) {
        System.out.println("\n[ADVERTENCIA DEL SISTEMA]: " + mensaje + "\n");
    }
}
