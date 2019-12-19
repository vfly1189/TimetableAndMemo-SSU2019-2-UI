package com.example.timetableandmemo;


public class ArrayStack<T> {

    public int top;
    private int maxSize;
    private T[] stackArray;

    public ArrayStack(int maxSize)
    {
        this.maxSize = maxSize;
        this.top = -1;
        this.stackArray = (T[]) new Object[maxSize];
    }

    public boolean empty()
    {
        return (top == -1);
    }

    public boolean full()
    {
        return (top == maxSize-1);
    }

    public void push(T item)
    {
        stackArray[++top] = item;
    }

    public T top()
    {
        return stackArray[top];
    }

    public void pop()
    {
        top--;
    }
}
