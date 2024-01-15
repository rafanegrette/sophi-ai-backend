package com.rafanegrette.books.services.pdf.preview;

import java.io.File;
import java.io.IOException;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.FormParameter;

public interface LoadPDFService {

    Book getBookFromByteFile(byte[] bookFile, FormParameter formParameter) throws IOException;

    File getCoverImage(byte[] byteFile) throws IOException;
}
