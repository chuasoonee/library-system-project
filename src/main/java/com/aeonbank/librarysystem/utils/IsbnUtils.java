package com.aeonbank.librarysystem.utils;

/**
 * Utility class for ISBN validation
 */
public class IsbnUtils {

	/**
	 * Removes hyphens and whitespace. Converts to uppercase for 'X' compatibility.
	 */
	public static String normalize(String isbn) {
		if (isbn == null)
			return null;
		return isbn.replaceAll("[-\\s]", "").toUpperCase();
	}

	public static boolean isValidIsbn(String rawIsbn) {
		String isbn = normalize(rawIsbn);
		return isValidIsbn10(isbn) || isValidIsbn13(isbn);
	}

	public static boolean isValidIsbn10(String isbn) {
		if (isbn == null || isbn.length() != 10 || !isbn.matches("\\d{9}[\\dX]")) {
			return false;
		}

		int sum = 0;
		for (int i = 0; i < 9; i++) {
			sum += (isbn.charAt(i) - '0') * (10 - i);
		}

		char lastChar = isbn.charAt(9);
		sum += (lastChar == 'X') ? 10 : (lastChar - '0');

		return sum % 11 == 0;
	}

	public static boolean isValidIsbn13(String isbn) {
		if (isbn == null || isbn.length() != 13 || !isbn.matches("\\d{13}")) {
			return false;
		}

		int sum = 0;
		for (int i = 0; i < 12; i++) {
			int digit = isbn.charAt(i) - '0';
			sum += (i % 2 == 0) ? digit : digit * 3;
		}

		int checkDigit = (10 - (sum % 10)) % 10;
		return checkDigit == (isbn.charAt(12) - '0');
	}
}