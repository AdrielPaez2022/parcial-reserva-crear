package reserva.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reserva.exception.PersistenceException;
import reserva.exception.ReservationAlreadyExistsException;
import reserva.modelo.EstadoReserva;
import reserva.modelo.Reserva;
import reserva.output.ReservationRepository;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class CreateReservationUseCaseTest {
    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final CreateReservationUseCase useCase = new CreateReservationUseCase(reservationRepository);

    private final UUID id = UUID.randomUUID();
    private final String emailContacto = "contacto@example.com";
    private final LocalDate fecha = LocalDate.of(2026, 10, 20);

    @BeforeEach
    void setUp() {
        when(reservationRepository.existsActiveReservation(emailContacto, fecha)).thenReturn(false);
    }

    @Test
    void creaYGuardaLaReservaYLaDevuelve() {
        when(reservationRepository.save(any(Reserva.class))).thenReturn(true);

        Reserva reserva = useCase.register(emailContacto, id, fecha, 3);

        assertEquals(id, reserva.getId());
        assertEquals(emailContacto, reserva.getEmailContacto());
        assertEquals(fecha, reserva.getFecha());
        assertEquals(3, reserva.getCantidadPersonas());
        assertEquals(EstadoReserva.ACTIVA, reserva.getEstado());
        verify(reservationRepository).existsActiveReservation(emailContacto, fecha);
        verify(reservationRepository).save(reserva);
        verifyNoMoreInteractions(reservationRepository);
    }

    @Test
    void rechazaLaCreacionSiYaExisteUnaReservaActiva() {
        when(reservationRepository.existsActiveReservation(emailContacto, fecha)).thenReturn(true);

        assertThrows(ReservationAlreadyExistsException.class,
                () -> useCase.register(emailContacto, id, fecha, 3));

        verify(reservationRepository).existsActiveReservation(emailContacto, fecha);
        verifyNoMoreInteractions(reservationRepository);
    }

    @Test
    void informaElFalloAlGuardar() {
        when(reservationRepository.save(any(Reserva.class))).thenReturn(false);

        assertThrows(PersistenceException.class,
                () -> useCase.register(emailContacto, id, fecha, 3));

        verify(reservationRepository).existsActiveReservation(emailContacto, fecha);
        verify(reservationRepository).save(any(Reserva.class));
        verifyNoMoreInteractions(reservationRepository);
    }
}
