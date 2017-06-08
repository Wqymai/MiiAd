
package com.mg.others.message;

import com.mg.others.utils.LogUtils;

import java.lang.ref.WeakReference;

/**
 * Runnable的包装，里面用软引用包含的Runnable才是真正的实际要运行的Runnable
 *
 * @author iooly
 */
/* package */class RunnableWrapper implements Runnable {

    private static final int MAX_POOL_SIZE = 10;

    static RunnableWrapper sPool;
    static Object sPoolSync = new Object();
    static int sPoolSize = 0;

    WeakReference<Runnable> mRunnableRef;
    Object mToken = null;
    RunnableWrapper mNext;
    boolean isRecycled;
    boolean isFree;

    RunnableWrapper(Runnable r, Object token) {
        set(r, token);
        isRecycled = false;
        isFree = true;
    }

    void set(Runnable r, Object token) {
        mRunnableRef = new WeakReference<Runnable>(r);
        mToken = token;
    }

    @Override
    public void run() {
        LogUtils.e("runnable test run ");
        if (!isRecycled) {
            setFree(false);
            Runnable r = mRunnableRef.get();
            if (r != null) {
                r.run();
            }
            setFree(true);
            recycle();
        }
    }

    void setFree(boolean isFree) {
        synchronized (sPoolSync) {
            this.isFree = isFree;
        }
    }

    void recycle() {
        synchronized (sPoolSync) {
            if (isFree && !isRecycled && sPoolSize < MAX_POOL_SIZE) {
                clearForRecycle();
                mNext = sPool;
                sPool = this;
                isRecycled = true;
                sPoolSize++;
                LogUtils.e("sPool = " + sPool);
            }
        }
    }

    void clearForRecycle() {
        mToken = null;
        mRunnableRef = null;
    }

    static RunnableWrapper obtain(Runnable r, Object token) {
        LogUtils.e("runnable test obtain " + r + "\n"+ sPool);
        synchronized (sPoolSync) {
            if (sPool != null) {
                RunnableWrapper w = sPool;
                sPool = w.mNext;
                w.mNext = null;
                w.set(r, token);
                w.isRecycled = false;
                return w;
            }
        }
        return new RunnableWrapper(r, token);
    }

}
