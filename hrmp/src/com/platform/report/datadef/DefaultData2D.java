package com.platform.report.datadef;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * A data structure that stores zero, one or many values, where each value
 * is associated with two keys (a 'row' key and a 'column' key).  The keys
 * should be (a) instances of {@link Comparable} and (b) immutable.
 */
public class DefaultData2D implements Data2D, Serializable{

    /** For serialization. */
    private static final long serialVersionUID = -5514169970951994748L;

    /** The row keys. */
    private List rowKeys;

    /** The column keys. */
    private List columnKeys;

    /** The row data. */
    private List rows;

    /** If the row keys should be sorted by their comparable order. */
    private boolean sortRowKeys;

    /**
     * Creates a new instance (initially empty).
     */
    public DefaultData2D() {
        this(false);
    }

    /**
     * Creates a new instance (initially empty).
     *
     * @param sortRowKeys  if the row keys should be sorted.
     */
    public DefaultData2D(boolean sortRowKeys) {
        this.rowKeys = new java.util.ArrayList();
        this.columnKeys = new java.util.ArrayList();
        this.rows = new java.util.ArrayList();
        this.sortRowKeys = sortRowKeys;
    }

    /**
     * Returns the row count.
     *
     * @return The row count.
     *
     * @see #getColumnCount()
     */
    public int getRowCount() {
        return this.rowKeys.size();
    }

    /**
     * Returns the column count.
     *
     * @return The column count.
     *
     * @see #getRowCount()
     */
    public int getColumnCount() {
        return this.columnKeys.size();
    }

    /**
     * Returns the value for a given row and column.
     *
     * @param row  the row index.
     * @param column  the column index.
     *
     * @return The value.
     *
     * @see #getValue(Comparable, Comparable)
     */
    public Object getValue(int row, int column) {
        Object result = null;
        DefaultData rowData = (DefaultData) this.rows.get(row);
        if (rowData != null) {
            Comparable columnKey = (Comparable) this.columnKeys.get(column);
            // the row may not have an entry for this key, in which case the
            // return value is null
            int index = rowData.getIndex(columnKey);
            if (index >= 0) {
                result = rowData.getValue(index);
            }
        }
        return result;
    }
    
    /**
     * Returns the value for a given row and column.
     *
     * @param row  the row index.
     * @param column  the column index.
     *
     * @return The value.
     * 
     * @throws ClassCastException if <code>value</code> is
     *     <code>not Number</code>.
     *
     * @see #getNumberValue(Comparable, Comparable)
     */
    public Number getNumberValue(int row, int column) {
        Object obj = getValue(row, column);
        if(obj == null) {
            return null;
        }
        return (Number) obj;
    }

    /**
     * Returns the key for a given row.
     *
     * @param row  the row index (in the range 0 to {@link #getRowCount()} - 1).
     *
     * @return The row key.
     *
     * @see #getRowIndex(Comparable)
     * @see #getColumnKey(int)
     */
    public Comparable getRowKey(int row) {
        return (Comparable) this.rowKeys.get(row);
    }

    /**
     * Returns the row index for a given key.
     *
     * @param key  the key (<code>null</code> not permitted).
     *
     * @return The row index.
     *
     * @see #getRowKey(int)
     * @see #getColumnIndex(Comparable)
     */
    public int getRowIndex(Comparable key) {
        if (key == null) {
            throw new IllegalArgumentException("Null 'key' argument.");
        }
        if (this.sortRowKeys) {
            return Collections.binarySearch(this.rowKeys, key);
        }
        else {
            return this.rowKeys.indexOf(key);
        }
    }

    /**
     * Returns the row keys in an unmodifiable list.
     *
     * @return The row keys.
     *
     * @see #getColumnKeys()
     */
    public List getRowKeys() {
        return Collections.unmodifiableList(this.rowKeys);
    }

    /**
     * Returns the key for a given column.
     *
     * @param column  the column (in the range 0 to {@link #getColumnCount()}
     *     - 1).
     *
     * @return The key.
     *
     * @see #getColumnIndex(Comparable)
     * @see #getRowKey(int)
     */
    public Comparable getColumnKey(int column) {
        return (Comparable) this.columnKeys.get(column);
    }

