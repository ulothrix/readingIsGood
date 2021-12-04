package com.example.readingisgood.models.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ReadingIsGoodResponse<T> {
    private MessageResponse message;
    private T data;

    public ReadingIsGoodResponse(T data, MessageResponse message) {
        this.data = data;
        this.message = message;
    }

    public ReadingIsGoodResponse(T data) {
        this.data = data;
    }

    public ReadingIsGoodResponse(MessageResponse message) {
        this.message = message;
    }
}
