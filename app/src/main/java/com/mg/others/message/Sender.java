
package com.mg.others.message;

import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.util.Printer;



import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息发送和分发类
 *
 * @author iooly
 */
public final class Sender implements ISender {

    private LooperHandler mHandler;
    private Handler mMessageHandler;
    private List<RunnableWrapper> mRunnableWrapperList;
    private SenderReference mSenderReference = null;

    /* package */  Sender(Handler base) {
        init(base, null, null);
    }

    /* package */ Sender(Handler base, Looper looper) {
        init(base, looper, null);
    }

     /* package */ Sender(Handler base, Looper looper, Callback callback) {
        init(base, looper, callback);
    }

     /* package */ Sender(Handler base, Callback callback) {
        init(base, null, callback);
    }

     /* package */ Sender() {
        init(null, null, null);
    }

     /* package */ Sender(Looper looper) {
        init(null, looper, null);
    }

     /* package */ Sender(Looper looper, Callback callback) {
        init(null, looper, callback);
    }

     /* package */ Sender(Callback callback) {
        init(null, null, callback);
    }

    private void init(Handler base, Looper looper, Callback callback) {

        LooperHandler handler = null;

        if (looper == null) {
            if (callback == null) {
                handler = new LooperHandler();
            } else {
                handler = new LooperHandler(callback);
            }
        } else {
            if (callback == null) {
                handler = new LooperHandler(looper);
            } else {
                handler = new LooperHandler(looper, callback);
            }
        }

        if (base == null) {
            mMessageHandler = new DefaultHandler();
        } else {
            mMessageHandler = base;
        }
        handler.mHandlerRef = new WeakReference<>(mMessageHandler);
        mHandler = handler;
        mRunnableWrapperList = new ArrayList<>();

    }

    @Override
    public final Message obtainMessage() {
        if (isBaseOk()) {
            return mHandler.obtainMessage();
        }
        return null;
    }

    @Override
    public final Message obtainMessage(int what) {
        if (isBaseOk()) {
            return mHandler.obtainMessage(what);
        }
        return null;
    }

    @Override
    public final Message obtainMessage(int what, Object obj) {
        if (isBaseOk()) {
            return mHandler.obtainMessage(what, obj);
        }
        return null;
    }

    @Override
    public final Message obtainMessage(int what, int arg1, int arg2) {
        if (isBaseOk()) {
            return mHandler.obtainMessage(what, arg1, arg2);
        }
        return null;
    }

    @Override
    public final Message obtainMessage(int what, int arg1, int arg2, Object obj) {
        if (isBaseOk()) {
            return mHandler.obtainMessage(what, arg1, arg2, obj);
        }
        return null;
    }

    @Override
    public final boolean post(Runnable r) {
        if (isBaseOk()) {
            return mHandler.post(addRunnable(r, null));
        }
        return false;
    }

    @Override
    public final boolean postAtTime(Runnable r, long uptimeMillis) {
        if (isBaseOk()) {
            return mHandler.postAtTime(addRunnable(r, null), uptimeMillis);
        }
        return false;
    }

    @Override
    public final boolean postAtTime(Runnable r, Object token, long uptimeMillis) {
        if (isBaseOk()) {
            return mHandler.postAtTime(addRunnable(r, token), token, uptimeMillis);
        }
        return false;
    }

    @Override
    public final boolean postDelayed(Runnable r, long delayMillis) {
        if (isBaseOk()) {
            return mHandler.postDelayed(addRunnable(r, null), delayMillis);
        }
        return false;
    }

    @Override
    public final boolean postAtFrontOfQueue(Runnable r) {
        if (isBaseOk()) {
            return mHandler.postAtFrontOfQueue(addRunnable(r, null));
        }
        return false;
    }

    @Override
    public final void removeCallbacks(Runnable r) {
        LooperHandler handler = mHandler;
        if (handler != null) {
            RunnableWrapper wrapper = removeRunnbale(r);
            if (wrapper != null) {
                handler.removeCallbacks(wrapper);
                wrapper.recycle();
            }
        }
    }

    @Override
    public final void removeCallbacks(Runnable r, Object token) {
        LooperHandler handler = mHandler;
        if (handler != null) {
            RunnableWrapper wrapper = removeRunnbale(r, token);
            if (wrapper != null) {
                handler.removeCallbacks(wrapper, token);
                wrapper.recycle();
            }
        }
    }

    private synchronized RunnableWrapper addRunnable(Runnable r, Object token) {

        if (r == null) {
            return null;
        }

        RunnableWrapper wrapper = RunnableWrapper.obtain(r, token);
        mRunnableWrapperList.add(wrapper);
        return wrapper;
    }

    private synchronized RunnableWrapper removeRunnbale(Runnable r) {
        int size = mRunnableWrapperList.size();
        RunnableWrapper wrapper = null;

        if (size > 0) {
            RunnableWrapper tmp = null;
            Runnable runnable = null;
            LooperHandler handler = mHandler;
            for (int i = size - 1; i >= 0; i--) {

                tmp = mRunnableWrapperList.get(i);

                if (tmp.isRecycled) {
                    mRunnableWrapperList.remove(i);
                    continue;
                }

                runnable = tmp.mRunnableRef.get();

                if (runnable == r) {
                    wrapper = tmp;
                    mRunnableWrapperList.remove(i);
                } else if (runnable == null) {
                    mRunnableWrapperList.remove(i);
                    if (handler != null) {
                        handler.removeCallbacks(tmp);
                        tmp.recycle();
                    }
                }

            }
        }
        return wrapper;
    }

