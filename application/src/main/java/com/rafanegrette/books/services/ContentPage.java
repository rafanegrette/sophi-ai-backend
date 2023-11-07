package com.rafanegrette.books.services;

import java.io.IOException;
import java.util.List;

import com.rafanegrette.books.model.formats.ParagraphFormats;
import org.apache.pdfbox.pdmodel.PDDocument;

import com.rafanegrette.books.model.Page;
import com.rafanegrette.books.model.Sentence;

public interface ContentPage {

	Page getContentPage(PDDocument document, int noPage,
						ParagraphFormats paragraphFormats) throws IOException;
	
	List<Sentence> createSentencesFromString(String sentencesStr, String[] splitArrayCharacter);
	
	String removedChar(String sentence, int i);
	
	List<Sentence> formatSentences(List<Sentence> sentences);
}
