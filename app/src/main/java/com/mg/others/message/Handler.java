
package com.mg.others.message;


import android.os.Message;

/**
 * Sender 使用的Handler接口，用于处理消息。可以作为内部类且不会导致内存泄露
 *
 * @author iooly
 */
public interface Handler {

    public void handleMessage(Message msg);
}
