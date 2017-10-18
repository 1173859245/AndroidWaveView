package com.sanfeng.hotelbutler.aigestudiocustomvie_712;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by Administrator on 2017/5/26 0026.
 */

public class MyCircleWaveView extends View {
   private int mProgress;//进度
   private String tubiNum;//兔币余额
   private int mTimeStep=10;//间隔时间
   private int mSpeed = 5;//波单次移动的距离
   private  int mViewHeight;  //定义视图的高度
   private  int mViewWidth;  //定义视图的宽度
   private int mLevelLine;//基准线
   private int mWaveLength;//波长 暂定view宽度为一个波长
   private int mStrokeWidth; //圆环线的宽度
   private  RectF rectF;//圆环区域
   private  int mWaveHight;//波浪的最高点
   private int mLeftWaveMoveLength;//波平移的距离，用来控制波的起点位置
   private int mWaveColor;//波颜色
   private Paint mPaint;// 波浪画笔
    private Paint mCirclePaint;//圆圈画笔
   
    private Paint mBorderPaint;//边界线画笔
   
    private int   mBorderWidth=4;//边界宽度
    private Paint mTextPaint;//文字画笔
    private Paint bitmapPaint;//图标画笔
    private Path mPath;  //波浪轨迹
    private Bitmap bitmap;//位图
    private Matrix matrix;//矩阵
    private Paint tubipaint;//兔币余额显示画笔
    
    
    //波浪变量
         private List<Point> mPoints;//点的集合
    private boolean  isMeasure=false; //判断是否测量过
    private boolean isCircle=false;//是否圆形默认false，可属性代码设置

    //处理消息
   private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           initWaveMove();
        }
    };



    /**
     * 初始化波的移动
     */
    private void initWaveMove(){
        mLeftWaveMoveLength+=mSpeed;//波向右移动距离增加mSpeed;
        if (mLeftWaveMoveLength>=mWaveLength){//当增加到一个波长时回复到0
            mLeftWaveMoveLength=0;
        }
        invalidate();

    }




    public MyCircleWaveView(Context context) {
        this(context,null);
    }

    public MyCircleWaveView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCircleWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
         getAttr(context,attrs,defStyleAttr);
         init();
        initRes(context);

    }
    
   private void init(){
        mPoints=new ArrayList<Point>();
        //波浪轨迹画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xFFA2D6AE);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPath=new Path();  //波浪轨迹


 //文字画笔

        mTextPaint=new Paint();
        mTextPaint.setColor(Color.RED);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(28);
        //圆环画笔
        mCirclePaint=new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setStyle(Paint.Style.STROKE);//填充内部和描边

        //边界线画笔
        mBorderPaint=new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(Color.parseColor("#F4D114"));
        mBorderPaint.setStyle(Paint.Style.STROKE);//填充内部和描边
        mBorderPaint.setStrokeWidth(mBorderWidth);//线是有宽度的  采用了这种方式画圆环
       //图标画笔
       bitmapPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
       //兔币余额画笔
       tubipaint=new Paint();
       mTextPaint.setAntiAlias(true);
       tubipaint.setColor(Color.RED);
       tubipaint.setTextAlign(Paint.Align.CENTER);
       tubipaint.setTextSize(32);

    }

    /**
     *  获取自定义attr下的属性
     * @param context
     * @param attrs
     * @param defStyle
     */
    private void getAttr(Context context, AttributeSet attrs, int defStyle) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyCircleWaveView, defStyle, 0);

        mWaveColor = a.getColor(R.styleable.MyCircleWaveView_wave_color, Color.RED);
        isCircle=a.getBoolean(R.styleable.MyCircleWaveView_wave_circle,false);
        a.recycle();

    }
    private void initRes(Context context){

       bitmap= BitmapFactory.decodeResource(context.getResources(),R.mipmap.tubi);
    }
    /**
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec  暂时不做处理，默认用户给的宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
 if (!isMeasure && Math.abs(getMeasuredHeight()-getMeasuredWidth())<50){   //只让onMeasure方法执行一次--------------
     mViewHeight=getMeasuredHeight();
     mViewWidth=getMeasuredWidth();
     mLevelLine=mViewHeight;  //基准线位于屏幕最底部？？？
     {
         mLevelLine = mViewHeight * (100-mProgress) / 100;
         if (mLevelLine < 0) mLevelLine = 0;
     }
    //计算波浪蜂值
     mWaveHight=mViewHeight/20;  //暂定20
     mWaveLength=getMeasuredWidth();
     //计算所有的点 这里取宽度为整个波长  往左再延伸一个波长 两个波长则需要9个点
     for (int i=0;i<9;i++){
         int y=0;
         switch (i%4){
             case 0:
                 y = mViewHeight;
                 break;
             case 1:
                 y =mViewHeight+ mWaveHight;
                 break;
             case 2:
                 y = mViewHeight;
                 break;
             case 3:
                 y = mViewHeight-mWaveHight;
                 break;
         }
         Point point = new Point(-mWaveLength + i * mWaveLength / 4, y);

         mPoints.add(point);


         }
     /**
      * 计算圆环宽度
      */
     int mIncircleRadius=mViewHeight<mViewWidth?mViewHeight/2:mViewWidth/2;//内切圆半径

     int mcircumcircleRadius= (int) (Math.sqrt((float)(Math.pow(mViewHeight/2,2)+Math.pow(mViewWidth/2,2)))+0.5);//外接圆半径
     int radius=mcircumcircleRadius/2+mIncircleRadius/2;

     rectF=new RectF(mViewWidth/2-radius,mViewHeight/2-radius,mViewWidth/2+radius,mViewHeight/2+radius);
     mStrokeWidth=mcircumcircleRadius-mIncircleRadius;
     mCirclePaint.setStrokeWidth(mStrokeWidth);//线是有宽度的 采用了这种方式画圆环
     isMeasure=true;
     }

 }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 绘制线条
         */
        mPath.reset();
        int i = 0;
        mPath.moveTo(mPoints.get(0).getX()+mLeftWaveMoveLength, mPoints.get(0).getY()-mViewHeight*mProgress/100);
        for (; i < mPoints.size() - 2; i += 2) {
            mPath.quadTo(mPoints.get(i + 1).getX()+mLeftWaveMoveLength, mPoints.get(i + 1).getY()-mViewHeight*mProgress/100, mPoints.get(i + 2).getX()+mLeftWaveMoveLength, mPoints.get(i + 2).getY()-mViewHeight*mProgress/100);
        }
        mPath.lineTo(mPoints.get(i).getX()+mLeftWaveMoveLength, mViewHeight);
        mPath.lineTo(mPoints.get(0).getX()+mLeftWaveMoveLength, mViewHeight);
        mPath.close();
        /**
         * 绘制轨迹
         */

        canvas.drawPath(mPath,mPaint);

        /**
         * 绘制字
         */
        Rect rect=new Rect();
       // String progress=String.format("%d%%",mProgress);
        String progress="兔币余额";
        mTextPaint.getTextBounds(progress,0,progress.length(), rect);
        int textHeight = rect.height();
