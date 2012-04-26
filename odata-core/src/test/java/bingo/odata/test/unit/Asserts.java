package bingo.odata.test.unit;

import junit.framework.Assert;
import bingo.lang.Func;
import bingo.odata.OFuncs;
import bingo.odata.Throwables;

public class Asserts {

    public static void assertThrows(Class<?> expectedThrowableType, Runnable code) {
        try {
            code.run();
        } catch (Throwable t) {
            if (expectedThrowableType.equals(t.getClass()))
                return;
            Throwables.propagate(t);
        }
        Assert.fail(String.format("Expected %s to be thrown, but nothing thrown", expectedThrowableType.getSimpleName()));
    }

    public static void assertThrows(Class<?> expectedThrowableType, Func<?> code) {
        assertThrows(expectedThrowableType, OFuncs.asRunnable(code));
    }

}
