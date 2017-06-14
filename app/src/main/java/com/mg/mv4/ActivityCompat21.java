package com.mg.mv4;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Parcelable;
import android.view.View;

import java.util.List;
import java.util.Map;

/**
 * Created by wuqiyan on 17/3/24.
 */

class ActivityCompat21 {
    public static void finishAfterTransition(Activity activity) {
        activity.finishAfterTransition();
    }

    public static void setEnterSharedElementCallback(Activity activity,
                                                     ActivityCompat21.SharedElementCallback21 callback) {
        activity.setEnterSharedElementCallback(createCallback(callback));
    }

    public static void setExitSharedElementCallback(Activity activity,
                                                    ActivityCompat21.SharedElementCallback21 callback) {
        activity.setExitSharedElementCallback(createCallback(callback));
    }

    public static void postponeEnterTransition(Activity activity) {
        activity.postponeEnterTransition();
    }

    public static void startPostponedEnterTransition(Activity activity) {
        activity.startPostponedEnterTransition();
    }

    public abstract static class SharedElementCallback21 {
        public abstract void onSharedElementStart(List<String> sharedElementNames,
                                                  List<View> sharedElements, List<View> sharedElementSnapshots);

        public abstract void onSharedElementEnd(List<String> sharedElementNames,
                                                List<View> sharedElements, List<View> sharedElementSnapshots);

        public abstract void onRejectSharedElements(List<View> rejectedSharedElements);

        public abstract void onMapSharedElements(List<String> names,
                                                 Map<String, View> sharedElements);
        public abstract Parcelable onCaptureSharedElementSnapshot(View sharedElement,
                                                                  Matrix viewToGlobalMatrix, RectF screenBounds);
        public abstract View onCreateSnapshotView(Context context, Parcelable snapshot);
    }

    private static SharedElementCallback createCallback(ActivityCompat21.SharedElementCallback21 callback) {
        SharedElementCallback newListener = null;
        if (callback != null) {
            newListener = new ActivityCompat21.SharedElementCallbackImpl(callback);
        }
        return newListener;
    }

    private static class SharedElementCallbackImpl extends SharedElementCallback {
        private ActivityCompat21.SharedElementCallback21 mCallback;

        public SharedElementCallbackImpl(ActivityCompat21.SharedElementCallback21 callback) {
            mCallback = callback;
        }

        @Override
        public void onSharedElementStart(List<String> sharedElementNames,
                                         List<View> sharedElements, List<View> sharedElementSnapshots) {
            mCallback.onSharedElementStart(sharedElementNames, sharedElements,
                    sharedElementSnapshots);
        }

        @Override
        public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements,
                                       List<View> sharedElementSnapshots) {
            mCallback.onSharedElementEnd(sharedElementNames, sharedElements,
                    sharedElementSnapshots);
        }

        @Override
        public void onRejectSharedElements(List<View> rejectedSharedElements) {
            mCallback.onRejectSharedElements(rejectedSharedElements);
        }

        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            mCallback.onMapSharedElements(names, sharedElements);
        }

        @Override
        public Parcelable onCaptureSharedElementSnapshot(View sharedElement,
                                                         Matrix viewToGlobalMatrix,
                                                         RectF screenBounds) {
            return mCallback.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix,
                    screenBounds);
        }

        @Override
        public View onCreateSnapshotView(Context context, Parcelable snapshot) {
            return mCallback.onCreateSnapshotView(context, snapshot);
        }
    }
}
