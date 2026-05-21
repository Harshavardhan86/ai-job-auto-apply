package com.harsha.autoapply.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

@Service
public class OCRService {

    @Value("${tesseract.datapath}")
    private String tessDataPath;

    public String extractText(String imagePath) {
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessDataPath);

        try {
            return tesseract.doOCR(new File(imagePath));
        } catch (TesseractException e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }
}