package app.carsharing.service.rental.impl;

import app.carsharing.model.Rental;
import app.carsharing.model.User;
import app.carsharing.repository.rental.RentalRepository;
import app.carsharing.repository.user.UserRepository;
import app.carsharing.service.notification.NotificationService;
import app.carsharing.util.Message;
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
        Map<Long, List<Rental>> rentalsByUser = groupRentalsByUser(overdueRentals);
        Set<Long> usersWithOverdueRentals = rentalsByUser.keySet();
        List<User> allUsers = userRepository.findAll();

        notifyUsers(allUsers, rentalsByUser, usersWithOverdueRentals);
    }

    private Map<Long, List<Rental>> groupRentalsByUser(List<Rental> rentals) {
        return rentals.stream()
                .collect(Collectors.groupingBy(rental -> rental.getUser().getId()));
    }

    private void notifyUsers(List<User> users, Map<Long, List<Rental>> rentalsByUser,
                             Set<Long> usersWithOverdueRentals) {
        for (User user : users) {
            Long userId = user.getId();
            Long tgChatId = user.getTgChatId();

            if (tgChatId != null) {
                if (usersWithOverdueRentals.contains(userId)) {
                    notifyUserAboutOverdueRentals(user, rentalsByUser.get(userId));
                } else {
                    sendNoOverdueRentalsNotification(tgChatId);
                }
            }
        }
    }

    private void notifyUserAboutOverdueRentals(User user, List<Rental> overdueRentals) {
        for (Rental rental : overdueRentals) {
            notificationService.sendNotification(
                    user.getTgChatId(),
                    Message.getOverdueRentalMessageForCustomer(rental)
            );
        }
    }

    private void sendNoOverdueRentalsNotification(Long tgChatId) {
        notificationService.sendNotification(
                tgChatId,
                "You have no overdue rentals. Thank you for returning your rentals on time!"
        );
    }
}
