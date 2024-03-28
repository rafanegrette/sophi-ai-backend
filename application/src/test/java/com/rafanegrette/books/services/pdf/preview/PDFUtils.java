package com.rafanegrette.books.services.pdf.preview;

import com.rafanegrette.books.model.FormParameter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class PDFUtils {

    static PDDocument getDocumentFromFilePath(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        byte[] bytesFile = Files.readAllBytes(path);
        return Loader.loadPDF(bytesFile);
    }
}
