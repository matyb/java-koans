package com.sandwich.koan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.Thread.State;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import com.sandwich.koan.constant.KoanConstants;

public class TestUtils {

	private static Random random = new Random(System.currentTimeMillis());
	public static final String EOL = System.getProperty("line.separator");
	public static final int MAX_UNIQUE_CHARS = 1000;
	
	public static long sizeInBytes(Serializable... objects) {
		byte[] ba = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(objects);
			oos.close();
			ba = baos.toByteArray();
			baos.close();
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
		return ba.length;
	}
	
	public static void chewUpVM(){
		chewUpVM(100);
	}
	
	public static void chewUpVM(long forApproxHowLongInMs){
		long start = System.currentTimeMillis();
		while(System.currentTimeMillis() - start > forApproxHowLongInMs){new Object();}
	}

	public static String getRandomText(int i) {
		return getRandomText(i,false);
	}

	public static String getRandomText(int i, boolean enforceUnique) {
		if(enforceUnique && i > MAX_UNIQUE_CHARS){
			throw new RuntimeException("call getRandomText w/ a value under 1000 if u wish to enforce unique");
		}
		StringBuilder sb = new StringBuilder();
		int j = 0;
		while(j < i){
			char nextChar = (char)random.nextInt();
			if(enforceUnique && sb.indexOf(String.valueOf(nextChar)) != -1){
				continue;
			}
			sb.append(nextChar);
			j++;
		}
		return sb.toString();
	}
	
	public static long time(Runnable runnable) {
		long start = System.currentTimeMillis();
		runnable.run();
		return System.currentTimeMillis() - start;
	}
	
