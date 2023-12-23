package com.rafanegrette.books.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.ContentIndex;
import com.rafanegrette.books.model.Title;
import com.rafanegrette.books.model.mother.BookMother;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SpringExtension.class)
@Testcontainers
@DataMongoTest
class PersistentRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.4.2"));
    
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }
    
    @Autowired
    PersistentRepository bookRepository;
    
    Book harryBook = BookMother.harryPotter1()
    		.contentTable(List.of(new ContentIndex(1, "Chapter1", 1 , 3, 1)))
    		.build();
    
    @BeforeAll
    public static void initialSetUp() {
        //loadPDFService = new ProcessPDF(new ProcessContentPage());
    }
    @BeforeEach
    public void setUp() throws IOException {
        /*File file = ResourceUtils.getFile("classpath:Harry-1.pdf");
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        harryBook = loadPDFService.getBookFromByteFile("Harry-1", fileBytes, ParagraphSeparator.TWO_JUMP);*/
        bookRepository.deleteAll();
    }
    
    @Test
    void saveBook() {
        bookRepository.save(harryBook);
        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        assertFalse(books.isEmpty());
    }
    
    @Test
    void getChapter1Book() {
        bookRepository.save(harryBook);
        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        
        assertFalse(books.get(0).chapters().get(1).pages().isEmpty());
    }

    @Test
    void getAllTitles() {
        bookRepository.save(harryBook);
        Book harryBook2 = BookMother.harryPotter2().title("Harry 2 the thieves").build();
        bookRepository.save(harryBook2);
        
        List<Title> titles = bookRepository.findTitlesBy();
        
        assertEquals("Harry 2 the thieves", titles.get(1).getTitle());
    }

}
