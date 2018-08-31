package demo.stepcount.com;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class StepCount extends View {
    private int mInnerColor = Color.RED;
    private int mOutterColor = Color.BLUE;
    //三只画笔
    private Paint mPaintText,mPaintInner,mPaintOutter;
    private int mBoderSize  = 8;
    private int mTextColor = Color.RED;
    private int mStepSize = 20;
    private int currentStep = 0;
    private float stepMax = (float) 4000.0;

    public StepCount(Context context) {
        this(context,null);
    }

    public StepCount(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StepCount(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StepCount);
        mInnerColor = typedArray.getColor(R.styleable.StepCount_innerColor,mInnerColor );
        mOutterColor = typedArray.getColor(R.styleable.StepCount_outterColor,mOutterColor );
        mTextColor = typedArray.getColor(R.styleable.StepCount_stepColor,mTextColor );
        mBoderSize = (int) typedArray.getDimension(R.styleable.StepCount_boderWidth,mBoderSize );
        mStepSize = (int) typedArray.getDimension(R.styleable.StepCount_stepSize, mStepSize);
        typedArray.recycle();

        mPaintInner = new Paint();
        mPaintInner.setColor(mInnerColor);
        mPaintInner.setAntiAlias(true);
        //圆形
        mPaintInner.setStrokeCap(Paint.Cap.ROUND);
        mPaintInner.setStrokeWidth(mBoderSize);
        //Stroke（笔触）是一个形状的轮廓。Fill（填充）是在形状内部的内容。
        mPaintInner.setStyle(Paint.Style.STROKE);

        mPaintOutter = new Paint();
        mPaintOutter.setColor(mOutterColor);
        mPaintOutter.setAntiAlias(true);
        mPaintOutter.setStrokeWidth(mBoderSize);
        mPaintOutter.setStrokeCap(Paint.Cap.ROUND);
        mPaintOutter.setStyle(Paint.Style.STROKE);

        mPaintText = new Paint();
        mPaintText.setColor(mTextColor);
        mPaintText.setAntiAlias(true);
        mPaintText.setTextSize(mStepSize);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int  heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec)==MeasureSpec.AT_MOST){ //如果是wrap_content 那么我应该为他指定一个宽高
            widthSize = 50;
        }
        if (MeasureSpec.getMode(heightMeasureSpec)==MeasureSpec.AT_MOST){
            heightSize = 50;
        }
        setMeasuredDimension(widthSize>heightSize?heightSize:widthSize, widthSize>heightSize?heightSize:widthSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //先画外圆弧
        RectF rectF = new RectF(getPaddingLeft()+mBoderSize/2,getPaddingTop()+mBoderSize/2,getWidth()-mBoderSize/2-getPaddingRight(),getHeight()-mBoderSize/2-getPaddingBottom());

        canvas.drawArc(rectF, 135, 270, false, mPaintOutter);
        float percentage = currentStep/stepMax;
        if (percentage<0){
            percentage=0;
        }
        if (percentage>1){
            percentage=1;
        }
        canvas.drawArc(rectF,135 , 270*percentage, false, mPaintInner);

        Rect bounds = new Rect();
        String valueText= currentStep + "";
        mPaintText.getTextBounds(valueText,0 , valueText.length(),bounds );
        Paint.FontMetrics fontMetrics = mPaintText.getFontMetrics();
        int dy = (int) ((fontMetrics.bottom-fontMetrics.top)/2-fontMetrics.bottom);
        int baseline = getHeight()/2+dy;
        canvas.drawText(valueText,getWidth()/2-bounds.width()/2 ,baseline ,mPaintText);
    }
    public synchronized void setCurrentStep(int step){
        this.currentStep = step;
        invalidate();
    }
}