    /**
     * Returns the column index for a given key.
     *
     * @param key  the key (<code>null</code> not permitted).
     *
     * @return The column index.
     *
     * @see #getColumnKey(int)
     * @see #getRowIndex(Comparable)
     */
    public int getColumnIndex(Comparable key) {
        if (key == null) {
            throw new IllegalArgumentException("Null 'key' argument.");
        }
        return this.columnKeys.indexOf(key);
    }

    /**
     * Returns the column keys in an unmodifiable list.
     *
     * @return The column keys.
     *
     * @see #getRowKeys()
     */
    public List getColumnKeys() {
        return Collections.unmodifiableList(this.columnKeys);
    }

    /**
     * Returns the value for the given row and column keys. 
     *
     * @param rowKey  the row key (<code>null</code> not permitted).
     * @param columnKey  the column key (<code>null</code> not permitted).
     * 
     * @throws ClassCastException if <code>value</code> is
     *     <code>not Number</code>.
     * @throws ClassCastException if <code>value</code> is
     *     <code>not Number</code>.
     *
     * @return The Number value (possibly <code>null</code>).
     *
     */
    public Number getNumberValue(Comparable rowKey, Comparable columnKey) {
        Object obj = getValue(rowKey, columnKey);
        if(obj == null) {
            return null;
        }
        return (Number) obj;
    }
    
    /**
     * Returns the value for the given row and column keys. 
     *
     * @param rowKey  the row key (<code>null</code> not permitted).
     * @param columnKey  the column key (<code>null</code> not permitted).
     *
     * @return The value (possibly <code>null</code>).
     *
     */
    public Object getValue(Comparable rowKey, Comparable columnKey) {
        if (rowKey == null) {
            throw new IllegalArgumentException("Null 'rowKey' argument.");
        }
        if (columnKey == null) {
            throw new IllegalArgumentException("Null 'columnKey' argument.");
        }

        int row = getRowIndex(rowKey);
        if (row >= 0) {
            DefaultData rowData
                = (DefaultData) this.rows.get(row);
            int col = rowData.getIndex(columnKey);
            return (col >= 0 ? rowData.getValue(col) : null);
        }
        return null;
    }

    /**
     * Adds a value to the table.  Performs the same function as
     * #setValue(Object, Comparable, Comparable).
     *
     * @param value  the value (<code>null</code> permitted).
     * @param rowKey  the row key (<code>null</code> not permitted).
     * @param columnKey  the column key (<code>null</code> not permitted).
     *
     */
    public void addValue(Object value, Comparable rowKey,
                         Comparable columnKey) {
        // defer argument checking
        setValue(value, rowKey, columnKey);
    }

    /**
     * Adds or updates a value.
     *
     * @param value  the value (<code>null</code> permitted).
     * @param rowKey  the row key (<code>null</code> not permitted).
     * @param columnKey  the column key (<code>null</code> not permitted).
     *
     * @see #addValue(Object, Comparable, Comparable)
     * @see #removeValue(Comparable, Comparable)
     */
    public void setValue(Object value, Comparable rowKey,
                         Comparable columnKey) {

        DefaultData row;
        int rowIndex = getRowIndex(rowKey);

        if (rowIndex >= 0) {
            row = (DefaultData) this.rows.get(rowIndex);
        }
        else {
            row = new DefaultData();
            if (this.sortRowKeys) {
                rowIndex = -rowIndex - 1;
                this.rowKeys.add(rowIndex, rowKey);
                this.rows.add(rowIndex, row);
            }
            else {
                this.rowKeys.add(rowKey);
                this.rows.add(row);
            }
        }
        row.setValue(columnKey, value);

        int columnIndex = this.columnKeys.indexOf(columnKey);
        if (columnIndex < 0) {
            this.columnKeys.add(columnKey);
        }
    }

