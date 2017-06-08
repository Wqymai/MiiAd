package com.mg.others.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;


public class FadeInImageView extends ImageView implements Animation.AnimationListener{
    private Animation mFadeInAnimation;

    public FadeInImageView(Context context) {
        super(context);
    }

    public FadeInImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FadeInImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void startAnimation(Animation animation) {
        startFeadInAnim();
    }

    private synchronized void startFeadInAnim(){
        if (mFadeInAnimation == null){
            initAnimation();
        }
        setAnimation(mFadeInAnimation);
        mFadeInAnimation.start();
    }

    private void initAnimation(){
        mFadeInAnimation = new AlphaAnimation(0,1);
        mFadeInAnimation.setDuration(500);
        mFadeInAnimation.setAnimationListener(this);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        removeAnimation();
        super.setImageBitmap(bm);
    }

    private synchronized void removeAnimation(){
        if (mFadeInAnimation != null){
            mFadeInAnimation.cancel();
            setAnimation(null);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        setAnimation(null);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
