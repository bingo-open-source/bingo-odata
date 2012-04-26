package bingo.odata;

import bingo.lang.Func;
import bingo.lang.Func1;
import bingo.lang.Named;
import bingo.lang.NamedValue;
import bingo.odata.edm.EdmType;

/**
 * A static factory to create useful generic function instances.
 */
public class OFuncs {

    private OFuncs() {
    }

    public static <T extends Named> Func1<T, String> name(Class<T> namedType) {
        return new Func1<T, String>() {
            public String evaluate(T input) {
                return input.getName();
            }
        };
    }

    public static <T extends Titled> Func1<T, String> title(Class<T> titledType) {
        return new Func1<T, String>() {
            public String evaluate(T input) {
                return input.getTitle();
            }
        };
    }

    public static Func1<EdmType, String> edmTypeFullyQualifiedTypeName() {
        return new Func1<EdmType, String>() {
            public String evaluate(EdmType input) {
                return input.getFullyQualifiedTypeName();
            }
        };
    }

    public static <TProperty> Func1<OEntity, TProperty> entityPropertyValue(final String propName, final Class<TProperty> propClass) {
        return new Func1<OEntity, TProperty>() {
            public TProperty evaluate(OEntity input) {
                return input.getProperty(propName, propClass).getValue();
            }
        };
    }

    public static <T> Func1<NamedValue<T>, OProperty<T>> namedValueToProperty() {
        return new Func1<NamedValue<T>, OProperty<T>>() {
            public OProperty<T> evaluate(NamedValue<T> input) {
                return OProperties.simple(input.getName(), input.getValue());
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static Func1<NamedValue, OProperty<?>> namedValueToPropertyRaw() {
        return new Func1<NamedValue, OProperty<?>>() {
            public OProperty<?> evaluate(NamedValue input) {
                return OProperties.simple(input.getName(), input.getValue());
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static Func1<Object, OProperty<?>> namedValueToPropertyRawCast() {
        return new Func1<Object, OProperty<?>>() {
            public OProperty<?> evaluate(Object input) {
                NamedValue namedValue = ((NamedValue) input);
                return OProperties.simple(namedValue.getName(), namedValue.getValue());
            }
        };
    }

    public static <T1, T2> Func1<Object, T2> widen(final Func1<T1, T2> fn) {
        return new Func1<Object, T2>() {
            @SuppressWarnings("unchecked")
            public T2 evaluate(Object input) {
                return fn.evaluate((T1) input);
            }
        };
    }

    public static Func1<OLink, String> olinkTitle() {
        return new Func1<OLink, String>() {
            public String evaluate(OLink input) {
                return input.getTitle();
            }
        };
    }

    public static Runnable asRunnable(final Func<?> func) {
        return new Runnable() {
            public void run() {
                func.evaluate();
            }
        };
    }

}
