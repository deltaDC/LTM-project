package com.n19.ltmproject.server.command;

import com.n19.ltmproject.server.model.dto.Request;
import com.n19.ltmproject.server.model.dto.Response;

public interface Command {
    Response execute(Request request);
}
