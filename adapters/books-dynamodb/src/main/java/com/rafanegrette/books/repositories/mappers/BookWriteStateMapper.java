package com.rafanegrette.books.repositories.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.rafanegrette.books.repositories.entities.BookWriteStateDyna;
import com.rafanegrette.books.model.BookWriteState;

@Mapper
public interface BookWriteStateMapper {

    BookWriteStateMapper INSTANCE = Mappers.getMapper(BookWriteStateMapper.class);

    BookWriteState map (BookWriteStateDyna bookWriteStateDyna);

    BookWriteStateDyna map(BookWriteState bookWriteState);

}
