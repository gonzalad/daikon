package org.talend.daikon.multitenancy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class NoOpForAllTest {

    @Test
    public void shouldExecute() throws Exception {
        // Given
        ForAll forAll = new NoOpForAll();

        // When
        AtomicBoolean executed = new AtomicBoolean(false);
        forAll.execute(() -> executed.set(true));

        // Then
        assertTrue(executed.get());
    }

    @Test
    public void shouldExecuteWithMatchCondition() throws Exception {
        // Given
        ForAll forAll = new NoOpForAll();

        // When
        AtomicBoolean executed = new AtomicBoolean(false);
        forAll.execute(forAll.condition().operational(new Object()), () -> executed.set(true));

        // Then
        assertTrue(executed.get());
    }

    @Test
    public void shouldNotExecuteWithNoMatchCondition() throws Exception {
        // Given
        ForAll forAll = new NoOpForAll();

        // When
        AtomicBoolean executed = new AtomicBoolean(false);
        forAll.execute(() -> false, () -> executed.set(true));

        // Then
        assertFalse(executed.get());
    }
}