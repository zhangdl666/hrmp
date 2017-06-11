package com.platform.report.datadef;

import java.util.List;

/**
 * An ordered list of (key, value) items where the keys are unique and
 * non-<code>null</code>.
 *
 * @see DefaultData
 */
public interface Data {

    /**
     * Returns the key associated with the item at a given position.  Note
     * that some implementations allow re-ordering of the data items, so the
     * result may be transient.
     *
     * @param index  the item index (in the range <code>0</code> to
     *     <code>getItemCount() - 1</code>).
     *
     * @return The key (never <code>null</code>).
     *
     * @throws IndexOutOfBoundsException if <code>index</code> is not in the
     *     specified range.
     */
    public Comparable getKey(int index);

    /**
     * Returns the index for a given key.
     *
     * @param key  the key (<code>null</code> not permitted).
     *
     * @return The index, or <code>-1</code> if the key is unrecognised.
     *
     * @throws IllegalArgumentException if <code>key</code> is
     *     <code>null</code>.
     */
    public int getIndex(Comparable key);

    /**
     * Returns the keys for the values in the collection.  Note that you can
     * access the values in this collection by key or by index.  For this
     * reason, the key order is important - this method should return the keys
     * in order.  The returned list may be unmodifiable.
     *
     * @return The keys (never <code>null</code>).
     */
    public List getKeys();

    /**
     * Returns the value for a given key.
     *
     * @param key  the key.
     *
     * @return The value (possibly <code>null</code>).
     *
     */
    public Object getValue(Comparable key);
    
    /**
     * Returns the value for a given key.
     *
     * @param key  the key.
     *
     * @return The Number value (possibly <code>null</code>).
     * 
     * @throws ClassCastException if <code>value</code> is
     *     <code>not Number</code>.
     */
    public Number getNumberValue(Comparable key);
    
    /**
     * Returns the number of items (values) in the collection.
     *
     * @return The item count (possibly zero).
     */
    public int getItemCount();
    
    /**
     * Returns the value with the specified index.
     *
     * @param index  the item index (in the range <code>0</code> to
     *     <code>getItemCount() - 1</code>).
     *
     * @return The value (possibly <code>null</code>).
     *
     * @throws IndexOutOfBoundsException if <code>index</code> is not in the
     *     specified range.
     */
    public Object getValue(int index);
    
    /**
     * Returns the value with the specified index.
     *
     * @param index  the item index (in the range <code>0</code> to
     *     <code>getItemCount() - 1</code>).
     *
     * @return The Number value (possibly <code>null</code>).
     *
     * @throws IndexOutOfBoundsException if <code>index</code> is not in the
     *     specified range.
     * @throws ClassCastException if <code>value</code> is
     *     <code>not Number</code>.
     */
    public Number getNumberValue(int index);
}
