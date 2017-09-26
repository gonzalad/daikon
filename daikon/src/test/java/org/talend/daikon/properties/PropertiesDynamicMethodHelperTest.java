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
import static org.mockito.Mockito.spy;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.runtime.RuntimeContext;
import org.talend.daikon.properties.runtime.RuntimeContextImpl;
import org.talend.daikon.properties.service.Repository;

/**
 * Unit-tests for {@link PropertiesDynamicMethodHelper}
 */
public class PropertiesDynamicMethodHelperTest {

    static class PropertiesWithoutCallbacks extends PropertiesImpl {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("rawtypes")
        public ReferenceProperties ref = new ReferenceProperties<>("ref", "AnyDefinitionName");

        public PropertiesWithoutCallbacks() {
            super("test");
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

    static class PropertiesWithOldCallbacks extends PropertiesImpl {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("rawtypes")
        public ReferenceProperties ref = new ReferenceProperties<>("ref", "AnyDefinitionName");

        public PropertiesWithOldCallbacks() {
            super("test");
        }

        public void beforeProperty() {
        }

        public void validateProperty() {
        }

        public void afterProperty() {
        }

        public void beforeFormPresentMain() {
        }

        public void afterFormBackMain() {
        }

        public void afterFormNextMain() {
        }

        public void afterRef() {
        }

        @SuppressWarnings("rawtypes")
        public void afterFormFinishMain(Repository repository) {
        }

    }

    static class PropertiesWithRuntimeContextCallbacks extends PropertiesImpl {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("rawtypes")
        public ReferenceProperties ref = new ReferenceProperties<>("ref", "AnyDefinitionName");

        public PropertiesWithRuntimeContextCallbacks() {
            super("test");
        }

        public void beforeProperty(RuntimeContext context) {
        }

        public void validateProperty(RuntimeContext context) {
        }

        public void afterProperty(RuntimeContext context) {
        }

        public void beforeFormPresentMain(RuntimeContext context) {
        }

        public void afterFormBackMain(RuntimeContext context) {
        }

        public void afterFormNextMain(RuntimeContext context) {
        }

        @SuppressWarnings("rawtypes")
        public void afterFormFinishMain(Repository repository, RuntimeContext context) {
        }

        public void afterRef(RuntimeContext context) {
        }
    }

    static class PropertiesWithBothCallbacks extends PropertiesImpl {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("rawtypes")
        public ReferenceProperties ref = new ReferenceProperties<>("ref", "AnyDefinitionName");

        public PropertiesWithBothCallbacks() {
            super("test");
        }

        public void beforeProperty() {
        }

        public void beforeProperty(RuntimeContext context) {
        }

        public void validateProperty() {
        }

        public void validateProperty(RuntimeContext context) {
        }

        public void afterProperty() {
        }

        public void afterProperty(RuntimeContext context) {
        }

        public void beforeFormPresentMain() {
        }

        public void beforeFormPresentMain(RuntimeContext context) {
        }

        public void afterFormBackMain() {
        }

        public void afterFormBackMain(RuntimeContext context) {
        }

        public void afterFormNextMain() {
        }

        public void afterFormNextMain(RuntimeContext context) {
        }

        @SuppressWarnings("rawtypes")
        public void afterFormFinishMain(Repository repository) {
        }

        @SuppressWarnings("rawtypes")
        public void afterFormFinishMain(Repository repository, RuntimeContext context) {
        }

        public void afterRef() {
        }

        public void afterRef(RuntimeContext context) {
        }
    }

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void testFindMethodNullPropertyName() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The ComponentService was used to access a property with a null(or empty) property name. Type:");

        PropertiesWithBothCallbacks props = new PropertiesWithBothCallbacks();
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
        PropertiesWithBothCallbacks props = new PropertiesWithBothCallbacks();
        PropertiesDynamicMethodHelper.findMethod(props, "unknownTriggerType", "property", true);
    }

    @Test
    public void testFindMethodUnknownTypeNotRequired() {
        PropertiesWithBothCallbacks props = new PropertiesWithBothCallbacks();
        Method method = PropertiesDynamicMethodHelper.findMethod(props, "unknownTriggerType", "property", false);
        Assert.assertNull(method);
    }

