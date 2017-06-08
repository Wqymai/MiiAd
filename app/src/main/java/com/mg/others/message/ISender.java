package com.mg.others.message;

import android.os.Looper;
import android.os.Message;
import android.util.Printer;

public interface ISender {

    public static final class Factory {

        public static ISender newSender(Handler base) {
            return new Sender(base);
        }

        public static ISender newMainThreadSender(Handler base) {
            return new Sender(base, Looper.getMainLooper());
        }

        public static ISender newMainThreadSender() {
            return new Sender(Looper.getMainLooper());
        }

        public static ISender newSender(Handler base, Looper looper) {
            return new Sender(base, looper);
        }

        public static ISender newSender(Handler base, Looper looper, android.os.Handler.Callback callback) {
            return new Sender(base, looper, callback);
        }

        public static ISender newSender(Handler base, android.os.Handler.Callback callback) {
            return new Sender(base, callback);
        }

        public static ISender newSender() {
            return new Sender();
        }

        public static ISender newSender(Looper looper) {
            return new Sender(looper);
        }

        public static ISender newSender(Looper looper, android.os.Handler.Callback callback) {
            return new Sender(looper, callback);
        }

        public static ISender newSender(android.os.Handler.Callback callback) {
            return new Sender(callback);
        }
    }

    public Message obtainMessage();

    public Message obtainMessage(int what);

    public Message obtainMessage(int what, Object obj);

    public Message obtainMessage(int what, int arg1, int arg2);

    public Message obtainMessage(int what, int arg1, int arg2, Object obj);

    public boolean post(Runnable r);

    public boolean postAtTime(Runnable r, long uptimeMillis);

    public boolean postAtTime(Runnable r, Object token, long uptimeMillis);

    public boolean postDelayed(Runnable r, long delayMillis);

    public boolean postAtFrontOfQueue(Runnable r);

    public void removeCallbacks(Runnable r);

    public void removeCallbacks(Runnable r, Object token);

    public boolean sendMessage(Message msg);

    public boolean sendEmptyMessage(int what);

    public boolean sendEmptyMessageDelayed(int what, long delayMillis);

    public boolean sendEmptyMessageAtTime(int what, long uptimeMillis);

    public boolean sendMessageDelayed(Message msg, long delayMillis);

    public boolean sendMessageAtTime(Message msg, long uptimeMillis);

    public boolean sendMessageAtFrontOfQueue(Message msg);

    public void removeMessages(int what);

    public void removeMessages(int what, Object object);

    public void removeCallbacksAndMessages(Object token);

    public boolean hasMessages(int what);

    public boolean hasMessages(int what, Object object);

    public Looper getLooper();

    public void dump(Printer pw, String prefix);

    public ISender getReference();
}
