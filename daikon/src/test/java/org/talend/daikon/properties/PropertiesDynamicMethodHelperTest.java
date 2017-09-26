// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.daikon.properties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.runtime.RuntimeContext;
import org.talend.daikon.properties.service.Repository;

/**
 * Unit-tests for {@link PropertiesDynamicMethodHelper}
 */
public class PropertiesDynamicMethodHelperTest {

    static class TestProperties extends PropertiesImpl {

        private static final long serialVersionUID = 1L;

        public TestProperties(String name) {
            super(name);
        }

        public void beforeProperty() {

        }

        public void validateProperty() {

        }

        public void afterProperty() {

        }

        public void afterProperty(RuntimeContext context) {

        }

        public void beforeFormPresentMain() {

        }

        public void afterFormBackMain() {

        }

        public void afterFormNextMain() {

        }

        @SuppressWarnings("rawtypes")
        public void afterFormFinishMain(Repository repository) {

        }

        /**
         * method with package visibility which won't be found
         */
        void beforeSomeProperty() {

        }

        /**
         * method with private visibility which won't be found
         */
        @SuppressWarnings("unused")
        private void validateAnotherProperty() {

        }

    }

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void testFindMethodNullPropertyName() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The ComponentService was used to access a property with a null(or empty) property name. Type:");

        TestProperties props = new TestProperties("test");
        PropertiesDynamicMethodHelper.findMethod(props, Properties.METHOD_AFTER, null, true);
    }

    @Test
    public void testFindMethodNullObject() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Instance whose method is being searched for should not be null");
        PropertiesDynamicMethodHelper.findMethod(null, Properties.METHOD_AFTER, "property", true);
    }

    @Test
    public void testFindMethodUnknownTypeRequired() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: unknownTriggerTypeProperty not found");
        TestProperties props = new TestProperties("test");
        PropertiesDynamicMethodHelper.findMethod(props, "unknownTriggerType", "property", true);
    }

    @Test
    public void testFindMethodUnknownTypeNotRequired() {
        TestProperties props = new TestProperties("test");
        Method method = PropertiesDynamicMethodHelper.findMethod(props, "unknownTriggerType", "property", false);
        Assert.assertNull(method);
    }

    /**
     * Asserts public methods are found
     */
    @Test
    public void testFindMethodPublic() {
        String expectedMethodDefinition = "public void org.talend.daikon.properties.PropertiesDynamicMethodHelperTest$TestProperties.afterProperty()";

        TestProperties props = new TestProperties("test");
        Method method = PropertiesDynamicMethodHelper.findMethod(props, Properties.METHOD_AFTER, "property", false);
        Assert.assertEquals(expectedMethodDefinition, method.toString());
    }

    /**
     * Asserts public method with parameter is found
     */
    @Test
    public void testFindMethodPublicWithParams() {
        String expectedMethodDefinition = "public void org.talend.daikon.properties.PropertiesDynamicMethodHelperTest$TestProperties.afterProperty("
                + "org.talend.daikon.properties.runtime.RuntimeContext)";

        TestProperties props = new TestProperties("test");
        Method method = PropertiesDynamicMethodHelper.findMethod(props, Properties.METHOD_AFTER, "property", false,
                RuntimeContext.class);
        Assert.assertEquals(expectedMethodDefinition, method.toString());
    }

    /**
     * Asserts that package visible methods are not found
     */
    @Test
    public void testFindMethodPackage() {
        TestProperties props = new TestProperties("test");
        Method method = PropertiesDynamicMethodHelper.findMethod(props, Properties.METHOD_BEFORE, "someProperty", false);
        Assert.assertNull(method);
    }

    /**
     * Asserts that private visible methods are not found
     */
    @Test
    public void testFindMethodPrivate() {
        TestProperties props = new TestProperties("test");
        Method method = PropertiesDynamicMethodHelper.findMethod(props, Properties.METHOD_VALIDATE, "anotherProperty", false);
        Assert.assertNull(method);
    }

    @Test
    public void testAfterFormBack() throws Throwable {
        TestProperties props = mock(TestProperties.class);
        PropertiesDynamicMethodHelper.afterFormBack(props, Form.MAIN);
        verify(props).afterFormBackMain();
    }

    @Test
    public void testAfterFormFinish() throws Throwable {
        @SuppressWarnings("rawtypes")
        Repository repository = mock(Repository.class);
        TestProperties props = mock(TestProperties.class);
        PropertiesDynamicMethodHelper.afterFormFinish(props, Form.MAIN, repository);
        verify(props).afterFormFinishMain(repository);
    }

    @Test
    public void testAfterFormNext() throws Throwable {
        TestProperties props = mock(TestProperties.class);
        PropertiesDynamicMethodHelper.afterFormNext(props, Form.MAIN);
        verify(props).afterFormNextMain();
    }

    @Test
    public void testAfterProperty() throws Throwable {
        TestProperties props = mock(TestProperties.class);
        PropertiesDynamicMethodHelper.afterProperty(props, "property");
        verify(props).afterProperty();
    }

    @Test
    public void testBeforeFormPresent() throws Throwable {
        TestProperties props = mock(TestProperties.class);
        PropertiesDynamicMethodHelper.beforeFormPresent(props, Form.MAIN);
        verify(props).beforeFormPresentMain();
    }

    @Test
    public void testBeforePropertyActivate() throws Throwable {
        TestProperties props = mock(TestProperties.class);
        PropertiesDynamicMethodHelper.beforePropertyActivate(props, "property");
        verify(props).beforeProperty();
    }

    @Test
    public void testBeforePropertyPresent() throws Throwable {
        TestProperties props = mock(TestProperties.class);
        PropertiesDynamicMethodHelper.beforePropertyPresent(props, "property");
        verify(props).beforeProperty();
    }

    @Test
    public void testValidateProperty() throws Throwable {
        TestProperties props = mock(TestProperties.class);
        PropertiesDynamicMethodHelper.validateProperty(props, "property");
        verify(props).validateProperty();
    }
}
