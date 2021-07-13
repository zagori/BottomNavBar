package com.zagori.bottomnavbar

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources.NotFoundException
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.shape.MaterialShapeDrawable
import com.zagori.bottomnavbar.databinding.BottomNavBarBinding

class BottomNavBar(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
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

    private var menuResID: Int
    private var menuItemColorState: ColorStateList? = null
    private var navBackgroundColor: Int

    private var curveMargin: Float
    private var roundedCornerRadius: Float
    private var cradleVerticalOffset: Float

    private var fabMenuIndex: Int
    private var fabBackgroundColor: ColorStateList? = null
    private var fabIconColor: ColorStateList? = null
    private var bnFabSize: Int

    private val fabDiameter: Float
        get() = when (bnFabSize) {
            1 -> resources.getDimension(R.dimen.fab_size_mini)
            else -> resources.getDimension(R.dimen.fab_size_normal)
        }


    init {
        addView(binding.root)

        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.BottomNavBar, 0, 0)

        try {
            menuResID = attributes.getResourceId(R.styleable.BottomNavBar_bn_menu, -1)
            menuItemColorState = attributes.getColorStateList(R.styleable.BottomNavBar_bn_item_color)
            navBackgroundColor = attributes.getColor(
                R.styleable.BottomNavBar_bn_background_color,
                binding.navView.solidColor
            )

            curveMargin = attributes.getDimension(R.styleable.BottomNavBar_bn_curve_margin, 0f)
            roundedCornerRadius = attributes.getDimension(R.styleable.BottomNavBar_bn_curve_rounded_corner_radius, 0f)
            cradleVerticalOffset = attributes.getDimension(R.styleable.BottomNavBar_bn_curve_vertical_offset, 0f)

            fabMenuIndex = attributes.getInt(R.styleable.BottomNavBar_bn_fab_menu_index, 0)
            fabBackgroundColor = attributes.getColorStateList(R.styleable.BottomNavBar_bn_fab_background_color)
            fabIconColor = attributes.getColorStateList(R.styleable.BottomNavBar_bn_fab_icon_color)
            bnFabSize = attributes.getInt(R.styleable.BottomNavBar_bn_fab_size, -1)
        } finally {
            attributes.recycle()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        // check if no menu has been added
        if (menuResID == -1) throw NotFoundException("Menu must be added to BottomNav. menuResID should not be null.")

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

        // check if fab position does not match any menu item position
        if (fabMenuIndex >= binding.navView.menu.size()) throw IllegalArgumentException(
            "ATTRIBUTE OUT OF BOUND",
            Throwable("The attribute bn_fab_menu_index does not match any menu item position. bn_fab_menu_index only accepts values from 0 to ${binding.navView.menu.size() - 1}")
        )

        // Set up fab
        binding.fab.apply {
            size = bnFabSize // set fav size
            backgroundTintList = fabBackgroundColor // set the fab background tint
            imageTintList = fabIconColor // set the fab icon tint

            // get the icon from menu item and set it to the fab
            setImageDrawable(binding.navView.menu.getItem(fabMenuIndex).icon)
        }

        val topEdge = TopEdgeTreatment(
            binding.navView.menu.size(),
            fabMenuIndex,
            curveMargin,
            roundedCornerRadius,
            cradleVerticalOffset
        ).also { it.setFabDiameter(fabDiameter) }

        ViewCompat.setBackground(binding.navView, MaterialShapeDrawable().apply {
            setTint(navBackgroundColor)
            shapeAppearanceModel = shapeAppearanceModel.toBuilder().setTopEdge(topEdge).build()
        })

        // remove the icon from the item in the bottom-nav
        binding.navView.menu.getItem(fabMenuIndex).icon = null
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val navItemView = (binding.navView.getChildAt(0) as BottomNavigationMenuView)
            .getChildAt(1) as BottomNavigationItemView

        // calculate the distance between the fab center and the bottom_nav's left edge
        val fabMarginLeft = fabMenuIndex * navItemView.measuredWidth + navItemView.measuredWidth / 2 - binding.fab.measuredWidth / 2

        // set the fab margins
        (binding.fab.layoutParams as MarginLayoutParams).setMargins(fabMarginLeft, 0, 0, 0)

        // apply changes and initial the fab view
        binding.fab.invalidate()
        binding.fab.requestLayout()
    }
}