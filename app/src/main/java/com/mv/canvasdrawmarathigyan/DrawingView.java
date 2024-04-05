package com.mv.canvasdrawmarathigyan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DrawingView extends View {
    private Bitmap bitmap;
    private Canvas canvas;
    private Path path;
    private Paint bitmapPaint;
    private Paint paint;
    private float scaleX, scaleY;

    static int WIDTH = 128;
    static int HEIGHT = 128;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        path = new Path();
        bitmapPaint = new Paint(Paint.DITHER_FLAG);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(8);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.RGB_565);
        bitmap.eraseColor(Color.WHITE);
        canvas = new Canvas(bitmap);
        scaleX = (float) w / HEIGHT;
        scaleY = (float) h / WIDTH;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Scale up the canvas to fit the view
        canvas.save();
        canvas.scale(scaleX, scaleY);
        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
        canvas.restore();

        // Draw the path with scaling
        canvas.save();
        canvas.scale(scaleX, scaleY);
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touchStart(float x, float y) {
        path.reset();
        path.moveTo(x / scaleX, y / scaleY);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX / scaleX, mY / scaleY, (x + mX) / (2 * scaleX), (y + mY) / (2 * scaleY));
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        path.lineTo(mX / scaleX, mY / scaleY);
        // Commit the path to our offscreen
        canvas.drawPath(path, paint);
        // Kill this so we don't double draw
        path.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }

    public void setPaintWidth(int size){
        paint.setStrokeWidth(size);
    }

    public void clearCanvas() {
        bitmap.eraseColor(Color.WHITE); // Clear the bitmap by filling it with a transparent color
        path.reset(); // Reset the path
        invalidate(); // Invalidate the view to force a redraw
    }

    public void saveDrawing(String path, String filename) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/CanvasDrawMarathiGyanImages/" + path);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        File file = new File (myDir, filename);
        //if (!file.exists()) {
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        //}
    }
}
