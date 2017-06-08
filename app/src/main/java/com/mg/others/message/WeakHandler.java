
package com.mg.others.message;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * WeakHandler 可以作为匿名内部类处理Message, 不会造成内存泄露。目前只支持发送消息,<br />
 * 因为Runnable作为其他类的内部类post出去,也会造成内部类Handler同样的问题
 *
 * @author iooly
 */
public abstract class WeakHandler {

    private InternalHandler mHandler;

    public WeakHandler() {
        mHandler = new InternalHandler(this);
    }

    public WeakHandler(Looper looper) {
        mHandler = new InternalHandler(this, looper);
    }

    public final boolean sendMessage(Message msg) {
        return mHandler.sendMessage(msg);
    }

    public final boolean sendEmptyMessage(int what) {
        return mHandler.sendEmptyMessage(what);
    }

    public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        return mHandler.sendEmptyMessageDelayed(what, delayMillis);
    }

    public final boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        return mHandler.sendEmptyMessageAtTime(what, uptimeMillis);
    }

    public final boolean sendMessageDelayed(Message msg, long delayMillis) {
        return mHandler.sendMessageDelayed(msg, delayMillis);
    }

    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        return mHandler.sendMessageAtTime(msg, uptimeMillis);
    }

    public final boolean sendMessageAtFrontOfQueue(Message msg) {
        return mHandler.sendMessageAtFrontOfQueue(msg);
    }

    public final Message obtainMessage() {
        return mHandler.obtainMessage();
    }

    public final Message obtainMessage(int what) {
        return mHandler.obtainMessage(what);
    }

    public final Message obtainMessage(int what, Object obj) {
        return mHandler.obtainMessage(what, obj);
    }

    public final Message obtainMessage(int what, int arg1, int arg2) {
        return mHandler.obtainMessage(what, arg1, arg2);
    }

    public final Message obtainMessage(int what, int arg1, int arg2, Object obj) {
        return mHandler.obtainMessage(what, arg1, arg2, obj);
    }

    public abstract void handleMessage(Message msg);

    /**
     * 必须是static, 保证内存不泄露
     *
     * @author iooly
     */
    private static class InternalHandler extends Handler {

        final WeakReference<WeakHandler> mHandlerRef;

        InternalHandler(WeakHandler handler) {
            mHandlerRef = new WeakReference<WeakHandler>(handler);
        }

        InternalHandler(WeakHandler handler, Looper looper) {
            super(looper);
            mHandlerRef = new WeakReference<WeakHandler>(handler);
        }

        @Override
        public void handleMessage(Message msg) {
            WeakHandler handler = mHandlerRef.get();
            if (handler != null) {
                handler.handleMessage(msg);
            }
        }

    }

}
