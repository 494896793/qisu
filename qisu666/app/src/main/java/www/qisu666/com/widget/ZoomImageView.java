package www.qisu666.com.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import www.qisu666.common.utils.LogUtils;

public class ZoomImageView extends ImageView implements OnScaleGestureListener,
        OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener

{
    private static final String TAG = ZoomImageView.class.getSimpleName();

    public static final float SCALE_MAX = 4.0f;
    /**
     * 初始化时的缩放比例，如果图片宽或高大于屏幕，此值将小于0
     */
    private float initScale = 1.0f;

    /**
     * 用于存放矩阵的9个值
     */
    private final float[] matrixValues = new float[9];

    private boolean once = true;

    /**
     * 缩放的手势检测
     */
    private ScaleGestureDetector mScaleGestureDetector = null;

    private final Matrix mScaleMatrix = new Matrix();

    public ZoomImageView(Context context)
    {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        super.setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(this);

    }

    @Override
    public boolean onScale(ScaleGestureDetector detector)
    {
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();

        if (getDrawable() == null)
            return true;

        /**
         * 缩放的范围控制
         */
        if ((scale < SCALE_MAX && scaleFactor > 1.0f)
                || (scale > initScale && scaleFactor < 1.0f))
        {
            /**
             * 最大值最小值判断
             */
            if (scaleFactor * scale < initScale)
            {
                scaleFactor = initScale / scale;
            }
            if (scaleFactor * scale > SCALE_MAX)
            {
                scaleFactor = SCALE_MAX / scale;
            }
            /**
             * 设置缩放比例
             */
            mScaleMatrix.postScale(scaleFactor, scaleFactor, getWidth() / 2,
                    getHeight() / 2);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mScaleMatrix);
        }

        return true;

    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector)
    {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector)
    {
    }

    private Point curPoint;

    private float start_y = 0;
    private float cur_y = 0;
    private float start_x = 0;
    private float cur_x = 0;
    //标记手势，避免手势冲突
    private boolean scaleFlag = false;

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        int count = event.getPointerCount();
        if(count >= 2) {
            scaleFlag = true;
            return mScaleGestureDetector.onTouchEvent(event);
        }else{
            RectF rect = getMatrixRectF();
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    start_y = event.getY();
                    start_x = event.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    cur_y = event.getY();
                    cur_x = event.getX();
                    float d_y = cur_y - start_y;
                    float d_x = cur_x - start_x;
//                    LogUtils.d("d_y:" + d_y + "||cur_y:" + cur_y + "||start_y:" + start_y);
                    start_y = cur_y;
                    start_x = cur_x;
//                    LogUtils.d("c_l:" + rect.left + "||c_r:" + rect.right + "||c_t:" + rect.top + "||c_b:" + rect.bottom + "||p_x:" +
//                            curPoint.x + "||p_y:" + curPoint.y + "||w/2:" + getWidth()/2 + "||h/2:"+ getHeight()/2);
                    if(!scaleFlag) {
                        if((rect.bottom > getHeight() && d_y < 0) || (rect.top < 0 && d_y > 0)){
                            if(rect.bottom + d_y < getHeight()){
                                d_y = getHeight() - rect.bottom;
                            }else if(rect.top + d_y > 0){
                                d_y = 0 - rect.top;
                            }
                            mScaleMatrix.postTranslate(0, d_y);
//                            curPoint.y += d_y;
                        }
                        if((rect.left < 0 && d_x > 0) || (rect.right > getWidth() && d_x < 0)){
                            if(rect.left + d_x > 0){
                                d_x = 0 - rect.left;
                            }else if(rect.right + d_x < getWidth()){
                                d_x = getWidth() - rect.right;
                            }
                            mScaleMatrix.postTranslate(d_x, 0);
//                            curPoint.x += d_x;
                        }
                        setImageMatrix(mScaleMatrix);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    start_y = 0;
                    cur_y = 0;
                    start_x = 0;
                    cur_x = 0;
                    if(scaleFlag){
                        scaleFlag = false;
                    }
                    break;
            }
            return true;
        }

    }


    /**
     * 获得当前的缩放比例
     *
     * @return
     */
    public final float getScale()
    {
        mScaleMatrix.getValues(matrixValues);
        return matrixValues[Matrix.MSCALE_X];
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    @Override
    public void onGlobalLayout()
    {
        if (once)
        {
            Drawable d = getDrawable();
            if (d == null)
                return;
            Log.e(TAG, d.getIntrinsicWidth() + " , " + d.getIntrinsicHeight());
            int width = getWidth();
            int height = getHeight();
            // 拿到图片的宽和高
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();
            float scale = 1.0f;
            // 如果图片的宽或者高大于屏幕，则缩放至屏幕的宽或者高
            if (dw > width && dh <= height)
            {
                scale = width * 1.0f / dw;
            }
            if (dh > height && dw <= width)
            {
                scale = height * 1.0f / dh;
            }
            // 如果宽和高都大于屏幕，则让其按按比例适应屏幕大小
            if (dw > width && dh > height)
            {
                scale = Math.min(dw * 1.0f / width, dh * 1.0f / height);
            }
            initScale = scale;
            // 图片移动至屏幕中心
            mScaleMatrix.postTranslate((width - dw) / 2, (height - dh) / 2);
            mScaleMatrix
                    .postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            setImageMatrix(mScaleMatrix);
            once = false;
        }

    }

    private void checkBorderAndCenterWhenScale()
    {

        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        // 如果宽或高大于屏幕，则控制范围
        if (rect.width() >= width)
        {
            if (rect.left > 0)
            {
                deltaX = -rect.left;
            }
            if (rect.right < width)
            {
                deltaX = width - rect.right;
            }
        }
        if (rect.height() >= height)
        {
            if (rect.top > 0)
            {
                deltaY = -rect.top;
            }
            if (rect.bottom < height)
            {
                deltaY = height - rect.bottom;
            }
        }
        // 如果宽或高小于屏幕，则让其居中
        if (rect.width() < width)
        {
            deltaX = width * 0.5f - rect.right + 0.5f * rect.width();
        }
        if (rect.height() < height)
        {
            deltaY = height * 0.5f - rect.bottom + 0.5f * rect.height();
        }
        Log.e(TAG, "deltaX = " + deltaX + " , deltaY = " + deltaY);

        mScaleMatrix.postTranslate(deltaX, deltaY);

    }

    /**
     * 根据当前图片的Matrix获得图片的范围
     *
     * @return
     */
    private RectF getMatrixRectF()
    {
        Matrix matrix = mScaleMatrix;
        RectF rect = new RectF();
        Drawable d = getDrawable();
        if (null != d)
        {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rect);
        }
        return rect;
    }

    public void onZoom(){
        if(getScale() < SCALE_MAX){

        }
    }
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
////        curPoint = new Point(getWidth()/2, getHeight()/2);
//    }
}
