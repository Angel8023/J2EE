package util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.HTMLWriter;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;

import sc.ustc.controller.SimpleController;

public class Xml2Html {
	public static String translateXml2Html(File file) {
		//�õ�xsl�ļ�·��
		String path = SimpleController.class.getClassLoader().getResource("../../").getPath() + "Pages/translator.xsl";
		File xslFile = new File(path);
		Document document = null;
		Document doc = null;
		try {
			document = new SAXReader().read(file);
			doc = styleDocument(document,xslFile);
		} catch (Exception e) {
			if (document == null)
				throw new RuntimeException(file.toString() + "������");
		}
		String html = writeHtml(doc);
		System.out.println(html);
		return html;
		
	}
	
	public static String writeHtml(Document doc){
	 	//��HTMLд���ַ�����
        StringWriter strWriter = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(doc.getXMLEncoding());
        format.setXHTML(true);
        HTMLWriter htmlWriter = new HTMLWriter(strWriter,format);
        format.setExpandEmptyElements(false);
        try {
            htmlWriter.write(doc);
            htmlWriter.flush();
        } catch (IOException e) {
        }
        return strWriter.toString();
    }
	
	public static Document styleDocument(Document document,File file) throws Exception {
		//ʹ��JAXP����xstl
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(file));
		DocumentSource source = new DocumentSource(document);
		DocumentResult result = new DocumentResult();
		transformer.transform(source, result);
		//����ת�������ĵ�
		Document transformedDoc = result.getDocument();
		return transformedDoc;
	}

}
