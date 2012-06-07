package odata;

import java.util.Comparator;

import bingo.lang.NamedValue;

/**
 * A static factory to create useful {@link Comparator} instances.
 */
public class OComparators {

    private OComparators() {
    }

    @SuppressWarnings("unchecked")
    public static Comparator<NamedValue> namedValueByNameRaw() {
        return new Comparator<NamedValue>() {
            public int compare(NamedValue lhs, NamedValue rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        };
    }

    public static Comparator<OProperty<?>> propertyByName() {
        return new Comparator<OProperty<?>>() {
            public int compare(OProperty<?> lhs, OProperty<?> rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        };
    }

}
