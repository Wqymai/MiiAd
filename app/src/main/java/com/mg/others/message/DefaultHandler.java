
package com.mg.others.message;

import android.os.Message;

/**
 * Sender默认的Handler，不处理任何消息。用于只post Runnable的Sender
 *
 * @author iooly
 */
/* package */class DefaultHandler implements Handler {

    @Override
    public void handleMessage(Message msg) {
        // do nothing
    }

}
