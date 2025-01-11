package app.carsharing.util;

import app.carsharing.model.Payment;
import app.carsharing.model.Rental;

public class Message {
    public static String getRentalMessageForCustomer(Rental rental) {
        return String.format("Dear %s, you've just rented %s from %tF to %tF",
                rental.getUser().getFirstName(),
                rental.getCar().getBrand() + " " + rental.getCar().getModel(),
                rental.getRentalDate(),
                rental.getReturnDate());
    }

    public static String getSuccessfulPaymentMessageForCustomer(Payment payment) {
        return String.format("Payment of %.2f usd until %tF has been accepted",
                payment.getAmountToPay(),
                payment.getRental().getReturnDate());
    }

    public static String getOverdueRentalMessageForCustomer(Rental rental) {
        return String.format("Dear %s, your rental of %s (from %tF to %tF) is overdue. "
                        + " Please return the car as soon as possible to "
                        + "lessen additional charges.",
                rental.getUser().getFirstName(),
                rental.getCar().getBrand() + " " + rental.getCar().getModel(),
                rental.getRentalDate(),
                rental.getReturnDate());
    }
}