    /**
     * Removes a value from the table by setting it to <code>null</code>.  If
     * all the values in the specified row and/or column are now
     * <code>null</code>, the row and/or column is removed from the table.
     *
     * @param rowKey  the row key (<code>null</code> not permitted).
     * @param columnKey  the column key (<code>null</code> not permitted).
     *
     */
    public void removeValue(Comparable rowKey, Comparable columnKey) {
        setValue(null, rowKey, columnKey);

        // 1. check whether the row is now empty.
        boolean allNull = true;
        int rowIndex = getRowIndex(rowKey);
        DefaultData row = (DefaultData) this.rows.get(rowIndex);

        for (int item = 0, itemCount = row.getItemCount(); item < itemCount;
             item++) {
            if (row.getValue(item) != null) {
                allNull = false;
                break;
            }
        }

        if (allNull) {
            this.rowKeys.remove(rowIndex);
            this.rows.remove(rowIndex);
        }

        // 2. check whether the column is now empty.
        allNull = true;
        //int columnIndex = getColumnIndex(columnKey);

        for (int item = 0, itemCount = this.rows.size(); item < itemCount;
             item++) {
            row = (DefaultData) this.rows.get(item);
            int columnIndex = row.getIndex(columnKey);
            if (columnIndex >= 0 && row.getValue(columnIndex) != null) {
                allNull = false;
                break;
            }
        }

        if (allNull) {
            for (int item = 0, itemCount = this.rows.size(); item < itemCount;
                 item++) {
                row = (DefaultData) this.rows.get(item);
                int columnIndex = row.getIndex(columnKey);
                if (columnIndex >= 0) {
                    row.removeValue(columnIndex);
                }
            }
            this.columnKeys.remove(columnKey);
        }
    }

    /**
     * Removes a row.
     *
     * @param rowIndex  the row index.
     *
     * @see #removeRow(Comparable)
     * @see #removeColumn(int)
     */
    public void removeRow(int rowIndex) {
        this.rowKeys.remove(rowIndex);
        this.rows.remove(rowIndex);
    }

    /**
     * Removes a row from the table.
     *
     * @param rowKey  the row key (<code>null</code> not permitted).
     *
     * @see #removeRow(int)
     * @see #removeColumn(Comparable)
     *
     * @throws IllegalArgumentException if <code>rowKey</code> is not defined in the
     *         table.
     */
    public void removeRow(Comparable rowKey) {
        if (rowKey == null) {
            throw new IllegalArgumentException("Null 'rowKey' argument.");
        }
        int index = getRowIndex(rowKey);
        if (index >= 0) {
            removeRow(index);
        }
        else {
            throw new IllegalArgumentException("Unknown key: " + rowKey);
        }
    }

    /**
     * Removes a column.
     *
     * @param columnIndex  the column index.
     *
     * @see #removeColumn(Comparable)
     * @see #removeRow(int)
     */
    public void removeColumn(int columnIndex) {
        Comparable columnKey = getColumnKey(columnIndex);
        removeColumn(columnKey);
    }

    /**
     * Removes a column from the table.
     *
     * @param columnKey  the column key (<code>null</code> not permitted).
     *
     * @throws IllegalArgumentException if the table does not contain a column with
     *     the specified key.
     * @throws IllegalArgumentException if <code>columnKey</code> is
     *     <code>null</code>.
     *
     * @see #removeColumn(int)
     * @see #removeRow(Comparable)
     */
    public void removeColumn(Comparable columnKey) {
        if (columnKey == null) {
            throw new IllegalArgumentException("Null 'columnKey' argument.");
        }
        if (!this.columnKeys.contains(columnKey)) {
            throw new IllegalArgumentException("Unknown key: " + columnKey);
        }
        Iterator iterator = this.rows.iterator();
        while (iterator.hasNext()) {
            DefaultData rowData = (DefaultData) iterator.next();
            int index = rowData.getIndex(columnKey);
            if (index >= 0) {
                rowData.removeValue(columnKey);
            }
        }
        this.columnKeys.remove(columnKey);
    }

    /**
     * Clears all the data and associated keys.
     */
    public void clear() {
        this.rowKeys.clear();
        this.columnKeys.clear();
        this.rows.clear();
    }

}
