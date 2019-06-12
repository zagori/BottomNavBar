package com.zagori.custombottomnavigation;

import android.util.Log;

import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.ShapePath;

/*
* https://github.com/material-components/material-components-android/blob/master/lib/java/com/google/android/material/bottomappbar/BottomAppBarTopEdgeTreatment.java
*
* */
public class TopEdgeTreatment extends EdgeTreatment {

    private static final String TAG = TopEdgeTreatment.class.getSimpleName();

    private Float fabCradleMargin;
    private Float fabCradleRoundedCornerRadius;
    private Float cradleVerticalOffset;

    private Float fabDiameter;
    private Float horizontalOffset;

    public TopEdgeTreatment(Float fabCradleMargin, Float fabCradleRoundedCornerRadius, Float cradleVerticalOffset) {
        this.fabCradleMargin = fabCradleMargin;
        this.fabCradleRoundedCornerRadius = fabCradleRoundedCornerRadius;
        this.cradleVerticalOffset = cradleVerticalOffset;

        fabDiameter = 0.0f;
        horizontalOffset = 0.0f;

        if (cradleVerticalOffset < 0.0f) {
            Log.e(TAG, "cradleVerticalOffset must be positive.");
            //throw IllegalArgumentException("cradleVerticalOffset must be positive.");
        } else {
            Log.w(TAG, "Yes! cradleVerticalOffset is positive.");
            this.horizontalOffset = 0.0f;
        }
    }

    @Override
    public void getEdgePath(float length, float center, float interpolation, ShapePath shapePath) {
        super.getEdgePath(length, center, interpolation, shapePath);
        
        if (this.fabDiameter == 0){
            // There is no cutout to draw.
            shapePath.lineTo(length, 0);
            return;
        }

        Float cradleDiameter = this.fabCradleMargin * 2.0f + this.fabDiameter;
        Float cradleRadius = cradleDiameter / 2.0f;
        Float roundedCornerOffset = interpolation * this.fabCradleRoundedCornerRadius;
        Float middle = length / 2.0f + this.horizontalOffset;

        Float verticalOffset = interpolation * this.cradleVerticalOffset + (1.0f - interpolation) * cradleRadius;
        Float verticalOffsetRatio = verticalOffset / cradleRadius;

        if (verticalOffsetRatio >= 1.0f) {
            // Vertical offset is so high that there's no curve to draw in the edge, i.e., the fab is
            // actually above the edge so just draw a straight line.
            shapePath.lineTo(length, 0.0f);
            return; // Early exit.
        }

        Float distanceBetweenCenters = cradleRadius + roundedCornerOffset;
        Float distanceBetweenCentersSquared = distanceBetweenCenters * distanceBetweenCenters;
        Float distanceY = verticalOffset + roundedCornerOffset;
        Float distanceX = (float) Math.sqrt(distanceBetweenCentersSquared - distanceY * distanceY);
        Float leftRoundedCornerCircleX = middle - distanceX;
        Float rightRoundedCornerCircleX = middle + distanceX;
        Float cornerRadiusArcLength = (float) Math.toDegrees(Math.atan(distanceX / distanceY));
        Float cutoutArcOffset = 90.0f - cornerRadiusArcLength;
        shapePath.lineTo(leftRoundedCornerCircleX - roundedCornerOffset, 0.0f);
        shapePath.addArc(
                leftRoundedCornerCircleX - roundedCornerOffset,
                0.0f,
                leftRoundedCornerCircleX + roundedCornerOffset,
                roundedCornerOffset * 2.0f,
                270.0f,
                cornerRadiusArcLength
        );
        shapePath.addArc(
                middle - cradleRadius,
                -cradleRadius - verticalOffset,
                middle + cradleRadius,
                cradleRadius - verticalOffset,
                180.0f - cutoutArcOffset,
                cutoutArcOffset * 2.0f - 180.0f
        );
        shapePath.addArc(
                rightRoundedCornerCircleX - roundedCornerOffset,
                0.0f,
                rightRoundedCornerCircleX + roundedCornerOffset,
                roundedCornerOffset * 2.0f,
                270.0f - cornerRadiusArcLength,
                cornerRadiusArcLength
        );
        shapePath.lineTo(length, 0.0f);

    }

    public void setFabDiameter(Float fabDiameter){
        this.fabDiameter = fabDiameter;
    }


}
