
package com.mg.others.message;

import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 实际发送消息和处理消息循环中Message的Handler
 *
 * @author iooly
 */
/* package */class LooperHandler extends android.os.Handler {

    /* package */ WeakReference<Handler> mHandlerRef;


    LooperHandler() {
        super(Looper.myLooper());
    }

    LooperHandler(Looper looper) {
        super(looper);
    }

    LooperHandler(Looper looper, Callback callback) {
        super(looper, callback);
    }

    LooperHandler(Callback callback) {
        super(callback);
    }

    @Override
    public void handleMessage(Message msg) {
        WeakReference<Handler> handlerRef = mHandlerRef;
        if (handlerRef == null) {
            return;
        }
        Handler handler = handlerRef.get();
        if (handler != null) {
            handler.handleMessage(msg);
        }

        Object obj = msg.obj;
        if (obj != null && (obj instanceof MessageObjects)) {
            ((MessageObjects) obj).recycle();
        }

        handler = null;
    }

}
