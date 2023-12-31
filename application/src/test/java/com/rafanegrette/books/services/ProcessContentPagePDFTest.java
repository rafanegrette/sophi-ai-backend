package com.rafanegrette.books.services;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import com.rafanegrette.books.model.formats.ParagraphFormats;
import com.rafanegrette.books.model.formats.ParagraphSeparator;
import com.rafanegrette.books.model.formats.ParagraphThreshold;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rafanegrette.books.model.Page;
import com.rafanegrette.books.model.Sentence;

@SpringBootTest(classes = {ContentPage.class, ProcessContentPagePDF.class})
class ProcessContentPagePDFTest {

    @Autowired
    ProcessContentPagePDF processContentPage;

    static PDDocument document;
    static PDDocument documentTC2;

    private final String textTC3 = """
                        
                        
            5
                        
            Joseph Jacobs
                        
            ENGLISH
            FAIRY TALES
                        
            COLLECTED BY
                        
            JOSEPH JACOBS
            HOW TO GET INTO THIS BOOK.
                        
                     Knock at the Knocker on the Door,
                     Pull the Bell at the side,
                        
            Then, if you are very quiet, you will hear a teeny tiny voice say
            through the grating “Take down the Key.” This you will find at
            the back: you cannot mistake it, for it has J. J. in the wards. Put
            the Key in the Keyhole, which it fits exactly, unlock the door and
            WALK IN.
                        
            TO MY DEAR LITTLE MAY
                        
            Preface
                        
            WHO SAYS that English folk have no fairy-tales of their own?
            The present volume contains only a selection out of some
            140, of which I have found traces in this country. It is prob-
            able that many more exist.
                        
            A quarter of the tales in this volume, have been collected
            during the last ten years or so, and some of them have not
            been hitherto published. Up to 1870 it was equally said of
            France and of Italy, that they possessed no folk-tales. Yet,
            within fifteen years from that date, over 1000 tales had been
            collected in each country. I am hoping that the present vol-
            ume may lead to equal activity in this country, and would
            earnestly beg any reader of this book who knows of similar
            tales, to communicate them, written down as they are told,
            to me, care of Mr. Nutt. The only reason, I imagine, why
            such tales have not hitherto been brought to light, is the
            lamentable gap between the governing and recording classes
            and the dumb working classes of this country—dumb to
            others but eloquent among themselves. It would be no un-
            patriotic task to help to bridge over this gulf, by giving a
                        
                        
                        
            """;

    @BeforeAll
    static void beforeAll() throws IOException {
    	BiConsumer<PDDocument, PDPage> pageContentFunction = (doc, page) -> setPageContent(doc, page);
    	BiConsumer<PDDocument, PDPage> pageContentFunctionTC2 = (doc, page) -> setPageContentTC2(doc, page);
    	
        document = getDocumentWithParagraph(pageContentFunction);
        documentTC2 = getDocumentWithParagraph(pageContentFunctionTC2);
    }

    @Test
    void testGetParagraphs() {
        var paragraphFormatsExtra = new ParagraphFormats(ParagraphThreshold.THREE, true, ParagraphSeparator.TWO_JUMP);
        var paragraphs =processContentPage.getParagraphs(textTC3, paragraphFormatsExtra);

        assertEquals(11, paragraphs.size());
    }
    @Test
    void testSplitSentencesRemoveSecondCharInFirstPageOfChapters() {
        String sentence = "This is not ok";
        List<String> sentencesExpected = new ArrayList<>();
        sentencesExpected.add("Tis is not ok");
        String sentencesReturned = processContentPage.removedChar(sentence, 1) ;
        assertTrue(sentencesReturned.equals(sentencesExpected.get(0)));
    }
    
    @Test
    void testSplitSentencesHyphen() {
        String sentences = "something peculiar — a cat reading a map. For a second, Mr. Dursley didn’t\n"
                + "realize what he had seen — then he jerked his head around to look again. There\n"
                + "was a tabby cat standing on the corner of Privet Drive, but there wasn’t a map in\n"
                + "sight. What could he have been thinking of? It must have been a trick of the\n"
                + "light. Mr. Dursley blinked and stared at the cat. It stared back. As Mr. Dursley\n"
                + "drove around the corner and up the road, he watched the cat in his mirror. It was\n"
                + "now reading the sign that said Privet Drive — no, looking at the sign; cats\n"
                + "couldn’t read maps or signs. Mr. Dursley gave himself a little shake and put the\n"
                + "cat out of his mind. As he drove toward town he thought of nothing except a\n"
                + "large order of drills he was hoping to get that day.";
        List<Sentence> sentencesExpected = new ArrayList<>();
        sentencesExpected.add(new Sentence(0, "something peculiar —"));
        sentencesExpected.add(new Sentence(1, "a cat reading a map"));
        sentencesExpected.add(new Sentence(2, "For a second, Mr. Dursley didn’t realize what he had seen"));
        sentencesExpected.add(new Sentence(3, "then he jerked his head around to look again"));
        List<Sentence> sentencesReturned = processContentPage.createSentencesFromString(sentences, new String[] {"—"}) ;
        assertEquals(sentencesExpected.get(0), sentencesReturned.get(0));
    }
    
