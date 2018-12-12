package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import entity.ActionXml;
import entity.ControllerXml;
import entity.ResultXml; 

public class XmlParser {
	private String xmlFilePath;  	//xml �ļ�·��
	private ControllerXml controllerXml;    //����controllerxml����
	
	public XmlParser(String xmlFilePath){
		this.xmlFilePath = xmlFilePath;
		controllerXml = new ControllerXml();
	}
	
	private Document getDocument(String url) throws DocumentException {		
		 SAXReader reader = new SAXReader();
	     Document document = reader.read(url);
	     return document;
	}
	
	public String getElementValue(Element element,String name){
		return element.attribute(name).getValue();
	}	
	
	public ControllerXml getControllerXml() {
		return this.controllerXml;
	}
	
	public void setControllerXml() throws DocumentException{
		List<ActionXml> actionXmllist = new ArrayList<ActionXml>();  //����actionxml����
		List<ResultXml> resultList = new ArrayList<ResultXml>();   //����resultxml ����
						
				
	    Document document = getDocument(this.xmlFilePath);   //��ȡxml�ļ�
	    Element rootElement = document.getRootElement();// ��ȡ���ڵ�	
	    
	  //�õ�controller�ڵ�
	  Element elementCtrl = rootElement.element("controller");
	  
      //�ֱ��ÿ��action�ӽڵ���е���
	  for(Iterator<?> actionIterator = elementCtrl.elementIterator();actionIterator.hasNext();){		
		ActionXml actionXml = new ActionXml();
		Element actionElement = (Element) actionIterator.next();
		
		//�ֱ��ÿ��result�ӽڵ���е���
		for(Iterator<?> resultIterator = actionElement.elementIterator();resultIterator.hasNext();){
			ResultXml resultXml = new ResultXml();			
			Element resultElement = (Element) resultIterator.next();
			resultXml.setAll(getElementValue(resultElement,"name"), 
					getElementValue(resultElement,"type"), 
					getElementValue(resultElement,"value"));
			resultList.add(resultXml);	
		}
		
		//��ȡ��ǰ��action
		actionXml.setResultList(resultList);  
		resultList.clear();   //ÿ���������Ҫ��list��գ�������һ��result�����ݴ洢
		
		actionXml.setAll(getElementValue(actionElement, "name"), 
				getElementValue(actionElement, "class"), 
				getElementValue(actionElement, "method"));
		
		//�ѵ�ǰ��action����list��
		actionXmllist.add(actionXml);			
	  }	 
	  //��ȡ��ǰ��controller
	  controllerXml.setActionList(actionXmllist);	  
	  actionXmllist.clear();  //ÿ���������Ҫ��list��գ�������һ��action�����ݴ洢
	}																				
}
