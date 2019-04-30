package com.rangel.dibujo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class Lienzo extends View {

    private static Path drawPath;                              // trazo del que vamos pintando
    private static Paint drawPaint, canvasPaint;        // es como el pincel,
    private static int paintColor = 0xFF660000;                //color inicial
    private static Canvas drawCanvas;                   //lienzo, fondo
    private static Bitmap canvasBitmap;                 //tipo de archivo par apoder guardarlo

    private static Boolean estaborrando;

    private float iniciotouchX ;
    private float iniciotouchY ;

    public Lienzo(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpDrawing();
    }

    private static void setUpDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE); //pintar solo bordes, trazos
        drawPaint.setStrokeJoin(Paint.Join.ROUND); //pintura sera redondeada
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Toast.makeText(getContext(), "w: "+w +" h:"+ h, Toast.LENGTH_SHORT).show();
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                iniciotouchX  = touchX;
                iniciotouchY = touchY;
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();

                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.RED);
                paint.setStrokeWidth(20);
                drawCanvas.drawRect(iniciotouchX, iniciotouchY, touchX, touchY, paint);
                break;
            default:
                return false;

        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    //actualiza color
    public void setColor(String newColor) {
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public static void setTamanioTrazo(float nuevoTamanio) {
        //float pixel = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, nuevoTamanio, getResources().getDisplayMetrics());
        //tamanioPunto = pixel;
        //drawPaint.setStrokeWidth(pixel);
        drawPaint.setStrokeWidth(nuevoTamanio);
    }

    public static void setErase(boolean estaborrand){
        estaborrando = estaborrand;
        if(estaborrando) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);
    }

    public void NuevoDibujo(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();

    }
    public static void setBitmap( Bitmap bitmap){
        canvasBitmap = bitmap;
        Bitmap mutableBitmap = canvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
        drawCanvas.setBitmap(mutableBitmap);
        //drawCanvas = new Canvas(mutableBitmap);
        setUpDrawing();
    }

    public static void rectangulo(){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(20);
        drawCanvas.drawRect(350, 350, 850, 850, paint);
    }
}
