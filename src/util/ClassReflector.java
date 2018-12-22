package util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassReflector {
	/*
	 * ���������õ������
	 */
	public static Class<?> getClass(String className) throws ClassNotFoundException {
		Class<?> cls = Class.forName(className);
		return cls;
	}

	/*
	 * ���������ͷ�������ִ�з���
	 */
	public static String executeMethod(Class<?> cls, Method method)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		String result = null;
		Object object = method.invoke(cls.newInstance());
		if (object instanceof String) {
			result = (String) object;
		}
		return result;
	}

	/*
	 * ͨ�������ͷ�������ִ�ж�Ӧ����
	 */
	public static String executeMethod(Class<?> cls, String methodName) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Method method = cls.getMethod(methodName);
		return executeMethod(cls, method);
	}

	/*
	 * ���������ͷ�������ִ�ж�Ӧ����ķ���
	 */
	public static String executeMethod(String className, String methodName)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException {
		Class<?> cls = getClass(className);
		return executeMethod(cls, methodName);
	}

}
