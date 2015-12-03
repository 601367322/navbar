package com.shen.actionbartab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Shen on 2015/11/23.
 */
public class NavActionBarTab extends RelativeLayout implements View.OnTouchListener {

    private int tabWidth = 0; //tab选中的宽度 无需改动

    private int tabHeight = 36;//高度

    private int tabWidthPlus = 25;//决定tab的大小 ---可以改动---

    private String[] defaultTabStr = {"第一个", "第二个", "第三个"};//tab内容

    private Boolean[] tabBadge = new Boolean[defaultTabStr.length];

    private int tabBorderWidth = 1;//边框宽度

    private int allWidth = 0;//整体宽度 无需改动

    private int allWidthPlus = 15;//决定整体的大小  ---可以改动---

    private int scrollWidth = 0;//每次滑动需要的距离

    private int scrollPix = 0;//当前滑动距离

    private int selection = 0;//当前选中项

    private int defalutColor = Color.BLACK;//背景颜色/字体颜色

    private int defalutBorderColor = Color.WHITE;//边框颜色/字体颜色

    private Paint border_paint = new Paint(Paint.ANTI_ALIAS_FLAG);//边框

    private Paint background_paint = new Paint(Paint.ANTI_ALIAS_FLAG);//背景

    private Paint tab_paint = new Paint(Paint.ANTI_ALIAS_FLAG);//tab

    private Paint normal_text_paint = new Paint(Paint.ANTI_ALIAS_FLAG);//字体

    private Paint badge_paint = new Paint(Paint.ANTI_ALIAS_FLAG);//小红点

    private int badgeRadius = 4;//红点大小

    private int textSize = 18;//字体大小

