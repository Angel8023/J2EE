
package sc.ustc.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import entity.ActionXml;
import entity.ControllerXml;
import entity.ResultXml;
import util.XmlParser;

public class SimpleController extends HttpServlet{					
	private static final long serialVersionUID = 1L;
	private static final String XML_FILE_NAME = "controller.xml";
	private XmlParser xmlParser;
	
	
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
				
		xmlParser = new XmlParser(xmlPath);   //��xml�ļ�·����������
		
		//��xml�ļ�װ��controller ����
		try {
			xmlParser.setControllerXml();
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ControllerXml controllerXml = xmlParser.getControllerXml();
		boolean isFoundAction = false;   //�ж��Ƿ����ҵ�ָ��action
		boolean isFoundResult = false;
		//���xml�ļ��е����ݲ���
		for(ActionXml actionXml : controllerXml.getActionList()){
			if(actionName.equals(actionXml.getName())){
				isFoundAction = true;		//�ҵ�ָ��action						
				Class<?> theAction = null;   
				try{
					//ͨ��java ������ƣ�ͨ��������ȡ��java ��
					theAction = Class.forName(actionXml.getclassLocation());
					//ͨ��java ������ƣ�ͨ�������ͷ�������ȡ����Ӧ������ִ��
					Method method=theAction.getMethod(actionXml.getMethod());
					//ִ�ж�Ӧ����
					String resultName = (String) method.invoke(theAction.newInstance());
					//System.out.println(resultName);
					for(ResultXml resultXml : actionXml.getResultList()){
						//System.out.println(resultXml.getName());
						if(resultName!=null && resultName.equals(resultXml.getName())){
							isFoundResult = true;
							System.out.println(resultXml.getType()+" : "+resultXml.getValue());
							//�ж�����������ת�������ض���
							if("forward".equals(resultXml.getType())){
								//ת������								
								request.setCharacterEncoding("UTF-8");  //�����ַ�����
								request.getRequestDispatcher(resultXml.getValue()).forward(request, response);
							}else{
								//����������ض���
								response.setContentType("text/html;charset=utf-8"); //�����ַ�����
								response.sendRedirect(resultXml.getValue());
							}
						}
					}					
				}catch(Exception e){
					e.printStackTrace();					
				} 															
			}		
		}
		if(!isFoundAction){
			System.out.println("����ʶ��� action ����");			
		}else {
			if(!isFoundResult) System.out.println("û���������Դ");
		}																																											
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
