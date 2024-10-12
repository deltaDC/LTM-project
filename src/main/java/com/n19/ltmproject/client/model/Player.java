package com.n19.ltmproject.client.model;

import com.n19.ltmproject.server.model.enums.PlayerStatus;
import lombok.*;

@Data
public class Player {

    private long id;
    private String username;
    private String password;
    private PlayerStatus status;
}
