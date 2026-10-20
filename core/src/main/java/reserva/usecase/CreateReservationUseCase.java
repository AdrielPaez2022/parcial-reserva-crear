package reserva.usecase;

import reserva.exception.PersistenceException;
import reserva.exception.ReservationAlreadyExistsException;
import reserva.exception.RevervasObligatoriaException;
import reserva.modelo.Reserva;
import reserva.output.ReservationRepository;

import java.time.LocalDate;
import java.util.UUID;

public class CreateReservationUseCase {
    private final ReservationRepository reservationRepository;

    public CreateReservationUseCase(ReservationRepository reservationRepository) {
        if (reservationRepository == null) {
            throw new RevervasObligatoriaException("El repositorio de reservas es obligatorio.");
        }
        this.reservationRepository = reservationRepository;
    }

    public Reserva register(String emailContacto,
                            UUID id,
                            LocalDate fecha,
                            int cantidadPersonas) {
        Reserva reserva = Reserva.instance(id, emailContacto, fecha, cantidadPersonas);

        if (reservationRepository.existsActiveReservation(emailContacto, fecha)) {
            throw new ReservationAlreadyExistsException(
                    "Ya existe una reserva activa para ese correo y fecha.");
        }

        if (!reservationRepository.save(reserva)) {
            throw new PersistenceException("No se pudo guardar la reserva.");
        }

        return reserva;
    }
}
