package entity;

import java.util.ArrayList;
import java.util.List;
/*
 * ʹ�õ���ģʽ��ʵ�ֶ���־�ļ���ͬ��
 * ʹ��ö��д��ʵ�ֵ���ģʽ
 * ʹ��ö�ٳ����̰߳�ȫ�ͷ�ֹ����ǿ�е��ù�����֮�⣬���ṩ���Զ����л����ƣ���ֹ�����л���ʱ�򴴽��µĶ���
 * ��ˣ�Effective Java�Ƽ������ܵ�ʹ��ö����ʵ�ֵ�����
 * */
public enum LogXml {
	INSTANCE;

	public static List<ActionLog> actionList = new ArrayList<ActionLog>();
	
	public void showLog(){
		for(ActionLog actionLog : actionList){
			System.out.println(actionLog.toString());
		}
	}

	// ��������е�������־��Ϣ
	public void clear() {
		this.actionList.clear();
	}

	// ��action�б������action��־��Ϣ
	public void addAction(ActionLog actionLog) {
		this.actionList.add(actionLog);
	}

	public List<ActionLog> getActionList() {
		return this.actionList;
	}

	public void setActionList(List<ActionLog> actionLogList) {
		for (ActionLog actionLog : actionLogList) {
			this.actionList.add(actionLog);
		}
	}

}