	public static void forEachLine(String text, ArgRunner<String> runnable){
		String[] strings = text.split(KoanConstants.EOLS, -1);
		for(String line : strings){
			runnable.run(line);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T serialize(T t) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(t);
			oos.close();
			final byte[] ba = baos.toByteArray();
			baos.close();
			ByteArrayInputStream bais = new ByteArrayInputStream(ba);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object returnValue = ois.readObject();
			ois.close();
			return (T) returnValue;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getValue(String fieldName, Object target) {
		Class<?> clazz = target instanceof Class<?> ? (Class<?>)target : target.getClass();
		Field field = getField(clazz, fieldName);
		try {
			boolean wasAccessible = field.isAccessible();
			field.setAccessible(true);
			Object value = field.get(target);
			field.setAccessible(wasAccessible);
			return (T)value;
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}	
	
	public static void setValue(String fieldName, Object value, Object target) {
		Class<?> clazz = target instanceof Class<?> ? (Class<?>)target : target.getClass();
		Field field = getField(clazz, fieldName);
		try {
			boolean wasAccessible = field.isAccessible();
			field.setAccessible(true);
			field.set(target, value);
			field.setAccessible(wasAccessible);
			return;
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Field getField(Class<?> clazz, String fieldName){
		String className = clazz.getName();
		do{
			for(Field field : clazz.getDeclaredFields()){
				if(field.getName().equals(fieldName)){
					return field;
				}
			}
			clazz = (Class<?>)clazz.getSuperclass();
		}while(clazz != null);
		throw new IllegalArgumentException(fieldName+" was not found in class: "+className);
	}
	
	/**
	 * Used to simulate multiple threads accessing the same method whilst busy.
	 * Will fail in insufficiently concurrent scenarios.
	 * 
	 * @param assertion
	 * @param rs
	 * @throws InterruptedException
	 */
	public static void doSimultaneouslyAndRepetitively(TwoObjectAssertion assertion, final Runnable... rs) throws InterruptedException{
		assertTrue("Whats the point of testing for synchronization w/ less than two threads?", rs.length > 1);
		final int[] c = new int[]{0};
		int startRuns = rs.length + 9;
		final Thread[] threads = new Thread[rs.length];
		int totalRuns = 0;
		for(int i = 0; i < rs.length; i++){
			final int it = i;
			final int startRunsTemp = startRuns;
			totalRuns += startRunsTemp;
			startRuns--;
			threads[i] = new Thread(new Runnable(){
				public void run() {
					Runnable runnable = rs[it];
					for(int j = 0; j < startRunsTemp; j++){
						runnable.run();
						c[0]++;
					}
				}
			});
			threads[i].start();
		}
		allowThreadsToFinish(threads);
		assertion.assertOn("Threads died before they could finish.", totalRuns, c[0]);
	}

	public static void doSimultaneouslyAndRepetitively(TwoObjectAssertion assertion,
			Class<? extends Throwable> anticipatedExceptionClass,
			final Runnable... rs) throws InterruptedException{
		PrintStream temp = System.err;
		try {
			ByteArrayOutputStream sysErr = new ByteArrayOutputStream();
			System.setErr(new PrintStream(sysErr));
			doSimultaneouslyAndRepetitively(assertion, rs);
			String errors = sysErr.toString();
			int i = rs.length;
			assertFalse(errors.contains("Thread-"+(i+1)));
			for(;i > 0; i--){
				assertTrue(errors.contains("Thread-"+i+"\" "+anticipatedExceptionClass.getName()));
			}
		} finally {
			System.setErr(new PrintStream(temp));
		}
	}
	
	/**
	 * Will lock Thread accessible when called from Thread via repetitive sleep
	 * calls until the passed in threads are terminated. Generally used to keep
	 * the test exec thread alive while the other threads are still working.
	 * 
	 * @param threads
	 * @throws InterruptedException
	 */
	public static void allowThreadsToFinish(final Thread[] threads)
			throws InterruptedException {
		boolean threadsDone = false;
		while(!threadsDone){
			threadsDone = true;
			for(Thread thread : threads){
				if(thread.getState() != State.TERMINATED){
					Thread.sleep(10);
					threadsDone = false;
				}
			}
		}
	}
	
	public static void assertEqualsContractEnforcement(Object obj0, Object obj1, Object obj2){
		assertFalse(obj0.equals(null));
		assertFalse(obj1.equals(null));
		assertFalse(obj2.equals(null));
		
		assertEquals(obj0, obj0);
		assertEquals(obj1, obj1);
		assertEquals(obj2, obj2);
		
		assertEquals(obj0, obj1);
		assertEquals(obj1, obj0);
		
		assertEquals(obj1, obj2);
		assertEquals(obj2, obj1);
		
		assertEquals(obj2, obj0);
		assertEquals(obj0, obj2);
	}
	
	public static void assertHashCodeContractEnforcement(Object obj0, Object obj1, Object obj2){
		assertEquals(obj0.hashCode(), obj0.hashCode());
		assertEquals(obj1.hashCode(), obj1.hashCode());
		assertEquals(obj2.hashCode(), obj2.hashCode());
		
		assertEquals(obj0.hashCode(), obj1.hashCode());
		assertEquals(obj1.hashCode(), obj0.hashCode());
		
		assertEquals(obj1.hashCode(), obj2.hashCode());
		assertEquals(obj2.hashCode(), obj1.hashCode());
		
		assertEquals(obj2.hashCode(), obj0.hashCode());
		assertEquals(obj0.hashCode(), obj2.hashCode());
	}
	
	public static void assertHashCodeEqualsContracts(Object obj0, Object obj1, Object obj2){
		assertEqualsContractEnforcement(obj0, obj1, obj2);
		assertHashCodeContractEnforcement(obj0, obj1, obj2);
	}

	public static Object invokePrivate(
			String name, 
			final Object obj,
			Object[] objects) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = null;
		Class<?> objClass = obj instanceof Class ? (Class<?>)obj : obj.getClass();
		while(objClass != null && method == null){
			for(Method m : objClass.getDeclaredMethods()){
				if (name.equals(m.getName()) && 
						(objects == null && m.getParameterTypes().length == 0) || 
						(objects != null && objects.length == m.getParameterTypes().length)){
					method = m;
					break;
				}
			}
			objClass = objClass.getSuperclass();
		}
		if(method == null){
			throw new IllegalArgumentException("method with name "+name+" was not found, check class definition.");
		}
		final boolean wasAccessible = method.isAccessible();
		try{
			method.setAccessible(true);
			return method.invoke(obj, objects);
		}finally{
			if(wasAccessible != method.isAccessible()){
				method.setAccessible(wasAccessible);
			}
		}
	}
	
	public static interface ArgRunner<T> {
		public void run(T t);
	}
	
	public static interface TwoObjectAssertion {
		/**
		 * Message to display when assertion fails. Followed by 2 objects to
		 * assertOn. Useful for reusing in object composition tests or a
		 * chain of command pattern based assertion chain.
		 * 
		 * @param msg
		 * @param o0
		 * @param o1
		 */
		void assertOn(String msg, Object o0, Object o1);
	}
	
}
