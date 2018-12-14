package com.test.memo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;


;

public class AddView extends AppCompatButton {
    public AddView(Context context) {
        super(context);
    }
    protected Paint paint;
    protected int HstartX, HstartY, HendX, HendY;//水平的线
    protected int SstartX, SstartY, SsendX, SsendY;//垂直的线
    protected int paintWidth = 8;//初始化加号的粗细为10
    protected int paintColor = Color.WHITE;//画笔颜色黑色
    protected int padding = 3;//默认3的padding
    protected int width;
    public int getPadding() {
        return padding;
    }

    public AddView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setColor(paintColor);
        paint.setStrokeWidth(paintWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            //  MeasureSpec.EXACTLY表示该view设置的确切的数值
            width = widthSize;
        } else {
            width = 60;//默认值
        }
        SstartX = SsendX = HstartY = HendY = width / 2;
        SsendY = HendX = width - getPadding()*20;
        SstartY = HstartX = getPadding()*20;
        //这样做是因为加号宽高是相等的，手动设置宽高
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float[] direction = new float[]{ 1, 1, 3 };   // 设置光源的方向
        float light = 0.7f;     //设置环境光亮度
        float specular = 8;     // 定义镜面反射系数
        float blur = 3.0f;      //模糊半径
        EmbossMaskFilter emboss=new EmbossMaskFilter(direction,light,specular,blur);
        paint.setMaskFilter(emboss);
        paint.setMaskFilter(emboss);
        super.onDraw(canvas);
        //水平的横线
        canvas.drawLine(HstartX, HstartY, HendX, HendY, paint);
        //垂直的横线
        canvas.drawLine(SstartX, SstartY, SsendX, SsendY, paint);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);     //关闭硬件加速
    }
}

