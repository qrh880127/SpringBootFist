package com.twj.hello.sourceRead.reflect.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.twj.hello.sourceRead.reflect.ReflectionUtils;
import com.twj.hello.sourceRead.reflect.ReflectionUtils.FieldCallback;
import com.twj.hello.sourceRead.reflect.ReflectionUtils.MethodCallback;
import com.twj.hello.user.domain.User;

import lombok.Data;

public class test {
	public static void main(String[] args) throws Exception {
		User u = new User();
		u.setId("id");
		Field field = ReflectionUtils.findField(u.getClass(), "id");
		ReflectionUtils.makeAccessible(field);
		Object a = ReflectionUtils.getField(field, u);
		System.out.println(a);
		
		
		@Data
		class U2 extends User{
			private String s2;
			
		}
		U2 u2 = new U2();
		ReflectionUtils.doWithLocalMethods(u2.getClass(), new MethodCallback() {
			@Override
			public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
				System.out.println(method.getName());
			}
			
		});

		System.out.println("\n---doWithLocalFields---");
		ReflectionUtils.doWithLocalFields(u2.getClass(), new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				System.out.println(field.getName());
			}
		});

		System.out.println("\n---doWithFields---");
		ReflectionUtils.doWithFields(u2.getClass(), new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				System.out.println(field.getName());
			}
		});
	}
}
