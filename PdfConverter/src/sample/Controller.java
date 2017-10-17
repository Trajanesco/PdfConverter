package sample;


import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.control.TextField;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;


public class Controller {
    @FXML
    private Pane mainWindowPane;
    @FXML
    private TextField pathField;


    private String fileName = "convertedFile.doc";




    @FXML
    public void browseFile(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(mainWindowPane.getScene().getWindow());
        if(file != null){
            conversionFile(file.getPath());
        }
    }

    @FXML
    public void convertSelectedFile(){
        if(pathField.getText() != null){
            try{
                XWPFDocument doc = new XWPFDocument();
                String pdf = pathField.getText();
                PdfReader reader = new PdfReader(pdf);
                PdfReaderContentParser parser = new PdfReaderContentParser(reader);
                    for(int i = 1; i<= reader.getNumberOfPages(); i++){
                        TextExtractionStrategy strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
                        String text = strategy.getResultantText();
                        XWPFParagraph para = doc.createParagraph();
                        XWPFRun run = para.createRun();
                        run.setText(text);
                        run.addBreak(BreakType.PAGE);
                    }
                File file = new File(fileName);
                FileOutputStream out = new FileOutputStream(file);
                doc.write(out);
                out.close();
                reader.close();
                System.out.println("Document converted successfully! ");
            }catch(IOException e){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Wrong file or directory");
                alert.setHeaderText("Please check type of the file and directory");
                alert.showAndWait();
                System.out.println("Wrong input file");
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void conversionFile(String path){
        pathField.setText(path);
    }
}
