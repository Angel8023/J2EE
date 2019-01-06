package service;

import bean.UserBean;
import sc.ustc.dao.UserDAO;

public class RealUser implements UserInterface {
	UserBean userBean;  //��ѯ���ݿ�õ���UserBean����
	String userPass;
	
	public RealUser(UserBean userBean) {
		// TODO Auto-generated constructor stub		
		this.userBean = userBean;	
		userPass = userBean.getUserPass();
		loadUserFromDbs();
	}
	
	public void loadUserFromDbs(){
		System.out.println("δ�������ݿ�֮ǰ�� "+userBean);
		UserDAO userDAO = new UserDAO();
		userBean = ((UserBean) userDAO.queryById(userBean.getUserId()));	
		System.out.println("�������ݿ�֮�� �� "+userBean);
	}

	@Override
	public boolean signIn() {
		// TODO Auto-generated method stub		
		return userPass.equals(userBean.getUserPass()); 							
	}
}
