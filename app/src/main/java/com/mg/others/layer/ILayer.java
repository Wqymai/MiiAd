
package com.mg.others.layer;

import android.content.Intent;
import android.view.View;

public interface ILayer {

    public void onCreate(Intent intent);

    public void onResume(Intent intent);

    public void onPause();

    public void onDestroy();

    public void onNewIntent(Intent intent);

    public void setContentView(View view);

    public void setContentView(int resId);

    public View getDecorView();

    public LayerManager getLayerManager();

}
