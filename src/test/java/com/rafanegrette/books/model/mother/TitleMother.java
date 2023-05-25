package com.rafanegrette.books.model.mother;


import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import com.rafanegrette.books.repositories.Title;

public class TitleMother {

    static ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    public static Title getTitle1() {
        var title1 = factory.createProjection(Title.class);
        title1.setId("Harry-1");
        title1.setTitle("Harry Potter and Philosophal Stone");
        return title1;
    }

    public static Title getTitle2() {
        var title2 = factory.createProjection(Title.class);
        title2.setId("Harry-2");
        title2.setTitle("Harry Potter and the forty thieves");
        return title2;
    }
}
