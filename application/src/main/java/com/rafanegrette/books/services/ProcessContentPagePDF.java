package com.rafanegrette.books.services;

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
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.rafanegrette.books.model.Page;
import com.rafanegrette.books.model.Paragraph;
import com.rafanegrette.books.model.Sentence;
import com.rafanegrette.books.string.formats.StringFormatFunctions;

@Slf4j
@Service
public class ProcessContentPagePDF implements ContentPage {

    private static final String CROSS_LORAINE = "☨";
    private static final String DAVID_CROSS = "✡";
    private static final int MAX_WORDS = 17;
    private static final int UMBLAL_MAX_WORDS = 5;

    @Deprecated
    public LinkedList<Sentence> formatSentences(List<Sentence> sentences) {

        return sentences.stream().map(s -> 
                    new Sentence(s.id(), s.text().replace("’", "'")
                    		.replace("\n", " "))
                    ).collect(Collectors.toCollection(LinkedList::new));
    }

    private List<String> dynamicTransform(String source) {
        String[] words = source.split(" ");
        int lengthSentences = 0;
        if (words.length < MAX_WORDS * 1.4) {
            return List.of(source);
        } else if (words.length < MAX_WORDS * 2.0) {
            lengthSentences = words.length / 2;
        } else {
            lengthSentences = MAX_WORDS + (words.length % MAX_WORDS) / (words.length / MAX_WORDS);
        }
        
        List<Integer> indexSentenceToSplit = new ArrayList<>();
        int weight = 0;
        for (int i = 0; i < words.length; i++) {
            weight++;
            if (words[i].endsWith(",") && isInUmbral(weight, lengthSentences)) {
                indexSentenceToSplit.add(i);
                weight = 0;
            }
            
            if (weight > MAX_WORDS + UMBLAL_MAX_WORDS) {
                indexSentenceToSplit.add(i - UMBLAL_MAX_WORDS);
                weight = UMBLAL_MAX_WORDS / 2;
            }
        }
        
        return splitByIndexes(words, indexSentenceToSplit);
    }
    
    private List<String> splitByIndexes (String[] words, List<Integer> indexes) {
        List<String> sentences = new LinkedList<>();
        int iniIndex = 0;
        int endIndex = 0;
        for(int i : indexes) {
            endIndex = i + 1;
            sentences.add(String.join(" ", Arrays.copyOfRange(words, iniIndex, endIndex)));
            iniIndex = endIndex;
        }
        sentences.add(String.join(" ",Arrays.copyOfRange(words, endIndex, words.length)));
        return sentences;
    }
    
    private boolean isInUmbral(int weight, int lengthSentences) {
        return weight >= lengthSentences - UMBLAL_MAX_WORDS && weight <= lengthSentences + UMBLAL_MAX_WORDS;
    }
    
    private Pattern getPatternRegex(String[] characters) {
        StringBuilder strPattern = new StringBuilder("(.*?[");
        for (String c: characters) {
            strPattern.append("\\\\" + c);
        }
        strPattern.append("])");
        return Pattern.compile(strPattern.toString());
    }

    public String removedChar(String sentence, int i) {
        // TODO Auto-generated method stub
        return (new StringBuilder(sentence)).deleteCharAt(i).toString();
    }

