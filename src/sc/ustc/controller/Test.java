package sc.ustc.controller;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import entity.ActionLog;
import entity.ActionXml;
import entity.ControllerXml;
import entity.LogXml;
import entity.ResultXml;
import entity.SingleActionLog;
import proxy.ActionExecute;
import proxy.ActionInterface;
import proxy.ActionInvocationHandler;
import util.ClassReflector;
import util.LogUtil;
import util.XmlParser;

public class Test {
	public static void main(String[] args) throws DocumentException, ClassNotFoundException {		
		String actionName = "login";
		String xmlPath = "src/controller.xml";
		XmlParser xmlParser = new XmlParser(xmlPath);
		xmlParser.setControllerXml();
		ControllerXml controllerXml = xmlParser.getControllerXml();							

		// ������ʵ�����
		// ���xml�ļ��е����ݲ���
		for (ActionXml actionXml : controllerXml.getActionList()) {
			if (actionName.equals(actionXml.getName())) {
				String resultName = null;
				try {
					// �õ�action�������ж��Ƿ�������������
					// ������������������� action ִ��֮ǰ�� ִ���������� predo()������
					// ���� action ִ��֮��ִ���������� afterdo()����
					// ���û����������������ֱ�ӷ���Ŀ��ation
					if (actionXml.getInterceptorrefList().size() != 0) {
						System.out.println("��������������ʹ�ö�̬�����action������д���");
						/************** java ��̬���� ****************/
						// ����һ��ʵ��������������Ǳ�����Ķ���
						ActionInterface theAction = new ActionExecute(actionXml);
						// ����һ�����������������InvocationHandler
						InvocationHandler actionHandler = new ActionInvocationHandler(theAction, xmlParser, actionName);

						// ����һ���������actionProxy������zhangsan����������ÿ��ִ�з��������滻ִ��Invocation�е�invoke����
						ActionInterface actionProxy = (ActionInterface) Proxy.newProxyInstance(
								ActionInterface.class.getClassLoader(), new Class<?>[] { ActionInterface.class },
								actionHandler);
						// ����ִ��executeAction()�ķ���
						resultName = actionProxy.executeAction();
					} else {
						System.out.println("δ������������ֱ�Ӷ�������д���");
						// ͨ��java ������ƣ�ͨ�������ͷ�������ִ�ж�Ӧ��Ķ�Ӧ����
						resultName = ClassReflector.executeMethod(actionXml.getclassLocation(), actionXml.getMethod());
					}					

					for (ResultXml resultXml : actionXml.getResultList()) {
						// System.out.println(resultXml.getName());
						if (resultName != null && resultName.equals(resultXml.getName())) {
							System.out.println(resultXml.getType() + " : " + resultXml.getValue());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// �ڶ���ʵ�����
		// ���xml�ļ��е����ݲ���

		for (ActionXml actionXml : controllerXml.getActionList()) {
			if (actionName.equals(actionXml.getName())) {
				//isFound = true;
				// ͨ��java������ƣ�ͨ��������ȡ��java ��
				System.out.println(Class.forName(actionXml.getclassLocation()));
				System.out.println(actionXml.getMethod());

				Class<?> theAction = null;
				try {
					// ͨ��java ������ƣ�ͨ��������ȡ��java ��
					theAction = Class.forName(actionXml.getclassLocation()); // ͨ��java
					// ������ƣ�ͨ�������ͷ�������ȡ����Ӧ������ִ��
					Method method = theAction.getMethod(actionXml.getMethod()); // ִ�ж�Ӧ����
					//String resultName = (String) method.invoke(theAction.newInstance()); 
					// ͨ��java ������ƣ�ͨ�������ͷ�������ִ�ж�Ӧ��Ķ�Ӧ����
					String resultName = ClassReflector.executeMethod(actionXml.getclassLocation(),
							actionXml.getMethod());
					// System.out.println(resultName);
					for (ResultXml resultXml : actionXml.getResultList()) {
						// System.out.println(resultXml.getName());
						if (resultName != null && resultName.equals(resultXml.getName())) {
							System.out.println(resultXml.getType() + " : " + resultXml.getValue());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		SAXReader reader = new SAXReader();
		Document document = reader.read(xmlPath);

		Element rootElement = document.getRootElement();// ��ȡ���ڵ�
		System.out.println(rootElement.getName().toString());

		// �����������Ӹ��ڵ㿪ʼ������xml���ṹ���н���

		for (Iterator<?> iterator = rootElement.elementIterator(); iterator.hasNext();) { 
			// �ֱ��ÿ���ӽڵ���е���
			Element element = (Element)iterator.next();
			System.out.println(element.getName());

			List<Element> list2 = element.elements();
			for (Element e : list2) {
				System.out.println(e.getText());
			}

			for (Iterator<?> iterator2 = element.elementIterator(); iterator2.hasNext();) {
				Element element2 = (Element) iterator2.next();
				System.out.print(element2.getName() + ":\t");

				// ��ȡĳ���ڵ��ÿ������ֵ 
				String nameValue = element2.attribute("name").getValue();// ��ȡԪ�ص�login���Զ���
				System.out.println(nameValue);

				String classValue = element2.attribute("class").getValue();//
				// ��ȡԪ�ص�class���Զ��� System.out.println(classValue);

				for (Iterator<?> iterator3 = element2.elementIterator(); iterator3.hasNext();) {
					Element element3 = (Element) iterator3.next();
					System.out.println(element3.getName());

					String nameValue1 = element3.attribute("name").getValue();
					System.out.println(nameValue1);

					String typeValue = element3.attribute("type").getValue();
					System.out.println(typeValue);

					String pageValue = element3.attribute("value").getValue();
					System.out.println(pageValue);
				}
			}
		}

	}
}
