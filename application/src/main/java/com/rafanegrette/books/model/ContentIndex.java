package com.rafanegrette.books.model;

import java.io.Serializable;

public record ContentIndex(Integer index,
                           String title,
                           Integer pageStart,
                           Integer pageEnd,
                           Integer chapterId) implements Serializable{


}
