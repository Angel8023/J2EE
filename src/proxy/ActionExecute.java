package proxy;
import entity.ActionXml;
import entity.SingleActionLog;
import util.ClassReflector;

/*
 * ��������������ʵ����
 * */
public class ActionExecute implements ActionInterface {
	private ActionXml actionXml;

	public ActionExecute() {
		// TODO Auto-generated constructor stub
		this.actionXml = new ActionXml();
	}

	public ActionExecute(ActionXml actionXml) {
		this.actionXml = actionXml;
	}
		
	//ʵ��executeAction()����
	@Override
	public String executeAction() {
		// TODO Auto-generated method stub
		String resultName = null;		
		try {
			// ͨ��java ������ƣ�ͨ�������ͷ�������ȡ����Ӧ������ִ��
			resultName = ClassReflector.executeMethod(actionXml.getclassLocation(), actionXml.getMethod());
			// �������浥��action��Ϣ�� ��־����
			SingleActionLog singleActionLog = SingleActionLog.INSTANCE;
			// ���������ƺͷ��ؽ����¼��һ��action��
			singleActionLog.setName(actionXml.getName());
			singleActionLog.setResult(resultName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultName;
	}
}