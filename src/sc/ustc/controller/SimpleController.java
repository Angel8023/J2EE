package sc.ustc.controller;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

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

public class SimpleController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String XML_FILE_NAME = "controller.xml";
	private XmlParser xmlParser;

	/*// �������浥��action��Ϣ�� ��־����
	SingleActionLog singleActionLog = SingleActionLog.INSTANCE;
	// ����log.xml���� ,ͨ������ģʽʵ�֣����еĶ���ά��ͬһ���ļ�
	LogXml logXml = LogXml.INSTANCE;*/

	// ͨ����ȡ��uri��ַ����uri���зָ�õ�action������
	private String getActionName(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return uri.substring(uri.lastIndexOf("/") + 1, uri.indexOf(".sc"));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// �ڶ���ʵ�����--------------------------
		// ��ȡaction����
		String actionName = getActionName(request);
		System.out.println(actionName);
		// ��ȡxml�ļ�·��
		String xmlPath = SimpleController.class.getClassLoader().getResource(XML_FILE_NAME).getPath();
		System.out.println(xmlPath);

		xmlParser = new XmlParser(xmlPath); // ��xml�ļ�·����������

		// ��xml�ļ�װ��controller ����
		try {
			xmlParser.setControllerXml();
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// �ֱ��ȡcontroller��interceptor����
		ControllerXml controllerXml = xmlParser.getControllerXml();
		// List<InterceptorXml> InterceptorXmlList =
		// xmlParser.getInterceptorXmlList();

		// �������浥��action��Ϣ�� ��־����
		//SingleActionLog singleActionLog = SingleActionLog.INSTANCE;
		// ����log.xml���� ,ͨ������ģʽʵ�֣����еĶ���ά��ͬһ���ļ�
		//LogXml logXml = LogXml.INSTANCE;

		boolean isFoundAction = false; // �ж��Ƿ����ҵ�ָ��action
		boolean isFoundResult = false;
		// ���xml�ļ��е����ݲ���
		for (ActionXml actionXml : controllerXml.getActionList()) {
			if (actionName.equals(actionXml.getName())) {
				String resultName = null;
				isFoundAction = true; // �ҵ�ָ��action
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

					/*
					//��ȡlog.xml �ļ�·��
					String logXmlPath = SimpleController.class.getClassLoader().getResource("log.xml").getPath();
					System.out.println(logXmlPath);
					//String logXmlPath = "src/log.xml";
					// �Ȼ�ȡ����ʷ����־��Ϣ����װ�Ķ���					
					LogUtil logUtil = new LogUtil(logXmlPath);
					logUtil.readLog();

					// ������action��¼����־��
					ActionLog actionLog = new ActionLog(singleActionLog);
					logXml.addAction(actionLog);
					// ��ʾ��־��Ϣ
					logXml.showLog();
					// ����־д��log.xml�ļ�
					logUtil.writeLog();*/

					// System.out.println(resultName);
					for (ResultXml resultXml : actionXml.getResultList()) {
						// System.out.println(resultXml.getName());
						if (resultName != null && resultName.equals(resultXml.getName())) {
							isFoundResult = true;
							System.out.println(resultXml.getType() + " : " + resultXml.getValue());
							// �ж�����������ת�������ض���
							if ("forward".equals(resultXml.getType())) {
								// ת������
								request.setCharacterEncoding("UTF-8"); // �����ַ�����
								request.getRequestDispatcher(resultXml.getValue()).forward(request, response);
							} else {
								// ����������ض���
								response.setContentType("text/html;charset=utf-8"); // �����ַ�����
								response.sendRedirect(resultXml.getValue());
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (!isFoundAction) {
			System.out.println("����ʶ��� action ����");
		} else {
			if (!isFoundResult)
				System.out.println("û���������Դ");
		}
		/*
		 * //��һ��ʵ�����-------------------------- //����
		 * //System.out.println("hello"); //��ȡҳ������� PrintStream out = new
		 * PrintStream(response.getOutputStream()); //��ȡҪ��ȡ��html�ļ�·�� String
		 * filePath =
		 * request.getSession().getServletContext().getRealPath("/welcome.html")
		 * ; BufferedReader br = new BufferedReader(new FileReader(filePath));;
		 * String line=""; while((line = br.readLine())!= null){
		 * //�����������ʽ���htmlҳ�� out.println(line); }
		 */
	}

	// ��������������ҳ��
	private void printPage(HttpServletResponse response) throws IOException {
		// ��ȡҳ�������
		PrintStream out = new PrintStream(response.getOutputStream());
		// ���HTMLҳ���ǩ
		out.println("<html>");
		out.println("<head>");
		out.println("<title>SimpleController</title>");
		out.println("</head>");
		out.println("<body>��ӭʹ��SimpleContreller!</body>");
		out.println("</html>");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
