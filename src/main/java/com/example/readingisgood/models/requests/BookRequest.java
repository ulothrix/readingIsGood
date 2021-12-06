package com.example.readingisgood.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest implements Serializable {

    private String name;
    private String author;

    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private int stock;

    @Pattern(regexp = "[0-9]+", message = "Only numbers must be sent")
    @Size(min = 13, max = 13, message = "ISBN number must contain 13 digits")
    private String isbn;

    @DecimalMin("0.01")
    private Double price;

    public BookRequest(String isbn, int stock) {
        this.isbn = isbn;
        this.stock = stock;
    }
}
