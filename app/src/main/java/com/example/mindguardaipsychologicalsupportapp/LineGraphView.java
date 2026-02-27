package com.example.mindguardaipsychologicalsupportapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class LineGraphView extends View {
    private Paint linePaint;
    private Paint dashedLinePaint;
    private Paint fillPaint;
    private Paint pointPaint;
    private Path path;
    private Path fillPath;
    private Path dashedPath;
    
    private float[] dataPoints = {0.4f, 0.35f, 0.5f, 0.55f, 0.6f, 0.7f, 0.65f};
    private float[] comparisonPoints = null; // For Today vs Last Week dashed line
    private int lineColor = Color.parseColor("#4A90E2");

    public LineGraphView(Context context) {
        super(context);
        init();
    }

    public LineGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(8f);
        linePaint.setStrokeCap(Paint.Cap.ROUND);

        dashedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dashedLinePaint.setStyle(Paint.Style.STROKE);
        dashedLinePaint.setStrokeWidth(4f);
        dashedLinePaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
        dashedLinePaint.setColor(Color.LTGRAY);

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);

        pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(Color.WHITE);

        path = new Path();
        fillPath = new Path();
        dashedPath = new Path();
    }

    public void setData(float[] points, int color) {
        this.dataPoints = points;
        this.lineColor = color;
        invalidate();
    }

    public void setComparisonData(float[] points) {
        this.comparisonPoints = points;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();

        // Draw Comparison (Dashed Line)
        if (comparisonPoints != null && comparisonPoints.length >= 2) {
            dashedPath.reset();
            float xStepComp = width / (comparisonPoints.length - 1);
            dashedPath.moveTo(0, height - (comparisonPoints[0] * height));
            for (int i = 1; i < comparisonPoints.length; i++) {
                dashedPath.lineTo(i * xStepComp, height - (comparisonPoints[i] * height));
            }
            canvas.drawPath(dashedPath, dashedLinePaint);
        }

        // Draw Primary Line
        if (dataPoints == null || dataPoints.length < 2) return;

        float xStep = width / (dataPoints.length - 1);
        linePaint.setColor(lineColor);
        fillPaint.setColor(lineColor);
        fillPaint.setAlpha(30);

        path.reset();
        fillPath.reset();

        float startX = 0;
        float startY = height - (dataPoints[0] * height);
        path.moveTo(startX, startY);
        fillPath.moveTo(startX, height);
        fillPath.lineTo(startX, startY);

        for (int i = 1; i < dataPoints.length; i++) {
            float x = i * xStep;
            float y = height - (dataPoints[i] * height);
            path.lineTo(x, y);
            fillPath.lineTo(x, y);
        }

        fillPath.lineTo(width, height);
        fillPath.close();

        canvas.drawPath(fillPath, fillPaint);
        canvas.drawPath(path, linePaint);

        // Draw points
        for (int i = 0; i < dataPoints.length; i++) {
            float x = i * xStep;
            float y = height - (dataPoints[i] * height);
            pointPaint.setColor(lineColor);
            canvas.drawCircle(x, y, 12f, pointPaint);
            pointPaint.setColor(Color.WHITE);
            canvas.drawCircle(x, y, 6f, pointPaint);
        }
    }
}