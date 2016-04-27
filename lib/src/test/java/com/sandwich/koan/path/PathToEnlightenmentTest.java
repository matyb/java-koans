package com.sandwich.koan.path;

import com.sandwich.koan.LocaleSwitchingTestCase;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertNotNull;

public class PathToEnlightenmentTest extends LocaleSwitchingTestCase {

    @Test
    public void testFallsBackToEnglishXmlWhenNoXmlForLocaleIsFound() {
        Locale.setDefault(Locale.CHINA);
        PathToEnlightenment.xmlToPathTransformer = null;
        assertNotNull(PathToEnlightenment.getXmlToPathTransformer());
    }

    @Test
    public void testEnglishXmlWhenXmlForLocaleIsFound_eventIsNotLogged() {
        Locale.setDefault(Locale.US);
        PathToEnlightenment.xmlToPathTransformer = null;
        assertLogged(PathToEnlightenment.class.getName(), new RBSensitiveLoggerExpectation() {
            protected boolean isLogCallRequired() {
                return false;
            }
        });
    }

    @Test
    public void testEnglishXmlWhenXmlForLocaleIsFound() {
        Locale.setDefault(Locale.US);
        PathToEnlightenment.xmlToPathTransformer = null;
        assertNotNull(PathToEnlightenment.getXmlToPathTransformer());
    }

    private class RBSensitiveLoggerExpectation extends LoggerExpectation {
        @Override
        protected void invokeImplementation() {
            PathToEnlightenment.getXmlToPathTransformer();
        }
    }
}
