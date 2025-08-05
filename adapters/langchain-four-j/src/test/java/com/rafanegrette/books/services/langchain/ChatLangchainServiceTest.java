package com.rafanegrette.books.services.langchain;

import com.rafanegrette.books.services.langchain.config.EnglishTeacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatLangchainServiceTest {


    @InjectMocks
    ChatLangchainService chatLangchainService;

    @Mock
    EnglishTeacher englishTeacher;
    @Mock
    TimeProvider timeProvider;
    final String BOOK_NAME = "Neuromancer";
    
    @Test
    void testSendMessageInMondayMorning() {
        // given
        var date = LocalDateTime.of(2025, 6, 2, 8, 30);
        var conversationId = UUID.randomUUID();

        given(timeProvider.getTime()).willReturn(date);
        //given(agents.englishTeacher(BOOK_NAME)).willReturn(englishTeacher);
        // when
        chatLangchainService.sendMessage(conversationId, "Hello");

        // then
        verify(englishTeacher).chat(conversationId, getSystemInstructionMorning(), "Hello");
    }

    @Test
    void testSendMessageInMondayNoon() {
        // given
        var date = LocalDateTime.of(2025, 6, 2, 12, 30);
        var conversationId = UUID.randomUUID();

        given(timeProvider.getTime()).willReturn(date);
        //given(agents.englishTeacher(BOOK_NAME)).willReturn(englishTeacher);
        // when
        chatLangchainService.sendMessage(conversationId, "Hello");

        // then
        verify(englishTeacher).chat(conversationId, getSystemInstructionNoon(), "Hello");
    }

    @Test
    void testSendMessageInMondayAfterNoon() {
        // given
        var date = LocalDateTime.of(2025, 6, 2, 18, 30);
        var conversationId = UUID.randomUUID();

        given(timeProvider.getTime()).willReturn(date);
        //given(agents.englishTeacher(BOOK_NAME)).willReturn(englishTeacher);
        // when
        chatLangchainService.sendMessage(conversationId, "Hello");

        // then
        verify(englishTeacher).chat(conversationId, getSystemInstructionAfterNoon(), "Hello");
    }

    private String getSystemInstructionMorning() {
        return """
                Be engaging like Blenderbot LLM, but give short responses. You're an English pronunciation coach for Latin American learners. Focus on challenging sounds: long/short vowels, /th/, /v/ vs. /b/. Use Neuromancer examples for minimal pairs practice (e.g., "bat/bath"). Have students practice 5-7 pairs aloud, give specific feedback, then guide them to create sentences. Model clear articulation and suggest weekly practice exercises.""";
    }


    private String getSystemInstructionNoon() {
        return """
                Be engaging like Blenderbot LLM, but give short responses. You're an English teacher focusing on past tense -ED pronunciation for Latin Americans. Teach the three rules: /d/, /t/, /ɪd/. Use Neuromancer examples, categorize verbs by type. Present base verbs for pronunciation practice, give feedback, then have students create sentences. Use book passages to identify pronunciation rules.""";
    }

    private String getSystemInstructionAfterNoon() {
        return """
                Be engaging like Blenderbot LLM, but give short responses. You're a conversational English tutor building speaking confidence in Latin Americans. Start natural conversations about Neuromancer or daily topics. Note common errors (missing pronouns, tense issues, pronunciation). After 5 minutes, give feedback on 2-3 patterns with examples. Practice again focusing on those areas.""";
    }

}