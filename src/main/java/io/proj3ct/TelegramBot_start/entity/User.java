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

    @Column(name = "user_name")
    private String userName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "is_bot")
    private Boolean isBot = false;
    private Integer score = 0;
    private Integer requtation = 0;
}
