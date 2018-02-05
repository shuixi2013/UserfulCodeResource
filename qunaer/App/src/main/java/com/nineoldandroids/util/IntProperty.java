package com.nineoldandroids.util;

public abstract class IntProperty<T> extends Property<T, Integer> {
    public abstract void setValue(T t, int i);

    public IntProperty(String str) {
        super(Integer.class, str);
    }

    public final void set(T t, Integer num) {
        set((Object) t, Integer.valueOf(num.intValue()));
    }
}
