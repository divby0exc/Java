package com.divby0exc.wswebappwithpostman.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DTOChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String username;
    public DTOChannel(String title, String username) {
        this.title = title;
        this.username = username;
    }

    public String toString() {
        return "Channel with ID: " + id + " has been created.";
    }
}
