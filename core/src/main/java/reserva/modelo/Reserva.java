package reserva.modelo;

import reserva.exception.ReservaInvalidaException;
import reserva.exception.ReservaYaCanceladaException;

import java.time.LocalDate;
import java.util.UUID;

public class Reserva {
    private final UUID id;
    private final String emailContacto;
    private final LocalDate fecha;
    private final int cantidadPersonas;
    private EstadoReserva estado;

    private Reserva(UUID id, String emailContacto, LocalDate fecha, int cantidadPersonas) {
        this.emailContacto = emailContacto;
        this.id = id;
        this.fecha = fecha;
        this.cantidadPersonas = cantidadPersonas;
        this.estado = EstadoReserva.ACTIVA;
    }

    public static Reserva instance(UUID id, String emailContacto, LocalDate fecha, int cantidadPersonas) {
        if (id == null) {
            throw new ReservaInvalidaException("El identificador de la reserva es obligatorio.");
        }
        if (fecha == null) {
            throw new ReservaInvalidaException("La fecha de la reserva es obligatoria.");
        }
        if (emailContacto == null || emailContacto.isBlank()) {
            throw new ReservaInvalidaException("El correo de contacto es obligatorio.");
        }
        if (cantidadPersonas <= 0) {
            throw new ReservaInvalidaException("La cantidad de personas debe ser mayor que cero.");
        }

        return new Reserva(id, emailContacto, fecha, cantidadPersonas);
    }

    public void cancelar() {
        if (estado == EstadoReserva.CANCELADA) {
            throw new ReservaYaCanceladaException("La reserva ya está cancelada.");
        }
        estado = EstadoReserva.CANCELADA;
    }

    public UUID getId() {
        return id;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public EstadoReserva getEstado() {
        return estado;
    }
}
