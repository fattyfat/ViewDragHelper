package fatty.a98viewdraghelper;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Fatty on 2017-02-21.
 */

public class ViewGroup extends FrameLayout {

    private ViewDragHelper mViewDragHelper;
    private View mMenuView, mMainView;
    private int mWidth;

    public ViewGroup(Context context){
        super(context);
        initView();
    }

    public ViewGroup(Context context, AttributeSet attrs){
        super(context ,attrs);
        initView();
    }

    public ViewGroup(Context context, AttributeSet attrs, int defStyleAttr){
        super(context ,attrs ,defStyleAttr);
        initView();
    }

    @Override
    protected void onFinishInflate(){

        super.onFinishInflate();
        mMenuView = getChildAt(0);
        mMainView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w ,h ,oldw ,oldh);
        mWidth = mMenuView.getMeasuredWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){

        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        //將觸摸事件傳遞給ViewDragHelper，此操作必不可少
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    private void initView(){

        mViewDragHelper = ViewDragHelper.create(this, callback);
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback(){
        //何時開始檢測觸摸事件
        @Override
        public boolean tryCaptureView(View child, int pointerID){
            //如果當前觸摸的child是mMainView時開始檢測
            return mMainView == child;
        }

        //處理垂直滑動
        @Override
        public int clampViewPositionVertical(View child, int top, int dy){
            return 0;
        }

        //處理水平滑動
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx){
            return left;
        }

        //拖動結束後調用
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel){

            super.onViewReleased(releasedChild ,xvel ,yvel);
            //手指抬起後緩慢移動到指定位置
            if(mMainView.getLeft() < 500) {
                //關閉menu
                //相當於Scroller的startScroll方法
                mViewDragHelper.smoothSlideViewTo(mMainView, 0, 0);
                ViewCompat.postInvalidateOnAnimation(ViewGroup.this);
            } else {
                //打開menu
                //mViewDragHelper.smoothSlideViewTo(mMenuView ,300 , 0);
                mViewDragHelper.smoothSlideViewTo(mMainView ,300 , 0);
                ViewCompat.postInvalidateOnAnimation(ViewGroup.this);
            }
        }
    };

    @Override
    public void computeScroll(){
        if (mViewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
