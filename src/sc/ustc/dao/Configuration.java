package sc.ustc.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import entity.orMapping.ClassMapping;
import entity.orMapping.ClassProperty;
import entity.orMapping.JDBC;
import sc.ustc.controller.SimpleController;
import util.ClassReflector;

//Configuration ������� UseSC ���̵����� or_mapping.xml
public class Configuration {
	private JDBC jdbc;
	private List<ClassMapping> classMappingList;
	private String orMappingXmlPath;

	public Configuration() {
		// TODO Auto-generated constructor stub
		jdbc = new JDBC();
		classMappingList = new ArrayList<ClassMapping>();
		orMappingXmlPath = SimpleController.class.getClassLoader().getResource("or_mapping.xml").getPath();
	}

	// �ȶ�ȡurl·���е��ļ�����ȡΪdocument����
	public Document getDocument(String url) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(url);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

	// ���ݽڵ�����ƣ���ȡ�ڵ�����
	private String getElementValue(Element element, String name) {
		return element.elementText(name);
	}

	// ��ȡ��jdbc ��������
	public JDBC getJdbc() {
		Document document = getDocument(orMappingXmlPath);
		Element rootElement = document.getRootElement(); // ��ȡ�����ڵ�
		// ��ȡ��jdbc�ڵ�
		Element elementJdbc = rootElement.element("jdbc");
		// �������е�property ��jdbc������и�ֵ
		for (Iterator<?> propertyIterator = elementJdbc.elementIterator(); propertyIterator.hasNext();) {
			Element propetyElement = (Element) propertyIterator.next();
			// Ϊjdbc�Ķ�Ӧ���Ը�ֵ
			ClassReflector.setField(jdbc, getElementValue(propetyElement, "name"),
					getElementValue(propetyElement, "value"));
		}
		return jdbc;
	}

	// ��ȡ���� table ��class ��ӳ����Ϣ
	public List<ClassMapping> getClassMappingList() {
		Document document = getDocument(orMappingXmlPath);
		Element rootElement = document.getRootElement(); // ��ȡ�����ڵ�

		// ��root�ڵ���е������������е�class�ڵ�
		for (Iterator<?> classIterator = rootElement.elementIterator(); classIterator.hasNext();) {
			Element elementClass = (Element) classIterator.next();
			// �����ǰ��������class�ڵ�
			if ("class".equals(elementClass.getName())) {
				ClassMapping classMapping = new ClassMapping();				
				classMapping.setClassName(getElementValue(elementClass, "name"));
				classMapping.setTable(getElementValue(elementClass, "table"));
				classMapping.setId(getElementValue(elementClass.element("id"), "name"));									

				for (Iterator<?> proIterator = elementClass.elementIterator(); proIterator.hasNext();) {
					Element proElement = (Element) proIterator.next();					
					
					if ("property".equals(proElement.getName())) {
						ClassProperty classProperty = new ClassProperty();
						classProperty.setName(getElementValue(proElement, "name"));
						classProperty.setColumn(getElementValue(proElement, "column"));
						classProperty.setType(getElementValue(proElement, "type"));
						classProperty.setLazy(getElementValue(proElement, "lazy"));												
						// ���������Է���propertyMaping��
						classMapping.getPropertyList().add(classProperty);
					}
				}
				// ����classMapping ����list��
				classMappingList.add(classMapping);
			}
		}		
		return classMappingList;
	}

	public String getOrMappingXmlPath() {
		return orMappingXmlPath;
	}

	public void setOrMappingXmlPath(String orMappingXmlPath) {
		this.orMappingXmlPath = orMappingXmlPath;
	}

}
