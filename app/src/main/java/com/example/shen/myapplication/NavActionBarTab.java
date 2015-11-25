package com.example.shen.myapplication;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Shen on 2015/11/23.
 */
public class NavActionBarTab extends RelativeLayout implements View.OnClickListener {

    private int tabWidth = 86;

    private int tabHeight = 36;

    private String[] defaultTabStr = {"消息", "关注", "粉丝"};

    private int tabBorderWidth = 1;

    private int allWidth = (tabWidth - tabHeight / 3) * defaultTabStr.length;

    private int defalutColor = Color.BLACK;

    private int defalutBorderColor = Color.WHITE;

    private Path border = new Path();

    private Paint border_paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint background_paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint tag_paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint normal_text_paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int textSize = 18;

    private static final PorterDuffXfermode PORTER_DUFF_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
    private static final PorterDuffXfermode PORTER_DUFF_XFERMODE1 = new PorterDuffXfermode(PorterDuff.Mode.DST_OVER);

    public NavActionBarTab(Context context) {
        super(context);
        init(context);
    }

    public NavActionBarTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NavActionBarTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        setWillNotDraw(false);
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画边框
        float border_s = ImageUtil.dip2px(getContext(), tabBorderWidth);
        float border_w = ImageUtil.dip2px(getContext(), allWidth);
        float border_h = ImageUtil.dip2px(getContext(), tabHeight);

        RectF border = new RectF(border_s, border_s, border_w, border_h);
        border_paint.setStyle(Paint.Style.STROKE);
        border_paint.setColor(defalutBorderColor);
        border_paint.setStrokeWidth(border_s);
        canvas.drawRoundRect(border, border_h, border_h, border_paint);

        //画背景
        float bg_s = border_s * 2;
        float bg_w = border_w - border_s;
        float bg_h = border_h - border_s;

        RectF bg = new RectF(bg_s, bg_s, bg_w, bg_h);
        background_paint.setStyle(Paint.Style.FILL);
        background_paint.setColor(defalutColor);
        canvas.drawRoundRect(bg, bg_h, bg_h, background_paint);

        //画tab
        Bitmap background = Bitmap.createBitmap((int) border_w, (int) border_h, Bitmap.Config.ARGB_8888);
        Canvas tab_cavas = new Canvas(background);

        float tab_s = border_s;
        float tab_w = ImageUtil.dip2px(getContext(), tabWidth);
        float tab_h = border_h;

        RectF tab = new RectF(tab_s + x, tab_s, tab_w + x, tab_h);
        tag_paint.setStyle(Paint.Style.FILL);
        tag_paint.setColor(defalutBorderColor);
        tab_cavas.drawRoundRect(tab, tab_h, tab_h, tag_paint);

        //写字
        normal_text_paint.setTextSize(ImageUtil.dip2px(getContext(), textSize));
        normal_text_paint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < defaultTabStr.length; i++) {
            String testString1 = defaultTabStr[i];
            normal_text_paint.setColor(defalutColor);
            Rect bounds = new Rect();
            normal_text_paint.getTextBounds(testString1, 0, testString1.length(), bounds);
            normal_text_paint.setXfermode(PORTER_DUFF_XFERMODE);
            Paint.FontMetricsInt fontMetrics = normal_text_paint.getFontMetricsInt();
            int baseline = ((int) border_h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            tab_cavas.drawText(testString1, (border_w - tab_w) / 2 * i + tab_w / 2, baseline, normal_text_paint);

            normal_text_paint.setColor(defalutBorderColor);
            normal_text_paint.setXfermode(PORTER_DUFF_XFERMODE1);
            tab_cavas.drawText(testString1, (border_w - tab_w) / 2 * i + tab_w / 2, baseline, normal_text_paint);
        }


        /*String testString1 = "消息";
        Rect bounds = new Rect();
        normal_text_paint.getTextBounds(testString1, 0, testString1.length(), bounds);
        normal_text_paint.setXfermode(PORTER_DUFF_XFERMODE);
        Paint.FontMetricsInt fontMetrics = normal_text_paint.getFontMetricsInt();
        int baseline = ((int) border_h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        tab_cavas.drawText(testString1, tab_w / 2, baseline, normal_text_paint);

        normal_text_paint.setColor(defalutBorderColor);
        normal_text_paint.setXfermode(PORTER_DUFF_XFERMODE1);
        tab_cavas.drawText(testString1, tab_w / 2, baseline, normal_text_paint);

        //写字
        normal_text_paint.setTextSize(ImageUtil.dip2px(getContext(), textSize));
        normal_text_paint.setTextAlign(Paint.Align.CENTER);
        normal_text_paint.setColor(defalutColor);

        String testString2 = "关注";
        bounds = new Rect();
        normal_text_paint.getTextBounds(testString2, 0, testString2.length(), bounds);
        normal_text_paint.setXfermode(PORTER_DUFF_XFERMODE);
        fontMetrics = normal_text_paint.getFontMetricsInt();
        baseline = ((int) border_h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        tab_cavas.drawText(testString2, border_w / 2, baseline, normal_text_paint);

        normal_text_paint.setColor(defalutBorderColor);
        normal_text_paint.setXfermode(PORTER_DUFF_XFERMODE1);
        tab_cavas.drawText(testString2, border_w / 2, baseline, normal_text_paint);

        //写字
        normal_text_paint.setTextSize(ImageUtil.dip2px(getContext(), textSize));
        normal_text_paint.setTextAlign(Paint.Align.CENTER);
        normal_text_paint.setColor(defalutColor);

        String testString3 = "粉丝";
        bounds = new Rect();
        normal_text_paint.getTextBounds(testString3, 0, testString3.length(), bounds);
        normal_text_paint.setXfermode(PORTER_DUFF_XFERMODE);
        fontMetrics = normal_text_paint.getFontMetricsInt();
        baseline = ((int) border_h - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        tab_cavas.drawText(testString3, border_w - tab_w / 2, baseline, normal_text_paint);

        normal_text_paint.setColor(defalutBorderColor);
        normal_text_paint.setXfermode(PORTER_DUFF_XFERMODE1);
        tab_cavas.drawText(testString3, border_w - tab_w / 2, baseline, normal_text_paint);*/

        canvas.drawBitmap(background, 0, 0, null);

    }

    int x = 0;
    int num = 0;

    @Override
    public void onClick(View v) {
        if (num == defaultTabStr.length) {
            num = 0;
            x = 0;
        }
        num++;
        int all_width = ImageUtil.dip2px(getContext(), allWidth);
        int tab_width = ImageUtil.dip2px(getContext(), tabWidth);
        int width = (int) ((float) (all_width - tab_width) / 2f);
        ValueAnimator animator = new ObjectAnimator().ofInt(x, x + width).setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                x = Integer.valueOf(animation.getAnimatedValue().toString());
                invalidate();
            }
        });
        animator.start();
    }
}
