package it.unisa.Client.GUI.components;

import java.time.LocalDate;

// ===== CLIENT CLASS =====
public class Client {
    String name;
    String surname;
    String cf;
    LocalDate birthDate;
    String nationality;
    String gender;
    String email;
    String phone;

    public Client(String name, String surname, String cf, LocalDate birthDate, String nationality, String gender, String email, String phone) {
        this.name = name;
        this.surname = surname;
        this.cf = cf;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }

    public String getFullName() {
        return name + " " + surname;
    }
}