package com.ycxu.chartview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/10/24.
 */

public class DarChartView extends View {
    private int lineNum = 10; //行数
    private float defLineHeight = 80.0f;//默认行高
    private float lineHeight = defLineHeight;//行距
    private int lineTextSize = 50;//行文本的大小
    private float lineTextWidth = 0.0f;
    private int lineMaxValue = 500; // 最大值
    private int lineValue = lineMaxValue / lineNum;//每一行的数值
    private int lineColor = Color.BLACK;
    private int lineTextColor = Color.BLACK;

    private int rankNum = 3;//列数
    private float defRankWidth = 100.0f;//默认列宽
    private float rankWidth = defRankWidth;// 列宽
    private int rankTextSize = 50; //列文本大小
    private float rankPadding = rankWidth / 3; //列距
    private ArrayList<Rank> ranks = new ArrayList<>();
    private float rankHeight = 0.0f;
    private int rankColor = Color.rgb(38, 166, 242);
    private int rankTextColor = Color.RED;

    private int width;//view的高度
    private int height;//view的宽度
    private final float lineTextMargin = 10.0f; //每行文本与行的间距
    private Paint linePaint, rankPaint;
    private Context context;
    private  int  rankTextHeight = 30;


    public DarChartView(Context context) {
        this(context, null);
    }

    public DarChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initPaint();
        setPadding(getPaddingLeft() + 10, getPaddingTop() + 10, getPaddingRight() + 10, getPaddingBottom() + 10);

        testRank();

        rankNum = ranks.size();


    }

    private void testRank() {
        for (int i = 0; i < 6; i++) {

            float value = 50+(float) (Math.random() * lineMaxValue-50);

            Rank rank = new Rank(value, i + 1 + "列");

            ranks.add(rank);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = meassureWidth((int) defRankWidth, widthMeasureSpec);
        height = meassureHeight((int) defLineHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h-rankTextHeight;
        lineHeight = (height - getPaddingTop() - getPaddingBottom()) / lineNum;

        lineTextWidth = measureTextMaxWidth(lineMaxValue, lineNum);
        rankWidth = (width - getPaddingRight() - getPaddingLeft() - lineTextWidth - lineTextMargin) / ranks.size();
        rankPadding = rankWidth / 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLines(canvas, lineNum);
        drawRank(canvas, ranks);
    }

    private void initPaint() {
        //行
        linePaint = new Paint();
        linePaint.setColor(lineColor);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(1);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setTextSize(lineTextSize);
        linePaint.setTextAlign(Paint.Align.CENTER);
        //列
        rankPaint = new Paint();
        rankPaint.setTextSize(rankTextSize);
        rankPaint.setColor(rankColor);
        rankPaint.setAntiAlias(true);
        rankPaint.setTextAlign(Paint.Align.CENTER);
        rankPaint.setStyle(Paint.Style.FILL);
        rankPaint.setStrokeWidth(1);

    }

    private float measureTextMaxWidth(int lineMaxValue, int lineNum) {
        int lineValue = lineMaxValue / lineNum;
        float lineTextWidth = 0.0f;
        for (int i = 0; i < lineNum; i++) {
            String lineText = String.valueOf(lineMaxValue - i * lineValue);
            float textWidht = linePaint.measureText(lineText);
            lineTextWidth = (textWidht < lineTextWidth ? lineTextWidth : textWidht);
        }
        return lineTextWidth;
    }

    private int meassureHeight(int defHeight, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY://固定宽度或充满父布局
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.AT_MOST://自适应,但不能超过父布局给的大小
                height = lineNum * defHeight + getPaddingBottom() + getPaddingTop()+rankTextHeight;
                break;
            case MeasureSpec.UNSPECIFIED://不限制大小，比如scrollView那个高度无限制一样
                break;
        }
        return height;
    }

    private int meassureWidth(int defWidth, int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY://固定宽度或充满父布局
                width = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.AT_MOST://自适应,但不能超过父布局给的大小
                width = rankNum * defWidth + getPaddingLeft() + getPaddingRight();
                break;
            case MeasureSpec.UNSPECIFIED://不限制大小.
                break;
        }
        return width;
    }


    /**
     * 1.画出每行的文本，文本以倒序绘制，文字要居中
     * 2，画出每行的横线
     *
     * @param canvas
     * @param lineNum
     */
    private void drawLines(Canvas canvas, int lineNum) {
        for (int i = 0; i < lineNum; i++) {
            String lineText = String.valueOf(lineMaxValue - i * lineValue);
            canvas.drawText(lineText, getPaddingLeft() + lineTextWidth / 2, getPaddingTop() + (getStringHeight(lineText) / 2) + lineHeight * i + lineHeight / 2, linePaint);


            float startX = getPaddingLeft() + lineTextWidth + lineTextMargin;
            float startY = getPaddingTop() + lineHeight * i + lineHeight / 2;
            float endX = width - getPaddingRight();
            float endY = getPaddingTop() + lineHeight * i + lineHeight / 2;
            canvas.drawLine(startX, startY, endX, endY, linePaint);
        }
    }

    /**
     * 1.绘制文字，正序排列，文字居中
     * 2.绘制每一列的矩形
     *
     * @param canvas
     * @param ranks
     */
    private void drawRank(Canvas canvas, ArrayList<Rank> ranks) {
        for (int i = 0; i < ranks.size(); i++) {
            float rankValue = ranks.get(i).ranks;
            rankHeight = (rankValue / lineMaxValue) * (lineHeight * lineNum) - lineHeight;
            //paddongLeft + 行文本的宽度 +行文本与行的间距+列的间距/2
            float left = getPaddingLeft() + lineTextWidth + lineTextMargin + rankPadding / 2 + i * rankWidth;
            //top+ 每列等比例高度（height - paddingTop-  rankHeight ）+ lineHeight/2
            float top = height - getPaddingBottom() - rankHeight - lineHeight / 2;
            float right = left + rankWidth - rankPadding;
            float bottom = top + rankHeight;
            rankPaint.setTextSize(50);
            rankPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(ranks.get(i).rankText, left+rankPadding, lineNum * lineHeight+rankTextHeight/2, rankPaint);
            canvas.drawRect(left, top, right, bottom, rankPaint);
        }
    }

    private int getStringHeight(String str) {
        Paint.FontMetrics fr = linePaint.getFontMetrics();
        return (int) Math.ceil(fr.descent - fr.top - fr.bottom - fr.leading) - 5;  //ceil() 函数向上舍入为最接近的整数。
    }


    public void setLineTextSize(@NonNull int lineTextSize) {
        this.lineTextSize = lineTextSize;
        linePaint.setTextSize(lineTextSize);
        invalidate();
    }

    public void setMaxValue(@NonNull int value) {
        this.lineMaxValue = value;
        lineValue = lineMaxValue / lineNum;
        invalidate();
    }

    public void setLineNum(@NonNull int lineNum) {
        this.lineNum = lineNum;
        lineValue = lineMaxValue / this.lineNum;
        invalidate();
    }

    public void setRanks(ArrayList<Rank> ranks) {
        this.ranks = ranks;
        rankNum = ranks.size();
        invalidate();
    }

    public class Rank {
        float ranks;
        String rankText;

        public Rank(float ranks, String rankText) {
            this.ranks = ranks;
            this.rankText = rankText;
        }
    }
}
