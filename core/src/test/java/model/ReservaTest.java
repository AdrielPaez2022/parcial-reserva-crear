package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reserva.exception.ReservaInvalidaException;
import reserva.exception.ReservaYaCanceladaException;
import reserva.modelo.EstadoReserva;
import reserva.modelo.Reserva;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservaTest {
    private final UUID id = UUID.randomUUID();
    private final String email = "contacto@example.com";
    private final LocalDate fecha = LocalDate.of(2026, 10, 20);

    @Test
    void creaReservaActivaConLosDatosRecibidos() {
        Reserva reserva = Reserva.instance(id, email, fecha, 2);

        assertEquals(id, reserva.getId());
        assertEquals(email, reserva.getEmailContacto());
        assertEquals(fecha, reserva.getFecha());
        assertEquals(2, reserva.getCantidadPersonas());
        Assertions.assertEquals(EstadoReserva.ACTIVA, reserva.getEstado());
    }

    @Test
    void rechazaIdentificadorNulo() {
        assertThrows(ReservaInvalidaException.class,
                () -> Reserva.instance(null, email, fecha, 2));
    }

    @Test
    void rechazaFechaNula() {
        assertThrows(ReservaInvalidaException.class,
                () -> Reserva.instance(id, email, null, 2));
    }

    @Test
    void rechazaCorreoNuloOVacio() {
        assertThrows(ReservaInvalidaException.class,
                () -> Reserva.instance(id, null, fecha, 2));
        assertThrows(ReservaInvalidaException.class,
                () -> Reserva.instance(id, "  ", fecha, 2));
    }

    @Test
    void rechazaCantidadDePersonasNoPositiva() {
        assertThrows(ReservaInvalidaException.class,
                () -> Reserva.instance(id, email, fecha, 0));
        assertThrows(ReservaInvalidaException.class,
                () -> Reserva.instance(id, email, fecha, -1));
    }

    @Test
    void permiteCancelarUnaReservaActivaUnaSolaVez() {
        Reserva reserva = Reserva.instance(id, email, fecha, 2);

        reserva.cancelar();

        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        assertThrows(ReservaYaCanceladaException.class, reserva::cancelar);
    }
}
