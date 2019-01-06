package entity.orMapping;

import java.util.ArrayList;
import java.util.List;
public class ClassMapping {
	private String className;
	private String table;
	private String id;
	private List<ClassProperty> propertyList;  //��һ��class �ĺܶ����Էŵ�һ��map�У�����key��column���б�ʶ

	public List<ClassProperty> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<ClassProperty> propertyList) {
		this.propertyList = propertyList;
	}

	public ClassMapping() {
		// TODO Auto-generated constructor stub		
		propertyList = new	ArrayList<ClassProperty>();	
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
