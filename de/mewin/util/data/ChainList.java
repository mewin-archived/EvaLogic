/*
 * Copyright (c) by mewin<mewin001@hotmail.de>
 * All rights reserved.
 */
package de.mewin.util.data;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class ChainList<T> implements List<T>
{
    private int size, chainLength;
    private Chain<Chain<T>> chains;
    
    public ChainList()
    {
        chains = new Chain<>();
        chainLength = 10;
    }
    
    @Override
    public int size()
    {
        return size;
    }

    @Override
    public boolean isEmpty()
    {
        return size <= 0;
    }

    @Override
    public boolean contains(Object o)
    {
        for (Chain<T> chain : chains)
        {
            for (T obj : chain)
            {
                if (obj.equals(o))
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object[] toArray()
    {
        return toArray(new Object[0]);
    }

    @Override
    public <S> S[] toArray(S[] ts)
    {
        S[] arr = (S[]) Array.newInstance(ts.getClass().getComponentType(), size);
        int pos = 0;
        for (Chain<T> chain : chains)
        {
            for (T obj : chain)
            {
                arr[pos++] = (S) obj;
            }
        }
        return arr;
    }

    @Override
    public boolean add(T e)
    {
        size++;
        for (Chain<T> chain : chains)
        {
            if (chain.getLength() < chainLength)
            {
                chain.append(e);
                return true;
            }
        }
        Chain<T> chain = new Chain<>();
        chain.append(e);
        chains.append(chain);
        return true;
    }

    @Override
    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean containsAll(Collection<?> clctn)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addAll(Collection<? extends T> clctn)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addAll(int i,
                          Collection<? extends T> clctn)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeAll(Collection<?> clctn)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean retainAll(Collection<?> clctn)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T get(int i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T set(int i, T e)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void add(int i, T e)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T remove(int i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int indexOf(Object o)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int lastIndexOf(Object o)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListIterator<T> listIterator()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListIterator<T> listIterator(int i)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<T> subList(int i, int i1)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
