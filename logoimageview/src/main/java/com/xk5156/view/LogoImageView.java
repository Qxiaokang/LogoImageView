package com.xk5156.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.xk5156.view.util.BitmapUtil;
import com.xk5156.view.util.LogoType;

import androidx.appcompat.widget.AppCompatImageView;


/**
 * author: xk
 * date: 2020/8/27
 * description:
 * version: 1.0
 */
public class LogoImageView extends AppCompatImageView {
    //图标类型
    int logoType = 0;
    //是否为圆角
    boolean isRoundCorner = false;
    //圆角大小，默认为10
    private float borderRadius = 10;
    private int radiusType=0;
    private int bgColor;
    //是否为圆形图
    boolean isCircle = false;
    //圆角大小，默认为10
    private int mBorderRadius = 10;
    //渲染图像，使用图像为绘制图形着色
    private BitmapShader mBitmapShader;
    int minWidth = 160, minHeight = 160, measureWidth, measureHeight, imgWidth, imgHeight, paddingLeft, paddingRight, paddingTop, paddingBottom;
    int[] defaultRes = {R.drawable.i_1, R.drawable.i_2, R.drawable.i_3, R.drawable.i_4, R.drawable.i_5, R.drawable.i_6};
    Paint mPaint,colorPaint;
    Bitmap bitmap;

    public LogoImageView(Context context) {
        this(context, 0);
    }

    public LogoImageView(Context context, int type) {
        this(context, type, 160, 160);
    }

    public LogoImageView(Context context, int type, int height, int width) {
        this(context, type, height, width, false, false, 10);
    }

    public LogoImageView(Context context, int type, int height, int width, boolean isCircle, boolean isRoundCorner, float borderRadius) {
        super(context);
        this.logoType = type;
        this.minWidth = width;
        this.minHeight = height;
        this.isCircle = isCircle;
        this.isRoundCorner = isRoundCorner;
        this.borderRadius = borderRadius;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        colorPaint = new Paint();
        colorPaint.setAntiAlias(true);
        colorPaint.setDither(true);
    }

