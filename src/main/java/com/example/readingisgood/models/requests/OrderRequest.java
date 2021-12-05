package com.example.readingisgood.models.requests;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<BookRequest> bookList;
}
