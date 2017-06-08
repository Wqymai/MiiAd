
package com.mg.others.message;

import android.os.Looper;
import android.os.Message;
import android.util.Printer;

import java.lang.ref.WeakReference;

/* package */class SenderReference implements ISender {

    private final WeakReference<ISender> mSenderRef;
    private final Looper mLooper;

    /* package */SenderReference(ISender sender) {
        mSenderRef = new WeakReference<>(sender);
        mLooper = sender.getLooper();
    }

    @Override
    public Message obtainMessage() {
        ISender sender = getSender();
        if (sender != null) {
            return sender.obtainMessage();
        }
        return null;
    }

    @Override
    public Message obtainMessage(int what) {
       ISender sender = getSender();
        if (sender != null) {
            return sender.obtainMessage(what);
        }
        return null;
    }

    @Override
    public Message obtainMessage(int what, Object obj) {
        ISender sender = getSender();
        if (sender != null) {
            return sender.obtainMessage(what, obj);
        }
        return null;
    }

    @Override
    public Message obtainMessage(int what, int arg1, int arg2) {
       ISender sender = getSender();
        if (sender != null) {
            return sender.obtainMessage(what, arg1, arg2);
        }
        return null;
    }

    @Override
    public Message obtainMessage(int what, int arg1, int arg2, Object obj) {
        ISender sender = getSender();
        if (sender != null) {
            return sender.obtainMessage(what, arg1, arg2, obj);
        }
        return null;
    }

    @Override
    public boolean post(Runnable r) {
        ISender sender = getSender();
        if (sender != null) {
            return sender.post(r);
        }
        return false;
    }

    @Override
    public boolean postAtTime(Runnable r, long uptimeMillis) {
        ISender sender = getSender();
        if (sender != null) {
            return sender.postAtTime(r, uptimeMillis);
        }
        return false;
    }

    @Override
    public boolean postAtTime(Runnable r, Object token, long uptimeMillis) {
        ISender sender = getSender();
        if (sender != null) {
            return sender.postAtTime(r, token, uptimeMillis);
        }
        return false;
    }

    @Override
    public boolean postDelayed(Runnable r, long delayMillis) {
        ISender sender = getSender();
        if (sender != null) {
            return sender.postDelayed(r, delayMillis);
        }
        return false;
    }

    @Override
    public boolean postAtFrontOfQueue(Runnable r) {
        ISender sender = getSender();
        if (sender != null) {
            return sender.postAtFrontOfQueue(r);
        }
        return false;
    }

    @Override
    public void removeCallbacks(Runnable r) {
        ISender sender = getSender();
        if (sender != null) {
            sender.removeCallbacks(r);
        }
    }

    @Override
    public void removeCallbacks(Runnable r, Object token) {
        ISender sender = getSender();
        if (sender != null) {
            sender.removeCallbacks(r, token);
        }
    }

    @Override
    public boolean sendMessage(Message msg) {
        ISender sender = getSender();
        if (sender != null) {
            return sender.sendMessage(msg);
        }
        return false;
    }

    @Override
    public boolean sendEmptyMessage(int what) {
        ISender sender = getSender();
        if (sender != null) {
            return sender.sendEmptyMessage(what);
        }
        return false;
    }

    @Override
    public boolean sendEmptyMessageDelayed(int what, long delayMillis) {
       ISender sender = getSender();
        if (sender != null) {
            return sender.sendEmptyMessageDelayed(what, delayMillis);
        }
        return false;
    }

    @Override
    public boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        ISender sender = getSender();
        if (sender != null) {
            return sender.sendEmptyMessageAtTime(what, uptimeMillis);
        }
        return false;
    }

    @Override
    public boolean sendMessageDelayed(Message msg, long delayMillis) {
      ISender sender = getSender();
        if (sender != null) {
            return sender.sendMessageDelayed(msg, delayMillis);
        }
        return false;
    }

    @Override
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
       ISender sender = getSender();
        if (sender != null) {
            return sender.sendMessageAtTime(msg, uptimeMillis);
        }
        return false;
    }

    @Override
    public boolean sendMessageAtFrontOfQueue(Message msg) {
       ISender sender = getSender();
        if (sender != null) {
            return sender.sendMessageAtFrontOfQueue(msg);
        }
        return false;
    }

    @Override
    public void removeMessages(int what) {
       ISender sender = getSender();
        if (sender != null) {
            sender.removeMessages(what);
        }
    }

    @Override
    public void removeMessages(int what, Object object) {
       ISender sender = getSender();
        if (sender != null) {
            sender.removeMessages(what, object);
        }
    }

    @Override
    public void removeCallbacksAndMessages(Object token) {
      ISender sender = getSender();
        if (sender != null) {
            sender.removeCallbacksAndMessages(token);
        }
    }

    @Override
    public boolean hasMessages(int what) {
     ISender sender = getSender();
        if (sender != null) {
            return sender.hasMessages(what);
        }
        return false;
    }

    @Override
    public boolean hasMessages(int what, Object object) {
 ISender sender = getSender();
        if (sender != null) {
            return sender.hasMessages(what, object);
        }
        return false;
    }

    @Override
    public Looper getLooper() {
        return mLooper;
    }

    @Override
    public void dump(Printer pw, String prefix) {
ISender sender = getSender();
        if (sender != null) {
            sender.dump(pw, prefix);
        }
    }

    @Override
    public ISender getReference() {
        return this;
    }

    private ISender getSender() {
        return mSenderRef.get();
    }

}
