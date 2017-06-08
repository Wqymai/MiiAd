
package com.mg.others.layer;

import android.app.Application;
import android.content.Intent;

public abstract class LayerManager implements ILayerManager {

    /**
     * 得到一个 LayerManager 实例, 该实例通过LayerManagerService管理悬浮窗<br />
     * <b>:main以外的进程使用此方法</b>
     *
     * @param app android.app.Application
     * @return LayerManager
     */
//    public static LayerManager obtainRemote(Application app) {
//        return LayerManagerNative.getInstance(app);
//    }

    /**
     * 得到一个 LayerManager 实例, 该实例直接管理悬浮窗<br />
     * <b>仅限:main进程使用</b>
     *
     * @param app android.app.Application
     * @return LayerManager
     */
    public static LayerManager obtainLocal(Application app) {
        return LayerManagerImpl.getInstance(app);
    }

    @Override
    public abstract void addLayer(Intent intent);

    @Override
    public abstract void resumeLayer(Intent intent);

    @Override
    public abstract void pauseLayer(Intent intent);

    @Override
    public abstract void removeLayer(Intent intent);

    @Override
    public abstract void newIntent(Intent intent);

    @Override
    public abstract boolean hasLayer(Intent intent);
}
