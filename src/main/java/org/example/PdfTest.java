package org.example;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.*;

import java.io.File;
import java.util.List;


public class PdfTest {
    public static void main(String[] args) {
        try{
            String inputFileName = "E:\\pdfTest\\template.pdf";
            String outputFileName = "E:\\pdfTest\\template_new.pdf";
            String fontPath = "simsun.ttf";

            PDDocument document = PDDocument.load(new File(inputFileName));
            PDDocumentCatalog catalog = document.getDocumentCatalog();
            PDAcroForm acroForm = catalog.getAcroForm();

            PDFont font = PDType0Font.load(document, ClassLoader.getSystemResourceAsStream(fontPath), false);
            PDResources res = acroForm.getDefaultResources();
            if (res == null) {
                res = new PDResources();
            }
            String fontName = res.add(font).getName();
            acroForm.setDefaultResources(res);

            List<PDField> fields = acroForm.getFields();
            String jsonS = "{\"fill_1\":\"测试甲方\",\"fill_5\":\"测试乙方\",\"fill_9\":\"测试丙方\",\"fill_2\":\"1111111111111111\",\"fill_6\":\"2222222222222222\",\"undefined\":\"测试门店号\",\"fill_11\":\"0731-11111111\",\"fill_12\":\"人民中路222号\",\"fill_13\":\"ABC123456\",\"fill_14\":\"测试甲方\",\"fill_15\":\"199\",\"fill_16\":\"住宅\",\"fill_6_2\":\"√\",\"fill_7_2\":\"自定义条约1111\",\"fill_8_2\":\"自定义条约2222\",\"fill_9_2\":\"2022年五月二十日\"}";
            JSONObject maps = JSONUtil.parseObj(jsonS);

            for (PDField field: fields) {
                String value = maps.getStr(field.getFullyQualifiedName());
                if (value != null){
                    if (field.getFieldType() == "Tx") {
                        PDVariableText pdfield = (PDVariableText) field;
                        pdfield.setDefaultAppearance("/" + fontName + " 12 Tf 0 g");
                    }
                    field.setValue(value);
                }
            }

//            acroForm.getField("toggle_2").setValue("On");
//            acroForm.getField("toggle_3").setValue("Off");

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