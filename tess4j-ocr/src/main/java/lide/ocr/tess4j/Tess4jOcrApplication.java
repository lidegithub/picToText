package lide.ocr.tess4j;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@SpringBootApplication
public class Tess4jOcrApplication {

	public static void main(String[] args) {
		
		
        
        SpringApplication.run(Tess4jOcrApplication.class, args);
	}
}
