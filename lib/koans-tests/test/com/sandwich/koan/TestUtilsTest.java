package com.sandwich.koan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.easymock.EasyMock;
import org.junit.Test;

import com.sandwich.koan.TestUtils.ArgRunner;
import com.sandwich.koan.TestUtils.TwoObjectAssertion;
import com.sandwich.koan.path.CommandLineTestCase;

public class TestUtilsTest extends CommandLineTestCase {

	@Test
	public void testEqualsContractEnforcement_integerIdentity_happyPath() throws Exception {
		Integer one = 1;
		TestUtils.assertEqualsContractEnforcement(one, one, one);
	}
	
	@Test
	public void testEqualsContractEnforcement_integerObject_happyPath() throws Exception {
		TestUtils.assertEqualsContractEnforcement(new Integer(1), new Integer(1), new Integer(1));
	}
	
	@Test(expected=AssertionError.class)
	public void testEqualsContractEnforcement_integer_exceptionPath0() throws Exception {
		TestUtils.assertEqualsContractEnforcement(new Integer(2), new Integer(1), new Integer(1));
	}
	
	@Test(expected=AssertionError.class)
	public void testEqualsContractEnforcement_integer_exceptionPath1() throws Exception {
		TestUtils.assertEqualsContractEnforcement(new Integer(1), new Integer(2), new Integer(1));
	}
	
	@Test(expected=AssertionError.class)
	public void testEqualsContractEnforcement_integer_exceptionPath() throws Exception {
		TestUtils.assertEqualsContractEnforcement(new Integer(1), new Integer(1), new Integer(2));
	}
	
	@Test
	public void testHashCodeContractEnforcement_integerIdentity_happyPath() throws Exception {
		Integer one = 1;
		TestUtils.assertHashCodeContractEnforcement(one, one, one);
	}
	
	@Test
	public void testHashCodeContractEnforcement_integerObject_happyPath() throws Exception {
		TestUtils.assertHashCodeContractEnforcement(new Integer(1), new Integer(1), new Integer(1));
	}
	
	@Test(expected=AssertionError.class)
	public void testHashCodeContractEnforcement_integer_exceptionPath0() throws Exception {
		TestUtils.assertHashCodeContractEnforcement(new Integer(2), new Integer(1), new Integer(1));
	}
	
	@Test(expected=AssertionError.class)
	public void testHashCodeContractEnforcement_integer_exceptionPath1() throws Exception {
		TestUtils.assertHashCodeContractEnforcement(new Integer(1), new Integer(2), new Integer(1));
	}
	
	@Test(expected=AssertionError.class)
	public void testHashCodeContractEnforcement_integer_exceptionPath() throws Exception {
		TestUtils.assertHashCodeContractEnforcement(new Integer(1), new Integer(1), new Integer(2));
	}
	
	@Test
	public void testHashCodeContractEnforcement_testObj_happyPath() throws Exception {
		TestUtils.assertHashCodeContractEnforcement(new ContractEnforcementBase(), 
													new ContractEnforcementBase(), 
													new ContractEnforcementBase());
	}
	
	@Test(expected=AssertionError.class)
	public void testHashCodeContractEnforcement_testObj_exceptionPath0() throws Exception {
		TestUtils.assertHashCodeContractEnforcement(new ContractEnforcementSubclass(), 
													new ContractEnforcementBase(), 
													new ContractEnforcementBase());
	}
	
	@Test(expected=AssertionError.class)
	public void testHashCodeContractEnforcement_testObj_exceptionPath1() throws Exception {
		TestUtils.assertHashCodeContractEnforcement(new ContractEnforcementBase(), 
													new ContractEnforcementSubclass(), 
													new ContractEnforcementBase());
	}
	
	@Test(expected=AssertionError.class)
	public void testHashCodeContractEnforcement_testObj_exceptionPath2() throws Exception {
		TestUtils.assertHashCodeContractEnforcement(new ContractEnforcementBase(), 
													new ContractEnforcementBase(),
													new ContractEnforcementSubclass());
	}
	
	@Test
	public void testEqualsContractEnforcement_testObj_happyPath() throws Exception {
		TestUtils.assertEqualsContractEnforcement(new ContractEnforcementBase(), 
													new ContractEnforcementBase(), 
													new ContractEnforcementBase());
	}
	
	@Test(expected=AssertionError.class)
	public void testEqualsContractEnforcement_testObj_exceptionPath0() throws Exception {
		TestUtils.assertEqualsContractEnforcement(new ContractEnforcementSubclass(), 
													new ContractEnforcementBase(), 
													new ContractEnforcementBase());
	}
	
