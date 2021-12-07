package com.example.readingisgood.models.requests;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class OrderRequest {
    @Valid
    private List<BookRequest> bookList;
}
