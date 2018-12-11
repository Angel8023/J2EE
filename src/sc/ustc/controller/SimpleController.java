
package sc.ustc.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.Xml2Html;
import util.XmlParser;

public class SimpleController extends HttpServlet{					
	private static final long serialVersionUID = 1L;
	private static final String XML_FILE_NAME = "controller.xml";
	private XmlParser parser;
	
	
	//ͨ����ȡ��uri��ַ����uri���зָ�õ�action������
	private String getActionName(HttpServletRequest request){
		String uri = request.getRequestURI();
		return uri.substring(uri.lastIndexOf("/")+1, uri.indexOf(".sc"));
	}
	
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		//�ڶ���ʵ�����--------------------------
		//��ȡaction����
		String actionName = getActionName(request);
		System.out.println(actionName);
		//��ȡxml�ļ�·��
		String xmlPath = 
				SimpleController.class.getClassLoader().getResource(XML_FILE_NAME).getPath();
		System.out.println(xmlPath);
		
		
		//parser = new XmlParser(xmlPath);
		
		/*
		String result = ProxyImplAssistant(actionName, request, response);
		String html = translateResult(result);
		if (html != null) {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write(html);
		} else {
			response.sendRedirect(result);
		}
		*/
																																						
		/*
		//��һ��ʵ�����--------------------------
		//����		
		//System.out.println("hello");			
		//��ȡҳ�������
	    PrintStream out = new PrintStream(response.getOutputStream());
		//��ȡҪ��ȡ��html�ļ�·��
		String filePath = 
				request.getSession().getServletContext().getRealPath("/welcome.html"); 		
		BufferedReader br = new BufferedReader(new FileReader(filePath));;		
		String line="";
		while((line = br.readLine())!= null){
			//�����������ʽ���htmlҳ��
			out.println(line);
		}
		*/							    
	}		
	
	private String ProxyImplAssistant(String actionName, Object... args) {
		String result = null;
		/*
		Executor executor = new ActionsExecutor(parser, actionName);
		ActoinProxy h = new ActoinProxy(executor, parser, actionName);
		Class<?> cls = executor.getClass();
		Executor proxy = (Executor) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), h);
		String result = proxy.executeAction(args);
		*/
		return result;
	}
	
	
	private String translateResult(String result) {
		String rear = "_view.xml";
		result = result.trim();
		if (result.endsWith(rear)) {
			// ���result��Ӧ���ļ�·��
			String path = SimpleController.class.getClassLoader().getResource("../../").getPath();
			String xslFilePath = path + result;
			// ���������׺Html�ļ�
			File file = new File(xslFilePath);
			return Xml2Html.translateXml2Html(file);
		} else {
			return null;
		}
	}	
	
	//��������������ҳ��
	private void printPage(HttpServletResponse response) throws IOException{		
		//��ȡҳ�������
		PrintStream out = new PrintStream(response.getOutputStream());				
		//���HTMLҳ���ǩ
		out.println("<html>");
		out.println("<head>");
		out.println("<title>SimpleController</title>");
		out.println("</head>");
		out.println("<body>��ӭʹ��SimpleContreller!</body>");
		out.println("</html>");				
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		doPost(request, response);		       
	}	
}
