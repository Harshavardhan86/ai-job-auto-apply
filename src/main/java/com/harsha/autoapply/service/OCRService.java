package com.harsha.autoapply.service;

import java.io.File;

import org.springframework.stereotype.Service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class OCRService {

    public String extractText(String imagePath) {

        ITesseract tesseract = new Tesseract();

        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");

        try {

            String text = tesseract.doOCR(new File(imagePath));

            return text;

        } catch (TesseractException e) {

            e.printStackTrace();

            return "Error while extracting the text";
        }
    }
}