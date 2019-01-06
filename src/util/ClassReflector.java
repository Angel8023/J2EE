package util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClassReflector {
	/*
	 * ���������õ������
	 */
	public static Class<?> getClass(String className) throws ClassNotFoundException {
		Class<?> cls = Class.forName(className);
		return cls;
	}

	/*
	 * ���������ͷ�������ִ��û�в����ķ���
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
	 * ���������ͷ��������в�����ִ�д��в����ķ���
	 */
	public static String executeMethod(String className, String methodName, Object... args)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException {
		String result = null;
		Class<?> cls = getClass(className);
		Method method = cls.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
		Object object = method.invoke(cls.newInstance(), args);
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

	/*
	 * ���ݶ���������������ֵ������Ӧ���Ը�ֵ
	 */
	public static void setField(Object object, String name, Object obj) {
		try {						
			Field field = object.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(object, obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * ��ȡĳ������ĳ�����Ե�ֵ��ֻ֪��object �������������
	 * ͨ�����øö������Ӧget��������ȡ����ֵ
	 * */
	public static Object getValueByName(Object obj, String name){
		 try {    
	           String firstLetter = name.substring(0, 1).toUpperCase();    
	           String getter = "get" + firstLetter + name.substring(1);  	           
	           Method method = obj.getClass().getMethod(getter, new Class[] {});    
	           Object value = method.invoke(obj, new Object[] {});    
	           return value;    
	       } catch (Exception e) {    
	    	   e.printStackTrace();
	           return null;    
	       }    
	} 	
}
