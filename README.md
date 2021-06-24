# Overview
BottomNavBar is a library that merges BottomNavigationView and FloatingActionButton into a single view. This library allows to replace one menuItem in the bottomNavigationView with a FloatingActionButton.

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# Setup for MavenCentral (recommended)
#### Step 1: Add mavenCentral to your project build.gralde file
```gradle
allprojects {
	repositories {
		//...
		mavenCentral()
	}
}
```


#### Step 2: add this dependency to your app build.gradle file
```gradle
dependencies {
  //...
  implementation "io.github.zagori:bottomnavbar:1.0.3"
}
```



# Setup for jCenter
#### Step 1: Add jCenter to your project build.gralde file
```gradle
allprojects {
	repositories {
		//...
		jCenter()
	}
}
```


#### Step 2: add this dependency to your app build.gradle file
```gradle
dependencies {
  //...
  implementation 'com.zagori:bottomnavbar:1.0.2'
}
```


# ScreenShots
<img src="https://github.com/zagori/BottomNavBar/blob/master/images/Screenshot_20190625-093459.png" width="200">	<img src="https://github.com/zagori/BottomNavBar/blob/master/images/Screenshot_20190625-094324.png" width="200">	<img src="https://github.com/zagori/BottomNavBar/blob/master/images/bottomnavbar_attributes.png" width="200">


# Usage
#### XML Overview
```XML
<com.zagori.bottomnavbar.BottomNavBar
        android:id="@+id/bottom_nav_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"

        app:bn_background_color="@color/colorBottomNav"
        app:bn_item_color="@drawable/bottom_nav_item_state"
        app:bn_menu="@menu/bottom_nav_menu"

        app:bn_curve_vertical_offset="0dp"
        app:bn_curve_margin="6dp"
        app:bn_curve_rounded_corner_radius="8dp"

        app:bn_fab_size="normal"
        app:bn_fab_menu_index="2"
        app:bn_fab_background_color="@color/colorPrimary"
        app:bn_fab_icon_color="@color/colorOnPrimary"/>
```


#### bn_background_color
The fill color of bar. If not set, background will inherit default color from the theme.


#### bn_item_color
The color of the menu items in the bar. It accept static resource color or ColorStateList(drawable selector) with `state_checked` is either `true` or `false`. Here is an example:
```XML
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:state_checked="true"
        android:color="@color/colorBottomNavItemChecked" />
    <item
        android:state_checked="false"
        android:color="@color/colorBottomNavItemUnchecked" />
</selector>
```


#### bn_menu
The menu resource, defining between 3 and 5 menu items. Here is an example:
```XML
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/navigation_home"
        android:icon="@drawable/ic_home_black_24dp"
        android:title="@string/title_home" />

    <item
        android:id="@+id/navigation_payment"
        android:icon="@drawable/ic_credit_card_black_24dp"
        android:title="@string/title_payment" />

    <item
        android:id="@+id/navigation_new_cart"
        android:icon="@drawable/ic_add_shopping_cart_black_24dp"
        android:title="@string/title_new_cart" />
</menu>
```


#### bn_curve_vertical_offset
The vertical offset, in pixels, of the FloatingActionButton being cradled. An offset of 0 indicates the vertical center of the FloatingActionButton is positioned on the top edge. This must be positive.


#### bn_curve_margin
The margin in pixels between the cutout and the fab.


#### bn_curve_rounded_corner_radius
The radius, in pixels, of the rounded corners created by the cutout. A value of 0 will produce a sharp cutout.


#### bn_fab_size
The FloatingActionButton size. It can be either `normal` or `mini`. If it's not set, it receives `normal` by default. The size of `mini` is 40dp, and 56dp for `normal`.


#### bn_fab_menu_index
The position of the FloatingActionButton in the BottomNavigationView menu. The value of this index can be between `0` and `menu size - 1`.


#### bn_fab_background_color
The FloatingActionButton background tint.


#### bn_fab_icon_color
The color of the icon in the FloatingActionButton.


# Developer
* [LinkedIn](https://www.linkedin.com/in/yousseflabihi/).
* [Twitter](https://twitter.com/yourizagori).


# Code Contributions
I welcome code contributions through pull requests. Please feel free to help make this plugin even better!


# License
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
```
Copyright 2019 Zagori

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
