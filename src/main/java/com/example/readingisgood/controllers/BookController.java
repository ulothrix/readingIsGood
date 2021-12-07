package com.example.readingisgood.controllers;

import com.example.readingisgood.models.dtos.BookDto;
import com.example.readingisgood.models.requests.BookRequest;
import com.example.readingisgood.models.responses.ReadingIsGoodResponse;
import com.example.readingisgood.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("new")
    public ReadingIsGoodResponse<Void> newBook(@Valid @RequestBody BookRequest bookRequest) {
        return bookService.addNewBook(bookRequest);
    }

    @PostMapping("updateStock")
    public ReadingIsGoodResponse<BookDto> updateStock(@Valid @RequestBody BookRequest bookRequest) {
        return bookService.updateStock(bookRequest);
    }

    @GetMapping("all")
    public ReadingIsGoodResponse<List<BookDto>> getAllBooks() {
        return bookService.getAllBooks();
    }
}
