package bingo.odata;

import bingo.lang.Named;
import bingo.lang.Predicate;
import bingo.odata.edm.EdmProperty;
import bingo.odata.edm.EdmStructuralType;

/**
 * A static factory to create useful generic predicate instances.
 */
public class OPredicates {

    private OPredicates() {
        
    }

    public static Predicate<OEntity> entityPropertyValueEquals(final String propName, final Object value) {
        return new Predicate<OEntity>() {
            public boolean evaluate(OEntity input) {
                Object pv = input.getProperty(propName).getValue();
                return (value == null) ? pv == null : value.equals(pv);
            }
        };
    }

    public static Predicate<OLink> linkTitleEquals(final String title) {
        return new Predicate<OLink>() {
            public boolean evaluate(OLink input) {
                String lt = input.getTitle();
                return (title == null) ? lt == null : title.equals(lt);
            }
        };
    }

    public static Predicate<OProperty<?>> propertyNameEquals(final String propName) {
        return new Predicate<OProperty<?>>() {
            public boolean evaluate(OProperty<?> input) {
                return input.getName().equals(propName);
            }
        };
    }

    public static Predicate<String> equalsIgnoreCase(final String value) {
        return new Predicate<String>() {
            public boolean evaluate(String input) {
                return input.equalsIgnoreCase(value);
            }
        };
    }

    public static Predicate<EdmProperty> edmPropertyNameEquals(final String name) {
        return new Predicate<EdmProperty>() {
            public boolean evaluate(EdmProperty input) {
                return input.getName().equals(name);
            }
        };
    }

    public static <T extends Named> Predicate<T> nameEquals(Class<T> namedType, final String name) {
        return new Predicate<T>() {
            public boolean evaluate(T input) {
                return input.getName().equals(name);
            }
        };
    }

    public static Predicate<EdmStructuralType> edmSubTypeOf(final EdmStructuralType t) {
        return new Predicate<EdmStructuralType>() {
            public boolean evaluate(EdmStructuralType input) {
                return !t.equals(input) && t.equals(input.getBaseType());
            }
        };
    }
}
