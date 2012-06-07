package odata.zinternal.lang;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/**
 * A List implementation whose structure is immutable after construction.
 * <p>Useful for apis that assume a returned list remains unmodified.
 * <p>All mutation methods throw <code>UnsupportedOperationException</code>
 *
 * @param <T>  the list item type
 */
public class ImmutableList<T> implements List<T>, RandomAccess {

    private static final ImmutableList<Object> EMPTY = new ImmutableList<Object>(Collections.emptyList());
    
    @SuppressWarnings("unchecked")
    public static <T> ImmutableList<T> create(T... values) {
        if (values.length == 0) {
            return (ImmutableList<T>) EMPTY;
        }
        return new ImmutableList<T>(Arrays.asList(values));
    }

    @SuppressWarnings("unchecked")
    public static <T> ImmutableList<T> copyOf(List<T> values) {
        if (values == null) {
            return (ImmutableList<T>) EMPTY;
        }
        
        if (values instanceof ImmutableList) {
            return (ImmutableList<T>) values;
        }
        
        return (ImmutableList<T>) create(values.toArray());
    }
    
    private final List<T> values;

    private ImmutableList(List<T> values) {
        this.values = values;
    }

    public int size() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public boolean contains(Object o) {
        return values.contains(o);
    }

    public Iterator<T> iterator() {
        return values.iterator();
    }

    public Object[] toArray() {
        return values.toArray();
    }

    public <TArray> TArray[] toArray(TArray[] a) {
        return values.toArray(a);
    }

    public boolean containsAll(Collection<?> c) {
        return values.containsAll(c);
    }

    public T get(int index) {
        return values.get(index);
    }

    public int indexOf(Object o) {
        return values.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return values.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return values.listIterator();
    }

    public ListIterator<T> listIterator(int index) {
        return values.listIterator(index);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return values.subList(fromIndex, toIndex);
    }
    
    @Override
    public String toString() {
        return values.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        return (obj instanceof ImmutableList) && ((ImmutableList<?>) obj).values.equals(values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }    

    private static UnsupportedOperationException newModificationUnsupported() {
        return new UnsupportedOperationException(ImmutableList.class.getSimpleName() + " cannot be modified");
    }

    public void clear() {
        throw newModificationUnsupported();
    }

    public T set(int paramInt, T paramE) {
        throw newModificationUnsupported();
    }

    public void add(int paramInt, T paramE) {
        throw newModificationUnsupported();
    }

    public T remove(int paramInt) {
        throw newModificationUnsupported();
    }

    public boolean add(T paramE) {
        throw newModificationUnsupported();
    }

    public boolean remove(Object paramObject) {
        throw newModificationUnsupported();
    }

    public boolean addAll(Collection<? extends T> paramCollection) {
        throw newModificationUnsupported();
    }

    public boolean addAll(int paramInt, Collection<? extends T> paramCollection) {
        throw newModificationUnsupported();
    }

    public boolean removeAll(Collection<?> paramCollection) {
        throw newModificationUnsupported();
    }

    public boolean retainAll(Collection<?> paramCollection) {
        throw newModificationUnsupported();
    }
}
