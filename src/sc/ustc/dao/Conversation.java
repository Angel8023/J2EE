package sc.ustc.dao;

import java.util.List;
import java.util.Stack;

import com.sun.xml.internal.bind.v2.model.core.ID;

import entity.orMapping.ClassMapping;
import util.ClassReflector;

/*Conversation ������ɽ��������ӳ��Ϊ���ݱ���������� Conversation �ж������ݲ��� CRUD ������
 * ÿ������������������ͳ�Ŀ�����ݿ�� DML �� DDL��ͨ�� JDBC ������ݳ־û�*/

//��ʵ�ֶԶ��������ɾ���顢�Ĺ���
public class Conversation {
	private static Configuration configuration;

	public static boolean addObject(Object obj) {
		return false;
	}

	public static boolean deleteObject(Object obj) {
		return false;
	}

	public static Object getObject(Object obj) {
		// �������ļ��л�ȡ��table �� �����ӳ����Ϣ				
		configuration = new Configuration();			
		List<ClassMapping> classMappingList = configuration.getClassMappingList();
		String sql = null;

		// �������еı��Է���Ҫ��ı���в���
		for (ClassMapping clm : classMappingList) {				
			if (obj.getClass().getSimpleName().equals(clm.getClassName())) {
				//��Ϊ���÷���ʱ����Ҫ���������İ��������������Դ˴���clm�е�������Ϊ������
				clm.setClassName(obj.getClass().getName());				
				//���õ����󣬴Ӷ��õ������id����,������Ϊclm.getId()��ֵ			
				String id = (String)ClassReflector.getValueByName(obj, clm.getId());				
				
				StringBuilder stringBuilder = new StringBuilder("select * from ");
				stringBuilder.append(clm.getTable());
				stringBuilder.append(" where ");
				stringBuilder.append(clm.getId()+"= \"");
				stringBuilder.append(id + "\"");
				sql = stringBuilder.toString();												
				return new BaseDAO().query(sql, clm);								
			}
		}
		return null;
	}

	public static boolean updateObject(Object obj) {
		return false;
	}
}
