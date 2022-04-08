package com.zecher;

import java.io.Serializable;
import java.util.*;

public class ObservableList<T> extends ArrayList<T> {

    private ListChangeListener<T> onListChange;

    public ObservableList() {
    }

    public ObservableList(ListChangeListener<T> onListChange) {
        this.onListChange = onListChange;
    }

    public ObservableList(int initialCapacity) {
        super(initialCapacity);
    }

    public ObservableList(int initialCapacity, ListChangeListener<T> onListChange) {
        super(initialCapacity);
        this.onListChange = onListChange;
    }

    public ObservableList(Collection<? extends T> c) {
        super(c);
    }

    public ObservableList(Collection<? extends T> c, ListChangeListener<T> onListChange) {
        super(c);
        this.onListChange = onListChange;
    }

    public void setOnListChange(ListChangeListener<T> onListChange) {
        this.onListChange = onListChange;
    }

    public ListChangeListener<T> getOnListChange() {
        return onListChange;
    }

    public boolean hasListener() {
        return onListChange != null;
    }

    @Override
    public boolean add(T object) {
        if (hasListener()) {
            onListChange.onListChange(new ListChangeListener.ListChange(size(), size()+1, List.of(object), null, null));
        }
        return super.add(object);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        if (hasListener()) {
            onListChange.onListChange(new ListChangeListener.ListChange(size(), size()+collection.size(), (List<Object>) collection, null, null));
        }
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> collection) {
        if (hasListener()) {
            onListChange.onListChange(new ListChangeListener.ListChange(size(), size()+collection.size(), (List<Object>) collection, null, null));
        }
        return super.addAll(index, collection);
    }

    @Override
    public boolean remove(Object object) {
        if (hasListener()) {
            onListChange.onListChange(new ListChangeListener.ListChange(size(), size()-1, null, List.of(object), null));
        }
        return super.remove(object);
    }

    @Override
    public T remove(int index) {
        Object removeObject = get(index);
        if (hasListener()) {
            onListChange.onListChange(new ListChangeListener.ListChange(size(), size()-1, null, List.of(removeObject), null));
        }
        return super.remove(index);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if (hasListener()) {
            onListChange.onListChange(new ListChangeListener.ListChange(size(), size()-collection.size(), null, (List<Object>) collection, null));
        }
        return super.removeAll(collection);
    }

    @Override
    public T set(int index, T object) {
        if (hasListener()) {
            onListChange.onListChange(new ListChangeListener.ListChange(size(), size(), null, null, List.of(object)));
        }
        return super.set(index, object);
    }

    public interface ListChangeListener<T> extends Serializable {
        void onListChange(ListChange change);

        class ListChange {
            private final int oldSize;
            private final int newSize;
            private final List<Object> addedValues;
            private final List<Object> removedValues;
            private final List<Object> setValues;

            public ListChange(int oldSize, int newSize, List<Object> addedValues, List<Object> removedValues, List<Object> setValues) {
                this.oldSize = oldSize;
                this.newSize = newSize;
                this.addedValues = addedValues;
                this.removedValues = removedValues;
                this.setValues = setValues;
            }

            public int getOldSize() {
                return oldSize;
            }

            public int getNewSize() {
                return newSize;
            }

            public List<Object> getRemovedValues() {
                return removedValues;
            }

            public List<Object> getAddedValues() {
                return addedValues;
            }

            public List<Object> getSetValues() {
                return setValues;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                ListChange that = (ListChange) o;
                return oldSize == that.oldSize && newSize == that.newSize && Objects.equals(addedValues, that.addedValues) && Objects.equals(removedValues, that.removedValues) && Objects.equals(setValues, that.setValues);
            }

            @Override
            public int hashCode() {
                return Objects.hash(oldSize, newSize, removedValues, addedValues, setValues);
            }
        }
    }
}
