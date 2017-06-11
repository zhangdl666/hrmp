package com.platform.report.datadef;
import java.util.List;

/**
 * An extension of the interface where a unique key is
 * associated with the row and column indices.
 * @see DefaultData2D
 */
public interface Data2D {

    /**
     * Returns the row key for a given index.
     *
     * @param row  the row index (zero-based).
     *
     * @return The row key.
     *
     * @throws IndexOutOfBoundsException if <code>row</code> is out of bounds.
     */
    public Comparable getRowKey(int row);

    /**
     * Returns the row index for a given key.
     *
     * @param key  the row key.
     *
     * @return The row index, or <code>-1</code> if the key is unrecognised.
     */
    public int getRowIndex(Comparable key);

    /**
     * Returns the row keys.
     *
     * @return The keys.
     */
    public List getRowKeys();

    /**
     * Returns the column key for a given index.
     *
     * @param column  the column index (zero-based).
     *
     * @return The column key.
     *
     * @throws IndexOutOfBoundsException if <code>row</code> is out of bounds.
     */
    public Comparable getColumnKey(int column);

    /**
     * Returns the column index for a given key.
     *
     * @param key  the column key.
     *
     * @return The column index, or <code>-1</code> if the key is unrecognised.
     */
    public int getColumnIndex(Comparable key);

    /**
     * Returns the column keys.
     *
     * @return The keys.
     */
    public List getColumnKeys();

    /**
     * Returns the value associated with the specified keys.
     *
     * @param rowKey  the row key (<code>null</code> not permitted).
     * @param columnKey  the column key (<code>null</code> not permitted).
     *
     * @return The value.
     *
     */
    public Object getValue(Comparable rowKey, Comparable columnKey);
    
    /**
     * Returns the value associated with the specified keys.
     *
     * @param rowKey  the row key (<code>null</code> not permitted).
     * @param columnKey  the column key (<code>null</code> not permitted).
     * 
     * @throws ClassCastException if <code>value</code> is
     *     <code>not Number</code>.
     *
     * @return The Number value.
     *
     */
    public Number getNumberValue(Comparable rowKey, Comparable columnKey);
    
    /**
     * Returns the number of rows in the table.
     *
     * @return The row count.
     */
    public int getRowCount();

    /**
     * Returns the number of columns in the table.
     *
     * @return The column count.
     */
    public int getColumnCount();

    /**
     * Returns a value from the table.
     *
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     *
     * @return The value (possibly <code>null</code>).
     *
     * @throws IndexOutOfBoundsException if the <code>row</code>
     *         or <code>column</code> is out of bounds.
     */
    public Object getValue(int row, int column);
    
    /**
     * Returns a value from the table.
     *
     * @param row  the row index (zero-based).
     * @param column  the column index (zero-based).
     *
     * @return The Number value (possibly <code>null</code>).
     *
     * @throws IndexOutOfBoundsException if the <code>row</code>
     *         or <code>column</code> is out of bounds.
     * @throws ClassCastException if <code>value</code> is
     *     <code>not Number</code>.
     */
    public Number getNumberValue(int row, int column);
}

