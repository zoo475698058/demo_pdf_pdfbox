package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDVariableText;

import java.io.File;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        try{
            String inputFileName = "E:\\pdfTest\\FillFormField.pdf";
            String outputFileName = "E:\\pdfTest\\FillFormField2.pdf";
            String fontPath = "simsun.ttf";

            PDDocument document = PDDocument.load(new File(inputFileName));
            PDDocumentCatalog catalog = document.getDocumentCatalog();
            PDAcroForm acroForm = catalog.getAcroForm();

            //加载字体
            PDFont font = PDType0Font.load(document, ClassLoader.getSystemResourceAsStream(fontPath), false);
            PDResources res = acroForm.getDefaultResources();
            if (res == null) {
                res = new PDResources();
            }
            String fontName = res.add(font).getName();
            String defaultAppearanceString = "/" + fontName + " 12 Tf 0 g";
            acroForm.setDefaultResources(res);
            acroForm.setDefaultAppearance(defaultAppearanceString);

            //修改表单默认字体
            List<PDField> fields = acroForm.getFields();
            for (PDField field: fields) {
                if (field.getFieldType() == "Tx") {
                    PDVariableText pdfield = (PDVariableText) acroForm.getField(field.getFullyQualifiedName());
                    pdfield.setDefaultAppearance(defaultAppearanceString);
                }
            }

            acroForm.getField("Text1").setValue("哈哈哈");
            acroForm.getField("Text2").setValue("李四2");
            acroForm.getField("Text3").setValue("咋回事");

//            acroForm.flatten(fields, true);
            acroForm.refreshAppearances();
            document.save(new File(outputFileName));
            document.close();

            System.out.println("===============PDF导出成功=============");
        }
        catch(Exception e){
            System.out.println("===============PDF导出失败=============");
            e.printStackTrace();
        }

    }
}