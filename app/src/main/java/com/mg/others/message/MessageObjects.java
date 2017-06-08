package com.mg.others.message;


import com.mg.others.v4.Pools;

public class MessageObjects {

    private static final int MAX_POOL_SIZE = 10;
    private static final Pools.Pool<MessageObjects> sPool = new Pools.SynchronizedPool<>(MAX_POOL_SIZE);

    public Object obj0;
    public Object obj1;
    public Object obj2;
    public Object obj3;
    public Object obj4;

    public int arg0;
    public int arg1;
    public int arg2;
    public int arg3;
    public int arg4;

    public long larg0;
    public long larg1;
    public long larg2;
    public long larg3;
    public long larg4;


    public static MessageObjects obtain() {
        MessageObjects instance = sPool.acquire();
        return instance == null ? new MessageObjects() : instance;
    }

    private MessageObjects() {
    }

    public void recycle() {
        obj0 = null;
        obj1 = null;
        obj2 = null;
        obj3 = null;
        obj4 = null;

        arg0 = 0;
        arg1 = 0;
        arg2 = 0;
        arg3 = 0;
        arg4 = 0;

        larg0 = 0;
        larg1 = 0;
        larg2 = 0;
        larg3 = 0;
        larg4 = 0;

        try {
            sPool.release(this);
        } catch (Exception ex) {
        }
    }
}