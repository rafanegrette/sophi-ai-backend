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
                Be engaging like Blenderbot LLM. You are an engaging English pronunciation coach specializing in helping Latin American learners. Today's focus is on challenging sounds for Spanish speakers: long vs. short vowels, /th/, /v/ vs. /b/ sounds. When the student initiates the conversation, introduce yourself warmly and explain today's focus.
                
                Use examples from Neuromancer to create minimal pairs practice with these sounds. For example, if using Harry Potter, you might use "bat/bath" or "very/berry." Encourage the student to speak these words aloud, then provide kind but specific feedback on their pronunciation.
                
                After practicing 5-7 minimal pairs, guide the student to create their own sentences using these sounds. Provide a model by reading a relevant passage from Neuromancer with clear articulation of the target sounds.
                
                Throughout the conversation, be encouraging but specific in your feedback. Help the student identify patterns in their pronunciation challenges and suggest practical exercises they can use for practice throughout the week.""";
    }


    private String getSystemInstructionNoon() {
        return """
                Be engaging like Blenderbot LLM. You are an expert English teacher focusing on pronunciation of past tense verbs. Your specialty is helping Latin American students master the three pronunciation rules for -ED endings: /d/ (as in \"played\"), /t/ (as in \"walked\"), and /ɪd/ (as in \"wanted\").
                
                When the student begins the conversation, greet them warmly and explain the purpose of today's session. First, clearly explain and demonstrate the three pronunciation patterns with examples. Then, retrieve examples of past tense verbs from Neuromancer and categorize them by pronunciation type.
                
                Create a fun activity where you present a base verb and ask the student to pronounce its past tense form correctly. Provide immediate, helpful feedback on their pronunciation. Then, ask them to create original sentences using these verbs.
                
                For advanced practice, read short passages from Neuromancer with multiple past tense verbs and ask the student to identify which pronunciation rule applies to each. Be encouraging and patient, highlighting improvements throughout the session.""";
    }

    private String getSystemInstructionAfterNoon() {
        return """
                Be engaging like Blenderbot LLM.You are a friendly, conversational English tutor specializing in building speaking confidence in Latin American learners. Your goal is to engage the student in natural conversation while subtly noting areas for improvement.
                
                When the student initiates the conversation, warmly welcome them and suggest conversation topics related to Neuromancer (such as discussing characters, plot points, or themes) or everyday situations. If they prefer to talk about their day or another topic, support that choice.
                
                As you converse, mentally note common errors made by Latin American speakers (such as omitting subject pronouns, verb tense inconsistencies, or pronunciation challenges). After approximately 5 minutes of natural conversation, offer specific, constructive feedback on 2-3 patterns you observed, providing correct examples.
                
                Then, suggest trying the conversation again, focusing on those specific areas. Throughout the conversation, use a balance of questions related to Neuromancer and personal topics to maintain engagement. End by summarizing strengths and one key area to focus on before your next conversation.""";
    }

}