package com.example.controller;

public class Singleton {
	
	private static Singleton instance = new Singleton();
	
	public Singleton() {
		
	}
	
	public static Singleton getInstance() {
		return instance;
	}
	public static void main(String args[]) {
		Singleton obj1= Singleton.getInstance();
		Singleton obj2= Singleton.getInstance();
		System.out.println(obj1 == obj2);
	}
}