    public LogoImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LogoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        colorPaint = new Paint();
        colorPaint.setAntiAlias(true);
        colorPaint.setDither(true);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LogoImageView);
        this.logoType = typedArray.getInteger(R.styleable.LogoImageView_logoType, logoType);
        this.isRoundCorner = typedArray.getBoolean(R.styleable.LogoImageView_isRoundCorner, false);
        this.isCircle = typedArray.getBoolean(R.styleable.LogoImageView_isCircle, false);
        this.borderRadius = typedArray.getDimension(R.styleable.LogoImageView_borderRadius, borderRadius);
        this.radiusType = typedArray.getInteger(R.styleable.LogoImageView_radiusType, radiusType);
        this.bgColor = typedArray.getColor(R.styleable.LogoImageView_bgColor, Color.TRANSPARENT);
        colorPaint.setColor(bgColor);
        colorPaint.setStyle(Paint.Style.FILL);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureWidth = measureWidth(minWidth, widthMeasureSpec);
        measureHeight = measureHeight(minHeight, heightMeasureSpec);
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();
        imgWidth = measureWidth - paddingLeft - paddingRight;
        imgHeight = measureHeight - paddingBottom - paddingTop;
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isCircle) {//圆
            drawCircleLogo(canvas, mPaint, logoType);
        } else if (isRoundCorner) {//圆角
            drawRoundLogo(canvas, mPaint, logoType);
        } else if (logoType > 0) {//默认图
            drawBimapLogo(canvas, mPaint, logoType);
        } else {
            super.onDraw(canvas);
        }
    }


    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return scaleBitmap(bd.getBitmap(), imgWidth, imgHeight, 1);
        }
        // 当设置不为图片，为颜色时，获取的drawable宽高会有问题，所有当为颜色时候获取控件的宽高
        int w = drawable.getIntrinsicWidth() <= 0 ? getWidth() : drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight() <= 0 ? getHeight() : drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 绘制圆形logo
     *
     * @param canvas   画布
     * @param mPaint   画笔
     * @param logoType 图片类型
     */
    private void drawCircleLogo(Canvas canvas, Paint mPaint, int logoType) {
        bitmap = getDrawBitmap(logoType);
        if (bitmap == null) {
            return;
        }
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(mBitmapShader);
        int w = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getHeight() / 2 : bitmap.getWidth() / 2;
        canvas.drawCircle(measureWidth / 2, measureHeight / 2, w, mPaint);
    }

    /**
     * 圆角logo
     *
     * @param canvas
     * @param mPaint
     */
    private void drawRoundLogo(Canvas canvas, Paint mPaint, int type) {
        bitmap = getDrawBitmap(type);
        if (bitmap == null) {
            return;
        }
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(mBitmapShader);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Path path=new Path();
            switch (radiusType){
                case 1:
                    path.addRoundRect(0, 0, measureWidth, measureHeight,new float[]{mBorderRadius, mBorderRadius, 0,0,0, 0, 0, 0}, Path.Direction.CCW);
                    break;
                case 2:
                    path.addRoundRect(0, 0, measureWidth, measureHeight,new float[]{0, 0, mBorderRadius,mBorderRadius,0, 0, 0, 0}, Path.Direction.CCW);
                    break;
                case 3:
                    path.addRoundRect(0, 0, measureWidth, measureHeight,new float[]{0, 0, 0,0,mBorderRadius, mBorderRadius, 0, 0}, Path.Direction.CCW);
                    break;
                case 4:
                    path.addRoundRect(0, 0, measureWidth, measureHeight,new float[]{0, 0, 0,0,0, 0, mBorderRadius, mBorderRadius}, Path.Direction.CCW);
                    break;
                case 12:
                    path.addRoundRect(0, 0, measureWidth, measureHeight,new float[]{mBorderRadius, mBorderRadius, mBorderRadius,mBorderRadius,0, 0, 0, 0}, Path.Direction.CCW);
                    break;
                case 13:
                    path.addRoundRect(0, 0, measureWidth, measureHeight,new float[]{mBorderRadius, mBorderRadius, 0,0,mBorderRadius, mBorderRadius, 0, 0}, Path.Direction.CCW);
                    break;
                case 14:
                    path.addRoundRect(0, 0, measureWidth, measureHeight,new float[]{mBorderRadius, mBorderRadius, 0,0,0, 0, mBorderRadius, mBorderRadius}, Path.Direction.CCW);
                    break;
                case 23:
                    path.addRoundRect(0, 0, measureWidth, measureHeight,new float[]{0, 0, mBorderRadius,mBorderRadius,mBorderRadius, mBorderRadius, 0, 0}, Path.Direction.CCW);
                    break;
                case 24:
                    path.addRoundRect(0, 0, measureWidth, measureHeight,new float[]{0, 0, mBorderRadius,mBorderRadius,0, 0, mBorderRadius, mBorderRadius}, Path.Direction.CCW);
                    break;
                case 34:
                    path.addRoundRect(0, 0, measureWidth, measureHeight,new float[]{0, 0, 0,0,mBorderRadius, mBorderRadius, mBorderRadius, mBorderRadius}, Path.Direction.CCW);
                    break;
                case 123:
                    path.addRoundRect(0, 0, measureWidth, measureHeight,new float[]{mBorderRadius, mBorderRadius, mBorderRadius,mBorderRadius,mBorderRadius, mBorderRadius, 0, 0}, Path.Direction.CCW);

                    break;
                case 124:
                    path.addRoundRect(0, 0, measureWidth, measureHeight,new float[]{mBorderRadius, mBorderRadius, mBorderRadius,mBorderRadius,0, 0, mBorderRadius, mBorderRadius}, Path.Direction.CCW);

                    break;
                case 234:
                    path.addRoundRect(0, 0, measureWidth, measureHeight,new float[]{0, 0, mBorderRadius,mBorderRadius,mBorderRadius, mBorderRadius, mBorderRadius, mBorderRadius}, Path.Direction.CCW);

                    break;
                default:
                    path.addRoundRect(0, 0, measureWidth, measureHeight,new float[]{mBorderRadius, mBorderRadius, mBorderRadius,mBorderRadius,mBorderRadius, mBorderRadius, mBorderRadius, mBorderRadius}, Path.Direction.CCW);
                    break;
            }
            if(bgColor!=Color.TRANSPARENT){
                canvas.drawPath(path,colorPaint);
            }
            canvas.drawPath(path,mPaint);
        }else {
            if(bgColor!=Color.TRANSPARENT){
                canvas.drawRoundRect(new RectF(0, 0, measureWidth, measureHeight), mBorderRadius, mBorderRadius,
                        colorPaint);
            }
            canvas.drawRoundRect(new RectF(0, 0, measureWidth, measureHeight), mBorderRadius, mBorderRadius,
                mPaint);
        }
    }

    /**
     * 获取要绘制的 bitmap
     *
     * @param type 图片类型
     * @return
     */
    private Bitmap getDrawBitmap(int type) {
        Bitmap bt = null;
        if (getDrawable() == null) {
            bt = getBitmapByType(type);
            bt = scaleBitmap(bt, imgWidth, imgHeight, 1);
        } else {
            bt = drawableToBitamp(getDrawable());
        }
        return bt;
    }

    /**
     * 通过type获取通用 Logo
     *
     * @param type
     * @return Bitmap
     */
    private Bitmap getBitmapByType(int type) {
        switch (type) {
            case LogoType.GeneralType.TYPE_1:
                Bitmap bitmap1 = BitmapUtil.getInstance().getLogoBitmapFromFile(LogoType.LOGO_1);
//                Bitmap bitmap1  = BitmapUtil.getPrintLogoBitmap(GBC.Logo.LOGO_4);
                if (bitmap1 != null) {
                    this.bitmap = bitmap1;
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(), defaultRes[0]);
                }
                break;
            case LogoType.GeneralType.TYPE_2:
                Bitmap bitmap2 = BitmapUtil.getInstance().getLogoBitmapFromFile(LogoType.LOGO_2);
                if (bitmap2 != null) {
                    this.bitmap = bitmap2;
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(), defaultRes[1]);
                }
                break;
            case LogoType.GeneralType.TYPE_3:
                Bitmap bitmap3 = BitmapUtil.getInstance().getLogoBitmapFromFile(LogoType.LOGO_3);
                if (bitmap3 != null) {
                    this.bitmap = bitmap3;
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(), defaultRes[2]);
                }
                break;
            case LogoType.GeneralType.TYPE_4:
                Bitmap bitmap4 = BitmapUtil.getInstance().getLogoBitmapFromFile(LogoType.LOGO_4);
                if (bitmap4 != null) {
                    this.bitmap = bitmap4;
                } else {
                    this.bitmap = BitmapFactory.decodeResource(getResources(), defaultRes[3]);
                }
                break;
            case LogoType.GeneralType.TYPE_5:
                Bitmap bitmap5 = BitmapUtil.getInstance().getLogoBitmapFromFile(LogoType.LOGO_5);
                if (bitmap5 != null) {
                    this.bitmap = bitmap5;
                } else {
                    this.bitmap = BitmapFactory.decodeResource(getResources(), defaultRes[4]);
                }
                break;
            case LogoType.GeneralType.TYPE_6:
                Bitmap bitmap6 = BitmapUtil.getInstance().getLogoBitmapFromFile(LogoType.LOGO_6);
                if (bitmap6 != null) {
                    this.bitmap = bitmap6;
                } else {
                    this.bitmap = BitmapFactory.decodeResource(getResources(), defaultRes[5]);
                }
                break;
            default:
                this.bitmap = null;
                break;

        }
        return bitmap;
    }


    /**
     * 绘制logo
     *
     * @param canvas
     * @param paint
     */
    private void drawBimapLogo(Canvas canvas, Paint paint, int type) {
        bitmap = getDrawBitmap(type);
        if (bitmap == null) {
            return;
        }
        Bitmap newBitmap = scaleBitmap(bitmap, imgWidth, imgHeight, 1);
        int left = (measureWidth - paddingLeft - paddingRight) / 2 - newBitmap.getWidth() / 2 + paddingLeft;
        int top = (measureHeight - paddingTop - paddingBottom) / 2 - newBitmap.getHeight() / 2 + paddingTop;
        canvas.drawBitmap(newBitmap, left, top, mPaint);
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param maxW   img最大宽
     * @param maxH   img最大高度
     * @param ratio  比例
     * @return 新的bitmap
     */
    private Bitmap scaleBitmap(Bitmap origin, int maxW, int maxH, float ratio) {
        if (origin == null) {
            return null;
        }
        //计算出的图片已是最大，只缩小，不放大
        if (ratio > 1) {
            ratio = 1;
        }
        int w = origin.getWidth();
        int h = origin.getHeight();
        int newWidth = 0;
        int newHeight = 0;
        float whRatio = 1.0f;
        if (maxW > maxH) {
            //超过最大高度
            whRatio = maxH * 1.0f / h * ratio;
            newHeight = maxH;
            newWidth = (int) (w * whRatio);
        } else {
            //超过最大宽度
            whRatio = maxW * 1.0f / w * ratio;
            newWidth = maxW;
            newHeight = (int) (h * whRatio);
        }
        return Bitmap.createScaledBitmap(origin, newWidth, newHeight, true);
    }

    /**
     * 测量宽度
     *
     * @param defaultWidth
     * @param measureSpec
     * @return
     */
    private int measureWidth(int defaultWidth, int measureSpec) {
        int width = defaultWidth;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case View.MeasureSpec.AT_MOST:
                break;
            case View.MeasureSpec.EXACTLY:
                width = specSize;
                break;
            case View.MeasureSpec.UNSPECIFIED:
                width = Math.max(defaultWidth, specSize);
                break;
            default:
                break;
        }
        return width;
    }

    /**
     * 测量高度
     *
     * @param defaultHeight
     * @param measureSpec
     * @return
     */
    private int measureHeight(int defaultHeight, int measureSpec) {
        int height = defaultHeight;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case View.MeasureSpec.AT_MOST:
                break;
            case View.MeasureSpec.EXACTLY:
                height = specSize;
                break;
            case View.MeasureSpec.UNSPECIFIED:
                height = Math.max(defaultHeight, specSize);
                break;
            default:
                break;
        }
        return height;
    }
}
