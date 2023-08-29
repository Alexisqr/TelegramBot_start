package io.proj3ct.TelegramBot_start.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "User" ,schema ="mySchema")
public class User {
    @Id
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private Boolean isBot = false;
    private Integer score = 0;
    private Integer reputation = 0;
}
