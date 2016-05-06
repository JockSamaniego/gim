package org.gob.gim.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.ajax4jsf.model.SerializableDataModel;

/**
 * 
 * @author 
 * http://eclecticprogrammer.com/2008/07/30/a-generic-superclass-for-sorting-and-paginating-in-the-database-with-richfaces/
 *
 * @param <T>
 * @param <U>
 */

public abstract class PageableDataModel<T, U> extends SerializableDataModel {
    /** */
    private static final long serialVersionUID = 8523897206L;	
	
    /** */
    protected boolean detached = false;
    /** */
    protected boolean descending = false;
    /** */
    protected String sortField = getDefaultSortField();    
    /** */
    protected U currentId;
    /** */
    protected Integer rowCount = 0;
    /** */
    protected Map<U, T> wrappedData = new HashMap<U, T>();    
    /** */
    protected List<U> wrappedKeys = new ArrayList<U>();
    /** */
    private int rowIndex;
    
    /**
     * @see org.ajax4jsf.model.ExtendedDataModel#getRowKey()
     */
    @Override
    public Object getRowKey()
    {
        return currentId;
    }

    /**
     * @see org.ajax4jsf.model.ExtendedDataModel#setRowKey(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setRowKey(final Object key)
    {
        this.currentId = (U) key;
    }

    /**
     * @see org.ajax4jsf.model.SerializableDataModel#update()
     */
    @Override
    public void update()
    {
        if (getSortFieldObject() != null)
        {
            final String newSortField = getSortFieldObject().toString();
            if (newSortField.equals(sortField))
            {
                descending = !descending;
            }
            sortField = newSortField;
        }
        detached = false;
    }
    
    @Override 
    public int getRowCount() 
    { 
        if (rowCount == null) 
        { 
            rowCount = getObjectsNumber(); 
        } 
        return rowCount; 
    }      
    
    /**
     * @see org.ajax4jsf.model.ExtendedDataModel#walk(javax.faces.context.FacesContext,
     *      org.ajax4jsf.model.DataVisitor, org.ajax4jsf.model.Range,
     *      java.lang.Object)
     */
    @Override
    public void walk(final FacesContext context, final DataVisitor visitor, final Range range, final Object argument)
            throws IOException
    {
       
        if (detached && getSortFieldObject() != null)
        {
            for (final U key : wrappedKeys)
            {
                setRowKey(key);
                visitor.process(context, key, argument);
            }
        } else
        { // if not serialized, than we request data from data provider
        	int firstRow = ((SequenceRange) range).getFirstRow();
            int numberOfRows = ((SequenceRange) range).getRows();
            wrappedKeys = new ArrayList<U>();
            Collection<T> objects = findObjects(firstRow, numberOfRows, sortField, descending);
            for (final T object : objects)
            {
                wrappedKeys.add(getId(object));
                wrappedData.put(getId(object), object);
                visitor.process(context, getId(object), argument);
            }
        }
    }


    /**
     * @return Object
     */
    protected Object getSortFieldObject()
    {
        final FacesContext context = FacesContext.getCurrentInstance();
        final Object sortFieldObject = context.getExternalContext().getRequestParameterMap().get("sortField");
        return sortFieldObject;
    }

    /**
     * @param sortField
     */
    public void setSortField(final String sortField)
    {
        if (this.sortField.equals(sortField))
        {
            descending = !descending;
        } else
        {
            this.sortField = sortField;
        }
    }

    /**
     * @return String
     */
    public String getSortField()
    {
        return sortField;
    }

    /**
     * @see org.ajax4jsf.model.ExtendedDataModel#getSerializableModel(org.ajax4jsf.model.Range)
     */
    @Override
    public SerializableDataModel getSerializableModel(final Range range)
    {
        if (wrappedKeys != null)
        {
            detached = true;
            return this;
        }
        return null;
    }

    /**
     * @see javax.faces.model.DataModel#setRowIndex(int)
     */
    @Override
    public void setRowIndex(int rowIndex)
    {
    	this.rowIndex = rowIndex;

    }

    /**
     * @see javax.faces.model.DataModel#setWrappedData(java.lang.Object)
     */
    @Override
    public void setWrappedData(final Object data)
    {
        throw new UnsupportedOperationException();

    }

    /**
     * @see javax.faces.model.DataModel#getRowIndex()
     */
    @Override
    public int getRowIndex()
    {
        return rowIndex;
    }

    /**
     * @see javax.faces.model.DataModel#getWrappedData()
     */
    @Override
    public Object getWrappedData()
    {
        throw new UnsupportedOperationException();
    }


    /**
     * @see javax.faces.model.DataModel#isRowAvailable()
     */
    @Override
    public boolean isRowAvailable()
    {
        if (currentId == null)
        {
            return false;
        }
        if (wrappedKeys.contains(currentId))
        {
            return true;
        }
        if (wrappedData.entrySet().contains(currentId))
        {
            return true;
        }
        try
        {
            if (getObjectById(currentId) != null)
            {
                return true;
            }
        } catch (final Exception e)
        {

        }
        return false;
    }

    /**
     * @see javax.faces.model.DataModel#getRowData()
     */
    @Override
    public Object getRowData()
    {
        if (currentId == null)
        {
            return null;
        }

        T object = wrappedData.get(currentId);
        if (object == null)
        {
            object = getObjectById(currentId);
            wrappedData.put(currentId, object);
        }
        return object;
    }


	
    
    /**
     *
     * @param object
     * @return U
     */
    public abstract U getId(T object);

    /**
     * @param firstRow
     * @param numberOfRows
     * @param sortField
     * @param descending
     * @return List<T>
     */
    public abstract List<T> findObjects(int firstRow, int numberOfRows, String sortField, boolean descending);

    /**
     * @param id
     * @return T
     */
    public abstract T getObjectById(U id);

    /**
     * @return String
     */
    public abstract String getDefaultSortField();

    /**
     * @return int
     */
    public abstract int getObjectsNumber();
}