	@Test(expected=AssertionError.class)
	public void testEqualsContractEnforcement_testObj_exceptionPath1() throws Exception {
		TestUtils.assertEqualsContractEnforcement(new ContractEnforcementBase(), 
													new ContractEnforcementSubclass(), 
													new ContractEnforcementBase());
	}
	
	@Test(expected=AssertionError.class)
	public void testEqualsContractEnforcement_testObj_exceptionPath2() throws Exception {
		TestUtils.assertEqualsContractEnforcement(new ContractEnforcementBase(), 
													new ContractEnforcementBase(),
													new ContractEnforcementSubclass());
	}
	
	static class ContractEnforcementBase {
		int i = 1;
		@Override
		public boolean equals(Object o){
			return o instanceof ContractEnforcementBase && i == ((ContractEnforcementBase)o).i;
		}
		@Override
		public int hashCode(){
			return 1;
		}
	}
	
	static class ContractEnforcementSubclass extends ContractEnforcementBase {
		int j = 2;
		@Override
		public boolean equals(Object o){
			return o instanceof ContractEnforcementSubclass 
				&& i == ((ContractEnforcementSubclass)o).i
				&& j == ((ContractEnforcementSubclass)o).j;
		}
		@Override
		public int hashCode(){
			return 2;
		}
	}
	
	@Test(expected=AssertionError.class, timeout=1000)
	public void testEqualsConcurrency_concurrentAccessFails() throws Exception {
		TestUtils.doSimultaneouslyAndRepetitively(new TwoObjectAssertion() {
			public void assertOn(String msg, Object o0, Object o1) {
				assertEquals(msg, o0, o1);
			}
		}, IllegalMonitorStateException.class,
		new Runnable() {
			public void run() {
				waste(10);
			}
		}, new Runnable() {
			public void run() {
				waste(11);
			}
		}, new Runnable() {
			public void run() {
				waste(3);
			}
		});
		assertSystemErrContains("Thread-1\" java.lang.IllegalMonitorStateException");
		assertSystemErrContains("Thread-2\" java.lang.IllegalMonitorStateException");
		assertSystemErrContains("Thread-3\" java.lang.IllegalMonitorStateException");
		assertSystemErrContains("Thread-4");
	}
	
	@Test(expected=java.lang.AssertionError.class, timeout=500)
	public void testEqualsConcurrency_concurrentAccessFails_assertIllegalMonitorStateException() throws Exception {
		TestUtils.doSimultaneouslyAndRepetitively(new TwoObjectAssertion() {
			public void assertOn(String msg, Object o0, Object o1) {
				assertEquals(msg, o0, o1);
			}
		}, 
		IllegalMonitorStateException.class,
		new Runnable() {
			public void run() {
				waste(10);
			}
		}, 
		new Runnable() {
			public void run() {
				waste(11);
			}
		}, 
		new Runnable() {
			public void run() {
				waste(3);
			}
		});
	}
	
	@Test
	public void testEqualsConcurrency() throws Exception {
		TestUtils.doSimultaneouslyAndRepetitively(new TwoObjectAssertion() {
			public void assertOn(String msg, Object o0, Object o1) {
				assertEquals(msg, o0, o1);
			}
		}, new Runnable(){
			public void run() {
				wasteSynchronized(10);
			}
		}, new Runnable(){
			public void run() {
				wasteSynchronized(11);
			}
		}, new Runnable(){
			public void run() {
				wasteSynchronized(3);
			}
		});
	}
	
	@Test
	public void testEqualsConcurrency_II() throws Exception {
		TestUtils.doSimultaneouslyAndRepetitively(new TwoObjectAssertion() {
			public void assertOn(String msg, Object o0, Object o1) {
				assertEquals(msg, o0, o1);
			}
		}, new Runnable(){
			public void run() {
				wasteSynchronized(10);
			}
		}, new Runnable(){
			public void run() {
				wasteSynchronized(11);
			}
		}, new Runnable(){
			public void run() {
				wasteSynchronized(4);
			}
		}, new Runnable(){
			public void run() {
				wasteSynchronized(6);
			}
		});
	}
	
