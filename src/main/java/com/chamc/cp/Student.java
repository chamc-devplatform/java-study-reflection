package com.chamc.cp;

import com.chamc.cp.annotation.Element;
import com.chamc.cp.annotation.InheritElement;

@InheritElement
public class Student {
	
	public static int TAG = 12345;
	
	@Element
	protected String name;
	
	public int age = -1;

	public Student() {
		
	}
	
	Student(String name, int age) {
		this.name = name;
		this.setAge(age);
	}
	
	@Element
	class Teacher extends Student{
		
		class Grade {
		}
	}
	
	public void show() {
		System.out.println("配置文件运行....");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
