
package sc.ustc.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleController extends HttpServlet{					
	private static final long serialVersionUID = 1L;		
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {	
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
