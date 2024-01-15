package com.rafanegrette.books.services.pdf.preview;

import java.io.IOException;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.UploadForm;

public interface PreviewBookService {

    Book previewPDF(UploadForm uploadForm)
            throws IOException;
}
