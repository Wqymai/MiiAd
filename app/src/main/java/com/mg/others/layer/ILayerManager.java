
package com.mg.others.layer;

import android.content.Intent;

public interface ILayerManager {

    /**
     * 添加一个itent对应的悬浮窗, 如果intent对应的悬浮窗已经存在, 就resume该悬浮窗
     */
    public void addLayer(Intent intent) throws android.os.RemoteException;

    /**
     * 如果intent对应的悬浮窗已经存在, 就resume该悬浮窗, 否则不操作
     */
    public void resumeLayer(Intent intent) throws android.os.RemoteException;

    /**
     * 如果intent对应的悬浮窗已经存在, 就pause该悬浮窗, 否则不操作
     */
    public void pauseLayer(Intent intent) throws android.os.RemoteException;

    /**
     * 如果intent对应的悬浮窗已经存在, 就移除该悬浮窗, 否则不操作
     */
    public void removeLayer(Intent intent) throws android.os.RemoteException;

    /**
     * 如果intent对应的悬浮窗已经存在, 就把intent发给这个悬浮窗, 否则不操作
     */
    public void newIntent(Intent intent) throws android.os.RemoteException;

    /**
     * 如果intent对应的悬浮窗已经存在, 就返回true, 否则返回false
     */
    public boolean hasLayer(Intent intent) throws android.os.RemoteException;
}