    /***
     * TC2 FirstPage, This Method include a Function to extract title mixup into sentences like:
     * T HE WORST BIRTHDAY for the first time
     * @param sentence with title in uppercase
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
        
        PDFTextStripper pdfStripper = new PDFTextStripper();
        pdfStripper.setStartPage(noPage);
        pdfStripper.setEndPage(noPage);
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        pdfStripper.setAddMoreFormatting(paragraphFormats.applyExtraFormat());
        pdfStripper.setSuppressDuplicateOverlappingText(false);
        pdfStripper.setDropThreshold(paragraphFormats.paragraphThreshold().getThreshold());
        pdfStripper.writeText(document, writer);

        String rawPageText = fixTitleMixupFirstSentenceHP2.apply(out.toString());
        

        return new Page(noPage, getParagraphs(rawPageText, paragraphFormats));
    }

    List<Paragraph> getParagraphs(String rawPageText, ParagraphFormats paragraphFormats) {
        String[] paragraphs = StringFormatFunctions.formatPages(rawPageText).split(paragraphFormats.paragraphSeparator().getSeparator());

        List<Paragraph> paragraphsFinal = new LinkedList<>();

        for (int i = 0; i < paragraphs.length; i++) {
            if (paragraphs[i].isBlank()) continue;

            LinkedList<Sentence> sentences = createSentencesFromString(paragraphs[i], new String[] {".", "—",";","?", ":", "(", ")", DAVID_CROSS} );

            sentences = formatSentences(sentences);

            if (!sentences.isEmpty()) {
                Paragraph paragrap = new Paragraph(i,sentences);
                paragraphsFinal.add(paragrap);
            }
        }

        return paragraphsFinal;
    }

    @Override
    public LinkedList<Sentence> createSentencesFromString(String sentencesStr, String[] breakerCharacterArray) {

        String tokenizedString = transformWordsToKeys(sentencesStr.trim());
        int lastIndex = tokenizedString.length() - 1;
        char lastCharacter = tokenizedString.charAt(lastIndex);
        tokenizedString = isBreakerCharacter(breakerCharacterArray, lastCharacter)
                ? tokenizedString : tokenizedString + DAVID_CROSS;
                
        return createSentencesFromTransformed(tokenizedString, breakerCharacterArray);
    }
    
    private boolean isBreakerCharacter(String[] breakerCharacterArray, char character) {
        for(String breakerChar : breakerCharacterArray) {
            if (breakerChar.charAt(0) == character) return true;
        }
        return false;
    }
    
    private String transformWordsToKeys(String currentSentence) {
        Map<String, String> abbrevMaps = Map.of("Mr.", "<-Key1->", 
                                        "Mrs.", "<-Key2->", 
                                        "“Mr.", "<-Key3->", 
                                        "“Mrs.", "<-Key4->");

        //abbrevMaps.entrySet().stream().map(e -> currentSentence.replace(e.getKey(), e.getValue())).reduce(currentSentence, "");
        for (Entry<String, String> e: abbrevMaps.entrySet()) {
            currentSentence = currentSentence.replace(e.getKey(), e.getValue());
        }
        return currentSentence;
    }
    
    private LinkedList<Sentence> createSentencesFromTransformed(String sentencesStr, String[] breakerCharacters) {
        
        LinkedList<Sentence> sentences = new LinkedList<>();
        Pattern pattern = getPatternRegex(breakerCharacters);
        Matcher matcher = pattern.matcher(sentencesStr.replace("\n", CROSS_LORAINE));

        Integer indexSentence = 0;
        while(matcher.find()) {
            if(containtsAlphaCharacters(matcher.group(0))) {
                String sentenceMatched = replaceCrossLoraine(matcher.group(0));
                String transformedSentence = transformKeysToWords(sentenceMatched).trim();
                List<String> transformedSentences = dynamicTransform(transformedSentence);
                sentences.addAll(getSentencesWithIndex(transformedSentences, sentences.size()));
            } else if (sentences.isEmpty()){
            	sentences.add(new Sentence(0, matcher.group(0)));
            } else {
                int currentLastIndex = sentences.size() - 1;
               	String updateText = sentences.getLast().text() + matcher.group(0);
                sentences.set(sentences.size() - 1, new Sentence(currentLastIndex, updateText));
            }
        }
        removeDavidCross(sentences);
        if(sentences.isEmpty() && !sentencesStr.isBlank()) {
        	String transformedSentence = transformKeysToWords(sentencesStr);
            sentences.add(new Sentence(0, removeDavidCross(transformedSentence)));
        }
        
        return sentences;
    }
    
    private boolean containtsAlphaCharacters(String text) {

        for (char c: text.toCharArray()) {
            if (Character.isAlphabetic(c)) return true;
        }
        return false;
    }
    
    private void removeDavidCross(LinkedList<Sentence> sentences) {
        
        if (!sentences.isEmpty()) {
            int indexSencence = sentences.size() -1;
            sentences.set(indexSencence ,
                    new Sentence (indexSencence, removeDavidCross(sentences.getLast().text())));
        }
    }
    
    private String removeDavidCross(String text) {
    	if (text.endsWith(DAVID_CROSS)) {
    		int lastIndexChar = text.length() - 1;
    		return text.substring(0, lastIndexChar);
    	}
    	return text;
    }
    
    private LinkedList<Sentence> getSentencesWithIndex(List<String> sentencesStr, Integer index) {
        LinkedList<Sentence> sentences = new LinkedList<>();
        for(String sentence : sentencesStr) {
            sentences.add(new Sentence(index++, sentence));
        }
        return sentences;
    }
    
    private String transformKeysToWords(String currentSentence) {
        Map<String, String> abbrevMaps = Map.of("Mr.", "<-Key1->", 
                                        "Mrs.", "<-Key2->", 
                                        "“Mr.", "<-Key3->", 
                                        "“Mrs.", "<-Key4->");

        //abbrevMaps.entrySet().stream().map(e -> currentSentence.replace(e.getKey(), e.getValue())).reduce(currentSentence, "");
        for (Entry<String, String> e: abbrevMaps.entrySet()) {
            currentSentence = currentSentence.replace(e.getValue(), e.getKey());
        }
        return currentSentence;
    }
    
    private String replaceCrossLoraine(String sentenceToRestables) {

        StringBuilder newSentence = new StringBuilder(sentenceToRestables);
        while(newSentence.indexOf(CROSS_LORAINE) >= 0) {
            int index = newSentence.indexOf(CROSS_LORAINE);
            if (index > 0 && newSentence.charAt(index - 1) != ' ') {
                newSentence.replace(index, index + 1, " \n");
            } else {
                newSentence.replace(index, index + 1, "\n");
            }
        }
        return newSentence.toString();

    }
}
