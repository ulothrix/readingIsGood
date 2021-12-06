package com.example.readingisgood.services;

import com.example.readingisgood.exceptions.BookAlreadyExistException;
import com.example.readingisgood.exceptions.BookNotFoundException;
import com.example.readingisgood.models.dtos.BookDto;
import com.example.readingisgood.models.requests.BookRequest;
import com.example.readingisgood.models.responses.ReadingIsGoodResponse;
import com.example.readingisgood.persistence.entitites.BookEntity;
import com.example.readingisgood.persistence.repositories.BookRepository;
import com.example.readingisgood.persistence.services.SequenceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import static com.example.readingisgood.persistence.entitites.BookEntity.BOOK_SEQUENCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private SequenceService sequenceService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private BookService bookService;

    @Test
    void it_should_add_new_book() {

        // given
        BookRequest bookRequest = BookRequest
                .builder()
                .name("bookName")
                .author("bookAuthor")
                .isbn("222222222222")
                .price(20.5)
                .stock(250)
                .build();

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(sequenceService.getNextSequence(BOOK_SEQUENCE));
        bookEntity.setName(bookRequest.getName());
        bookEntity.setAuthor(bookRequest.getAuthor());
        bookEntity.setIsbn(bookRequest.getIsbn());
        bookEntity.setPrice(bookRequest.getPrice());
        bookEntity.setStock(bookRequest.getStock());

        given(bookRepository.existsBookEntityByIsbn(bookRequest.getIsbn())).willReturn(false);

        // when
        bookService.addNewBook(bookRequest);

        // then
        ArgumentCaptor<BookEntity> bookEntityArgumentCaptor = ArgumentCaptor.forClass(BookEntity.class);
        verify(bookRepository, times(1)).save(bookEntityArgumentCaptor.capture());
        assertThat(bookEntityArgumentCaptor.getValue().getIsbn()).isEqualTo("222222222222");
    }

    @Test
    void it_should_throw_book_already_exists_exception() {

        // given
        BookRequest bookRequest = BookRequest
                .builder()
                .name("bookName")
                .author("bookAuthor")
                .isbn("222222222222")
                .price(20.5)
                .stock(250)
                .build();

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(sequenceService.getNextSequence(BOOK_SEQUENCE));
        bookEntity.setName(bookRequest.getName());
        bookEntity.setAuthor(bookRequest.getAuthor());
        bookEntity.setIsbn(bookRequest.getIsbn());
        bookEntity.setPrice(bookRequest.getPrice());
        bookEntity.setStock(bookRequest.getStock());

        given(bookRepository.existsBookEntityByIsbn(bookRequest.getIsbn())).willReturn(true);

        // when
        BookAlreadyExistException bookAlreadyExistException = assertThrows(BookAlreadyExistException.class,
                () -> bookService.addNewBook(bookRequest));

        // then
        assertThat(bookAlreadyExistException.getMessage()).isNotBlank();
    }

    @Test
    void it_should_update_stock() {

        // given
        int OLD_STOCK = 500;
        int NEW_STOCK = 2000;
        long ID = 2L;
        String ISBN = "222222222222";

        BookRequest bookRequest = BookRequest
                .builder()
                .isbn(ISBN)
                .stock(NEW_STOCK)
                .build();

        BookEntity oldBookEntity = new BookEntity();
        oldBookEntity.setId(ID);
        oldBookEntity.setIsbn(ISBN);
        oldBookEntity.setStock(OLD_STOCK);

        BookEntity newBookEntity = new BookEntity();
        newBookEntity.setId(ID);
        newBookEntity.setIsbn(ISBN);
        newBookEntity.setStock(NEW_STOCK);

        Optional<BookEntity> optionalBookEntity = Optional.of(oldBookEntity);

        given(bookRepository.findBookEntityByIsbn(bookRequest.getIsbn())).willReturn(optionalBookEntity);

        // when
        bookService.updateStock(bookRequest);

        // then
        ArgumentCaptor<BookEntity> bookEntityArgumentCaptor = ArgumentCaptor.forClass(BookEntity.class);
        verify(bookRepository, times(1)).save(bookEntityArgumentCaptor.capture());
        assertThat(bookEntityArgumentCaptor.getValue().getIsbn()).isEqualTo(ISBN);
        assertThat(bookEntityArgumentCaptor.getValue().getStock()).isEqualTo(NEW_STOCK);

    }

    @Test
    void it_should_throw_book_not_found_exception_on_updating_stock() {

        // given
        int OLD_STOCK = 500;
        int NEW_STOCK = 2000;
        long ID = 2L;
        String ISBN = "222222222222";

        BookRequest bookRequest = BookRequest
                .builder()
                .isbn(ISBN)
                .stock(NEW_STOCK)
                .build();

        BookEntity oldBookEntity = new BookEntity();
        oldBookEntity.setId(ID);
        oldBookEntity.setIsbn(ISBN);
        oldBookEntity.setStock(OLD_STOCK);

        Optional<BookEntity> optionalBookEntity = Optional.empty();

        given(bookRepository.findBookEntityByIsbn(bookRequest.getIsbn())).willReturn(optionalBookEntity);

        // when
        BookNotFoundException bookNotFoundException = assertThrows(BookNotFoundException.class,
                () -> bookService.updateStock(bookRequest));

        // then
        assertThat(bookNotFoundException.getMessage()).isNotBlank();

    }

    @Test
    void it_should_return_all_books() {
        // given
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(1L);
        bookEntity.setName("book1");
        bookEntity.setAuthor("author1");
        bookEntity.setIsbn("1111111111111");
        bookEntity.setPrice(10.0);
        bookEntity.setStock(100);

        BookEntity bookEntity2 = new BookEntity();
        bookEntity2.setId(2L);
        bookEntity2.setName("book2");
        bookEntity2.setAuthor("author2");
        bookEntity2.setIsbn("222222222222");
        bookEntity2.setPrice(20.0);
        bookEntity2.setStock(200);

        BookDto bookDto = BookDto
                .builder()
                .id(1L)
                .name("book1")
                .author("author1")
                .isbn("1111111111111")
                .stock(100)
                .build();

        BookDto bookDto2 = BookDto
                .builder()
                .id(2L)
                .name("book2")
                .author("author2")
                .isbn("2222222222222")
                .stock(200)
                .build();

        BookDto bookDto3 = BookDto
                .builder()
                .id(3L)
                .name("book3")
                .author("author3")
                .isbn("3333333333333")
                .stock(300)
                .build();

        List<BookEntity> bookEntityList = List.of(bookEntity, bookEntity2);
        List<BookDto> bookDtoList = List.of(bookDto, bookDto2);

        Type listType = new TypeToken<List<BookDto>>() {
        }.getType();

        given(bookRepository.findAll()).willReturn(bookEntityList);
        given(modelMapper.map(bookEntityList,listType)).willReturn(bookDtoList);

        // when
        ReadingIsGoodResponse<List<BookDto>> response = bookService.getAllBooks();

        // then
        assertThat(response.getData()).hasSize(2);
        assertThat(response.getData()).containsExactlyInAnyOrder(bookDto, bookDto2);
        assertThat(response.getData()).doesNotContain(bookDto3);

    }

    @Test
    void it_should_get_given_isbn_book(){
        // given
        String ISBN = "999999999999";

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(9L);
        bookEntity.setName("book9");
        bookEntity.setAuthor("author9");
        bookEntity.setIsbn(ISBN);
        bookEntity.setPrice(90.0);
        bookEntity.setStock(900);

        Optional<BookEntity> optionalBookEntity = Optional.of(bookEntity);

        given(bookRepository.findBookEntityByIsbn(ISBN)).willReturn(optionalBookEntity);

        // when
        BookEntity actualBookEntity = bookService.getBook(ISBN);

        // then
        assertThat(actualBookEntity.getIsbn()).isEqualTo(ISBN);
        assertThat(actualBookEntity.getId()).isEqualTo(9L);
        assertThat(actualBookEntity.getPrice()).isEqualTo(90.0);
        assertThat(actualBookEntity.getStock()).isEqualTo(900);
        assertThat(actualBookEntity.getName()).isEqualTo("book9");
        assertThat(actualBookEntity.getAuthor()).isEqualTo("author9");

    }

    @Test
    void it_should_throw_book_not_found_exception_while_getting_a_book(){
        // given
        String ISBN = "999999999999";

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(9L);
        bookEntity.setName("book9");
        bookEntity.setAuthor("author9");
        bookEntity.setIsbn(ISBN);
        bookEntity.setPrice(90.0);
        bookEntity.setStock(900);

        Optional<BookEntity> optionalBookEntity = Optional.empty();

        given(bookRepository.findBookEntityByIsbn(ISBN)).willReturn(optionalBookEntity);

        // when
        BookNotFoundException bookNotFoundException = assertThrows(BookNotFoundException.class ,
                () -> bookService.getBook(ISBN));

        // then
        assertThat(bookNotFoundException.getMessage()).isNotBlank();


    }
}