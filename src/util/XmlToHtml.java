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

public class XmlToHtml {
	// ͨ��xml�ļ�����html�ļ��������ַ�������ʽ����
	// �������һ��xml�ļ�
	public static String getHtmlByXml(File file) {
		// TODO Auto-generated method stub
		// �õ�xsl�ļ�·��
		String path = SimpleController.class.getClassLoader().getResource("../../").getPath() + "xslFile/translator.xsl";
		File xslFile = new File(path);
		Document xmlDoc = null; // xml document
		Document htmlDoc = null; // html document
		try {
			xmlDoc = new SAXReader().read(file);
			// ����xsl�ļ��ж���ĸ�ʽ����documentת��ΪHtml document
			htmlDoc = getHtmlDocumentByXsl(xmlDoc, xslFile);
		} catch (Exception e) {
			if (xmlDoc == null)
				throw new RuntimeException(file.toString() + "������");
		}
		//System.out.println(getHtmlString(htmlDoc));
		String htmlString  = getHtmlString(htmlDoc);
		System.out.println(htmlString);
		// ��html�ļ�д���ַ�����ʽ
		return htmlString;
	}

	// ��html�ļ�תΪ�ַ�����ʽ
	public static String getHtmlString(Document doc) {
		// ��HTMLд���ַ�����
		StringWriter strWriter = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(doc.getXMLEncoding());
		format.setXHTML(true);
		// ����format�ĸ�ʽ����html�ļ�д��strWriter�ַ���
		HTMLWriter htmlWriter = new HTMLWriter(strWriter, format);
		format.setExpandEmptyElements(false);
		try {
			htmlWriter.write(doc);
			htmlWriter.flush();
		} catch (IOException e) {
		}
		return strWriter.toString();
	}

	// ��document����xsl��ʽת����HTML document
	public static Document getHtmlDocumentByXsl(Document document, File file) throws Exception {
		// ʹ��JAXP����xstl
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(file));
		DocumentSource source = new DocumentSource(document);
		DocumentResult result = new DocumentResult();
		transformer.transform(source, result);
		// ����ת�������ĵ�
		Document transformedDoc = result.getDocument();
		return transformedDoc;
	}
}
