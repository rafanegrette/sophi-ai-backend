package com.rafanegrette.books.services;

import java.io.IOException;
import java.util.List;

import com.rafanegrette.books.model.formats.ParagraphFormats;
import com.rafanegrette.books.services.pdf.preview.SentenceLength;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.rafanegrette.books.model.Page;
import com.rafanegrette.books.model.Sentence;

public interface ContentPage {

	Page getContentPage(PDDocument document, int noPage,
						ParagraphFormats paragraphFormats) throws IOException;

}
