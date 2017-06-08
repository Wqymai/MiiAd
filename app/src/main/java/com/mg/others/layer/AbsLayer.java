
package com.mg.others.layer;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

public abstract class AbsLayer extends ApplicationContextWrapper implements ILayer,
        View.OnClickListener,
        View.OnKeyListener, ILayerManager {

    /* package */ LayerManager mLayerManager = null;
    private FrameLayout mDecorView;

    public WindowManager.LayoutParams createLayoutParams(int adtype, String bp, int adSource) {
        return new WindowManager.LayoutParams();
    }

    public void finish() {
        removeLayer(new Intent(this, ((Object) this).getClass()));
    }

    public final View findViewById(int id) {
        return mDecorView.findViewById(id);
    }

    @Override
    public void setContentView(View view) {
        mDecorView.removeAllViews();
        mDecorView.addView(view);
    }

    @Override
    public void setContentView(int resId) {
        mDecorView.removeAllViews();
        View.inflate(this, resId, mDecorView);
    }

    @Override
    public synchronized View getDecorView() {
        return mDecorView;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public final void addLayer(Intent intent) {
        if (mLayerManager != null) {
            mLayerManager.addLayer(intent);
        }
    }

    @Override
    public final void resumeLayer(Intent intent) {
        if (mLayerManager != null) {
            mLayerManager.resumeLayer(intent);
        }
    }

    @Override
    public final void pauseLayer(Intent intent) {
        if (mLayerManager != null) {
            mLayerManager.pauseLayer(intent);
        }
    }

    @Override
    public final void removeLayer(Intent intent) {
        if (mLayerManager != null) {
            mLayerManager.removeLayer(intent);
        }
    }

    @Override
    public final boolean hasLayer(Intent intent) {
        if (mLayerManager != null) {
            return mLayerManager.hasLayer(intent);
        }
        return false;
    }

    @Override
    public final void newIntent(Intent intent) {
        if (mLayerManager != null) {
            mLayerManager.newIntent(intent);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME ||
                keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

    @Override
    public LayerManager getLayerManager() {
        return mLayerManager;
    }

    /* package */void initWiondow() {
        mDecorView = new FrameLayout(this);
        mDecorView.setOnKeyListener(this);
    }

    public abstract String getLayerName();

}
