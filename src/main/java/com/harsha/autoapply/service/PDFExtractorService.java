package com.harsha.autoapply.service;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

@Service
public class PDFExtractorService {

    public String extractText(String filePath) throws IOException {

        File file = new File(filePath);

        if (!file.exists()) {
            throw new IOException("File not found: " + file.getAbsolutePath());
        }

        try (PDDocument document = Loader.loadPDF(file)) {

            if (document.isEncrypted()) {
                throw new IOException("PDF is encrypted/password protected");
            }

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            if (text == null || text.isBlank()) {
                throw new IOException(
                    "No text found in PDF. It may be a scanned/image-based PDF.");
            }

            return text;
        }
    }
}