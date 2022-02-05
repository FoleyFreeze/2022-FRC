package frc.robot.Util;

import java.util.LinkedList;

public class LimitedList<T> extends LinkedList<T>{
    private int size;

    public LimitedList(int size){
        super();
        this.size = size;
    }

    @Override
    public void addFirst(T e){
        super.addFirst(e);
        if(this.size > size){
            super.removeLast();
        }
    }
}
