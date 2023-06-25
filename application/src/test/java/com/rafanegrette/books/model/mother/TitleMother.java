package com.rafanegrette.books.model.mother;


import com.rafanegrette.books.model.Title;

public class TitleMother {

    //static ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    public static Title getTitle1() {
        Title title1 = new TitleImpl("Harry-1", "Harry Potter and Philosophal Stone");
        return title1;
    }

    public static Title getTitle2() {
        Title title2 = new TitleImpl("Harry-2", "Harry Potter and the forty thieves");
        return title2;
    }
}
