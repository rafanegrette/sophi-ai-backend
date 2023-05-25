package com.rafanegrette.books.model;

public class BookNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1615456443531107753L;

	public BookNotFoundException() {
		super("BOOK NOT FOUND");
	}

	
}
