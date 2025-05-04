package com.rafanegrette.books.chat.model;

import java.util.List;

public record ChatCompletionResponse(String id,
                                     String object,
                                     long created,
                                     String model,
                                     List<Choice> choices) {
}
