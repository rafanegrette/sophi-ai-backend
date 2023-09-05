package com.rafanegrette.books.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Title;
import com.rafanegrette.books.port.out.BookRepository;
import com.rafanegrette.books.repositories.entities.BookDyna;
import com.rafanegrette.books.repositories.entities.TitleImpl;
import com.rafanegrette.books.repositories.entities.mother.BookMother;
import com.rafanegrette.books.repositories.mappers.BookMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component
@Primary
public class BookRepositoryDynamo implements BookRepository {

    private final DBBookTable bookTable;

    public void save(Book book) {
    	var bookDyna = BookMapper.INSTANCE.bookToBookDyna(book);
        bookTable.save(bookDyna);
    }

    public Optional<Book> findById(String bookId) {
        Key key = Key.builder().partitionValue(bookId).build();
        var bookDyna = bookTable.findById(key);
        var book = BookMapper.INSTANCE.bootDynaToBook(bookDyna);
        return Optional.ofNullable(book);
    }

    public List<Title> findTitlesBy() {
    	List<TitleImpl> titlesValue = bookTable.findAllTitles().stream().map(t -> new TitleImpl(t.getId(), t.getTitle(), t.getLabel())).toList();
        List<Title> titles = new ArrayList<>();
        titles.addAll(titlesValue);
    	return titles;
    }

    public void deleteById(String bookId) {
    	Key key = Key.builder().partitionValue(bookId).build();
    	try {
    		bookTable.deleteById(key);
    	} catch (DynamoDbException de) {
    		log.error("Deleting the book with id: {}, detail error: {}", bookId, de);
    	}
    }

    public void deleteAll() {
        
    }

    public List<Book> findAll() {
        return new ArrayList<>();
    }

    /*public static void main(String...args) {
        //var book = BookMother.harryPotter1().build();
        //var student = new Student();
        //student.setId("sklfjd87898");
        //student.setName("Ralph");
        var service = new BookRepositoryImpl();
        service.save(book);
        
        //var stuResult = service.findById("Harry-1");
        
        //System.out.println(stuResult.get().getTitle());
    }*/

}
