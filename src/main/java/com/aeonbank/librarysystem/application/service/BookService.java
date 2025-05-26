package com.aeonbank.librarysystem.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.aeonbank.librarysystem.domain.model.Book;
import com.aeonbank.librarysystem.domain.model.LoanStatus;
import com.aeonbank.librarysystem.domain.repository.BookRepository;
import com.aeonbank.librarysystem.domain.repository.LoanRepository;
import com.aeonbank.librarysystem.domain.repository.specification.BookSpecifications;
import com.aeonbank.librarysystem.exception.InvalidBookException;
import com.aeonbank.librarysystem.utils.IsbnUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {

	private final BookRepository bookRepository;
	private final LoanRepository loanRepository;

	public Book registerBook(String isbn, String title, String author) {

		String normalizedIsbn = IsbnUtils.normalize(isbn);
		Book existingBook = bookRepository.findFirstByIsbn(normalizedIsbn);
		if (existingBook != null) {
			if (!existingBook.getTitle().equalsIgnoreCase(title)
					|| !existingBook.getAuthor().equalsIgnoreCase(author)) {
				throw new InvalidBookException(normalizedIsbn, title, author);
			}
		}
		return bookRepository.save(new Book(normalizedIsbn, title, author));
	}

	public List<Book> getAllBooks() {

		List<Book> allBooks = bookRepository.findAll();
		allBooks.forEach(book -> {
			boolean isOnLoan = loanRepository.findByBookIdAndReturnedDateIsNull(book.getId()).isPresent();
			book.setAvailable(!isOnLoan); // true = available, false = on loan
		});
		return allBooks;
	}

	public Page<Book> getAllBooks(String isbn, String title, String author, LoanStatus loanStatus, Pageable pageable) {

		String normalizedIsbn = IsbnUtils.normalize(isbn);
		Specification<Book> spec = BookSpecifications.withFilters(normalizedIsbn, title, author, loanStatus);
		Page<Book> books = bookRepository.findAll(spec, pageable);
		books.forEach(book -> {
			boolean isOnLoan = loanRepository.findByBookIdAndReturnedDateIsNull(book.getId()).isPresent();
			book.setAvailable(!isOnLoan); // true = available, false = on loan
		});
		return books;
	}
}