/*
 * Copyright (c) by mewin<mewin001@hotmail.de>
 * All rights reserved.
 */
package de.mewin.util.data;

/**
 *
 * @author mewin<mewin001@hotmail.de>
 */
public class BinaryTree<T>
{
    private BinaryTree<T> leftTree, rightTree;
    private T value;
    
    public BinaryTree<T> getLeftTree()
    {
        return this.leftTree;
    }
    
    public BinaryTree<T> getRightTree()
    {
        return this.rightTree;
    }
    
    public T getValue()
    {
        return this.value;
    }
    
    public void setValue(T value)
    {
        this.value = value;
    }
    
    public void setLeftTree(BinaryTree<T> leftTree)
    {
        this.leftTree = leftTree;
    }

    public void setRightTree(BinaryTree<T> rightTree)
    {
        this.rightTree = rightTree;
    }
    
    public boolean isLeaf()
    {
        return leftTree == null && rightTree == null;
    }
}