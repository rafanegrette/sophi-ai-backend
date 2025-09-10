package com.rafanegrette.books.repositories;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Title;
import com.rafanegrette.books.port.out.BookRepository;
import com.rafanegrette.books.repositories.entities.TitleImpl;
import com.rafanegrette.books.repositories.mappers.BookMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("BookPhoneticDynamoService")
public class BookPhoneticRepositoryDynamo implements BookRepository {

    private final DBBookTable bookTable;

    public BookPhoneticRepositoryDynamo(@Qualifier("DynamoDBPhoneticTable") DBBookTable bookTable) {
        this.bookTable = bookTable;
    }

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
}
