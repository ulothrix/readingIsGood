package com.example.readingisgood.services;

import com.example.readingisgood.exceptions.BookAlreadyExistException;
import com.example.readingisgood.exceptions.BookNotFoundException;
import com.example.readingisgood.models.dtos.BookDto;
import com.example.readingisgood.models.requests.BookRequest;
import com.example.readingisgood.models.responses.MessageResponse;
import com.example.readingisgood.models.responses.ReadingIsGoodResponse;
import com.example.readingisgood.persistence.entitites.BookEntity;
import com.example.readingisgood.persistence.repositories.BookRepository;
import com.example.readingisgood.persistence.services.SequenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import static com.example.readingisgood.persistence.entitites.BookEntity.BOOK_SEQUENCE;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final SequenceService sequenceService;
    private final ModelMapper modelMapper;

    public ReadingIsGoodResponse<Void> addNewBook(BookRequest bookRequest) {

        if (bookRepository.existsBookEntityByIsbn(bookRequest.getIsbn())) {
            throw new BookAlreadyExistException();
        } else {
            BookEntity bookEntity = new BookEntity();
            bookEntity.setId(sequenceService.getNextSequence(BOOK_SEQUENCE));
            bookEntity.setName(bookRequest.getName());
            bookEntity.setAuthor(bookRequest.getAuthor());
            bookEntity.setIsbn(bookRequest.getIsbn());
            bookEntity.setPrice(bookRequest.getPrice());
            bookEntity.setStock(bookRequest.getStock());

            bookRepository.save(bookEntity);
            log.info("New book added: {}", bookEntity);
        }

        return new ReadingIsGoodResponse<>(new MessageResponse("Book successfuly added"));
    }

    @Transactional
    public ReadingIsGoodResponse<BookDto> updateStock(BookRequest bookRequest) {

        Optional<BookEntity> optionalBookEntity = bookRepository.findBookEntityByIsbn(bookRequest.getIsbn());

        if (optionalBookEntity.isPresent()) {
            BookEntity bookEntity = optionalBookEntity.get();
            bookEntity.setStock(bookRequest.getStock());
            bookRepository.save(bookEntity);

            BookDto bookDto = modelMapper.map(bookEntity, BookDto.class);

            log.info("Stock update of ISBN {} : new stock: {}",bookEntity.getIsbn(),bookEntity.getStock());
            return new ReadingIsGoodResponse<>(bookDto);
        } else {
            throw new BookNotFoundException();
        }
    }

    public ReadingIsGoodResponse<List<BookDto>> getAllBooks() {

        List<BookEntity> bookEntityList = bookRepository.findAll();

        Type listType = new TypeToken<List<BookDto>>() {
        }.getType();
        List<BookDto> bookDtoList = modelMapper.map(bookEntityList, listType);

        return new ReadingIsGoodResponse<>(bookDtoList);
    }

    public BookEntity getBook(String isbn) {
        Optional<BookEntity> optionalBookEntity = bookRepository.findBookEntityByIsbn(isbn);
        if (optionalBookEntity.isPresent()) {
            return optionalBookEntity.get();
        } else {
            throw new BookNotFoundException();
        }
    }
}
