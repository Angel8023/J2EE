package water.ustc.interceptor;

import java.text.DateFormat;
import java.util.Date;

import entity.LogXml;
import entity.SingleActionLog;

public class LogInterceptor {

	DateFormat dateFormat = DateFormat.getDateTimeInstance();
	
	//����ĳ��action�ĵ���
	SingleActionLog singleActionLog = SingleActionLog.INSTANCE;

	public void preAction() {
		String startTime = dateFormat.format(new Date());
		singleActionLog.setStime(startTime);   //��¼���ʿ�ʼʱ��
		System.out.println("Action������������ǰLogInterceptor preAction:" + startTime);
	}

	public void afterAction() {
		String endTime = dateFormat.format(new Date());
		singleActionLog.setEtime(endTime);   //��¼���ʽ���ʱ��
		System.out.println("Action�����������غ�LogInterceptor afterAction:" + endTime);
	}
}
