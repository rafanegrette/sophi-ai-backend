package com.rafanegrette.books.services;

import java.io.IOException;
import java.util.Base64;

import com.rafanegrette.books.model.*;
import com.rafanegrette.books.model.formats.ParagraphFormats;
import com.rafanegrette.books.model.formats.ParagraphSeparator;
import com.rafanegrette.books.model.formats.ParagraphThreshold;
import org.springframework.stereotype.Service;

@Service
public class PreviewPDFBook implements PreviewBookService {

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
		var formParameter = getFormParameters(uploadForm, separator, chapterTitleType);

		byte[] byteFile = Base64.getDecoder().decode(uploadForm.file());

        return pdfService.getBookFromByteFile(byteFile, formParameter);
    }

	private static FormParameter getFormParameters(UploadForm uploadForm, ParagraphSeparator separator, ChapterTitleType chapterTitleType) {
		FirstPageOffset firstPageOffset = uploadForm.firstPageOffset() == null ||
				uploadForm.firstPageOffset().equals(1) ?
				FirstPageOffset.ONE
				: FirstPageOffset.TWO;
		var paragraphThreshold = uploadForm.paragraphThreshold() == 300 ? ParagraphThreshold.THREE
				: ParagraphThreshold.DEFAULT;
		var formParameter = new FormParameter(
				uploadForm.bookLabel(),
				new ParagraphFormats(paragraphThreshold, uploadForm.extraFormat(), separator),
				chapterTitleType,
				firstPageOffset,
				uploadForm.fixTitleHP1());
		return formParameter;
	}


}
