package usecase;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import reserva.exception.PersistenceException;
import reserva.exception.ReservationAlreadyExistsException;
import reserva.modelo.Reserva;
import reserva.output.ReservationRepository;
import reserva.usecase.CreateReservationUseCase;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateReservationUseCaseTest {

    @Mock
    private ReservationRepository mockReservationRepository;

    private final UUID id = UUID.fromString("7e8ad860-716d-4b86-9643-430870caefe7");
    private final String emailContacto = "contacto@example.com";
    private final LocalDate fecha = LocalDate.of(2026, 10, 20);

    @Test
    public void register_success_returnReservation() {
        // Arrange
        CreateReservationUseCase createReservationUseCase = new CreateReservationUseCase(mockReservationRepository);
        when(mockReservationRepository.existsActiveReservation(emailContacto, fecha)).thenReturn(false);
        when(mockReservationRepository.save(any(Reserva.class))).thenReturn(true);

        // Act
        Reserva result = createReservationUseCase.register(emailContacto, id, fecha, 3);

        // Assert

        verify(mockReservationRepository).existsActiveReservation(emailContacto, fecha);
        verify(mockReservationRepository).save(result);
        verifyNoMoreInteractions(mockReservationRepository);
    }

    @Test
    public void register_alreadyExists_ReservationAlreadyExistsException() {
        // Arrange
        CreateReservationUseCase createReservationUseCase = new CreateReservationUseCase(mockReservationRepository);
        when(mockReservationRepository.existsActiveReservation(emailContacto, fecha)).thenReturn(true);

        // Act & Assert
        assertThrows(ReservationAlreadyExistsException.class, () -> createReservationUseCase.register(emailContacto, id, fecha, 3));

        verify(mockReservationRepository).existsActiveReservation(emailContacto, fecha);
        verify(mockReservationRepository, never()).save(any(Reserva.class));
        verifyNoMoreInteractions(mockReservationRepository);
    }

    @Test
    public void register_saveReturnsFalse_PersistenceException() {
        // Arrange
        CreateReservationUseCase createReservationUseCase =
                new CreateReservationUseCase(mockReservationRepository);
        when(mockReservationRepository.existsActiveReservation(emailContacto, fecha)).thenReturn(false);
        when(mockReservationRepository.save(any(Reserva.class))).thenReturn(false);

        // Act & Assert
        assertThrows(PersistenceException.class,
                () -> createReservationUseCase.register(emailContacto, id, fecha, 3));

        verify(mockReservationRepository).existsActiveReservation(emailContacto, fecha);
        verify(mockReservationRepository).save(any(Reserva.class));
        verifyNoMoreInteractions(mockReservationRepository);
    }
}
