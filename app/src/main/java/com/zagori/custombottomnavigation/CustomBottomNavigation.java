package com.zagori.custombottomnavigation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

/*
* https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/bottomappbar/BottomAppBar.java
* */

public class CustomBottomNavigation extends BottomNavigationView {

    private static final String TAG = CustomBottomNavigation.class.getSimpleName();
    private MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();

    public CustomBottomNavigation(Context context) {
        //super(context);
        this(context, null, 0);
    }

    public CustomBottomNavigation(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.bottomNavStyle);
    }

    public CustomBottomNavigation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray styleAttrs = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomBottomNavigation, 0, 0);

        Float fabSize = styleAttrs.getDimension(R.styleable.CustomBottomNavigation_fab_size, 0F);
        Float fabCradleMargin = styleAttrs.getDimension(R.styleable.CustomBottomNavigation_fab_cradle_margin, 0F);
        Float fabCradleRoundedCornerRadius = styleAttrs.getDimension(R.styleable.CustomBottomNavigation_fab_cradle_rounded_corner_radius, 0F);
        Float cradleVerticalOffset = styleAttrs.getDimension(R.styleable.CustomBottomNavigation_cradle_vertical_offset, 0F);
        @ColorInt int cbnBackgroundColor = styleAttrs.getColor(R.styleable.CustomBottomNavigation_cbn_background_color, context.getResources().getColor(R.color.colorSecondary));

        Log.w(TAG, "cradleVerticalOffset: " + cradleVerticalOffset);

        TopEdgeTreatment topEdgeTreatment = new TopEdgeTreatment(fabCradleMargin, fabCradleRoundedCornerRadius, cradleVerticalOffset);
        topEdgeTreatment.setFabDiameter(fabSize);

        ShapeAppearanceModel shapeAppearanceModel = materialShapeDrawable.getShapeAppearanceModel();

        shapeAppearanceModel.setTopEdge(topEdgeTreatment);
        materialShapeDrawable.initializeElevationOverlay(context);
        materialShapeDrawable.setTint(cbnBackgroundColor);

        //materialShapeDrawable.setElevation(4);
        materialShapeDrawable.setShadowCompatibilityMode(MaterialShapeDrawable.SHADOW_COMPAT_MODE_NEVER);
        materialShapeDrawable.setPaintStyle(Paint.Style.FILL_AND_STROKE);

        materialShapeDrawable.setShapeAppearanceModel(shapeAppearanceModel);

        setBackground(materialShapeDrawable);
    }
}
