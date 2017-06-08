
package com.mg.others.layer;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

public class ApplicationContextWrapper extends android.content.ContextWrapper {

    private Application mAplication;

    public ApplicationContextWrapper() {
        super(null);
    }

    /* package */void attach(Application application) {
        if (getBaseContext() == null) {
            mAplication = application;
            attachBaseContext(application);
        }
    }

    public Application getApplication() {
        return mAplication;
    }

    @Override
    public void startActivity(Intent intent) {
        if (!(getBaseContext() instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        super.startActivity(intent);
    }
}
