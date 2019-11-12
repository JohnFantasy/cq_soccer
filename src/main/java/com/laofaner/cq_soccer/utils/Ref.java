package com.laofaner.cq_soccer.utils;

/**
 * Created by
 */
public class Ref<T extends Object> {
    T value;
    public Ref(T value){
        this.value = value;
    }
    public T get(){
        return value;
    }
    public void set(T value){
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

