/*
 * Copyright (c) by mewin<mewin001@hotmail.de>
 * All rights reserved.
 */
package de.mewin.util.data;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class Chain<T> implements Iterable<T>
{
    private int length;
    private ChainElement first, last;
    
    private Chain(ChainElement first, int length)
    {
        this.first = first;
    }
    
    public Chain()
    {
        
    }
    
    public Chain(Iterable<T> initValues)
    {
        this.append(initValues);
    }
    
    public final void append(Iterable<T> values)
    {
        for (T val : values)
        {
            append(val);
        }
    }
    
    public void sort(Comparator<T> comp)
    {
        this.first = sort(this, comp).first;
    }
    
    public static <T> Chain<T> sort(Chain<T> chain, Comparator<T> comp)
    {
        if (chain.getLength() < 2)
        {
            return chain;
        }
        else
        {
            Chain<T> lower = new Chain<>();
            Chain<T> higher = new Chain<>();
            T mid = chain.first.value;
            for (T ele : chain)
            {
                if (ele == mid)
                {
                    continue;
                }
                else if (comp.compare(ele, mid) < 0)
                {
                    lower.append(ele);
                }
                else
                {
                    higher.append(ele);
                }
            }
            Chain<T> newChain = new Chain<>(sort(lower, comp));
            newChain.append(mid);
            newChain.append(sort(higher, comp));
            return newChain;
        }
    }
    
    public void append(T value)
    {
        ChainElement ele = new ChainElement(value);
        if (last != null)
        {
            last.next = ele;
        }
        else
        {
            first = ele;
        }
        last = ele;
        length++;
    }
    
    public Chain<T> splitAt(int pos)
    {
        ChainElement prev = null, current = first;
        for (int i = 1; i < pos; i++)
        {
            if (current == null)
            {
                return null;
            }
            prev = current;
            current = current.next;
        }
        Chain<T> newChain = new Chain<>(current, length - pos);
        if (prev != null)
        {
            prev.next = null;
        }
        this.length = pos;
        return newChain;
    }
    
    public Object[] toArray()
    {
        Object[] arr = new Object[length];
        int pos = 0;
        for (T ele : this)
        {
            arr[pos++] = ele;
        }
        return arr;
    }
    
    public T[] toArray(Class<T> clazz)
    {
        T[] arr = (T[]) Array.newInstance(clazz, length);
        int pos = 0;
        for (T ele : this)
        {
            arr[pos++] = ele;
        }
        return arr;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new ChainIterator();
    }
    
    public int getLength()
    {
        return length;
    }
    
    private class ChainElement
    {
        private T value;
        private ChainElement next;
        
        private ChainElement(T value)
        {
            this.value = value;
        }
    }
    
    private class ChainIterator implements Iterator<T>
    {
        private ChainElement previous, current;
        
        public ChainIterator()
        {
            current = Chain.this.first;
        }
        
        @Override
        public boolean hasNext()
        {
            return current != null;
        }

        @Override
        public T next()
        {
            T val = null;
            if (current != null)
            {
                val = current.value;
                current = current.next;
            }
            return val;
        }

        @Override
        public void remove()
        {
            if (current != null)
            {
                if (previous != null)
                {
                    previous.next = current.next;
                }
            }
            else
            {
                Chain.this.first = Chain.this.first.next;
                Chain.this.length--;
            }
        }
        
    }
}