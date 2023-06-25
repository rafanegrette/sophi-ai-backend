package com.rafanegrette.books.services.audioprocess;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AudioParams {
    public static final String DELIMITER_FILE = "/";
    
    private final String idBook;
    private final Integer idChapter;
    private final Integer idPage;
    private final Integer idParagraph;
    private final Integer idSentence;
    public String getPath() {
        return String.join(DELIMITER_FILE, 
                            idBook, 
                            idChapter.toString(), 
                            idPage.toString(), 
                            idParagraph.toString(),
                            idSentence.toString());
    }
}