    @Test
    void testSplitLongSentencesWithNotSplitCharactersButComma() {
        String paragraph ="He missed the castle, with its secret passageways and ghosts, his classes ( though \n"
                + "perhaps not Snape, the Potions master) , the mail arriving by owl, eating banquets in \n"
                + "the Great Hall, sleeping in his four-poster bed in the tower dormitory, visiting the \n"
                + "gamekeeper, Hagrid, in his cabin next to the Forbidden Forest in the grounds, and, \n"
                + "especially, Quidditch, the most popular sport in the Wizarding world ( six tall goalposts, "
                + "four flying balls, and fourteen players on broomsticks)";
        List<Sentence> expectedSentences = new ArrayList<>();
        expectedSentences.add(new Sentence(0, "He missed the castle, with its secret passageways and ghosts, his classes ("));
        expectedSentences.add(new Sentence(1, "though \nperhaps not Snape, the Potions master)"));
        expectedSentences.add(new Sentence(2, ", the mail arriving by owl, eating banquets in \n"
                + "the Great Hall, sleeping in his four-poster bed in the tower dormitory,"));
        expectedSentences.add(new Sentence(3, "visiting the \n"
                + "gamekeeper, Hagrid, in his cabin next to the Forbidden Forest in the grounds, and, \n"
                + "especially, Quidditch,"));
        expectedSentences.add(new Sentence(4, "the most popular sport in the Wizarding world ("));
        expectedSentences.add(new Sentence(5, "six tall goalposts, four flying balls, and fourteen players on broomsticks)"));
        List<Sentence> returnedSentences = processContentPage.createSentencesFromString(paragraph, new String[] {".",";", "?", "(", ")"});
        assertEquals(expectedSentences.get(0), returnedSentences.get(0));
        assertEquals(expectedSentences.get(1), returnedSentences.get(1));
        assertEquals(expectedSentences.get(2), returnedSentences.get(2));
        assertEquals(expectedSentences.get(3), returnedSentences.get(3));
        assertEquals(expectedSentences.get(4), returnedSentences.get(4));
    }
    
    @Test
    void testSplitSentencesByDot() {
        String sentences = "a cat reading a map. For a second, Mr. Dursley didn’t realize what he had seen";
        List<Sentence> sentencesExpected = new ArrayList<>();
        sentencesExpected.add(new Sentence(0, "a cat reading a map."));
        sentencesExpected.add(new Sentence(1, "For a second, Mr. Dursley didn’t realize what he had seen"));
        List<Sentence> sentencesReturned = processContentPage.createSentencesFromString(sentences, new String[] {".", "✡"});
        assertEquals(sentencesExpected.get(0), sentencesReturned.get(0));
        assertEquals(sentencesExpected.get(1), sentencesReturned.get(1));
    }
    
    @Test
    void testIgnoreSentenceWithoutAlphaCharacters() {
        String sentences = "“Next to the tall kid with the red hair.”";
        List<Sentence> sentencesExpected = new ArrayList<>();
        sentencesExpected.add(new Sentence(0, "“Next to the tall kid with the red hair.”"));
        List<Sentence> sentencesReturned = processContentPage.createSentencesFromString(sentences, new String[] {".", "—",";","?", ":", "(", ")", "✡"});
        assertEquals(1, sentencesReturned.size());
        assertEquals(sentencesExpected.get(0), sentencesReturned.get(0));
    }
    /**
     * 
     * Probably the method formatSentences will be deprecated.
    @Test
    void testFormatSentence() {
        List<String> sentences = List.of("a cat reading a map. For a second, Mr. Dursley didn’t\n"
                + "realize what he had seen");
        List<String> sentencesExpected = new ArrayList<>();
        sentencesExpected.add("a cat reading a map. For a second, Mr. Dursley didn't\nrealize what he had seen");
        List<String> sentencesReturned = processContentPage.formatSentences(sentences);
        assertEquals(sentencesExpected.get(0), sentencesReturned.get(0));
    }
    */
    @Test
    void testSplitSentencesNotRemoveSecondCharInSecondPageOfChapters() {
        String sentences = "This is not ok";
        List<Sentence> sentencesExpected = new ArrayList<>();
        sentencesExpected.add(new Sentence(0, "This is not ok"));
        List<Sentence> sentencesReturned = processContentPage.createSentencesFromString(sentences, new String[]{"."}) ;
        assertTrue(sentencesReturned.get(0).text().startsWith(sentencesExpected.get(0).text()));
    }
    
