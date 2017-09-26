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

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;
import org.talend.daikon.properties.runtime.RuntimeContext;

/**
 * Unit-tests for {@link PropertiesDynamicMethodHelper}
 */
public class PropertiesDynamicMethodHelperTest {

    static class TestProperties extends PropertiesImpl {

        private static final long serialVersionUID = 1L;

        public Property<String> test = PropertyFactory.newString("test");

        public TestProperties(String name) {
            super(name);
        }

        public void afterTest() {

        }

        public void afterTest(RuntimeContext context) {

        }

        void beforeTest() {

        }

        @SuppressWarnings("unused")
        private void validateTest() {

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
        PropertiesDynamicMethodHelper.findMethod(null, Properties.METHOD_AFTER, "testProp", true);
    }

    @Test
    public void testFindMethodUnknownTypeRequired() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: unknownTriggerTypeTestProp not found");
        TestProperties props = new TestProperties("test");
        PropertiesDynamicMethodHelper.findMethod(props, "unknownTriggerType", "testProp", true);
    }

    @Test
    public void testFindMethodUnknownTypeNotRequired() {
        TestProperties props = new TestProperties("test");
        Method method = PropertiesDynamicMethodHelper.findMethod(props, "unknownTriggerType", "testProp", false);
        Assert.assertNull(method);
    }

    /**
     * Asserts public methods are found
     */
    @Test
    public void testFindMethodPublic() {
        String expectedMethodDefinition = "public void org.talend.daikon.properties.PropertiesDynamicMethodHelperTest$TestProperties.afterTest()";

        TestProperties props = new TestProperties("test");
        Method method = PropertiesDynamicMethodHelper.findMethod(props, Properties.METHOD_AFTER, "test", false);
        Assert.assertEquals(expectedMethodDefinition, method.toString());
    }

    /**
     * Asserts public method with parameter is found
     */
    @Test
    public void testFindMethodPublicWithParams() {
        String expectedMethodDefinition = "public void org.talend.daikon.properties.PropertiesDynamicMethodHelperTest$TestProperties.afterTest("
                + "org.talend.daikon.properties.runtime.RuntimeContext)";

        TestProperties props = new TestProperties("test");
        Method method = PropertiesDynamicMethodHelper.findMethod(props, Properties.METHOD_AFTER, "test", false,
                RuntimeContext.class);
        Assert.assertEquals(expectedMethodDefinition, method.toString());
    }

    /**
     * Asserts that package visible methods are not found
     */
    @Test
    public void testFindMethodPackage() {
        TestProperties props = new TestProperties("test");
        Method method = PropertiesDynamicMethodHelper.findMethod(props, Properties.METHOD_BEFORE, "test", false);
        Assert.assertNull(method);
    }

    /**
     * Asserts that private visible methods are not found
     */
    @Test
    public void testFindMethodPrivate() {
        TestProperties props = new TestProperties("test");
        Method method = PropertiesDynamicMethodHelper.findMethod(props, Properties.METHOD_VALIDATE, "test", false);
        Assert.assertNull(method);
    }
}
