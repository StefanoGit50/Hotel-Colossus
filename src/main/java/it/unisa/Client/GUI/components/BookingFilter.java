package it.unisa.Client.GUI.components;

import it.unisa.Client.FrontDesk.FrontDeskClient;
import it.unisa.Common.Prenotazione;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class BookingFilter {
    String guestName;
    LocalDate checkIn;
    LocalDate checkOut;
    String room;
    String mealPlan;
    protected static ObservableList<Prenotazione> allBookings = FXCollections.observableArrayList();

    public BookingFilter() {}
    public BookingFilter(String guestName, LocalDate checkIn, LocalDate checkOut, String room, String mealPlan) {
        this.guestName = guestName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.room = room;
        this.mealPlan = mealPlan;
    }

    public static int initializeSampleBookings(){
        FrontDeskClient frontDeskClient = new FrontDeskClient();
        allBookings.setAll(frontDeskClient.getPrenotazioni());

        return allBookings.size();
    }

    public static ObservableList<Prenotazione> getAllBookings() {
        return allBookings;
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