    /**
     * Asserts public methods are found
     */
    @Test
    public void testFindMethodPublic() {
        String expectedMethodDefinition = "public void org.talend.daikon.properties.PropertiesDynamicMethodHelperTest$PropertiesWithBothCallbacks.afterProperty()";

        PropertiesWithBothCallbacks props = new PropertiesWithBothCallbacks();
        Method method = PropertiesDynamicMethodHelper.findMethod(props, Properties.METHOD_AFTER, "property", false);
        Assert.assertEquals(expectedMethodDefinition, method.toString());
    }

    /**
     * Asserts public method with parameter is found
     */
    @Test
    public void testFindMethodPublicWithParams() {
        String expectedMethodDefinition = "public void org.talend.daikon.properties.PropertiesDynamicMethodHelperTest$PropertiesWithBothCallbacks.afterProperty("
                + "org.talend.daikon.properties.runtime.RuntimeContext)";

        PropertiesWithBothCallbacks props = new PropertiesWithBothCallbacks();
        Method method = PropertiesDynamicMethodHelper.findMethod(props, Properties.METHOD_AFTER, "property", false,
                RuntimeContext.class);
        Assert.assertEquals(expectedMethodDefinition, method.toString());
    }

    /**
     * Asserts that package visible methods are not found
     */
    @Test
    public void testFindMethodPackage() {
        PropertiesWithoutCallbacks props = new PropertiesWithoutCallbacks();
        Method method = PropertiesDynamicMethodHelper.findMethod(props, Properties.METHOD_BEFORE, "someProperty", false);
        Assert.assertNull(method);
    }

    /**
     * Asserts that private visible methods are not found
     */
    @Test
    public void testFindMethodPrivate() {
        PropertiesWithoutCallbacks props = new PropertiesWithoutCallbacks();
        Method method = PropertiesDynamicMethodHelper.findMethod(props, Properties.METHOD_VALIDATE, "anotherProperty", false);
        Assert.assertNull(method);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has no triggers at all
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormBackNoCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: afterFormBackMain not found");
        PropertiesWithoutCallbacks withoutCallbacks = mock(PropertiesWithoutCallbacks.class);
        PropertiesDynamicMethodHelper.afterFormBack(withoutCallbacks, Form.MAIN);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has callback with
     * {@link RuntimeContext} parameter, but has no callback without parameters
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormBackNewCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: afterFormBackMain not found");
        PropertiesWithRuntimeContextCallbacks withNewCallbacks = mock(PropertiesWithRuntimeContextCallbacks.class);
        PropertiesDynamicMethodHelper.afterFormBack(withNewCallbacks, Form.MAIN);
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormBackOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = mock(PropertiesWithOldCallbacks.class);
        PropertiesDynamicMethodHelper.afterFormBack(withOldCallbacks, Form.MAIN);
        verify(withOldCallbacks).afterFormBackMain();
    }

