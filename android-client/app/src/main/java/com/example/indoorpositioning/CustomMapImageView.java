package com.example.indoorpositioning;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

public class CustomMapImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Paint markerPaint;
    private float markerRadius = 10; // Marker radius in pixels
    private float[] markerCoordinates={}; // Default marker coordinates
    private float density;
    private int measuredWidth, measuredHeight;

    public CustomMapImageView(Context context) {
        super(context);
        init(context);
    }
    // 279 x 574 image size
    // 60 243 my_desk (21.5 42.33)
    // 243×422 mouse desk (87 73.5)
    // 151×530 storage_ room (54 92)
    // 100×27 ming desk (36 5)
    // 240×324 yoey desk (86 56)
    // 244×156 jacky desk (87.5 27)
    private Map<String, double[]> coordinateMap;

    public CustomMapImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomMapImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        markerPaint = new Paint();
        this.coordinateMap = new HashMap<>();
//        coordinateMap.put("my_desk", new double[]{21.5, 42.33});
//        coordinateMap.put("mouse_desk", new double[]{87, 73.5});
//        coordinateMap.put("storage_room", new double[]{54, 92});
//        coordinateMap.put("ming_desk", new double[]{36, 5});
//        coordinateMap.put("yoey_desk", new double[]{86, 56});
//        coordinateMap.put("jacky_desk", new double[]{87.5, 27});

        markerPaint.setColor(Color.RED);
        markerPaint.setStyle(Paint.Style.FILL);

        // Get the device's screen density
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        density = displayMetrics.density;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Drawable drawable = getDrawable();
        if (drawable != null) {

            int imageWidth = drawable.getIntrinsicWidth();
            int imageHeight = drawable.getIntrinsicHeight();

            int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
            int viewHeight = MeasureSpec.getSize(heightMeasureSpec);

            float scaleWidth = (float) viewWidth / imageWidth;
            float scaleHeight = (float) viewHeight / imageHeight;

            measuredWidth = (int) (imageWidth * scaleHeight);
            measuredHeight = (int) (imageHeight * scaleHeight);

            setMeasuredDimension(measuredWidth, measuredHeight);
        } else {
            setMeasuredDimension(0, 0);
        }
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawCircle(0, 0, markerRadius * density, markerPaint);
//        canvas.drawCircle( measuredWidth, measuredHeight, markerRadius * density, markerPaint);
            if(markerCoordinates.length==0) return;
            float x = (float) (markerCoordinates[0] * measuredWidth);
            float y = (float) (markerCoordinates[1] * measuredHeight);
            canvas.drawCircle(x, y, markerRadius * density, markerPaint);
            Log.d("TAG", "markerCoordinates[0]: "+markerCoordinates[0]+" ,"+markerCoordinates[1]);
            Log.d("TAG", "Width, Height: "+measuredWidth+" ,"+measuredHeight);
            Log.d("TAG", "x, y: "+x+" ,"+y);


//        for (String name : coordinateMap.keySet()) {
//            double[] coordinates = coordinateMap.get(name);
//            float x = (float) (coordinates[0] * measuredWidth / 100f);
//            float y = (float) coordinates[1] * measuredHeight / 100f;
//            canvas.drawCircle(x, y, markerRadius * density, markerPaint);
//
//            System.out.println("Name: " + name + ", Coordinates: (" + coordinates[0] + ", " + coordinates[1] + ")");
//        }


    }

    public void setMarkerCoordinates(float[] coordinates) {
        this.markerCoordinates = coordinates;
        invalidate(); // Redraw the markers
    }
}