package sc.ustc.dao;

public class UserDAO extends BaseDAO {	
	//��ʼ�����ݿ�������Ϣ
	
	//����mysql���ݿ�����
	public UserDAO() {
		setDriver("com.mysql.jdbc.Driver");
		setUrl("jdbc:mysql://localhost:3306/j2eedbs?useUnicode=true&amp;characterEncoding=UTF-8");
		setUserName("root");
		setUserPassword("");
	}	
	
	public Object queryByName(String userName){
		String sql = "select * from Users where userName = "+"\""+userName+"\"";
		return query(sql);
	}
}
