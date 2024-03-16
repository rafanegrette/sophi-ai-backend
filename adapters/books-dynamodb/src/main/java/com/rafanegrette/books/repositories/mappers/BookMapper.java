package com.rafanegrette.books.repositories.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.rafanegrette.books.model.Book;
import com.rafanegrette.books.repositories.entities.BookDyna;

@Mapper
public interface BookMapper {

	BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);;
	
	BookDyna bookToBookDyna (Book book);
	
	Book bootDynaToBook (BookDyna bookDyna);
}