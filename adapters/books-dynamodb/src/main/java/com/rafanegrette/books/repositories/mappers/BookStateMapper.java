package com.rafanegrette.books.repositories.mappers;

import com.rafanegrette.books.model.BookCurrentState;
import com.rafanegrette.books.repositories.entities.BookStateDyna;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookStateMapper {

    BookStateMapper INSTANCE = Mappers.getMapper(BookStateMapper.class);

    BookCurrentState map (BookStateDyna bookStateDyna);

    BookStateDyna map(BookCurrentState bookCurrentState);

}
