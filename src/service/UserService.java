package service;

//UserService �Ǵ�����󣬴���RealUserִ�з���
import bean.UserBean;

public class UserService implements UserInterface {
	RealUser realUser; // ������Ķ���
	UserBean userBean; // ��������ݶ���

	public UserService(UserBean userBean) {
		// TODO Auto-generated constructor stub
		this.userBean = userBean;
	}

	@Override
	public boolean signIn() {	
		if (realUser == null) {			
			realUser = new RealUser(userBean);			
		}
		return realUser.signIn();
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

}
