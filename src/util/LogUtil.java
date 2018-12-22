package util;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import entity.ActionLog;
import entity.LogXml;

public class LogUtil {
	private String logXmlPath;
	private LogXml logXml;

	public LogUtil(String logXmlPath) {
		// TODO Auto-generated constructor stub
		this.logXmlPath = logXmlPath;
		logXml = LogXml.INSTANCE;
	}

	private Document getDocument(String url) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		if (document == null) {
			System.out.println("�ļ�Ϊ��");
		}
		return document;
	}

	private String getElementValue(Element element, String name) {
		return element.elementText(name);
	}

	// ��log.xml�ļ���ȡΪLogXml ����
	public LogXml readLog() throws DocumentException {
		// ����actionLog�б�
		List<ActionLog> actionLogList = new ArrayList<ActionLog>();

		Document document = getDocument(this.logXmlPath); // ��ȡxml�ļ�
		Element rootElement = document.getRootElement();// ��ȡ���ڵ�

		// ��������ÿ��action�ڵ�
		for (Iterator<?> actionIterator = rootElement.elementIterator(); actionIterator.hasNext();) {
			ActionLog actionLog = new ActionLog();
			Element actionElement = (Element) actionIterator.next();

			actionLog.setAll(getElementValue(actionElement, "name"), getElementValue(actionElement, "s-time"),
					getElementValue(actionElement, "e-time"), getElementValue(actionElement, "result"));

			logXml.addAction(actionLog);			
		}
		return logXml;
	}

	// �Ѷ���д����־�ļ�log.xml��
	public void writeLog() {
		// �����ĵ���
		Document document = DocumentHelper.createDocument();
		// �ĵ����ӽڵ�log�ڵ㣬�����ڵ㣬һ���ĵ�ֻ����һ�����ڵ㣬��ӳ���
		Element root = document.addElement("log");
		// ���ע��
		root.addComment("some actions");

		for (ActionLog actionLog : logXml.getActionList()) {
			// ��root�ڵ��£����action�ڵ�
			Element actionElement = root.addElement("action");
			// ��action�ڵ��£���������ڵ�
			Element nameElement = actionElement.addElement("name");
			// ����name�ڵ���������
			nameElement.setText(actionLog.getName());
			Element stimeElement = actionElement.addElement("s-time");
			// ����stime�ڵ���������
			stimeElement.setText(actionLog.getStime());
			Element etimeElement = actionElement.addElement("e-time");
			// ����etime�ڵ���������
			etimeElement.setText(actionLog.getEtime());
			Element resultElement = actionElement.addElement("result");
			// ����result�ڵ���������
			resultElement.setText(actionLog.getResult());
		}
		// ��������xml�ļ�д�������
		try {
			// ������ʽ����
			OutputFormat format = OutputFormat.createPrettyPrint();
			// ���ñ����ʽ��Ĭ��UTF-8
			format.setEncoding("UTF-8");
			// ������������˴�Ҫʹ��Writer����Ҫָ����������ʽ��ʹ��OutputStream����
			FileOutputStream fos = new FileOutputStream("src/log.xml");
			// ����xml�����
			XMLWriter writer = new XMLWriter(fos, format);
			// ����xml�ļ�
			writer.write(document);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
