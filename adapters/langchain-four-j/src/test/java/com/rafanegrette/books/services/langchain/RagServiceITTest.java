package com.rafanegrette.books.services.langchain;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.port.out.DeleteAudioService;
import com.rafanegrette.books.port.out.SaveBookService;
import com.rafanegrette.books.port.out.SentenceSegmentator;
import com.rafanegrette.books.port.out.SignedUrlsService;
import com.rafanegrette.books.port.out.SpeechToTextService;
import com.rafanegrette.books.port.out.TextToSpeechService;
import com.rafanegrette.books.port.out.UserRepository;
import com.rafanegrette.books.services.ReadBookService;
import com.rafanegrette.books.services.UserSecurityService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Disabled
@TestPropertySource(properties = {
        "openai.authorization=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",
        "openai.chat.model=gpt-4-turbo",
        "zilliz.milvus.host=https://in03-807a8a0902d3a3c.serverless.gcp-us-west1.cloud.zilliz.com",
        "zilliz.milvus.token=52b986f397b06be3038ae1d4caae6fa906d99dc0c9274e450f26cd8d672c03c40e5910ecb1100a65d630a195fc39e37cc1693dac",
        "aws.bucketName=sophi-books",
        "aws.region=us-east-1"
})
public class RagServiceITTest {

    @Autowired
    ReadBookService readBookService;
    @Autowired
    RagService ragService;
    @MockBean
    UserRepository userRepository;
    @MockBean
    SentenceSegmentator sentenceSegmentator;
    @MockBean(name = "WhisperService")
    SpeechToTextService speechToTextService;
    @MockBean
    UserSecurityService userSecurityService;
    @MockBean(name = "SaveBookAudioService")
    SaveBookService saveBookService;
    @MockBean
    SignedUrlsService signedUrlsService;
    @MockBean
    TextToSpeechService textToSpeechService;
    @MockBean(name = "DeleteAudioFileService")
    DeleteAudioService deleteAudioService;

    @Test
    @Disabled
    void embedNeuromancer() {
        // given
        Book neuromancer = readBookService.getBook("3ed94a59-a8a0-4bab-a9ce-8e16726dd0fb").get();
        // when
        ragService.embedDocument(neuromancer);
        // then
    }

    @Test
    @Disabled
    void textSearch() {
        var response = ragService.findText("Neuromancer", "How is Molly");
        System.out.println(response);
        assertNotNull(response);
    }
}
