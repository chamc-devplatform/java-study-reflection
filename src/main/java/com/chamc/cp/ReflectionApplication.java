package com.chamc.cp;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.chamc.cp.Student.Teacher;
import com.chamc.cp.Student.Teacher.Grade;

@SpringBootApplication
public class ReflectionApplication {

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static void main(String[] args) throws Exception {
		/*
		 * 每个类只有一个Class对象
		 */
		Student student1 = new Student();
		Student student2 = new Student();
		System.out.println(student1 == student2);	//false
		System.out.println(student1.getClass() == student2.getClass());  //true
		
		/*
		 * 获取Class对象的4种方法
		 */
    	//第一种
    	Student stu1 = new Student();  //这一new 产生一个Student对象，一个Class对象。
		Class clazz = stu1.getClass(); //获取Class对象
    	System.out.println(clazz);
    	
    	
    	//第二种
    	Class clazz2 = Student.class;  
    	System.out.println(clazz == clazz2);// true 判断第一种方式获取的Class对象和第二种方式获取的是否是同一个
    	
    	//第三种（常用）
    	Class clazz3 = Class.forName("com.chamc.cp.Student");//真实路径，就是带包名的类路径，包名.类名
    	System.out.println(clazz == clazz3);// true 判断第一种方式获取的Class对象和第三种方式获取的是否是同一个
    	
    	//第四种
    	Class clazz4 = Integer.TYPE; //基本类型的包装类型提供了TYPE常量，返回一个对应的Class对象
    	System.out.println(clazz4);
    	
    	
    	/*
    	 * 获取类的构造方法
    	 */
    	Class clazz6 = Student.class;
    	Constructor<?>[] publicConstructors = clazz6.getConstructors();	//获得类所有的公共构造方法
    	Constructor<?>[] allConstructors = clazz6.getDeclaredConstructors();	//获得类所有的构造方法
    	Constructor<?> c1 = clazz6.getConstructor();	//获得类的无参构造方法(public)
    	Constructor<?> c2 = clazz6.getDeclaredConstructor(String.class, int.class);	//获得类的一个有参构造方法(非public)
    	
    	
    	/*
    	 * Declared方法
    	 */
    	Class clazz7 = Student.class;
    	Class[] ccc = clazz7.getDeclaredClasses();	//获取所有内部类
    	Class clazz8 = Teacher.class;
    	Class cc2 = clazz8.getDeclaringClass();	//Student 获取Teacher类的上一级父类
    	Class clazz9 = Grade.class;
    	Class cc3 = clazz9.getDeclaringClass();	//Teacher 获取Grade类的上一级父类

    	
    	/*
    	 * 类的成员变量解析
    	 */
    	Class clazz10 = Student.class;
    	Field[] fields = clazz10.getDeclaredFields();
    	for (Field field : fields) {
    		String fieldName = field.getName(); //变量名
    		Class<?> type = field.getType();	//变量的类型
    		Type genericType = field.getGenericType(); //变量的泛型
    		int m = field.getModifiers();	//返回int类型值表示该字段的修饰符
    		Annotation[] anno1 = field.getAnnotations();	//获取变量上的注解
    		Annotation[] anno2 = field.getDeclaredAnnotations(); //获取变量上的注解 
    	}
    	
    	Field ff = clazz10.getDeclaredField("age");
    	System.out.println(ff.getInt(new Student("chenshuaibi", 18)));	//18  获取对象种tag变量的值
    	
    	Class clazz11 = Teacher.class;
    	Annotation[] anno11 = clazz11.getAnnotations();	//获取类的所有注解（包括父类上的继承注解）
    	Annotation[] anno22 = clazz11.getDeclaredAnnotations(); //获取当前类上的注解

    	
    	/*
    	 * 类的属性解析
    	 */
    	Class clazz13 = Student.class;
    	Object stu = clazz13.newInstance();
    	PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
    	for (PropertyDescriptor pd : pds) {
    		if("class".equals(pd.getName())) {	//跳过class属性
				continue;
			}
//    		Object param = null;   //每个属性对应的值（这里有个坑）
//    		pd.getWriteMethod().invoke(stu, param);  //调用属性的set方法将值写入
    	}
    	
    	
    	/*
    	 * 新建类实例的4种方法
    	 */
    	Class clazz5 = Student.class;
    	
    	//第一种
    	Object student = clazz5.newInstance();
    	
    	//第二种
    	Constructor constructor = clazz5.getConstructor();
    	Object student3 = constructor.newInstance();	//调用无参构造方法
    	
    	//第三种
    	Constructor<?> constructor3 =  clazz5.getDeclaredConstructor(String.class, int.class);
    	Object[] params = {"test", 18}; 
    	Object student4 = constructor3.newInstance(params);	//调用有参构造方法，传入一个数组
    	
    	
    	/*
    	 * 越过泛型检查
    	 */
    	//泛型擦除
    	List<String> list1 = new ArrayList<String>(1);
    	List<Integer> list2 = new ArrayList<Integer>(1);
    	System.out.println(list1.getClass() == list2.getClass());  //true
    	
    	List<String> strList = new ArrayList<String>();
		strList.add("aaa");
		strList.add("bbb");
  //	strList.add(100);	//无法直接添加整型数据
		Class listClass = strList.getClass(); //得到 strList 的Class对象
		Method m = listClass.getMethod("add", Object.class); //获取add()方法
		m.invoke(strList, 100); //调用add()方法
		
		for(Object obj : strList){
			System.out.println(obj);
		}
		
		
		/*
		 * 运行配置文件
		 */
		Class clazz12 = Class.forName(getPropertyValue("className"));
		Method method = clazz12.getMethod(getPropertyValue("methodName"));
		method.invoke(clazz12.newInstance());
		
    }
	
	 
	//获取配置文件中对应key的值
	public static String getPropertyValue(String key) throws Exception {
		Properties prop = new Properties();
		InputStream in = ReflectionApplication.class.getClassLoader()
							.getResourceAsStream("application.properties");
		prop.load(in);
		return prop.getProperty(key);
	}
}
