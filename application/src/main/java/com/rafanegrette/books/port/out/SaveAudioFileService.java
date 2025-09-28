package com.rafanegrette.books.port.out;

public interface SaveAudioFileService {
    void save(String pathFile, byte[] file);
}
