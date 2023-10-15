package com.rafanegrette.books.port.out;

import java.util.List;

import com.rafanegrette.books.model.SentenceAudio;

public interface SignedUrlsService {
	List<SentenceAudio> generateSignedUrls(String bookPath);
}
