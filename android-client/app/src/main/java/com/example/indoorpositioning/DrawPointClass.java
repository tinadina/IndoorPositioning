package com.example.indoorpositioning;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;

public class DrawPointClass extends androidx.appcompat.widget.AppCompatImageView {
    private Bitmap floorPlanImage;
    private PointF markerPosition;
    private Paint markerPaint;

    private boolean isLongPress;
    private Runnable longPressRunnable;
    private Handler handler;
    private float lastTouchX, lastTouchY;


    private int measuredWidth, measuredHeight;



    public DrawPointClass(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Load the floor plan image
//        floorPlanImage = BitmapFactory.decodeResource(getResources(), R.drawable.floor_plan);

        // Initialize the marker paint
        markerPaint = new Paint();
        markerPaint.setColor(Color.RED);
        markerPaint.setStrokeWidth(4f);

        isLongPress = false;
        handler = new Handler(Looper.getMainLooper());
        longPressRunnable = this::handleLongPress;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Calculate the desired size of the view

        Drawable drawable = getDrawable();

        int imageWidth = drawable.getIntrinsicWidth();
        int imageHeight = drawable.getIntrinsicHeight();

            Log.d("TAG", " imageWidth ="+imageWidth+ " imageHeight ="+imageHeight);

            int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
            int viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        Log.d("TAG", " viewWidth ="+viewWidth+ " viewHeight ="+viewHeight);


        float scaleWidth = (float) viewWidth / imageWidth;
            float scaleHeight = (float) viewHeight / imageHeight;

        Log.d("TAG", " scaleHeight ="+scaleHeight);


        measuredWidth = (int) (imageWidth * scaleHeight);
            measuredHeight = (int) (imageHeight * scaleHeight);
        Log.d("TAG", " measuredWidth ="+measuredWidth);


        setMeasuredDimension(measuredWidth, measuredHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(0, 0, 20f, markerPaint);
        canvas.drawCircle( measuredWidth, measuredHeight, 20f, markerPaint);

        // Draw the floor plan image
//        canvas.drawBitmap(floorPlanImage, 0, 0, null);

        // Draw the marker if it exists
        if (markerPosition != null) {
            canvas.drawCircle(markerPosition.x , markerPosition.y, 10f, markerPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Start the long-press detection timer
                isLongPress = false;
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                handler.postDelayed(longPressRunnable, ViewConfiguration.getLongPressTimeout());
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // Cancel the long-press detection timer
                handler.removeCallbacks(longPressRunnable);
                if (!isLongPress) {
                    // Handle the short-press event here
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void handleLongPress() {
        isLongPress = true;
        Log.d("TAG","handleLongPress" );
        setMarkerPosition(lastTouchX, lastTouchY);
        invalidate(); // Redraw the view with the marker
    }

    public void setMarkerPosition(float x, float y) {
        // Save the marker position relative to the image
        markerPosition = new PointF(x, y);
        Log.d("TAG", "x= "+x+"y= "+y);
    }

    public PointF getMarkerPosition() {
        return markerPosition;
    }

    public float[] getCoordinates(){
        float xpercent= markerPosition.x/measuredWidth;
        float ypercent= markerPosition.y/measuredHeight;

        float[] coordinates = {xpercent, ypercent};
        return coordinates;
    }
}