    private synchronized RunnableWrapper removeRunnbale(Runnable r, Object token) {
        int size = mRunnableWrapperList.size();
        RunnableWrapper wrapper = null;

        if (size > 0) {
            RunnableWrapper tmp = null;
            Runnable runnable = null;
            LooperHandler handler = mHandler;
            for (int i = size - 1; i >= 0; i--) {

                tmp = mRunnableWrapperList.get(i);

                if (tmp.isRecycled) {
                    mRunnableWrapperList.remove(i);
                    continue;
                }

                runnable = tmp.mRunnableRef.get();

                if (runnable == r && tmp.mToken == token) {
                    mRunnableWrapperList.remove(i);
                    wrapper = tmp;
                } else if (runnable == null) {
                    mRunnableWrapperList.remove(i);
                    if (handler != null) {
                        handler.removeCallbacks(tmp);
                        tmp.recycle();
                    }
                }

            }
        }
        return wrapper;
    }

    private synchronized List<RunnableWrapper> removeRunnbale(Object token) {

        int size = mRunnableWrapperList.size();
        List<RunnableWrapper> wrapperList = new ArrayList<>();

        if (size > 0) {
            RunnableWrapper tmp = null;
            LooperHandler handler = mHandler;
            for (int i = size - 1; i >= 0; i--) {

                tmp = mRunnableWrapperList.get(i);

                if (tmp.isRecycled) {
                    mRunnableWrapperList.remove(i);
                    continue;
                }

                if (tmp.mToken == token) {
                    mRunnableWrapperList.remove(i);
                    wrapperList.add(tmp);
                } else if (tmp.mRunnableRef.get() == null) {
                    mRunnableWrapperList.remove(i);
                    if (handler != null) {
                        handler.removeCallbacks(tmp);
                        tmp.recycle();
                    }
                }

            }
        }
        return wrapperList;
    }

    @Override
    public final boolean sendMessage(Message msg) {
        if (isBaseOk()) {
            return mHandler.sendMessage(msg);
        }
        return false;
    }

    @Override
    public final boolean sendEmptyMessage(int what) {
        if (isBaseOk()) {
            return mHandler.sendEmptyMessage(what);
        }
        return false;
    }

    @Override
    public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        if (isBaseOk()) {
            return mHandler.sendEmptyMessageDelayed(what, delayMillis);
        }
        return false;
    }

    @Override
    public final boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        if (isBaseOk()) {
            return mHandler.sendEmptyMessageAtTime(what, uptimeMillis);
        }
        return false;
    }

    @Override
    public final boolean sendMessageDelayed(Message msg, long delayMillis) {
        if (isBaseOk()) {
            return mHandler.sendMessageDelayed(msg, delayMillis);
        }
        return false;
    }

    @Override
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        if (isBaseOk()) {
            return mHandler.sendMessageAtTime(msg, uptimeMillis);
        }
        return false;
    }

    @Override
    public final boolean sendMessageAtFrontOfQueue(Message msg) {
        if (isBaseOk()) {
            return mHandler.sendMessageAtFrontOfQueue(msg);
        }
        return false;
    }

    @Override
    public final void removeMessages(int what) {
        if (mHandler != null) {
            mHandler.removeMessages(what);
        }
    }

    @Override
    public final void removeMessages(int what, Object object) {
        if (mHandler != null) {
            mHandler.removeMessages(what, object);
        }
    }

    @Override
    public final void removeCallbacksAndMessages(Object token) {
        if (mHandler != null) {
            List<RunnableWrapper> wrapperList = removeRunnbale(token);
            mHandler.removeCallbacksAndMessages(token);
            for (RunnableWrapper wrapper : wrapperList) {
                wrapper.recycle();
            }
        }
    }

    @Override
    public final boolean hasMessages(int what) {
        if (mHandler != null) {
            return mHandler.hasMessages(what);
        }
        return false;
    }

    @Override
    public final boolean hasMessages(int what, Object object) {
        if (isBaseOk()) {
            return mHandler.hasMessages(what, object);
        }
        return false;
    }

    @Override
    public final Looper getLooper() {
        if (isBaseOk()) {
            return mHandler.getLooper();
        }
        return Looper.myLooper();
    }

    @Override
    public final void dump(Printer pw, String prefix) {
        if (isBaseOk()) {
            mHandler.dump(pw, prefix);
        }
    }

    @Override
    public String toString() {
        return "HandlerProxy{" + Integer.toHexString(System.identityHashCode(this)) + "}";
    }

    private boolean isBaseOk() {
        return mHandler != null && mHandler.mHandlerRef != null
                && mHandler.mHandlerRef.get() != null;
    }

    @Override
    public synchronized ISender getReference() {
        if (mSenderReference == null) {
            mSenderReference = new SenderReference(this);
        }
        return mSenderReference;
    }

}
