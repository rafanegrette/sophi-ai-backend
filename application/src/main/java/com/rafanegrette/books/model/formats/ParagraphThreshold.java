package com.rafanegrette.books.model.formats;

public enum ParagraphThreshold {

    DEFAULT(2.5f),
    THREE(3.0f);

    private float threshold;

    ParagraphThreshold(float threshold) {
        this.threshold = threshold;
    }

    public float getThreshold() {
        return threshold;
    }
}