    /**
     * Asserts that callback without parameters is called even if {@link Properties} class has both kinds of callbacks
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormBackBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = mock(PropertiesWithBothCallbacks.class);
        PropertiesDynamicMethodHelper.afterFormBack(withBothCallbacks, Form.MAIN);
        verify(withBothCallbacks).afterFormBackMain();
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has no triggers at all
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormBackRuntimeContextNoCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: afterFormBackMain not found");
        PropertiesWithoutCallbacks withoutCallbacks = mock(PropertiesWithoutCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterFormBack(withoutCallbacks, Form.MAIN, context);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has callback with
     * {@link RuntimeContext} parameter, but has no callback without parameters
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormBackRuntimeContextNewCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: afterFormBackMain not found");
        PropertiesWithRuntimeContextCallbacks withNewCallbacks = mock(PropertiesWithRuntimeContextCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterFormBack(withNewCallbacks, Form.MAIN, context);
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormBackRuntimeContextOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = mock(PropertiesWithOldCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterFormBack(withOldCallbacks, Form.MAIN, context);
        verify(withOldCallbacks).afterFormBackMain();
    }

    /**
     * Asserts that callback with {@link RuntimeContext} parameter is called, when {@link Properties} class has both kinds of
     * callbacks
     * and product passes {@link RuntimeContext} argument to the method
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormBackRuntimeContextBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = mock(PropertiesWithBothCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterFormBack(withBothCallbacks, Form.MAIN, context);
        verify(withBothCallbacks).afterFormBackMain(context);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has no triggers at all
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormFinishkNoCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: afterFormFinishMain not found");
        PropertiesWithoutCallbacks withoutCallbacks = mock(PropertiesWithoutCallbacks.class);
        Repository repository = mock(Repository.class);
        PropertiesDynamicMethodHelper.afterFormFinish(withoutCallbacks, Form.MAIN, repository);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has callback with
     * {@link RuntimeContext} parameter, but has no callback without parameters
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormFinishNewCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: afterFormFinishMain not found");
        PropertiesWithRuntimeContextCallbacks withNewCallbacks = mock(PropertiesWithRuntimeContextCallbacks.class);
        Repository repository = mock(Repository.class);
        PropertiesDynamicMethodHelper.afterFormFinish(withNewCallbacks, Form.MAIN, repository);
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormFinishOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = mock(PropertiesWithOldCallbacks.class);
        Repository repository = mock(Repository.class);
        PropertiesDynamicMethodHelper.afterFormFinish(withOldCallbacks, Form.MAIN, repository);
        verify(withOldCallbacks).afterFormFinishMain(repository);
    }

    /**
     * Asserts that callback without parameters is called even if {@link Properties} class has both kinds of callbacks
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormFinishBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = mock(PropertiesWithBothCallbacks.class);
        Repository repository = mock(Repository.class);
        PropertiesDynamicMethodHelper.afterFormFinish(withBothCallbacks, Form.MAIN, repository);
        verify(withBothCallbacks).afterFormFinishMain(repository);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has no triggers at all
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormFinishRuntimeContextNoCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: afterFormFinishMain not found");
        PropertiesWithoutCallbacks withoutCallbacks = mock(PropertiesWithoutCallbacks.class);
        Repository repository = mock(Repository.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterFormFinish(withoutCallbacks, Form.MAIN, repository, context);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has callback with
     * {@link RuntimeContext} parameter, but has no callback without parameters
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormFinishRuntimeContextNewCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: afterFormFinishMain not found");
        PropertiesWithRuntimeContextCallbacks withNewCallbacks = mock(PropertiesWithRuntimeContextCallbacks.class);
        Repository repository = mock(Repository.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterFormFinish(withNewCallbacks, Form.MAIN, repository, context);
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormFinishRuntimeContextOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = mock(PropertiesWithOldCallbacks.class);
        Repository repository = mock(Repository.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterFormFinish(withOldCallbacks, Form.MAIN, repository, context);
        verify(withOldCallbacks).afterFormFinishMain(repository);
    }

    /**
     * Asserts that callback with {@link RuntimeContext} parameter is called, when {@link Properties} class has both kinds of
     * callbacks
     * and product passes {@link RuntimeContext} argument to the method
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormFinishRuntimeContextBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = mock(PropertiesWithBothCallbacks.class);
        Repository repository = mock(Repository.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterFormFinish(withBothCallbacks, Form.MAIN, repository, context);
        verify(withBothCallbacks).afterFormFinishMain(repository, context);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has no triggers at all
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormNextNoCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: afterFormNextMain not found");
        PropertiesWithoutCallbacks withoutCallbacks = mock(PropertiesWithoutCallbacks.class);
        PropertiesDynamicMethodHelper.afterFormNext(withoutCallbacks, Form.MAIN);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has callback with
     * {@link RuntimeContext} parameter, but has no callback without parameters
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormNextNewCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: afterFormNextMain not found");
        PropertiesWithRuntimeContextCallbacks withNewCallbacks = mock(PropertiesWithRuntimeContextCallbacks.class);
        PropertiesDynamicMethodHelper.afterFormNext(withNewCallbacks, Form.MAIN);
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormNextOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = mock(PropertiesWithOldCallbacks.class);
        PropertiesDynamicMethodHelper.afterFormNext(withOldCallbacks, Form.MAIN);
        verify(withOldCallbacks).afterFormNextMain();
    }

    /**
     * Asserts that callback without parameters is called even if {@link Properties} class has both kinds of callbacks
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormNextBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = mock(PropertiesWithBothCallbacks.class);
        PropertiesDynamicMethodHelper.afterFormNext(withBothCallbacks, Form.MAIN);
        verify(withBothCallbacks).afterFormNextMain();
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has no triggers at all
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormNextRuntimeContextNoCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: afterFormNextMain not found");
        PropertiesWithoutCallbacks withoutCallbacks = mock(PropertiesWithoutCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterFormNext(withoutCallbacks, Form.MAIN, context);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has callback with
     * {@link RuntimeContext} parameter, but has no callback without parameters
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormNextRuntimeContextNewCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: afterFormNextMain not found");
        PropertiesWithRuntimeContextCallbacks withNewCallbacks = mock(PropertiesWithRuntimeContextCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterFormNext(withNewCallbacks, Form.MAIN, context);
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormNextRuntimeContextOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = mock(PropertiesWithOldCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterFormNext(withOldCallbacks, Form.MAIN, context);
        verify(withOldCallbacks).afterFormNextMain();
    }

    /**
     * Asserts that callback with {@link RuntimeContext} parameter is called, when {@link Properties} class has both kinds of
     * callbacks
     * and product passes {@link RuntimeContext} argument to the method
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterFormNextRuntimeContextBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = mock(PropertiesWithBothCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterFormNext(withBothCallbacks, Form.MAIN, context);
        verify(withBothCallbacks).afterFormNextMain(context);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has no triggers at all
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterPropertyNoCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: afterProperty not found");
        PropertiesWithoutCallbacks withoutCallbacks = mock(PropertiesWithoutCallbacks.class);
        PropertiesDynamicMethodHelper.afterProperty(withoutCallbacks, "property");
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has callback with
     * {@link RuntimeContext} parameter, but has no callback without parameters
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterPropertyNewCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: afterProperty not found");
        PropertiesWithRuntimeContextCallbacks withNewCallbacks = mock(PropertiesWithRuntimeContextCallbacks.class);
        PropertiesDynamicMethodHelper.afterProperty(withNewCallbacks, "property");
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterPropertyOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = mock(PropertiesWithOldCallbacks.class);
        PropertiesDynamicMethodHelper.afterProperty(withOldCallbacks, "property");
        verify(withOldCallbacks).afterProperty();
    }

    /**
     * Asserts that callback without parameters is called even if {@link Properties} class has both kinds of callbacks
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterPropertyBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = mock(PropertiesWithBothCallbacks.class);
        PropertiesDynamicMethodHelper.afterProperty(withBothCallbacks, "property");
        verify(withBothCallbacks).afterProperty();
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has no triggers at all
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterPropertyRuntimeContextNoCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: afterProperty not found");
        PropertiesWithoutCallbacks withoutCallbacks = mock(PropertiesWithoutCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterProperty(withoutCallbacks, "property", context);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has callback with
     * {@link RuntimeContext} parameter, but has no callback without parameters
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterPropertyRuntimeContextNewCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: afterProperty not found");
        PropertiesWithRuntimeContextCallbacks withNewCallbacks = mock(PropertiesWithRuntimeContextCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterProperty(withNewCallbacks, "property", context);
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterPropertyRuntimeContextOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = mock(PropertiesWithOldCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterProperty(withOldCallbacks, "property", context);
        verify(withOldCallbacks).afterProperty();
    }

    /**
     * Asserts that callback with {@link RuntimeContext} parameter is called, when {@link Properties} class has both kinds of
     * callbacks
     * and product passes {@link RuntimeContext} argument to the method
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterPropertyRuntimeContextBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = mock(PropertiesWithBothCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterProperty(withBothCallbacks, "property", context);
        verify(withBothCallbacks).afterProperty(context);
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterReferenceOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = spy(PropertiesWithOldCallbacks.class);
        PropertiesDynamicMethodHelper.afterReference(withOldCallbacks, withOldCallbacks.ref);
        verify(withOldCallbacks).afterRef();
    }

    /**
     * Asserts that callback without parameters is called even if {@link Properties} class has both kinds of callbacks
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterReferenceBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = spy(PropertiesWithBothCallbacks.class);
        PropertiesDynamicMethodHelper.afterReference(withBothCallbacks, withBothCallbacks.ref);
        verify(withBothCallbacks).afterRef();
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterReferenceRuntimeContextOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = spy(PropertiesWithOldCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterReference(withOldCallbacks, withOldCallbacks.ref, context);
        verify(withOldCallbacks).afterRef();
    }

    /**
     * Asserts that callback with {@link RuntimeContext} parameter is called, when {@link Properties} class has both kinds of
     * callbacks
     * and product passes {@link RuntimeContext} argument to the method
     * 
     * @throws Throwable
     */
    @Test
    public void testAfterReferenceRuntimeContextBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = spy(PropertiesWithBothCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.afterReference(withBothCallbacks, withBothCallbacks.ref, context);
        verify(withBothCallbacks).afterRef(context);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has no triggers at all
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforeFormPresentNoCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: beforeFormPresentMain not found");
        PropertiesWithoutCallbacks withoutCallbacks = mock(PropertiesWithoutCallbacks.class);
        PropertiesDynamicMethodHelper.beforeFormPresent(withoutCallbacks, Form.MAIN);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has callback with
     * {@link RuntimeContext} parameter, but has no callback without parameters
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforeFormPresentNewCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: beforeFormPresentMain not found");
        PropertiesWithRuntimeContextCallbacks withNewCallbacks = mock(PropertiesWithRuntimeContextCallbacks.class);
        PropertiesDynamicMethodHelper.beforeFormPresent(withNewCallbacks, Form.MAIN);
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforeFormPresentOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = mock(PropertiesWithOldCallbacks.class);
        PropertiesDynamicMethodHelper.beforeFormPresent(withOldCallbacks, Form.MAIN);
        verify(withOldCallbacks).beforeFormPresentMain();
    }

