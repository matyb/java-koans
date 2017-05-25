package com.sandwich.koan.constant;

import com.sandwich.koan.path.CommandLineTestCase;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArgumentTypeTest extends CommandLineTestCase {

    @Test
    public void testClassPrecedesMethod() throws Exception {
        assertTrue(ArgumentType.CLASS_ARG.compareTo(ArgumentType.METHOD_ARG) == -1);
        // further drive point home - method inserted at index 0, class index 1
        List<ArgumentType> classVsMethod = Arrays.asList(ArgumentType.METHOD_ARG, ArgumentType.CLASS_ARG);
        assertEquals(0, classVsMethod.indexOf(ArgumentType.METHOD_ARG));
        assertEquals(1, classVsMethod.indexOf(ArgumentType.CLASS_ARG));
        Collections.sort(classVsMethod);
        // now - because of comparable impl was applied, class precedes method - this is necessary
        // @ see KoanSuiteRunner.run()
        assertEquals(1, classVsMethod.indexOf(ArgumentType.METHOD_ARG));
        assertEquals(0, classVsMethod.indexOf(ArgumentType.CLASS_ARG));
    }

}
