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
import entity.InterceptorXml;
import entity.Interceptorref;
import entity.ResultXml;

public class XmlParser {
	private String xmlFilePath; // xml �ļ�·��
	private ControllerXml controllerXml; // ����controllerxml����
	private List<InterceptorXml> interceptorXmlList; // ����interceptor����

	public XmlParser(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
		controllerXml = new ControllerXml();
		interceptorXmlList = new ArrayList<InterceptorXml>();
	}

	private Document getDocument(String url) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		return document;
	}

	public String getElementValue(Element element, String name) {
		return element.attribute(name).getValue();
	}

	public ControllerXml getControllerXml() {
		return this.controllerXml;
	}

	public List<InterceptorXml> getInterceptorXmlList() {
		return this.interceptorXmlList;
	}

	public void setControllerXml() throws DocumentException {
		List<ActionXml> actionXmllist = new ArrayList<ActionXml>(); // ����actionxml����
		List<ResultXml> resultList = new ArrayList<ResultXml>(); // ����resultxml
		List<Interceptorref> interceptorrefList = new ArrayList<Interceptorref>(); // ����interceptorrefList����

		Document document = getDocument(this.xmlFilePath); // ��ȡxml�ļ�
		Element rootElement = document.getRootElement();// ��ȡ���ڵ�

		// �õ�interceptor�ڵ㣬�������ж���������������б���
		for (Iterator<?> interceptorIterator = rootElement.elementIterator(); interceptorIterator.hasNext();) {
			InterceptorXml interceptorXml = new InterceptorXml();
			Element interceptorElement = (Element) interceptorIterator.next();
			if ("interceptor".equals(interceptorElement.getName())) {
				interceptorXml.setAll(getElementValue(interceptorElement, "name"),
						getElementValue(interceptorElement, "class"), getElementValue(interceptorElement, "predo"),
						getElementValue(interceptorElement, "afterdo"));
				interceptorXmlList.add(interceptorXml); // ������������������б���
			}
		}

		// �õ�controller�ڵ�
		Element elementCtrl = rootElement.element("controller");
		// �ֱ��ÿ��action�ӽڵ���е���
		for (Iterator<?> actionIterator = elementCtrl.elementIterator(); actionIterator.hasNext();) {
			ActionXml actionXml = new ActionXml();
			Element actionElement = (Element) actionIterator.next();
			// �ֱ��ÿ��result�ӽڵ���е���
			for (Iterator<?> resultIterator = actionElement.elementIterator(); resultIterator.hasNext();) {
				// ��ȡaction �����õ���������Ϣ
				Interceptorref interceptorref = new Interceptorref();
				// ��ȡaction�����õ�result��Ϣ
				ResultXml resultXml = new ResultXml();

				Element resultElement = (Element) resultIterator.next();
				if ("interceptor-ref".equals(resultElement.getName())) {
					interceptorref.setName(getElementValue(resultElement, "name"));
					interceptorrefList.add(interceptorref);
				}
				if ("result".equals(resultElement.getName())) {
					resultXml.setAll(getElementValue(resultElement, "name"), getElementValue(resultElement, "type"),
							getElementValue(resultElement, "value"));
					resultList.add(resultXml);
				}

			}
			// ��ȡ��ǰ��action
			actionXml.setResultList(resultList);
			actionXml.setInterceptorrefList(interceptorrefList);
			interceptorrefList.clear(); //// ÿ���������Ҫ��list��գ�������һ��interceptor�����ݴ洢
			resultList.clear(); // ÿ���������Ҫ��list��գ�������һ��result�����ݴ洢
			actionXml.setAll(getElementValue(actionElement, "name"), getElementValue(actionElement, "class"),
					getElementValue(actionElement, "method"));
			// �ѵ�ǰ��action����list��
			actionXmllist.add(actionXml);
		}
		// ��ȡ��ǰ��controller
		controllerXml.setActionList(actionXmllist);
		actionXmllist.clear(); // ÿ���������Ҫ��list��գ�������һ��action�����ݴ洢
	}
}