    /**
     * Asserts that callback without parameters is called even if {@link Properties} class has both kinds of callbacks
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforeFormPresentBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = mock(PropertiesWithBothCallbacks.class);
        PropertiesDynamicMethodHelper.beforeFormPresent(withBothCallbacks, Form.MAIN);
        verify(withBothCallbacks).beforeFormPresentMain();
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has no triggers at all
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforeFormPresentRuntimeContextNoCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: beforeFormPresentMain not found");
        PropertiesWithoutCallbacks withoutCallbacks = mock(PropertiesWithoutCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.beforeFormPresent(withoutCallbacks, Form.MAIN, context);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has callback with
     * {@link RuntimeContext} parameter, but has no callback without parameters
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforeFormPresentRuntimeContextNewCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: beforeFormPresentMain not found");
        PropertiesWithRuntimeContextCallbacks withNewCallbacks = mock(PropertiesWithRuntimeContextCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.beforeFormPresent(withNewCallbacks, Form.MAIN, context);
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforeFormPresentRuntimeContextOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = mock(PropertiesWithOldCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.beforeFormPresent(withOldCallbacks, Form.MAIN, context);
        verify(withOldCallbacks).beforeFormPresentMain();
    }

    /**
     * Asserts that callback with {@link RuntimeContext} parameter is called, when {@link Properties} class has both kinds of
     * callbacks
     * and product passes {@link RuntimeContext} argument to the method
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforeFormPresentRuntimeContextBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = mock(PropertiesWithBothCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.beforeFormPresent(withBothCallbacks, Form.MAIN, context);
        verify(withBothCallbacks).beforeFormPresentMain(context);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has no triggers at all
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforePropertyActivateNoCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: beforeProperty not found");
        PropertiesWithoutCallbacks withoutCallbacks = mock(PropertiesWithoutCallbacks.class);
        PropertiesDynamicMethodHelper.beforePropertyActivate(withoutCallbacks, "property");
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has callback with
     * {@link RuntimeContext} parameter, but has no callback without parameters
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforePropertyActivateNewCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: beforeProperty not found");
        PropertiesWithRuntimeContextCallbacks withNewCallbacks = mock(PropertiesWithRuntimeContextCallbacks.class);
        PropertiesDynamicMethodHelper.beforePropertyActivate(withNewCallbacks, "property");
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforePropertyActivateOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = mock(PropertiesWithOldCallbacks.class);
        PropertiesDynamicMethodHelper.beforePropertyActivate(withOldCallbacks, "property");
        verify(withOldCallbacks).beforeProperty();
    }

    /**
     * Asserts that callback without parameters is called even if {@link Properties} class has both kinds of callbacks
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforePropertyActivateBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = mock(PropertiesWithBothCallbacks.class);
        PropertiesDynamicMethodHelper.beforePropertyActivate(withBothCallbacks, "property");
        verify(withBothCallbacks).beforeProperty();
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has no triggers at all
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforePropertyActivateRuntimeContextNoCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: beforeProperty not found");
        PropertiesWithoutCallbacks withoutCallbacks = mock(PropertiesWithoutCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.beforePropertyActivate(withoutCallbacks, "property", context);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has callback with
     * {@link RuntimeContext} parameter, but has no callback without parameters
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforePropertyActivateRuntimeContextNewCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: beforeProperty not found");
        PropertiesWithRuntimeContextCallbacks withNewCallbacks = mock(PropertiesWithRuntimeContextCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.beforePropertyActivate(withNewCallbacks, "property", context);
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforePropertyActivateRuntimeContextOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = mock(PropertiesWithOldCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.beforePropertyActivate(withOldCallbacks, "property", context);
        verify(withOldCallbacks).beforeProperty();
    }

    /**
     * Asserts that callback with {@link RuntimeContext} parameter is called, when {@link Properties} class has both kinds of
     * callbacks
     * and product passes {@link RuntimeContext} argument to the method
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforePropertyActivateRuntimeContextBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = mock(PropertiesWithBothCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.beforePropertyActivate(withBothCallbacks, "property", context);
        verify(withBothCallbacks).beforeProperty(context);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has no triggers at all
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforePropertyPresentNoCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: beforeProperty not found");
        PropertiesWithoutCallbacks withoutCallbacks = mock(PropertiesWithoutCallbacks.class);
        PropertiesDynamicMethodHelper.beforePropertyPresent(withoutCallbacks, "property");
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has callback with
     * {@link RuntimeContext} parameter, but has no callback without parameters
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforePropertyPresentNewCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: beforeProperty not found");
        PropertiesWithRuntimeContextCallbacks withNewCallbacks = mock(PropertiesWithRuntimeContextCallbacks.class);
        PropertiesDynamicMethodHelper.beforePropertyPresent(withNewCallbacks, "property");
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforePropertyPresentOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = mock(PropertiesWithOldCallbacks.class);
        PropertiesDynamicMethodHelper.beforePropertyPresent(withOldCallbacks, "property");
        verify(withOldCallbacks).beforeProperty();
    }

    /**
     * Asserts that callback without parameters is called even if {@link Properties} class has both kinds of callbacks
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforePropertyPresentBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = mock(PropertiesWithBothCallbacks.class);
        PropertiesDynamicMethodHelper.beforePropertyPresent(withBothCallbacks, "property");
        verify(withBothCallbacks).beforeProperty();
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has no triggers at all
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforePropertyPresentRuntimeContextNoCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: beforeProperty not found");
        PropertiesWithoutCallbacks withoutCallbacks = mock(PropertiesWithoutCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.beforePropertyPresent(withoutCallbacks, "property", context);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has callback with
     * {@link RuntimeContext} parameter, but has no callback without parameters
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforePropertyPresentRuntimeContextNewCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: beforeProperty not found");
        PropertiesWithRuntimeContextCallbacks withNewCallbacks = mock(PropertiesWithRuntimeContextCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.beforePropertyPresent(withNewCallbacks, "property", context);
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforePropertyPresentRuntimeContextOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = mock(PropertiesWithOldCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.beforePropertyPresent(withOldCallbacks, "property", context);
        verify(withOldCallbacks).beforeProperty();
    }

    /**
     * Asserts that callback with {@link RuntimeContext} parameter is called, when {@link Properties} class has both kinds of
     * callbacks
     * and product passes {@link RuntimeContext} argument to the method
     * 
     * @throws Throwable
     */
    @Test
    public void testBeforePropertyPresentRuntimeContextBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = mock(PropertiesWithBothCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.beforePropertyPresent(withBothCallbacks, "property", context);
        verify(withBothCallbacks).beforeProperty(context);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has no triggers at all
     * 
     * @throws Throwable
     */
    @Test
    public void testValidatePropertyNoCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: validateProperty not found");
        PropertiesWithoutCallbacks withoutCallbacks = mock(PropertiesWithoutCallbacks.class);
        PropertiesDynamicMethodHelper.validateProperty(withoutCallbacks, "property");
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has callback with
     * {@link RuntimeContext} parameter, but has no callback without parameters
     * 
     * @throws Throwable
     */
    @Test
    public void testValidatePropertyNewCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: validateProperty not found");
        PropertiesWithRuntimeContextCallbacks withNewCallbacks = mock(PropertiesWithRuntimeContextCallbacks.class);
        PropertiesDynamicMethodHelper.validateProperty(withNewCallbacks, "property");
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testValidatePropertyOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = mock(PropertiesWithOldCallbacks.class);
        PropertiesDynamicMethodHelper.validateProperty(withOldCallbacks, "property");
        verify(withOldCallbacks).validateProperty();
    }

