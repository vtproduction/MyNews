package com.midsummer.mynews.helper;

/**
 * Created by nienb on 13/2/16.
 */
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class CustomFadingActionBarHelper extends FixedFadingActionBarHelperBase {

    private ActionBar mActionBar;

    @Override
    protected int getActionBarHeight() {
        return mActionBar.getHeight();
    }

    @Override
    protected boolean isActionBarNull() {
        return mActionBar == null;
    }

    @Override
    protected void setActionBarBackgroundDrawable(Drawable drawable) {
        mActionBar.setBackgroundDrawable(drawable);
    }

    @Override
    public void initActionBar(Activity activity) {
        mActionBar = ((AppCompatActivity) activity).getSupportActionBar();
        super.initActionBar(activity);
    }
}