//      if (mProgress>=50){
////如果进度达到50 颜色变为白色，没办法啊，进度在中间 不变颜色看不到
//            mTextPaint.setColor(Color.WHITE);
//           canvas.drawText(progress,0,progress.length(),mViewWidth/2,mViewHeight/2+textHeight/2,mTextPaint);
//        }else{
            mTextPaint.setColor(Color.BLACK);
            canvas.drawText(progress,0,progress.length(),mViewWidth/2,mViewHeight/(float)2.7,mTextPaint);
       // }
        /**
         * 绘制图标
         */
        matrix=new Matrix();
        int newWidth = 150;
        int newHeight = 150;
        matrix.postScale((float)newWidth/bitmap.getWidth(),(float)newHeight/bitmap.getHeight());
        //新图片
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                true);
        canvas.drawBitmap(newbm,mViewWidth/(float)2.7,20,bitmapPaint);
        /**
         * 绘制兔币余额
         */
        Rect rect2=new Rect();
        String tubiNumText="余额:"+tubiNum;
       mTextPaint.getTextBounds(tubiNumText,0,tubiNumText.length(), rect2);
        tubipaint.setColor(Color.BLACK);
        canvas.drawText(tubiNumText,0,tubiNumText.length(),mViewWidth/2,mViewHeight/2,tubipaint);

        /**
         * 判断是否为圆
         */
        if (isCircle){
            canvas.drawArc(rectF, 0, 360, true, mCirclePaint);
            Paint circlePaint = new Paint();
            circlePaint.setStrokeWidth(5);
            circlePaint.setColor(Color.WHITE);
            circlePaint.setAntiAlias(true);
            circlePaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(mViewWidth / 2, mViewHeight / 2, mViewHeight / 2, circlePaint);
            /**
             * 绘制边界
             */
            mBorderPaint.setStrokeWidth(mBorderWidth/2);
            canvas.drawCircle(mViewWidth/2,mViewHeight/2,mViewHeight/2-mBorderWidth/2,mBorderPaint);
        }else {
            /**
             * 绘制矩形边框
             */
            canvas.drawRect(0,0,mViewWidth,mViewHeight,mBorderPaint);
        }
        //
        handler.sendEmptyMessageDelayed(0,mTimeStep);



    }

 
    /**
     * 设置进度 基准线
     * @param mProgress
     */
    public void setmProgress(int mProgress) {
        this.mProgress = mProgress;
        mLevelLine=(100-mProgress)*mViewHeight/100;
    }
    public void setmTubiNum(String tubiNum){
        this.tubiNum=tubiNum;
    }

    /**
     * 设置是否为圆形
     * @param circle
     */
    public void setCircle(boolean circle) {
        isCircle = circle;
    }

}