    /**
     * Asserts that callback without parameters is called even if {@link Properties} class has both kinds of callbacks
     * 
     * @throws Throwable
     */
    @Test
    public void testValidatePropertyBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = mock(PropertiesWithBothCallbacks.class);
        PropertiesDynamicMethodHelper.validateProperty(withBothCallbacks, "property");
        verify(withBothCallbacks).validateProperty();
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has no triggers at all
     * 
     * @throws Throwable
     */
    @Test
    public void testValidatePropertyRuntimeContextNoCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: validateProperty not found");
        PropertiesWithoutCallbacks withoutCallbacks = mock(PropertiesWithoutCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.validateProperty(withoutCallbacks, "property", context);
    }

    /**
     * Asserts that method throws {@link IllegalArgumentException}, when {@link Properties} class has callback with
     * {@link RuntimeContext} parameter, but has no callback without parameters
     * 
     * @throws Throwable
     */
    @Test
    public void testValidatePropertyRuntimeContextNewCallback() throws Throwable {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Method: validateProperty not found");
        PropertiesWithRuntimeContextCallbacks withNewCallbacks = mock(PropertiesWithRuntimeContextCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.validateProperty(withNewCallbacks, "property", context);
    }

    /**
     * Asserts that callback without parameters is called, when it is the only callback present in {@link Properties} class
     * 
     * @throws Throwable
     */
    @Test
    public void testValidatePropertyRuntimeContextOldCallback() throws Throwable {
        PropertiesWithOldCallbacks withOldCallbacks = mock(PropertiesWithOldCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.validateProperty(withOldCallbacks, "property", context);
        verify(withOldCallbacks).validateProperty();
    }

    /**
     * Asserts that callback with {@link RuntimeContext} parameter is called, when {@link Properties} class has both kinds of
     * callbacks
     * and product passes {@link RuntimeContext} argument to the method
     * 
     * @throws Throwable
     */
    @Test
    public void testValidatePropertyRuntimeContextBothCallbacks() throws Throwable {
        PropertiesWithBothCallbacks withBothCallbacks = mock(PropertiesWithBothCallbacks.class);
        RuntimeContext context = new RuntimeContextImpl();
        PropertiesDynamicMethodHelper.validateProperty(withBothCallbacks, "property", context);
        verify(withBothCallbacks).validateProperty(context);
    }

}
