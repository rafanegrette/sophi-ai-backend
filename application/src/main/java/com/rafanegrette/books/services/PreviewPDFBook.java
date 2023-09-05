package com.rafanegrette.books.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.ChapterTitleType;
import com.rafanegrette.books.model.FirstPageOffset;
import com.rafanegrette.books.model.FormParameter;
import com.rafanegrette.books.model.UploadForm;
import com.rafanegrette.books.model.Paragraph.ParagraphSeparator;

@Service
public class PreviewPDFBook implements PreviewBookService{

	private LoadPDFService pdfService;
	
	public PreviewPDFBook(LoadPDFService loadPdfService) {
		this.pdfService = loadPdfService;
	}
	
    @Override
    public Book previewPDF(UploadForm uploadForm) throws IOException {
        ParagraphSeparator separator = uploadForm.paragraphSeparator().equals("ONE") ? ParagraphSeparator.ONE_JUMP
                : ParagraphSeparator.TWO_JUMP;
        ChapterTitleType chapterTitleType = uploadForm.bookMarkType() == null ||
        		uploadForm.bookMarkType().equals(ChapterTitleType.BOOKMARK.toString())
                ? ChapterTitleType.BOOKMARK
                : ChapterTitleType.CONTENT;
        FirstPageOffset firstPageOffset = uploadForm.firstPageOffset() == null ||
        		uploadForm.firstPageOffset().equals(1) ? 
        				FirstPageOffset.ONE 
        				: FirstPageOffset.TWO;
        var formParameter = new FormParameter(
        		uploadForm.bookLabel(),        		
        		separator, 
        		chapterTitleType, 
        		firstPageOffset, 
        		uploadForm.fixTitleHP1());
        byte[] byteFile = Base64Utils.decodeFromString(uploadForm.file());
        
        return pdfService.getBookFromByteFile(byteFile, formParameter);
	}

	
}
