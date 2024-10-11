package com.n19.ltmproject.client.model;

import com.n19.ltmproject.server.model.enums.PlayerStatus;
import lombok.*;

import javax.persistence.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private PlayerStatus status;
}
