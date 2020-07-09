package com.bytedance.camp.chapter4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClockView extends View {

    private static final int FULL_CIRCLE_DEGREE = 360;
    private static final int UNIT_DEGREE = 6;
    private static final float UNIT_LINE_WIDTH = 8; // 刻度线的宽度
    private static final int HIGHLIGHT_UNIT_ALPHA = 0xFF;
    private static final int NORMAL_UNIT_ALPHA = 0x80;

    private static final float HOUR_NEEDLE_LENGTH_RATIO = 0.4f; // 时针长度相对表盘半径的比例
    private static final float MINUTE_NEEDLE_LENGTH_RATIO = 0.6f; // 分针长度相对表盘半径的比例
    private static final float SECOND_NEEDLE_LENGTH_RATIO = 0.8f; // 秒针长度相对表盘半径的比例
    private static final float HOUR_NEEDLE_WIDTH = 12; // 时针的宽度
    private static final float MINUTE_NEEDLE_WIDTH = 8; // 分针的宽度
    private static final float SECOND_NEEDLE_WIDTH = 4; // 秒针的宽度

    private float radius = 0; // 表盘半径
    private float centerX = 0; // 表盘圆心X坐标
    private float centerY = 0; // 表盘圆心Y坐标

    private String[] clockNumbers = {"12","1","2","3","4","5","6","7","8","9","10","11"};
    private Calendar calendar = Calendar.getInstance();
    private List<RectF> unitLinePositions = new ArrayList<>();
    private Paint unitPaint = new Paint();
    private Paint needlePaint = new Paint();
    private Paint numberPaint = new Paint();
    private Paint pointPaint = new Paint();
    private Handler handler = new Handler();
    private Runnable clockRunnable;
    private Rect textBounds = new Rect();

    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        unitPaint.setAntiAlias(true);
        unitPaint.setColor(Color.WHITE);
        unitPaint.setStrokeWidth(UNIT_LINE_WIDTH);
        unitPaint.setStrokeCap(Paint.Cap.ROUND);
        unitPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setColor(Color.WHITE);
        clockRunnable = new Runnable() {
            @Override
            public void run() {
                postInvalidate();
                handler.postDelayed(this, 1000);
            }
        };
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        configWhenLayoutChanged();
    }

    private void configWhenLayoutChanged() {
        float newRadius = Math.min(getWidth(), getHeight()) / 2f;
        if (newRadius == radius) {
            return;
        }
        radius = newRadius;
        centerX = getWidth() / 2f;
        centerY = getHeight() / 2f;

        // 当视图的宽高确定后就可以提前计算表盘的刻度线的起止坐标了
        for (int degree = 0; degree < FULL_CIRCLE_DEGREE; degree += UNIT_DEGREE) {
            double radians = Math.toRadians(degree);
            float startX = (float) (centerX + (radius * (1 - 0.05f)) * Math.cos(radians));
            float startY = (float) (centerX + (radius * (1 - 0.05f)) * Math.sin(radians));
            float stopX = (float) (centerX + radius * Math.cos(radians));
            float stopY = (float) (centerY + radius * Math.sin(radians));
            unitLinePositions.add(new RectF(startX, startY, stopX, stopY));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawUnit(canvas);
        drawTimeNeedles(canvas);
        drawTimeNumbers(canvas);
        canvas.drawCircle(centerX,centerY,20,pointPaint);

        // TODO 实现时间的转动，每一秒刷新一次
        handler.postDelayed(clockRunnable, 1000);
    }

    // 绘制表盘上的刻度
    private void drawUnit(Canvas canvas) {
        unitPaint.setColor(Color.WHITE);
        for (int i = 0; i < unitLinePositions.size(); i++) {
            if (i % 5 == 0) {
                unitPaint.setAlpha(HIGHLIGHT_UNIT_ALPHA);
            } else {
                unitPaint.setAlpha(NORMAL_UNIT_ALPHA);
            }
            RectF linePosition = unitLinePositions.get(i);
            canvas.drawLine(linePosition.left, linePosition.top, linePosition.right, linePosition.bottom, unitPaint);
        }
    }

    private void drawTimeNeedles(Canvas canvas) {
        Time time = getCurrentTime();
        int hour = time.getHours()+8;
        int minute = time.getMinutes();
        int second = time.getSeconds();

        /**
         * 思路：
         * 1、以时针为例，计算从0点（12点）到当前时间，时针需要转动的角度
         * 2、根据转动角度、时针长度和圆心坐标计算出时针终点坐标（起始点为圆心）
         * 3、从圆心到终点画一条线，此为时针
         * 注1：计算时针转动角度时要把时和分都得考虑进去
         * 注2：计算坐标时需要用到正余弦计算，请用Math.sin()和Math.cos()方法
         * 注3：Math.sin()和Math.cos()方法计算时使用不是角度而是弧度，所以需要先把角度转换成弧度，
         *     可以使用Math.toRadians()方法转换，例如Math.toRadians(180) = 3.1415926...(PI)
         * 注4：Android视图坐标系的0度方向是从圆心指向表盘3点方向，指向表盘的0点时是-90度或270度方向，要注意角度的转换
         */

        // 画秒针
        drawPointer(canvas, 2, second);
        // todo 画分针
        drawPointer(canvas, 1, minute);
        // 画时针
        int part = minute / 12;
        drawPointer(canvas, 0, 5 * hour + part);

    }

    private void drawPointer(Canvas canvas, int type, int value) {
        float degree;
        float[] pointerHeadXY = new float[2];
        needlePaint.setStrokeWidth(10);
        // 一个小格的度数
        float UNIT_DEGREE = (float) (6 * Math.PI / 180);
        degree = value * UNIT_DEGREE;
        needlePaint.setColor(Color.WHITE);
        switch (type) {
            case 0:
                needlePaint.setStrokeWidth(HOUR_NEEDLE_WIDTH);
                pointerHeadXY = getPointerHeadXY(radius * HOUR_NEEDLE_LENGTH_RATIO, degree);
                break;
            case 1:

                needlePaint.setStrokeWidth(MINUTE_NEEDLE_WIDTH);
                pointerHeadXY = getPointerHeadXY(radius * MINUTE_NEEDLE_LENGTH_RATIO, degree);
                break;
            case 2:
                needlePaint.setStrokeWidth(SECOND_NEEDLE_WIDTH);
                pointerHeadXY = getPointerHeadXY(radius*SECOND_NEEDLE_LENGTH_RATIO, degree);
                break;
        }
        canvas.drawLine(centerX, centerY, pointerHeadXY[0], pointerHeadXY[1], needlePaint);
    }

    private float[] getPointerHeadXY(float pointerLength, float degree) {
        float[] xy = new float[2];
        xy[0] = (float) (centerX + pointerLength * Math.sin(degree));
        xy[1] = (float) (centerY - pointerLength * Math.cos(degree));
        return xy;
    }

    private void drawTimeNumbers(Canvas canvas) {
        // TODO 绘制表盘时间数字（可选）
        numberPaint.setTextSize(80);
        numberPaint.setColor(Color.YELLOW);
        numberPaint.setFakeBoldText(true);
        float preX = getWidth() / 2;
        int strokeWidth = 2;
        float preY = getHeight() / 2 -450.0f - strokeWidth;
        float x,y;
        int degree = 360 / clockNumbers.length;
        for(int i = 0; i < clockNumbers.length; i++){
            String num = clockNumbers[i];
            numberPaint.getTextBounds(num, 0, num.length(), textBounds);
            x = (preX - numberPaint.measureText(num) / 2);
            y = preY - textBounds.height();//从文本的中心点处开始绘制
            canvas.drawText(num, x, y, numberPaint);
            canvas.rotate(degree, getWidth() / 2, getHeight() / 2);//以圆中心进行旋转
        }

    }

    // 获取当前的时间：时、分、秒
    private Time getCurrentTime() {
        calendar.setTimeInMillis(System.currentTimeMillis());
        return new Time(
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
    }
}
