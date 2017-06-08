
package com.mg.others.layer;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;

import com.mg.others.ooa.MConstant;

import java.util.HashMap;
import java.util.Map;

/*package*/class LayerManagerImpl extends LayerManager {

    private static final int MSG_INTERSTITIAL_ADD_LAYER = 1;
    private static final int MSG_INTERSTITIAL_RESUME_LAYER = 2;
    private static final int MSG_INTERSTITIAL_PAUSE_LAYER = 3;
    private static final int MSG_INTERSTITIAL_REMOVE_LAYER = 4;
    private static final int MSG_INTERSTITIAL_NEW_LAYER = 5;

    private static LayerManager sInstance = null;

    private final LayerManager mLayerManagerInternal;

    private final Handler mHandler;

    public synchronized static final LayerManager getInstance(Application app) {
        if (sInstance == null) {
            sInstance = new LayerManagerImpl(app);
        }
        return sInstance;
    }

    private LayerManagerImpl(Application app) {
        mLayerManagerInternal = new LayerManagerInternal(app);
        mHandler = new LayerHandler();
    }

    @Override
    public void addLayer(Intent intent) {
        sendMessage(MSG_INTERSTITIAL_ADD_LAYER, intent);
    }

    @Override
    public void resumeLayer(Intent intent) {
        sendMessage(MSG_INTERSTITIAL_RESUME_LAYER, intent);
    }

    @Override
    public void pauseLayer(Intent intent) {
        sendMessage(MSG_INTERSTITIAL_PAUSE_LAYER, intent);
    }

    @Override
    public void removeLayer(Intent intent) {
        sendMessage(MSG_INTERSTITIAL_REMOVE_LAYER, intent);
    }

    @Override
    public void newIntent(Intent intent) {
        sendMessage(MSG_INTERSTITIAL_NEW_LAYER, intent);
    }

    @Override
    public boolean hasLayer(Intent intent) {
        return mLayerManagerInternal.hasLayer(intent);
    }

    private void sendMessage(int what, Intent intent) {
        Message msg = mHandler.obtainMessage(what);
        msg.obj = intent;
        msg.sendToTarget();
    }

    @SuppressLint("HandlerLeak")
    private class LayerHandler extends Handler {

        private LayerHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg == null || msg.obj == null || !(msg.obj instanceof Intent)) {
                return;
            }
            Intent intent = (Intent) msg.obj;
            switch (msg.what) {
                case MSG_INTERSTITIAL_ADD_LAYER: {
                    mLayerManagerInternal.addLayer(intent);
                    break;
                }
                case MSG_INTERSTITIAL_RESUME_LAYER: {
                    mLayerManagerInternal.resumeLayer(intent);
                    break;
                }
                case MSG_INTERSTITIAL_PAUSE_LAYER: {
                    mLayerManagerInternal.pauseLayer(intent);
                    break;
                }
                case MSG_INTERSTITIAL_REMOVE_LAYER: {
                    mLayerManagerInternal.removeLayer(intent);
                    break;
                }
                case MSG_INTERSTITIAL_NEW_LAYER: {
                    mLayerManagerInternal.newIntent(intent);
                    break;
                }
            }
        }

    }

    private static class LayerManagerInternal extends LayerManager {

        private final Map<String, LayerWrapper> mLayers;
        private final Application mApplication;
        private WindowManager mWindowManager = null;

        private LayerManagerInternal(Application app) {
            mApplication = app;
            mLayers = new HashMap<>();
            checkWindowManager();
        }

        @Override
        public void addLayer(Intent intent) {

            LayerWrapper wrapper = getWrapperSafe(intent);

            if (wrapper != null) {
//                resumeLayer(intent);
//                return;
                removeLayer(intent);
            }

            if (checkWindowManager()) {

                try {
//                    wrapper = createLayer(intent.getComponent());
                    wrapper = createLayer(intent);
                } catch (Exception e) {
                    wrapper = null;
//                    ThrowableUtils.throwRuntimeException(Log.getStackTraceString(e));
                }

                if (wrapper == null) {
                    return;
                }

                wrapper.layer.onCreate(intent);

                View view = wrapper.layer.getDecorView();

                if (view == null) {
//                    ThrowableUtils.throwNullPointerException("ILayer getView is null");
                }

                if (wrapper.params == null) {
//                    ThrowableUtils.throwNullPointerException("ILayer createLayoutParams is null");
                }

                if (safeAddView(view, wrapper.params)) {
                    mLayers.put(wrapper.layer.getClass().getName(), wrapper);
                    wrapper.layer.onResume(intent);
                }
            }
        }

        @Override
        public void resumeLayer(Intent intent) {
            LayerWrapper wrapper = getWrapperSafe(intent);
            if (wrapper == null) {
                return;
            }
            wrapper.layer.onResume(intent);
        }

        @Override
        public void pauseLayer(Intent intent) {
            LayerWrapper wrapper = getWrapperSafe(intent);
            if (wrapper == null) {
                return;
            }
            wrapper.layer.onPause();
        }

        @Override
        public void removeLayer(Intent intent) {
            LayerWrapper wrapper = removeWrapperSafe(intent);
            if (wrapper == null) {
                return;
            }

            if (checkWindowManager()) {
                wrapper.layer.onPause();
                try {
                    mWindowManager.removeView(wrapper.layer.getDecorView());
                } catch (Exception ex) {
                } finally {
                }
                wrapper.layer.onDestroy();
                wrapper = null;
                System.gc();
            }
        }

        @Override
        public boolean hasLayer(Intent intent) {
            return getWrapperSafe(intent) != null;
        }

        @Override
        public void newIntent(Intent intent) {
            LayerWrapper wrapper = getWrapperSafe(intent);
            if (wrapper == null) {
                return;
            }
            wrapper.layer.onNewIntent(intent);
        }

//        private LayerWrapper createLayer(ComponentName component) throws Exception {
//            Class<? extends ILayer> clazz = mApplication.getClassLoader()
//                    .loadClass(component.getClassName())
//                    .asSubclass(ILayer.class);
//            AbsLayer layer = (AbsLayer) clazz.newInstance();
//            layer.attach(mApplication);
//            layer.mLayerManager = this;
//            layer.initWiondow();
//
//            LayerWrapper wrapper = new LayerWrapper();
//            wrapper.layer = layer;
//            wrapper.params = layer.createLayoutParams();
//            return wrapper;
//        }

        private LayerWrapper createLayer(Intent intent) throws Exception {
            ComponentName component = intent.getComponent();
            int adtype = (int) intent.getExtras().get(MConstant.key.ADTYPE);
            String bp= (String) intent.getExtras().get("BP");
            int adSource= (int) intent.getExtras().get("Source");
            Class<? extends ILayer> clazz = mApplication.getClassLoader()
                    .loadClass(component.getClassName())
                    .asSubclass(ILayer.class);
            AbsLayer layer = (AbsLayer) clazz.newInstance();
            layer.attach(mApplication);
            layer.mLayerManager = this;
            layer.initWiondow();

            LayerWrapper wrapper = new LayerWrapper();
            wrapper.layer = layer;
            wrapper.params = layer.createLayoutParams(adtype,bp,adSource);
            return wrapper;
        }

        private boolean checkWindowManager() {
            if (mWindowManager == null) {
                mWindowManager = (WindowManager) mApplication
                        .getSystemService(Context.WINDOW_SERVICE);
            }
            return mWindowManager != null;
        }

        private boolean safeAddView(View view, WindowManager.LayoutParams params) {
            try {
                mWindowManager.addView(view, params);
                return true;
            } catch (Throwable tr) {
                try {
                    safeRemoveView(view);
                    mWindowManager.addView(view, params);
                    return true;
                } catch (Throwable tr1) {
                }
            }
            return false;
        }

        private boolean safeRemoveView(View view) {
            try {
                mWindowManager.removeView(view);
                return true;
            } catch (Throwable tr) {
            }
            return false;
        }

        private LayerWrapper getWrapperSafe(Intent intent) {
            LayerWrapper wrapper = null;
            try {
                wrapper = mLayers.get(intent.getComponent().getClassName());
            } catch (Exception ex) {
            }
            return wrapper;
        }

        private LayerWrapper removeWrapperSafe(Intent intent) {
            LayerWrapper wrapper = null;
            try {
                wrapper = mLayers.remove(intent.getComponent().getClassName());
            } catch (Exception ex) {
            }
            return wrapper;
        }

    }

}
