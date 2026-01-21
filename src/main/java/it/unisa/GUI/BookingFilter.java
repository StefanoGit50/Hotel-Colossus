package it.unisa.GUI;

import javafx.collections.ObservableList;

import java.time.LocalDate;

public class BookingFilter {
    String guestName;
    LocalDate checkIn;
    LocalDate checkOut;
    String room;
    String mealPlan;


    public BookingFilter(String guestName, LocalDate checkIn, LocalDate checkOut, String room, String mealPlan) {
        this.guestName = guestName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.room = room;
        this.mealPlan = mealPlan;
    }

    public static void initializeSampleBookings(ObservableList<BookingFilter> allBookings){
        allBookings.add(new BookingFilter("Anna Bianchi",
                LocalDate.of(2025, 9, 15),
                LocalDate.of(2025, 9, 22),
                "305", "Mezza Pensione"));

        allBookings.add(new BookingFilter("Mario Rossi",
                LocalDate.of(2025, 9, 30),
                LocalDate.of(2025, 10, 10),
                "110", "Mezza Pensione"));

        allBookings.add(new BookingFilter("Barbara d'Orso",
                LocalDate.of(2025, 10, 15),
                LocalDate.of(2025, 10, 20),
                "231", "Pensione Completa"));

        allBookings.add(new BookingFilter("Giorgio Ventura",
                LocalDate.of(2025, 11, 1),
                LocalDate.of(2025, 11, 5),
                "121", "Solo Pernottamento"));

        allBookings.add(new BookingFilter("Luca Verdi",
                LocalDate.of(2025, 12, 20),
                LocalDate.of(2025, 12, 27),
                "412", "Pensione Completa"));
    }

    public String getGuestName() {
        return guestName;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public String getRoom() {
        return room;
    }

    public String getMealPlan() {
        return mealPlan;
    }
}
