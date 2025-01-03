package app.carsharing.service.rental.impl;

import app.carsharing.model.Rental;
import app.carsharing.model.User;
import app.carsharing.repository.rental.RentalRepository;
import app.carsharing.repository.user.UserRepository;
import app.carsharing.service.notification.Message;
import app.carsharing.service.notification.NotificationService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalOverdueChecker {
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 0 * * ?")
    private void checkOverDueRentals() {
        List<Rental> overdueRentals = rentalRepository.findOverdueRentals(LocalDate.now());
        Map<Long, List<Rental>> rentalsByUser = overdueRentals.stream()
                .collect(Collectors.groupingBy(rental -> rental.getUser().getId()));
        Set<Long> usersWithOverdueRentals = rentalsByUser.keySet();
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            Long userId = user.getId();
            Long tgChatId = user.getTgChatId();

            if (tgChatId != null) {
                if (usersWithOverdueRentals.contains(userId)) {
                    List<Rental> userOverdueRentals = rentalsByUser.get(userId);
                    for (Rental rental : userOverdueRentals) {
                        notificationService.sentNotification(
                                tgChatId,
                                Message.getOverdueRentalMessageForCustomer(rental)
                        );
                    }
                } else {
                    notificationService.sentNotification(tgChatId,
                            "You have no overdue rentals. "
                                    + "Thank you for returning your rentals on time!"
                    );
                }
            }
        }
    }
}
