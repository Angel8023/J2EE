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
	  
	 /* for(ActionXml actionXml2 : actionXmllist){
		  System.out.println(actionXml2.getMethod());
	  }*/
	  //��ȡ��ǰ��controller
	  controllerXml.setActionList(actionXmllist);	  
	  actionXmllist.clear();  //ÿ���������Ҫ��list��գ�������һ��action�����ݴ洢
	}																	
	
	/*
	private static final String CTRL_NODE_NAME = "controller";	
	private static final String ACTION_NODE_NAME = "action";
	private static final String RESULT_NODE_NAME = "result";
	private static final String ATTR_NAME = "name";
	private static final String NONE_STR = "";	
	
	private static final String INTERCEPTOR_NODE_TYPE = "interceptor";					
	
	public Element matchInterceptor (String interceptorName) throws DocumentException {
		//�õ�Document����
		Document document = getDocument(xmlFilePath);
		//��ȡXML���ڵ�
		Element root = document.getRootElement();
		return matchElement(INTERCEPTOR_NODE_TYPE ,interceptorName, root);
	}
	
	public Element matchAction(String actionName) throws DocumentException {
		//�õ�Document����
		Document document = getDocument(xmlFilePath);
		//��ȡXML���ڵ�
		Element root = document.getRootElement();
		//�õ�controller�ڵ�
		Element elementCtrl = root.element(CTRL_NODE_NAME);
		
		return matchElement(ACTION_NODE_NAME ,actionName, elementCtrl);
	}
	
	public Element matchResult(String parentActionName, String resultName) throws DocumentException {
		//�õ�Document����
		Document document = getDocument(xmlFilePath);
		//��ȡXML���ڵ�
		Element root = document.getRootElement();
		//�õ�controller�ڵ�
		Element elementCtrl = root.element(CTRL_NODE_NAME);
		//�õ��ض���action�ڵ�
		Element elementAction = matchElement( ACTION_NODE_NAME ,parentActionName, elementCtrl);
		
		return matchElement(RESULT_NODE_NAME ,resultName, elementAction);
	}
	
	public Element matchElement (String elementType, String elementName, Element parentElement ) {
		return matchElement(elementType,ATTR_NAME,elementName,parentElement);
	}
	
	public static Element matchElement (String elementType,String attrType, String attrName, Element parentElement ) {
		Element matchedElement = null;
		//��ȡparentElement�ڵ�������ֱ�ӽ���б�
		List<Element> elementList = getElementNodeList(parentElement,elementType);
		//�ж�parentElement�ڵ����Ƿ��нڵ�
		if ( elementList.size() == 0 ) {
			throw new RuntimeException(parentElement.getName()+"�ڵ�����û�нڵ�");
		}
		//��������elementList
		for (Element element : elementList) {
			//ȥ�������elementNameǰ��ո�
			attrName = attrName.trim();
			//�õ�name���Զ�Ӧ��ֵ
			String attrValue = getAttributeValue(element,attrType);
			//ƥ��
			if ( NONE_STR.equals( attrName ) ) {
				throw new RuntimeException("attrName����Ϊȫ�ո�");
			} else if ( attrName.equals( attrValue ) ) {
				matchedElement = element;
				break;
			}
		}
		//����������elementList,û���ҵ�ƥ��elementName��element
		if (matchedElement == null) {
			throw new RuntimeException( attrName + "����ƥ�䵽���ʵ�" + elementType );
		}
		return matchedElement;
	}
	
	public static String getAttributeValue( Element element , String attrName) {
		Attribute attribute = element.attribute(attrName.trim());
		if(attribute == null) {
			throw new RuntimeException("Ԫ�ؽڵ�"+element.getName()+"���Ҳ���");
		}
		return attribute.getValue().trim();
	}
	
	public static String getNodeContent( Element element ) {
		return element.getTextTrim();
	}
	
	@SuppressWarnings("unchecked")
	private static List<Element> getElementNodeList(Element root, String elementType) {
		return  root.elements(elementType);
	}
		
	*/			
}
