package odata.zinternal;

import java.util.Map;
import java.util.Map.Entry;

import bingo.lang.Func;
import bingo.lang.Func1;

public class Funcs {

    public static <TResult> Func1<TResult, TResult> identity(Class<TResult> clazz) {
        return new Func1<TResult, TResult>() {
            public TResult apply(TResult input) {
                return input;
            }
        };
    }

    public static <TResult> Func<TResult> constant(final TResult value) {
        return new Func<TResult>() {
            public TResult apply() {
                return value;
            }
        };
    }

    public static <TWhatever, TConstant> Func1<TWhatever, TConstant> constant(Class<TWhatever> whateverClass, final TConstant constant) {
        return new Func1<TWhatever, TConstant>() {
            public TConstant apply(TWhatever input) {
                return constant;
            }
        };
    }

    public static <TKey, TValue> Func1<Entry<TKey, TValue>, TKey> mapEntryKey() {
        return new Func1<Entry<TKey, TValue>, TKey>() {
            public TKey apply(Entry<TKey, TValue> input) {
                return input.getKey();
            }
        };
    }

    public static <TKey, TValue> Func1<Entry<TKey, TValue>, TValue> mapEntryValue(Map<TKey, TValue> values) {
        return new Func1<Entry<TKey, TValue>, TValue>() {
            public TValue apply(Entry<TKey, TValue> input) {
                return input.getValue();
            }
        };
    }
}