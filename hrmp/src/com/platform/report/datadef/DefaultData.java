package com.platform.report.datadef;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An ordered list of (key, value) items.  This class provides a default
 * implementation of the {@link Data} interface.
 */
public class DefaultData implements Data, Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 8468154364608194797L;

    /** Storage for the keys. */
    private ArrayList keys;

    /** Storage for the values. */
    private ArrayList values;

    /**
     * Contains (key, Integer) mappings, where the Integer is the index for
     * the key in the list.
     */
    private HashMap indexMap;

  /**
     * Creates a new collection (initially empty).
     */
    public DefaultData() {
        this.keys = new ArrayList();
        this.values = new ArrayList();
        this.indexMap = new HashMap();
    }

    /**
     * Returns the number of items (values) in the collection.
     *
     * @return The item count.
     */
    public int getItemCount() {
        return this.indexMap.size();
    }

    /**
     * Returns a value.
     *
     * @param item  the item of interest (zero-based index).
     *
     * @return The value (possibly <code>null</code>).
     *
     * @throws IndexOutOfBoundsException if <code>item</code> is out of bounds.
     */
    public Object getValue(int item) {
        return this.values.get(item);
    }
    
    /**
     * Returns a value.
     *
     * @param item  the item of interest (zero-based index).
     *
     * @return The Number value (possibly <code>null</code>).
     *
     * @throws IndexOutOfBoundsException if <code>item</code> is out of bounds.
     * @throws ClassCastException if <code>value</code> is
     *     <code>not Number</code>.
     */
    public Number getNumberValue(int item) {
        Object obj = this.values.get(item);
        if(obj == null) {
            return null;
        }
        return (Number) obj;
    }

    /**
     * Returns a key.
     *
     * @param index  the item index (zero-based).
     *
     * @return The row key.
     *
     * @throws IndexOutOfBoundsException if <code>item</code> is out of bounds.
     */
    public Comparable getKey(int index) {
        return (Comparable) this.keys.get(index);
    }

    /**
     * Returns the index for a given key.
     *
     * @param key  the key (<code>null</code> not permitted).
     *
     * @return The index, or <code>-1</code> if the key is not recognised.
     *
     * @throws IllegalArgumentException if <code>key</code> is
     *     <code>null</code>.
     */
    public int getIndex(Comparable key) {
        if (key == null) {
            throw new IllegalArgumentException("Null 'key' argument.");
        }
        final Integer i = (Integer) this.indexMap.get(key);
        if (i == null) {
            return -1;  // key not found
        }
        return i.intValue();
    }

    /**
     * Returns the keys for the values in the collection.
     *
     * @return The keys (never <code>null</code>).
     */
    public List getKeys() {
        return (List) this.keys.clone();
    }

    /**
     * Returns the value for a given key.
     *
     * @param key  the key (<code>null</code> not permitted).
     *
     * @return The value (possibly <code>null</code>).
     *
     * @see #getValue(int)
     */
    public Object getValue(Comparable key) {
        int index = getIndex(key);
        if (index < 0) {
            return null;
        }
        return getValue(index);
    }
    
    /**
     * Returns the value for a given key.
     *
     * @param key  the key (<code>null</code> not permitted).
     *
     * @return The Number value (possibly <code>null</code>).
     * 
     * @throws ClassCastException if <code>value</code> is
     *     <code>not Number</code>.
     *
     * @see #getNumberValue(int)
     */
    public Number getNumberValue(Comparable key) {
        int index = getIndex(key);
        if (index < 0) {
            return null;
        }
        return getNumberValue(index);
    }

    /**
     * Updates an existing value, or adds a new value to the collection.
     *
     * @param key  the key (<code>null</code> not permitted).
     * @param value  the value.
     *
     * @see #addValue(Comparable, Object)
     */
    public void addValue(Comparable key, double value) {
        addValue(key, new Double(value));
    }

    /**
     * Adds a new value to the collection, or updates an existing value.
     * This method passes control directly to the
     * {@link #setValue(Comparable, Object)} method.
     *
     * @param key  the key (<code>null</code> not permitted).
     * @param value  the value (<code>null</code> permitted).
     */
    public void addValue(Comparable key, Object value) {
        setValue(key, value);
    }

    /**
     * Updates an existing value, or adds a new value to the collection.
     *
     * @param key  the key (<code>null</code> not permitted).
     * @param value  the value.
     */
    public void setValue(Comparable key, double value) {
        setValue(key, new Double(value));
    }

    /**
     * Updates an existing value, or adds a new value to the collection.
     *
     * @param key  the key (<code>null</code> not permitted).
     * @param value  the value (<code>null</code> permitted).
     */
    public void setValue(Comparable key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("Null 'key' argument.");
        }
        int keyIndex = getIndex(key);
        if (keyIndex >= 0) {
            this.keys.set(keyIndex, key);
            this.values.set(keyIndex, value);
        }
        else {
            this.keys.add(key);
            this.values.add(value);
            this.indexMap.put(key, new Integer(this.keys.size() - 1));
        }
    }
    
    /**
     * Inserts a new value at the specified position in the dataset or, if
     * there is an existing item with the specified key, updates the value
     * for that item and moves it to the specified position.
     *
     * @param position  the position (in the range 0 to getItemCount()).
     * @param key  the key (<code>null</code> not permitted).
     * @param value  the value.
     *
     * @since 1.0
     */
    public void insertValue(int position, Comparable key, double value) {
        insertValue(position, key, new Double(value));
    }

    /**
     * Inserts a new value at the specified position in the dataset or, if
     * there is an existing item with the specified key, updates the value
     * for that item and moves it to the specified position.
     *
     * @param position  the position (in the range 0 to getItemCount()).
     * @param key  the key (<code>null</code> not permitted).
     * @param value  the value (<code>null</code> permitted).
     *
     * @since 1.0
     */
    public void insertValue(int position, Comparable key, Object value) {
        if (position < 0 || position > getItemCount()) {
            throw new IllegalArgumentException("'position' out of bounds.");
        }
        if (key == null) {
            throw new IllegalArgumentException("Null 'key' argument.");
        }
        int pos = getIndex(key);
        if (pos == position) {
            this.keys.set(pos, key);
            this.values.set(pos, value);
        }
        else {
            if (pos >= 0) {
                this.keys.remove(pos);
                this.values.remove(pos);
            }

            this.keys.add(position, key);
            this.values.add(position, value);
            rebuildIndex();
        }
    }

    /**
     * Rebuilds the key to indexed-position mapping after an positioned insert
     * or a remove operation.
     */
    private void rebuildIndex () {
        this.indexMap.clear();
        for (int i = 0; i < this.keys.size(); i++) {
            final Object key = this.keys.get(i);
            this.indexMap.put(key, new Integer(i));
        }
    }

    /**
     * Removes a value from the collection.
     *
     * @param index  the index of the item to remove (in the range
     *     <code>0</code> to <code>getItemCount() - 1</code>).
     *
     * @throws IndexOutOfBoundsException if <code>index</code> is not within
     *     the specified range.
     */
    public void removeValue(int index) {
        this.keys.remove(index);
        this.values.remove(index);
        rebuildIndex();
    }

    /**
     * Removes a value from the collection.
     *
     * @param key  the item key (<code>null</code> not permitted).
     *
     * @throws IllegalArgumentException if <code>key</code> is
     *     <code>null</code>.
     * @throws IllegalArgumentException if <code>key</code> is not recognised.
     */
    public void removeValue(Comparable key) {
        int index = getIndex(key);
        if (index < 0) {
            throw new IllegalArgumentException("The key (" + key
                    + ") is not recognised.");
        }
        removeValue(index);
    }

    /**
     * Clears all values from the collection.
     *
     * @since 1.0
     */
    public void clear() {
        this.keys.clear();
        this.values.clear();
        this.indexMap.clear();
    }
    
}

