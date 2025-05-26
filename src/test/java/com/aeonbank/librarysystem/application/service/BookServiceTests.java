package com.aeonbank.librarysystem.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.aeonbank.librarysystem.domain.model.Book;
import com.aeonbank.librarysystem.domain.repository.BookRepository;
import com.aeonbank.librarysystem.domain.repository.LoanRepository;
import com.aeonbank.librarysystem.exception.InvalidBookException;
import com.aeonbank.librarysystem.utils.IsbnUtils;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

	@Mock
	private BookRepository bookRepository;

	@Mock
	private LoanRepository loanRepository;

	@InjectMocks
	private BookService bookService;

	private Book book;

	@BeforeEach
	public void setup() {
		book = new Book("9781443456623", "The 5 AM Club", "Robin Sharma");
	}

	@Test
	public void BookService_RegisterBook_WhenBookDoesNotExist_SaveBook() {
		String normalizedIsbn = IsbnUtils.normalize(book.getIsbn());
		when(bookRepository.findFirstByIsbn(normalizedIsbn)).thenReturn(null);
		when(bookRepository.save(any(Book.class))).thenReturn(book);

		Book savedBook = bookService.registerBook(book.getIsbn(), book.getTitle(), book.getAuthor());

		assertThat(savedBook).isNotNull();
		assertThat(savedBook.getIsbn()).isEqualTo(normalizedIsbn);
		verify(bookRepository, times(1)).save(any(Book.class));
	}

	@Test
	public void BookService_RegisterBook_WhenBookExistsWithDifferentDetails_ThrowInvalidBookException() {
		Book existingBook = new Book("9781443456623", "Different Title", "Different Author");
		when(bookRepository.findFirstByIsbn(book.getIsbn())).thenReturn(existingBook);

		assertThatThrownBy(() -> bookService.registerBook(book.getIsbn(), book.getTitle(), book.getAuthor()))
				.isInstanceOf(InvalidBookException.class).hasMessageContaining(book.getIsbn());

		verify(bookRepository, never()).save(any(Book.class));
	}

	@Test
	public void BookService_RegisterBook_WhenBookExistsWithSameDetails_NotThrowException() {
		when(bookRepository.findFirstByIsbn(book.getIsbn())).thenReturn(book);
		when(bookRepository.save(any(Book.class))).thenReturn(book);

		Book savedBook = bookService.registerBook(book.getIsbn(), book.getTitle(), book.getAuthor());

		assertThat(savedBook).isNotNull();
		assertThat(savedBook.getIsbn()).isEqualTo(book.getIsbn());
		verify(bookRepository, times(1)).save(any(Book.class));
	}

	@Test
	public void BookService_GetAllBooks_ReturnAllBooks() {
		when(bookRepository.findAll())
				.thenReturn(List.of(book));

		List<Book> allBooks = bookService.getAllBooks();

		assertThat(allBooks).isNotNull();
		assertThat(allBooks.size()).isEqualTo(1);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void BookService_GetBooks_ReturnFilteredBooks() {
		Pageable pageable = PageRequest.of(0, 10);

		when(bookRepository.findAll(any(Specification.class), any(Pageable.class)))
				.thenReturn(new PageImpl<>(List.of(book)));

		Page<Book> result = bookService.getAllBooks(book.getIsbn(), book.getTitle(), book.getAuthor(), null, pageable);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).containsExactly(book);
	}
}