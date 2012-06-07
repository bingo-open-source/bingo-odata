/**
 * This file created at 2012-4-19.
 *
 * Copyright (c) 2002-2012 Bingosoft, Inc. All rights reserved.
 */
package odata.zinternal.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <code>{@link CollectionUtils}</code>
 */
public final class CollectionUtils {

    public static <T> List<T> newList(List<? extends T> list) {
        return new ArrayList<T>(list);
    }

    public static <K, V> Map<K, V> newMap(Map<? extends K, ? extends V> map) {
        return new HashMap<K, V>(map);
    }

    public static <T> Set<T> newSet(Set<? extends T> set) {
        return new HashSet<T>(set);
    }

}