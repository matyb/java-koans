package com.sandwich.koan;

import java.lang.reflect.Method;

import com.sandwich.koan.path.xmltransformation.KoanElementAttributes;
import com.sandwich.koan.path.xmltransformation.RbVariableInjector;
import com.sandwich.util.io.KoanSuiteCompilationListener;

public class KoanMethod {
	
	private final transient Method method;
	private final String lesson;
	private final boolean displayIncompleteException;
	private static final KoanSuiteCompilationListener listener = new KoanSuiteCompilationListener();
	
	private KoanMethod(KoanElementAttributes koanAttributes) throws SecurityException, NoSuchMethodException{
		this(null, koanAttributes);
	}
	
	public static KoanMethod getInstance(KoanElementAttributes koanAttributes){
		try {
			return new KoanMethod(koanAttributes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static KoanMethod getInstance(Method method){
		return new KoanMethod(null, method, true);
	}
	
	public static KoanMethod getInstance(String lesson, Method method){
		return new KoanMethod(lesson, method, true);
	}
	
	private KoanMethod(String lesson, Method method, boolean displayIncompleteException){
		if(method == null){
			throw new IllegalArgumentException("method may not be null");
		}
		this.method = method;
		this.lesson = new RbVariableInjector(lesson, method).injectLessonVariables();
		this.displayIncompleteException = displayIncompleteException;
	}

	public KoanMethod(String lesson, KoanElementAttributes koanAttributes) throws SecurityException, NoSuchMethodException {
		this(	lesson, 
				KoanClassLoader.getInstance().loadClass(koanAttributes.className, listener)
					.getMethod(koanAttributes.name),
				!"false".equalsIgnoreCase(koanAttributes.displayIncompleteKoanException));
	}

	public String getLesson() {
		return lesson;
	}

	public Method getMethod() {
		return method;
	}
	
	public boolean displayIncompleteException() {
		return displayIncompleteException;
	}
	
	public KoanMethod clone(Method method){
		return new KoanMethod(lesson, method, displayIncompleteException);
	}
	
	@Override public String toString(){
		return "{"+getMethod().getName()
				+" : "+ (lesson.length() > 20 ? lesson.substring(0, 20) + "..." : lesson)+"}";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (displayIncompleteException ? 1231 : 1237);
		result = prime * result + ((lesson == null) ? 0 : lesson.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KoanMethod other = (KoanMethod) obj;
		if (displayIncompleteException != other.displayIncompleteException)
			return false;
		if (lesson == null) {
			if (other.lesson != null)
				return false;
		} else if (!lesson.equals(other.lesson))
			return false;
		return true;
	}
	
	
}
