package com.rafanegrette.books.services.pdf.preview;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.rafanegrette.books.model.formats.ParagraphFormats;
import com.rafanegrette.books.services.ContentPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.model.Page;
import com.rafanegrette.books.model.Paragraph;
import com.rafanegrette.books.model.Sentence;
import com.rafanegrette.books.string.formats.StringFormatFunctions;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessContentPagePDF implements ContentPage {


    private final ProcessParagraphPDF processParagraphPDF;


    /***
     * TC2 FirstPage, This Method include a Function to extract title mixup into sentences like:
     * T HE WORST BIRTHDAY for the first time
     * @param document with title in uppercase
     * @return clean sentence
     */
    public Page getContentPageFirstPage(PDDocument document, int noPage,
                                        ParagraphFormats paragraphFormats) throws IOException{
        Function<String,  String> fixTitleMixupFirstSentence = sentence -> {
            int idxTitleEnd = 0;
            for (char currentChar : sentence.toCharArray())
            {
                if (Character.isLowerCase(currentChar))
                {
                	try {
                    return sentence.substring(1, idxTitleEnd) + ".\n"
                    		+ sentence.charAt(0) 
                    		+ sentence.substring(idxTitleEnd);
                	} catch(Exception e) {
                		System.out.println(e.getMessage());
                	}
                }
                idxTitleEnd++;    
            }
            return "";
        };
        return getContentPage(document, noPage, paragraphFormats, fixTitleMixupFirstSentence);
    }
    
    public Page getContentPage(PDDocument document, int noPage,
                               ParagraphFormats paragraphFormats) throws IOException {

        Function<String, String> fixTitleMixupFirstSentence = sentence -> sentence;
        return getContentPage(document, noPage, paragraphFormats, fixTitleMixupFirstSentence);
    }


    public Page getContentPage(PDDocument document, int noPage,
                               ParagraphFormats paragraphFormats,  Function<String, String> fixTitleMixupFirstSentenceHP2) throws IOException{

        String rawPageText = extractRawText(document, noPage, paragraphFormats, fixTitleMixupFirstSentenceHP2);


        return new Page(noPage, processParagraphPDF.getParagraphs(rawPageText, paragraphFormats));
    }

    String extractRawText(PDDocument document, int noPage, ParagraphFormats paragraphFormats, Function<String, String> fixTitleMixupFirstSentenceHP2) throws IOException {
        PDFTextStripper pdfStripper = new PDFTextStripper();
        pdfStripper.setStartPage(noPage);
        pdfStripper.setEndPage(noPage);
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        pdfStripper.setAddMoreFormatting(paragraphFormats.applyExtraFormat());
        pdfStripper.setSuppressDuplicateOverlappingText(true);
        pdfStripper.setDropThreshold(paragraphFormats.paragraphThreshold());
        pdfStripper.writeText(document, writer);

        return fixTitleMixupFirstSentenceHP2.apply(out.toString());
    }

}