    @Test
    void testGetPageWithSeparator() throws IOException {
        var paragraphFormats = new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP);
        Page pageExpected = processContentPage.getContentPage(document, 6, paragraphFormats);
        assertEquals("Page Title", pageExpected.paragraphs().get(0).sentences().get(0).text());
    }

    @Test
    void testGetPageThreshold() throws IOException {
        // Given
        var paragraphFormatsDefault = new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.TWO_JUMP);
        var paragraphFormatsExtra = new ParagraphFormats(ParagraphThreshold.THREE, true, ParagraphSeparator.TWO_JUMP);

        // When
        Page pageDefaultFormat = processContentPage.getContentPage(document, 6, paragraphFormatsDefault);
        Page pageExtraFormat = processContentPage.getContentPage(document, 6, paragraphFormatsExtra);

        // Then
        assertEquals(4, pageExtraFormat.paragraphs().size());
        assertEquals(3, pageDefaultFormat.paragraphs().size());
    }

    @Test
    void testGetContentPageRightParagraphNumbeersTC2() throws IOException {
        int paragraphsNoExpected = 2;
        var paragraphFormats = new ParagraphFormats(ParagraphThreshold.DEFAULT, false, ParagraphSeparator.ONE_JUMP);

        Page pageExpected = processContentPage.getContentPageFirstPage(documentTC2, 9, paragraphFormats);
        assertEquals(paragraphsNoExpected, pageExpected.paragraphs().size());
    }
    
    @Test
    void testSplitLongSentencesTC1() throws IOException {
        String sentences = "Could there really be piles of wizard gold buried miles "
                + "beneath them? Were there really shops that sold "
                + "spell books and broomsticks? Might this not all be some huge "
                + "joke that the Dursleys had cooked up? If Harry hadn't known "
                + "that the Dursleys had no sense of humor, he might have "
                + "thought so; yet somehow, even though everything Hagrid had "
                + "told him so far was unbelievable, Harry couldn't help trusting him.";
        List<Sentence> expectedSentences = new ArrayList<>();
        expectedSentences.add(new Sentence(0, "Could there really be piles of wizard gold buried miles beneath them?"));
        expectedSentences.add(new Sentence(1, "Were there really shops that sold spell books and broomsticks?"));
        expectedSentences.add(new Sentence(2, "Might this not all be some huge joke that the Dursleys had cooked up?"));
        expectedSentences.add(new Sentence(3, "If Harry hadn't known that the Dursleys had no sense of humor, he might have thought so;"));
        expectedSentences.add(new Sentence(4, "yet somehow, even though everything Hagrid had told him so far was unbelievable, Harry couldn't help trusting him."));
        List<Sentence> returnedSentences = processContentPage.createSentencesFromString(sentences, new String[] {".",";", "?"});
        assertEquals(5, returnedSentences.size());
    }
    
    @Test
    void testSplitLongSentencesTC2_1() throws IOException {
        String sentences = "All Harry's spellbooks, his wand, robes, cauldron, and top-of-the-line Nimbus Two \n"
                + "Thousand broomstick had been locked in a cupboard under the stairs by Uncle Vernon \n"
                + "the instant Harry had come home. ";
        List<Sentence> expectedSentences = new ArrayList<>();
        expectedSentences.add(new Sentence(0, "All Harry's spellbooks, his wand, robes, cauldron, and top-of-the-line Nimbus Two \nThousand broomstick had been locked in a"));
        expectedSentences.add(new Sentence(1, "cupboard under the stairs by Uncle Vernon \n"
                + "the instant Harry had come home."));
        List<Sentence> returnedSentences = processContentPage.createSentencesFromString(sentences, new String[] {".",";", "?"});
        assertEquals(expectedSentences.get(0), returnedSentences.get(0));
        assertEquals(expectedSentences.get(1), returnedSentences.get(1));
    }  
    /*
        */

    @Test
    void testSplitLongSentencesTC2_2() throws IOException {
        String sentences = "but now the school year was over, and he was back with the Dursleys for the \n"
                + "summer, back to being treated like a dog that had rolled in something smelly.";
        List<Sentence> expectedSentences = new ArrayList<>();
        expectedSentences.add(new Sentence(0, "but now the school year was over, and he was back with the Dursleys for the \nsummer,"));
        expectedSentences.add(new Sentence(1, "back to being treated like a dog that had rolled in something smelly."));
        List<Sentence> returnedSentences = processContentPage.createSentencesFromString(sentences, new String[] {".",";", "?"});
        assertEquals(expectedSentences.get(0), returnedSentences.get(0));
        assertEquals(expectedSentences.get(1), returnedSentences.get(1));
    }  
    
    /***
     * 
     * 
     * 
     * 
     */
    
    @Test
    void testSplitLongSentencesTC2_3() throws IOException {
        String paragraph = "Harry paid dearly for his moment of fun. As neither Dudley nor the\n"
        		+ "hedge was in any way hurt, Aunt Petunia knew he hadn’t really done magic,\n"
        		+ "but he still had to duck as she aimed a heavy blow at his head with the\n";
        List<Sentence> expectedSentences = new ArrayList<>();
        expectedSentences.add(new Sentence(0, "good"));
        expectedSentences.add(new Sentence(1, "As neither Dudley nor the \nhedge was in any way hurt, Aunt Petunia knew he hadn’t really done magic,"));
        expectedSentences.add(new Sentence(2, "\nbut he still had to duck as she aimed a heavy blow at his head with the"));
        List<Sentence> returnedSentences = processContentPage.createSentencesFromString(paragraph, new String[] {".",";", "?", "✡"});
        assertEquals(expectedSentences.get(1), returnedSentences.get(1));
        assertEquals(expectedSentences.get(2), returnedSentences.get(2));
    }  
    @Test
    void testRegext() {
        String myString = "H arry woke early the next morning. Although he could tell it was daylight,\n"
                + "he kept his eyes shut tight.";
        List<Sentence> returnSentences =processContentPage.createSentencesFromString(myString, new String[] {".", "—", "\n"} );
        assertEquals(2, returnSentences.size());
    }
    
    @Test
    void testTC1Bug() {
    	String text = "“ — yes, their son, Harry —”\n"
    			+ "      Mr. Dursley stopped dead. Fear flooded him. He looked back at the\n"
    			+ "whisperers as if he wanted to say something to them, but thought better of it.";
    	List<Sentence> returnedSentences =  processContentPage.createSentencesFromString(text, new String[] {".",";", "?", "✡"});
    	assertEquals(3, returnedSentences.size());
    	
    }
    
    static private PDDocument getDocumentWithParagraph(BiConsumer<PDDocument, PDPage> pageContentFunction) {
        PDDocument document = new PDDocument();

        PDDocumentOutline outline = new PDDocumentOutline();
        document.getDocumentCatalog().setDocumentOutline(outline);

        for (int i = 0; i <= 17; i++) {
        
            PDPage page = new PDPage(PDRectangle.A4);          
            pageContentFunction.accept(document, page);
            
            document.addPage(page);

            PDPageXYZDestination dest = new PDPageXYZDestination();
            dest.setPage(page);
            PDOutlineItem bookmark = new PDOutlineItem();
            bookmark.setDestination(dest);
            bookmark.setTitle("Page " + i);
            outline.addLast(bookmark);

            for (int pageNo = 0; pageNo <= 3; pageNo ++) {
                PDPage page1 = new PDPage(PDRectangle.A4);
                pageContentFunction.accept(document, page1);
                document.addPage(page1);
            }
            
        }

        try {
            PDFTextStripper textStripper = new PDFTextStripper();
            String result = textStripper.getText(document);
            System.out.println(result);
        } catch(IOException e) {
            fail("cannot extract text");
        }
        

        return document;

    }

    static private void setPageContent(PDDocument document, PDPage page) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, 700);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, 700);
            contentStream.showText("Page Title");
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, 700);
            contentStream.showText("Paragraph 1 text. Second sentence of the first Paragraph.");
            contentStream.newLineAtOffset(0, 700);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, 700);

            contentStream.showText("          Paragraph Two text");
            contentStream.newLineAtOffset(0, -30);
            contentStream.showText("          Paragraph Three text");
            contentStream.endText();
            contentStream.close();
        } catch (IOException e) {
            fail("Getting document with paragraph");
        }
    }
    
    static private void setPageContentTC2(PDDocument document, PDPage page) {
        try {
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("B");
            contentStream.newLineAtOffset(20, 700);
            contentStream.showText(" ");
            contentStream.newLineAtOffset(20, 700);
            contentStream.showText("EPISODE  ONE");
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("DOLVI'S RECOMMENDATION");
            contentStream.newLineAtOffset(20, 700);
            contentStream.showText("arry worked too well for a student, but it was worse than he think. The little");
            contentStream.newLineAtOffset(0, 700);
            contentStream.showText("task was spoiled and very bad, not functional to work with green eyes");
            contentStream.newLineAtOffset(20, 700);

            contentStream.showText("the size of futball balls.");
            contentStream.endText();
            contentStream.close();
        } catch (IOException e) {
            fail("Getting document with paragraph");
        }
    }
}
