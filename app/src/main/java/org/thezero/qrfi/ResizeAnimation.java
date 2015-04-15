package org.thezero.qrfi;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by thezero on 11/04/15.
 */
public class ResizeAnimation extends Animation
{
    float finalHeight;
    View imageview;

    public ResizeAnimation(View view,float deltaheight){
        this.imageview=view;
        this.finalHeight=deltaheight;
    }

    protected void applyTransformation(float interpolatedtime,Transformation t){
        imageview.getLayoutParams().height=(int)(finalHeight*interpolatedtime);
        imageview.requestLayout();
    }
    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}