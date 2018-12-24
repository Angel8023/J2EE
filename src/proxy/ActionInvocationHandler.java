package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
/*
 * ����ActionInvocataionHandler�࣬ʵ��InvocationHandler�ӿڣ�������г���һ������������ʵ��target��
 * InvocationHandler����һ��invoke����������ִ�д������ķ������ᱻ�滻��ִ��invoke������
 * ����invoke������ִ�б��������target����Ӧ������
 * */
import java.util.List;
import entity.InterceptorXml;
import entity.Interceptorref;
import util.ClassReflector;
import util.XmlParser;

public class ActionInvocationHandler implements InvocationHandler {
	// invocationHandler���еı��������
	private Object object; // ���뱻����Ķ���
	private XmlParser xmlParser; // �����xml�ļ��Ľ���
	private String actionName; // ������������

	// ͨ�����캯������Ҫ�õ��Ķ�������ݴ�����
	public ActionInvocationHandler(Object object, XmlParser xmlParser, String actionName) {
		this.object = object;
		this.xmlParser = xmlParser;
		this.actionName = actionName;				
	}

	/**
	 * proxy:����̬������� method����������ִ�еķ��� args���������Ŀ�귽��ʱ�����ʵ��
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		preAction();
		System.out.println("����ִ��" + method.getName() + "����");
		Object result = method.invoke(object, args);
		afterAction();
		return result;
	}

	//��ȡ��һ��action�������õ�����������
	private List<InterceptorXml> getInterceptorXmlList(String actionName) {				
		List<InterceptorXml> interceptorXmlList = new ArrayList<InterceptorXml>();				
		for (Interceptorref interceptorref : xmlParser.getControllerXml().getActionByName(actionName)
				.getInterceptorrefList()) {						
			for (InterceptorXml interceptorXml : xmlParser.getInterceptorXmlList()) {				
				if (interceptorXml != null && interceptorXml.getName().equals(interceptorref.getName()))
					interceptorXmlList.add(interceptorXml);
			}
		}
		return interceptorXmlList;
	}

	/*
	 * ����java ������ƣ�ͨ��InterceptorXml�����л�ȡ����������predo��afterdo������ ִ��predo��afterdo����
	 */
	private void preAction() {
		// TODO Auto-generated method stub
		// ����action�е�������������ִ��
		if (getInterceptorXmlList(actionName) != null) {
			for (InterceptorXml interceptorXml : getInterceptorXmlList(actionName)) {				
				try {
					ClassReflector.executeMethod(interceptorXml.getClassLocation(), interceptorXml.getPredo());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("ActoinInvcationHandler��ִ��predo()��������");
					e.printStackTrace();
				}
			}
		}
	}

	private void afterAction() {
		// TODO Auto-generated method stub
		// ����action�е�������������ִ��
		if (getInterceptorXmlList(actionName) != null) {
			for (InterceptorXml interceptorXml : getInterceptorXmlList(actionName)) {
				try {
					ClassReflector.executeMethod(interceptorXml.getClassLocation(), interceptorXml.getAfterdo());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("ActoinInvcationHandler��ִ��afterdo()��������");
					e.printStackTrace();
				}
			}
		}
	}
}