	private void waste(int i) {
		try {
			wait(i);
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
	}
	
	private int wasteSynchronized(int i) { synchronized(this){
		try {
			wait(i);
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}
		return i;
	}}
	
	@Test
	public void testForEachLine_threeNewLines() throws Exception {
		@SuppressWarnings("unchecked")
		ArgRunner<String> runner = EasyMock.createStrictMock(ArgRunner.class);
		
		runner.run("");
		EasyMock.expectLastCall();
		
		runner.run("");
		EasyMock.expectLastCall();
		
		runner.run("");
		EasyMock.expectLastCall();
		
		runner.run("");
		EasyMock.expectLastCall();
		
		EasyMock.replay(runner);
		
		TestUtils.forEachLine("\n\n\n", runner);
		EasyMock.verify(runner);
	}
	
	@Test
	public void testForEachLine_spaceNewLineNewLine(){
		@SuppressWarnings("unchecked")
		ArgRunner<String> runner = EasyMock.createStrictMock(ArgRunner.class);
		
		runner.run(" ");
		EasyMock.expectLastCall();
		
		runner.run("");
		EasyMock.expectLastCall();
		
		runner.run("");
		EasyMock.expectLastCall();
		
		EasyMock.replay(runner);
		
		TestUtils.forEachLine(" \n\n", runner);
		EasyMock.verify(runner);
	}
	
	@Test
	public void testForEachLine_newLineNewLineSpace(){
		@SuppressWarnings("unchecked")
		ArgRunner<String> runner = EasyMock.createStrictMock(ArgRunner.class);
		
		runner.run("");
		EasyMock.expectLastCall();
		
		runner.run("");
		EasyMock.expectLastCall();
		
		runner.run(" ");
		EasyMock.expectLastCall();
		
		EasyMock.replay(runner);
		
		TestUtils.forEachLine("\n\n ", runner);
		EasyMock.verify(runner);
	}
	
	@Test
	public void testMixingBackslashRAndBackslashNNewLines() throws Exception {
		@SuppressWarnings("unchecked")
		ArgRunner<String> runner = EasyMock.createStrictMock(ArgRunner.class);
		
		runner.run("");
		EasyMock.expectLastCall();
		
		runner.run(" ");
		EasyMock.expectLastCall();
		
		runner.run(" ");
		EasyMock.expectLastCall();
		
		EasyMock.replay(runner);
		
		TestUtils.forEachLine("\r \n ", runner); // can mix and match \r and \n
		EasyMock.verify(runner);
	}
	
	@Test
	public void testMixingBackslashNAndBackslashRNewLines() throws Exception {
		@SuppressWarnings("unchecked")
		ArgRunner<String> runner = EasyMock.createStrictMock(ArgRunner.class);
		
		runner.run("");
		EasyMock.expectLastCall();
		
		runner.run(" ");
		EasyMock.expectLastCall();
		
		runner.run(" ");
		EasyMock.expectLastCall();
		
		EasyMock.replay(runner);
		
		TestUtils.forEachLine("\n \r ", runner); // can mix and match \r and \n
		EasyMock.verify(runner);
	}
	
	@Test
	public void testForEachLine_emptyString() throws Exception {
		@SuppressWarnings("unchecked")
		ArgRunner<String> runner = EasyMock.createStrictMock(ArgRunner.class);
		runner.run("");
		EasyMock.expectLastCall();
		
		EasyMock.replay(runner);
		
		TestUtils.forEachLine("", runner);
		EasyMock.verify(runner);
	}
	
	@Test
	public void testForEachLine_nothingThreeNewLinesSeparatedBy1Space() throws Exception {
		@SuppressWarnings("unchecked")
		ArgRunner<String> runner = EasyMock.createStrictMock(ArgRunner.class);
		
		runner.run("");
		EasyMock.expectLastCall();
		
		runner.run(" ");
		EasyMock.expectLastCall();
		
		runner.run(" ");
		EasyMock.expectLastCall();
		
		runner.run("");
		EasyMock.expectLastCall();
		
		EasyMock.replay(runner);
		
		TestUtils.forEachLine("\n \n \n", runner);
		EasyMock.verify(runner);
	}
	
	@Test
	public void testForEachLine_nothingThreeNewLinesSeparatedBy1SpaceThen2Spaces() throws Exception {
		@SuppressWarnings("unchecked")
		ArgRunner<String> runner = EasyMock.createStrictMock(ArgRunner.class);
		
		runner.run("");
		EasyMock.expectLastCall();
		
		runner.run(" ");
		EasyMock.expectLastCall();
		
		runner.run("  ");
		EasyMock.expectLastCall();
		
		runner.run("");
		EasyMock.expectLastCall();
		
		EasyMock.replay(runner);
		
		TestUtils.forEachLine("\n \n  \n", runner);
		EasyMock.verify(runner);
	}
}
