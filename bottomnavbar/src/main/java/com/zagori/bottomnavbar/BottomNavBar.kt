package com.zagori.bottomnavbar

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.MaterialShapeDrawable
import com.zagori.bottomnavbar.databinding.BottomNavBarBinding

class BottomNavBar(context: Context, attrs: AttributeSet?) : CoordinatorLayout(context, attrs) {
    private val binding by lazy { BottomNavBarBinding.inflate(LayoutInflater.from(getContext())) }
    private var onBottomNavigationListener: OnBottomNavigationListener? = null

    /**
     * Sets callback for bottom nav menu item selection.
     * @param onBottomNavigationListener bottomNavigation bar callback
     */
    fun setBottomNavigationListener(onBottomNavigationListener: OnBottomNavigationListener?) {
        this.onBottomNavigationListener = onBottomNavigationListener
    }

    /**
     * Interface definition for a callback to be invoked when user select a menu item on the bottomNav bar
     */
    interface OnBottomNavigationListener {
        /**
         * Fires when user select BottomNav menu item
         * @param menuItem menuItem selected by user
         */
        fun onNavigationItemSelected(menuItem: MenuItem?): Boolean
    }

    companion object {
        private val TAG = BottomNavBar::class.java.simpleName
    }

    init {
        addView(binding.root)

        val styleAttrs = context.theme.obtainStyledAttributes(attrs, R.styleable.BottomNavBar, 0, 0)
        val curveMargin = styleAttrs.getDimension(R.styleable.BottomNavBar_bn_curve_margin, 0f)
        val roundedCornerRadius =
            styleAttrs.getDimension(R.styleable.BottomNavBar_bn_curve_rounded_corner_radius, 0f)
        val cradleVerticalOffset =
            styleAttrs.getDimension(R.styleable.BottomNavBar_bn_curve_vertical_offset, 0f)
        @ColorInt val backgroundColor = styleAttrs.getColor(
            R.styleable.BottomNavBar_bn_background_color,
            binding.navView.solidColor
        )
        @IdRes val menuResID = styleAttrs.getResourceId(R.styleable.BottomNavBar_bn_menu, -1)
        val fabMenuIndex = styleAttrs.getInt(R.styleable.BottomNavBar_bn_fab_menu_index, -1)
        val menuItemColorState =
            styleAttrs.getColorStateList(R.styleable.BottomNavBar_bn_item_color)
        val fabBackgroundColor =
            styleAttrs.getColorStateList(R.styleable.BottomNavBar_bn_fab_background_color)
        val fabIconColor = styleAttrs.getColorStateList(R.styleable.BottomNavBar_bn_fab_icon_color)
        val fabDiameter: Float
        val fabSize: Int
        val bnFabSize = styleAttrs.getString(R.styleable.BottomNavBar_bn_fab_size)
        if (bnFabSize == null || bnFabSize.equals("normal", ignoreCase = true)) {
            // set fab param to default fab parameters
            fabDiameter = resources.getDimension(R.dimen.fab_size_normal)
            fabSize = FloatingActionButton.SIZE_NORMAL
        } else if (bnFabSize.equals("mini", ignoreCase = true)) {
            fabDiameter = resources.getDimension(R.dimen.fab_size_mini)
            fabSize = FloatingActionButton.SIZE_MINI
        } else {
            Log.e(TAG, "bnFabSize should be either 'mini' or 'normal'")
            throw IllegalArgumentException("bnFabSize '$bnFabSize' does not match any of the fab size values")
        }

        // check if no menu has been added to BottomNav view
        if (menuResID == -1) {
            Log.e(TAG, "menuResID should not be null.")
            throw NotFoundException("Menu must be added to BottomNav.")
        }

        // add the menu to the bottom-nav
        binding.navView.apply {
            inflateMenu(menuResID)
            itemIconTintList = menuItemColorState
            itemTextColor = menuItemColorState

            // set listener
            setOnItemSelectedListener { item ->
                onBottomNavigationListener?.onNavigationItemSelected(item) ?: false
            }
        }

        // set listener on the fab
        binding.fab.setOnClickListener {
            binding.navView.selectedItemId = binding.navView.menu.getItem(fabMenuIndex).itemId
        }

        // check if fab is not added
        if (fabMenuIndex == -1) {
            binding.fab.visibility = GONE
            Log.w(TAG, "No fab is added")
        }

        // check if fab position does not match any menu item position
        if (fabMenuIndex >= binding.navView.menu.size()) {
            binding.fab.visibility = GONE
            Log.e(
                TAG,
                "fabMenuIndex is out of bound. fabMenuIndex only accepts values from 0 to " + (binding.navView.menu.size() - 1)
            )
            throw IllegalArgumentException("fabMenuIndex does not match any menu item position")
        }

        // Set up fab
        binding.fab.apply {
            visibility = VISIBLE // show fab
            size = fabSize // set fav size
            backgroundTintList = fabBackgroundColor // set the fab background tint
            imageTintList = fabIconColor // set the fab icon tint
        }

        val topEdge = TopEdgeTreatment(
            binding.navView.menu.size(),
            fabMenuIndex,
            curveMargin,
            roundedCornerRadius,
            cradleVerticalOffset
        ).also { it.setFabDiameter(fabDiameter) }

        ViewCompat.setBackground(binding.navView, MaterialShapeDrawable().apply {
            setTint(backgroundColor)
            shapeAppearanceModel = shapeAppearanceModel.toBuilder().setTopEdge(topEdge).build()
        })

        // get the icon from menu item and set it to the fab
        binding.fab.setImageDrawable(binding.navView.menu.getItem(fabMenuIndex).icon)

        // remove the icon from the item in the bottom-nav
        binding.navView.menu.getItem(fabMenuIndex).icon = null

        // get an instance of the 1st menu item, as we need it to
        val bottomNavigationItemView = (binding.navView.getChildAt(0)
                as BottomNavigationMenuView).getChildAt(1) as BottomNavigationItemView

        binding.navView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                // kill the global layout listener so it does not keep calling the layout
                bottomNavigationItemView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // calculate the distance between the fab center and the bottom_nav's left edge
                val fabMarginLeft =
                    fabMenuIndex * bottomNavigationItemView.measuredWidth + bottomNavigationItemView.measuredWidth / 2 - binding.fab.measuredWidth / 2

                // set the fab margins
                (binding.fab.layoutParams as MarginLayoutParams).setMargins(fabMarginLeft, 0, 0, 0)

                // apply changes and initial the fab view
                binding.fab.requestLayout()
            }
        })
    }
}