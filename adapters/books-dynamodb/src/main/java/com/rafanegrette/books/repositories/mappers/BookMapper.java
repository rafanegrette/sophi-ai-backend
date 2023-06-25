package com.rafanegrette.books.repositories.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.model.Sentence;
import com.rafanegrette.books.repositories.entities.BookDyna;
import com.rafanegrette.books.repositories.entities.SentenceDyna;
import com.rafanegrette.books.repositories.entities.Student;
import com.rafanegrette.books.repositories.entities.StudentDyna;

@Mapper
public interface BookMapper {

	public BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);;
	
	public BookDyna bookToBookDyna (Book book);
	
	public Book bootDynaToBook (BookDyna bookDyna);
}