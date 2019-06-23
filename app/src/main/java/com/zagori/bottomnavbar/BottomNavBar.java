package com.zagori.bottomnavbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

public class BottomNavBar extends CoordinatorLayout {

    private static final String TAG = BottomNavBar.class.getSimpleName();

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;

    private OnBottomNavigationListener onBottomNavigationListener;

    /**
     * Sets callback for bottom nav menu item selection.
     * @param onBottomNavigationListener bottomNavigation bar callback
     */
    public void setBottomNavigationListener(OnBottomNavigationListener onBottomNavigationListener) {
        this.onBottomNavigationListener = onBottomNavigationListener;
    }

    /**
     * Interface definition for a callback to be invoked when user select a menu item on the bottomNav bar
     */
    public interface OnBottomNavigationListener {

        /**
         * Fires when user select BottomNav menu item
         *
         * @param menuItem menuItem selected by user
         */
        boolean onNavigationItemSelected(MenuItem menuItem);
    }

    public BottomNavBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.bottom_nav_bar, this);

        // initialize the bottom nav and fab views
        this.bottomNavigationView = findViewById(R.id.nav_view);
        this.fab = findViewById(R.id.fab);

        TypedArray styleAttrs = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BottomNavBar, 0, 0);

        Float curveMargin = styleAttrs.getDimension(R.styleable.BottomNavBar_bn_curve_margin, 0F);
        Float roundedCornerRadius = styleAttrs.getDimension(R.styleable.BottomNavBar_bn_curve_rounded_corner_radius, 0F);
        Float cradleVerticalOffset = styleAttrs.getDimension(R.styleable.BottomNavBar_bn_curve_vertical_offset, 0F);
        @ColorInt int backgroundColor = styleAttrs.getColor(R.styleable.BottomNavBar_bn_background_color, bottomNavigationView.getSolidColor()); // bottomNavigationView.getItemBackground()
        @IdRes int menuResID = styleAttrs.getResourceId(R.styleable.BottomNavBar_bn_menu, -1);
        final int fabMenuIndex = styleAttrs.getInt(R.styleable.BottomNavBar_bn_fab_menu_index, -1);
        ColorStateList menuItemColorState = styleAttrs.getColorStateList(R.styleable.BottomNavBar_bn_item_color);
        ColorStateList fabBackgroundColor = styleAttrs.getColorStateList(R.styleable.BottomNavBar_bn_fab_background_color);
        ColorStateList fabIconColor = styleAttrs.getColorStateList(R.styleable.BottomNavBar_bn_fab_icon_color);

        Float fabDiameter;
        int fabSize;
        String bnFabSize = styleAttrs.getString(R.styleable.BottomNavBar_bn_fab_size);
        if (bnFabSize == null || bnFabSize.equalsIgnoreCase("normal")){
            // set fab param to default fab parameters
            fabDiameter = getResources().getDimension(R.dimen.fab_size_normal);
            fabSize = FloatingActionButton.SIZE_NORMAL;
        } else if (bnFabSize.equalsIgnoreCase("mini")){
            fabDiameter = getResources().getDimension(R.dimen.fab_size_mini);
            fabSize = FloatingActionButton.SIZE_MINI;
        } else {
            Log.e(TAG, "bnFabSize should be either 'mini' or 'normal'");
            throw new IllegalArgumentException("bnFabSize '"+ bnFabSize +"' does not match any of the fab size values");
        }

        // check if no menu has been added to BottomNav view
        if (menuResID == -1) {
            Log.e(TAG, "menuResID should not be null.");
            throw new Resources.NotFoundException("Menu must be added to BottomNav.");
        }

        // add the menu to the bottom-nav
        bottomNavigationView.inflateMenu(menuResID);

        bottomNavigationView.setItemIconTintList(menuItemColorState);
        bottomNavigationView.setItemTextColor(menuItemColorState);

        // set listener on the bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return onBottomNavigationListener != null && onBottomNavigationListener.onNavigationItemSelected(item);
            }
        });

        // set listener on the fab
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(fabMenuIndex).getItemId());
            }
        });

        // check if fab is not added
        if (fabMenuIndex == -1) {
            fab.setVisibility(GONE);
            Log.w(TAG, "No fab is added");
            return;
        }

        // check if fab position does not match any menu item position
        if (fabMenuIndex >= bottomNavigationView.getMenu().size()) {
            fab.setVisibility(GONE);
            Log.e(TAG, "fabMenuIndex is out of bound. fabMenuIndex only accepts values from 0 to " + (bottomNavigationView.getMenu().size() - 1));
            throw new IllegalArgumentException("fabMenuIndex does not match any menu item position");
        }

        // show fab
        fab.setVisibility(VISIBLE);

        // set fav size
        fab.setSize(fabSize);

        // set the fab background tint
        fab.setBackgroundTintList(fabBackgroundColor);

        // set the fab icon tint
        fab.setImageTintList(fabIconColor);

        TopEdgeTreatment topEdgeTreatment = new TopEdgeTreatment(bottomNavigationView.getMenu().size(), fabMenuIndex, curveMargin, roundedCornerRadius, cradleVerticalOffset);
        topEdgeTreatment.setFabDiameter(fabDiameter);

        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        ShapeAppearanceModel shapeAppearanceModel = materialShapeDrawable.getShapeAppearanceModel();
        shapeAppearanceModel.setTopEdge(topEdgeTreatment);

        materialShapeDrawable.setTint(backgroundColor);
        materialShapeDrawable.setShapeAppearanceModel(shapeAppearanceModel);

        //setBackground(materialShapeDrawable);
        bottomNavigationView.setBackground(materialShapeDrawable);

        // get the icon from menu item and set it to the fab
        fab.setImageDrawable(bottomNavigationView.getMenu().getItem(fabMenuIndex).getIcon());

        // remove the icon from the item in the bottom-nav
        bottomNavigationView.getMenu().getItem(fabMenuIndex).setIcon(null);

        // get an instance of the 1st menu item, as we need it to
        final BottomNavigationItemView bottomNavigationItemView = (BottomNavigationItemView) ((BottomNavigationMenuView) bottomNavigationView.getChildAt(0)).getChildAt(1);

        bottomNavigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                // kill the global layout listener so it does not keep calling the layout
                bottomNavigationItemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // calculate the distance between the fab center and the bottom_nav's left edge
                int fabMarginLeft = (fabMenuIndex * bottomNavigationItemView.getMeasuredWidth()) + (bottomNavigationItemView.getMeasuredWidth() / 2) - (fab.getMeasuredWidth() / 2);

                //get the fab layout params
                ViewGroup.MarginLayoutParams mp = (ViewGroup.MarginLayoutParams) fab.getLayoutParams();

                // set the fab margins
                mp.setMargins(fabMarginLeft, 0, 0, 0);

                // apply changes and initial the fab view
                fab.requestLayout();
            }
        });
    }
}
