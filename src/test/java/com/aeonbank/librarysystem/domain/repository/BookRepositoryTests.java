package com.aeonbank.librarysystem.domain.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.aeonbank.librarysystem.domain.model.Book;
import com.aeonbank.librarysystem.domain.repository.specification.BookSpecifications;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BookRepositoryTests {

	@Autowired
	private BookRepository bookRepository;

	private Book book1;
	private Book book2;
	private Book book3;

	@BeforeEach
	public void setup(TestInfo testInfo) {
		
		if (testInfo.getTags().contains("skipBeforeEach")) {
            return;
        }
		book1 = new Book("978-1443456623", "The 5 AM Club", "Robin Sharma");
		book2 = new Book("9781443456623", "The 5 AM Club", "Robin Sharma");
		book3 = new Book("1936594234", "The Power of Your Subconcious Mind", "Joseph Murphy");
		bookRepository.save(book1);
		bookRepository.save(book2);
		bookRepository.save(book3);
	}

	@Test
	@Tag("skipBeforeEach")
	public void BookRepository_FindFirstByIsbn_ReturnNull() {
		String isbn = "9781443456623"; // Arrange
		Book noBook = bookRepository.findFirstByIsbn(isbn); // Act
		Assertions.assertThat(noBook).isNull(); // Assert
	}

	@Test
	public void BookRepository_FindFirstByIsbn_ReturnOneBook() {
		Book returnBook = bookRepository.findFirstByIsbn(book1.getIsbn());
		Assertions.assertThat(returnBook).isNotNull();
		Assertions.assertThat(returnBook.getId()).isGreaterThan(0);
	}
	
	@Test
	public void BookRepository_FindAll_ReturnBookInList() {
		List<Book> returnBook = bookRepository.findAll();
		Assertions.assertThat(returnBook).isNotNull();
		Assertions.assertThat(returnBook.size()).isEqualTo(3);
	}

	@Test
	public void BookRepository_FindAllWithSpecificationAndPageable_ReturnAllThreeBooks() {
		Specification<Book> spec = BookSpecifications.withFilters(null, null, null, null);
		Pageable pageable = PageRequest.of(0, 10);
		Page<Book> returnBooks = bookRepository.findAll(spec, pageable);
		Assertions.assertThat(returnBooks).isNotNull();
		Assertions.assertThat(returnBooks.getTotalElements()).isEqualTo(3);
		Assertions.assertThat(returnBooks.getTotalPages()).isEqualTo(1);
		Assertions.assertThat(returnBooks.getContent()).hasSize(3).extracting(Book::getIsbn)
				.containsExactlyInAnyOrder(book1.getIsbn(), book2.getIsbn(), book3.getIsbn());
	}

	@Test
	public void BookRepository_FindAllWithSpecificationAndPageableFilterByIsbn_ReturnMoreThanOneBook() {
		Specification<Book> spec = BookSpecifications.withFilters(book3.getIsbn(), null, null, null);
		Pageable pageable = PageRequest.of(0, 10);
		Page<Book> returnBooks = bookRepository.findAll(spec, pageable);
		Assertions.assertThat(returnBooks).isNotNull();
		Assertions.assertThat(returnBooks.getTotalElements()).isEqualTo(1);
		Assertions.assertThat(returnBooks.getTotalPages()).isEqualTo(1);
		Assertions.assertThat(returnBooks.getContent()).hasSize(1).extracting(Book::getIsbn)
				.containsExactlyInAnyOrder(book3.getIsbn());
	}

	@Test
	public void BookRepository_FindAllWithSpecificationAndPageableFilterByTitle_ReturnMoreThanOneBook() {
		Specification<Book> spec = BookSpecifications.withFilters(null, book2.getTitle(), null, null);
		Pageable pageable = PageRequest.of(0, 10);
		Page<Book> returnBooks = bookRepository.findAll(spec, pageable);
		Assertions.assertThat(returnBooks).isNotNull();
		Assertions.assertThat(returnBooks.getTotalElements()).isEqualTo(2);
		Assertions.assertThat(returnBooks.getTotalPages()).isEqualTo(1);
		Assertions.assertThat(returnBooks.getContent()).hasSize(2).extracting(Book::getTitle)
				.containsExactlyInAnyOrder(book1.getTitle(), book2.getTitle());
	}

	@Test
	public void BookRepository_FindAllWithSpecificationAndPageableFilterByAuthor_ReturnMoreThanOneBook() {
		Specification<Book> spec = BookSpecifications.withFilters(null, null, book1.getAuthor(), null);
		Pageable pageable = PageRequest.of(0, 10);
		Page<Book> returnBooks = bookRepository.findAll(spec, pageable);
		Assertions.assertThat(returnBooks).isNotNull();
		Assertions.assertThat(returnBooks.getTotalElements()).isEqualTo(2);
		Assertions.assertThat(returnBooks.getTotalPages()).isEqualTo(1);
		Assertions.assertThat(returnBooks.getContent()).hasSize(2).extracting(Book::getAuthor)
				.containsExactlyInAnyOrder(book1.getAuthor(), book2.getAuthor());
	}

	@Test
	public void BookRepository_FindAllWithSpecificationAndPageableFilterByIsbnAndAuthor_ReturnMoreThanOneBook() {
		Specification<Book> spec = BookSpecifications.withFilters(book1.getIsbn(), null, book1.getAuthor(), null);
		Pageable pageable = PageRequest.of(0, 10);
		Page<Book> returnBooks = bookRepository.findAll(spec, pageable);
		Assertions.assertThat(returnBooks).isNotNull();
		Assertions.assertThat(returnBooks.getTotalElements()).isEqualTo(2);
		Assertions.assertThat(returnBooks.getTotalPages()).isEqualTo(1);
		Assertions.assertThat(returnBooks.getContent()).hasSize(2).extracting(Book::getIsbn, Book::getAuthor)
				.containsExactlyInAnyOrder(
						Tuple.tuple(book1.getIsbn(), book1.getAuthor()),
						Tuple.tuple(book2.getIsbn(), book2.getAuthor()));

	}

	@Test
	public void BookRepository_FindAllWithSpecificationAndPageableFilterByIsbnAndTitle_ReturnMoreThanOneBook() {
		Specification<Book> spec = BookSpecifications.withFilters(book1.getIsbn(), book1.getTitle(), null, null);
		Pageable pageable = PageRequest.of(0, 10);
		Page<Book> returnBooks = bookRepository.findAll(spec, pageable);
		Assertions.assertThat(returnBooks).isNotNull();
		Assertions.assertThat(returnBooks.getTotalElements()).isEqualTo(2);
		Assertions.assertThat(returnBooks.getTotalPages()).isEqualTo(1);
		Assertions.assertThat(returnBooks.getContent()).hasSize(2).extracting(Book::getIsbn, Book::getTitle)
		.containsExactlyInAnyOrder(
				Tuple.tuple(book1.getIsbn(), book1.getTitle()),
				Tuple.tuple(book2.getIsbn(), book2.getTitle()));
	}

	@Test
	public void BookRepository_FindAllWithSpecificationAndPageableFilterByIsbnAndTitleAndAuthor_ReturnMoreThanOneBook() {
		Specification<Book> spec = BookSpecifications.withFilters(book1.getIsbn(), book1.getTitle(), book1.getAuthor(), null);
		Pageable pageable = PageRequest.of(0, 10);
		Page<Book> returnBooks = bookRepository.findAll(spec, pageable);
		Assertions.assertThat(returnBooks).isNotNull();
		Assertions.assertThat(returnBooks.getTotalElements()).isEqualTo(2);
		Assertions.assertThat(returnBooks.getTotalPages()).isEqualTo(1);
		Assertions.assertThat(returnBooks.getContent()).hasSize(2).extracting(Book::getIsbn, Book::getTitle, Book::getAuthor)
		.containsExactlyInAnyOrder(
				Tuple.tuple(book1.getIsbn(), book1.getTitle(), book1.getAuthor()),
				Tuple.tuple(book2.getIsbn(), book2.getTitle(), book2.getAuthor()));
	}
}