    private static final PorterDuffXfermode PORTER_DUFF_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);//白字
    private static final PorterDuffXfermode PORTER_DUFF_XFERMODE1 = new PorterDuffXfermode(PorterDuff.Mode.DST_OVER);//黑字

    public NavActionBarTab(Context context) {
        super(context);
        init();
    }

    public NavActionBarTab(Context context, AttributeSet attrs) {
        super(context, attrs);

        initText(attrs);

        init();
    }

    public NavActionBarTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initText(attrs);

        init();
    }

    void initText(AttributeSet attrs) {
        try {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.NavActionBarTab);
            if (a != null) {
                String text = a.getString(R.styleable.NavActionBarTab_text);
                defaultTabStr = text.split(",");
                tabBadge = new Boolean[defaultTabStr.length];
                a.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        setWillNotDraw(false);

        setOnTouchListener(this);

        normal_text_paint.setTextSize(dip2px(getContext(), textSize));
        normal_text_paint.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i < defaultTabStr.length; i++) {
            String testString1 = defaultTabStr[i];
            Rect bounds = new Rect();
            normal_text_paint.getTextBounds(testString1, 0, testString1.length(), bounds);
            //将字体宽度累加计算
            allWidth += bounds.width();

            tabWidth = bounds.width() + dip2px(getContext(), tabWidthPlus);//后面的数字决定选中时的tab背景大小
        }
        allWidth += (defaultTabStr.length + 1) * dip2px(getContext(), allWidthPlus);//后面的数字决定整体宽度

        scrollWidth = (int) ((float) (allWidth - tabWidth) / (defaultTabStr.length - 1));

        tabHeight = dip2px(getContext(), tabHeight);

        badgeRadius = dip2px(getContext(), badgeRadius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int tab_border = dip2px(getContext(), tabBorderWidth);
        setMeasuredDimension(allWidth + tab_border, tabHeight + tab_border);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画边框
        float border_s = dip2px(getContext(), tabBorderWidth);//边宽
        float border_w = allWidth;//宽
        float border_h = tabHeight;//高

        //向右下角偏移，否则会出框，偏移量为边宽
        RectF border = new RectF(border_s, border_s, border_w, border_h);
        border_paint.setStyle(Paint.Style.STROKE);//空心
        border_paint.setColor(defalutBorderColor);
        border_paint.setStrokeWidth(border_s);
        canvas.drawRoundRect(border, border_h, border_h, border_paint);

        //画背景
        float bg_s = border_s * 2;
        float bg_w = border_w - border_s;
        float bg_h = border_h - border_s;

        //同样需要偏移，而且宽高要缩小一边框
        RectF bg = new RectF(bg_s, bg_s, bg_w, bg_h);
        background_paint.setStyle(Paint.Style.FILL);
        background_paint.setColor(defalutColor);
        canvas.drawRoundRect(bg, bg_h, bg_h, background_paint);

        //画tab，需要新建画布,画布大小和上面所画边框一致
        Bitmap background = Bitmap.createBitmap((int) border_w, (int) border_h, Bitmap.Config.ARGB_8888);
        Canvas tab_cavas = new Canvas(background);

        float tab_s = border_s;
        float tab_w = tabWidth;
        float tab_h = border_h;

        RectF tab = new RectF(tab_s + scrollPix, tab_s, tab_w + scrollPix, tab_h);
        tab_paint.setStyle(Paint.Style.FILL);
        tab_paint.setColor(defalutBorderColor);
        //画在新的画布上
        tab_cavas.drawRoundRect(tab, tab_h, tab_h, tab_paint);

        //写字，在新的画布上写字
        for (int i = 0; i < defaultTabStr.length; i++) {
            String testString1 = defaultTabStr[i];

            Rect bounds = new Rect();
            normal_text_paint.getTextBounds(testString1, 0, testString1.length(), bounds);
            //未选中时的字体，需要设置这样
            normal_text_paint.setColor(defalutColor);
            normal_text_paint.setXfermode(PORTER_DUFF_XFERMODE);
            //获取字的宽高，设置为居中
            Paint.FontMetricsInt fontMetrics = normal_text_paint.getFontMetricsInt();
            int baseline = ((int) border_h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            //整体宽度，减去选中时的宽度，除以剩下的数量，再乘以.....醉了，数学不好，无法简化
            tab_cavas.drawText(testString1, (border_w - tab_w) / (defaultTabStr.length - 1) * i + tab_w / 2, baseline, normal_text_paint);

            //选中时的字体，需要设置这样
            normal_text_paint.setColor(defalutBorderColor);
            normal_text_paint.setXfermode(PORTER_DUFF_XFERMODE1);
            tab_cavas.drawText(testString1, (border_w - tab_w) / (defaultTabStr.length - 1) * i + tab_w / 2, baseline, normal_text_paint);

            if (tabBadge[i] != null && tabBadge[i]) {
                badge_paint.setColor(Color.RED);
                badge_paint.setStyle(Paint.Style.FILL);
                tab_cavas.drawCircle((border_w - tab_w) / (defaultTabStr.length - 1) * (i + 1) - badgeRadius, (tabHeight - (fontMetrics.bottom + fontMetrics.top)) / 2 + fontMetrics.top + badgeRadius, badgeRadius, badge_paint);
            }
        }

        canvas.drawBitmap(background, 0, 0, null);
    }

    public void setPositionOffsetPixels(int position, float pix) {
        scrollPix = position * scrollWidth + (int) (scrollWidth * pix);
        invalidate();
    }

    public void setSelection(int position) {
        selection = position;
        scrollPix = position * scrollWidth;
        invalidate();
    }

    public void setBadge(int position, boolean badge) {
        tabBadge[position] = badge;
        invalidate();
    }

    float touchX, touchY;
    int touchSelect; //1、2、3 点击的下标

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int one = allWidth / 3;//每个tab点击区域的宽度

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                touchY = event.getY();

                //计算手指按下去时点击的下标
                touchSelect = (int) (touchX / one);
                if (touchX % one > 0f) {
                    touchSelect++;
                }
                return true;
            case MotionEvent.ACTION_UP:
                touchX = event.getX();
                touchY = event.getY();

                if (touchY < tabHeight) {//如果在整体范围内
                    int up_touchSelect = (int) (touchX / one);
                    if (touchX % one > 0f) {
                        up_touchSelect++;
                    }
                    //松开时和点击时的点击下标一致时
                    if (up_touchSelect == touchSelect && listener != null) {
                        listener.selected(up_touchSelect);
                    }
                }
                return false;
        }
        return false;
    }

    public void setListener(onTabSelectedListener listener) {
        this.listener = listener;
    }

    private onTabSelectedListener listener;

    public interface onTabSelectedListener {
        void selected(int position);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
