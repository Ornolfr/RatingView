package com.github.ornolfr.ratingview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by Ornolfr on 10.06.2016.
 */
public class RatingView extends View implements View.OnTouchListener {

    //Default values
    private static final boolean DEFAULT_IS_INDICATOR = false;

    private static final float DEFAULT_RATING = 3.5f;

    private static final int DEFAULT_MAX_COUNT = 5;

    private static final int DEFAULT_DRAWABLE_SIZE_IN_DP = 32;

    private static final int DEFAULT_DRAWABLE_MARGIN_IN_DP = 4;

    private OnRatingChangedListener mListener;

    //Bitmaps for your rating drawables
    private Bitmap mDrawableEmpty, mDrawableHalf, mDrawableFilled;

    //For drawing view
    private Rect mRect = new Rect();

    //Boolean field: if true - user cannot affect the view
    private boolean mIsIndicator;

    //Float field: displayed rating, 0 <= mRating <= mMaxCount
    private float mRating;

    //Integer field: max drawables count and rating as well
    private int mMaxCount;

    //Integer field: drawable size
    private int mDrawableSize;

    //Integer field: inner margin between drawables
    private int mDrawableMargin;

    /**
     * This interface will return rating value before changing it and after
     */
    public interface OnRatingChangedListener {
        void onRatingChange(float oldRating, float newRating);
    }


    public RatingView(Context context) {
        super(context);
        init(null, 0, 0);
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public RatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RatingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = getContext().obtainStyledAttributes
                (attrs, R.styleable.RatingView, defStyleAttr, defStyleRes);

        mDrawableMargin = (int) a.getDimension(R.styleable.RatingView_drawable_margin,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_DRAWABLE_MARGIN_IN_DP, getResources().getDisplayMetrics()));

        mDrawableSize = (int) a.getDimension(R.styleable.RatingView_drawable_size,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_DRAWABLE_SIZE_IN_DP, getResources().getDisplayMetrics()));

        if (mDrawableSize < 0)
            throw new IllegalArgumentException("Drawable size < 0");

        mMaxCount = a.getInteger(R.styleable.RatingView_max_count, DEFAULT_MAX_COUNT);

        if (mMaxCount < 1)
            throw new IllegalArgumentException("Max count < 1");

        mRating = a.getFloat(R.styleable.RatingView_rating, DEFAULT_RATING);

        mIsIndicator = a.getBoolean(R.styleable.RatingView_is_indicator, DEFAULT_IS_INDICATOR);

        mDrawableEmpty = BitmapFactory.decodeResource(getContext().getResources(),
                a.getResourceId(R.styleable.RatingView_drawable_empty, R.drawable.ic_star_empty));

        mDrawableHalf = BitmapFactory.decodeResource(getContext().getResources(),
                a.getResourceId(R.styleable.RatingView_drawable_half, R.drawable.ic_star_half));

        mDrawableFilled = BitmapFactory.decodeResource(getContext().getResources(),
                a.getResourceId(R.styleable.RatingView_drawable_filled, R.drawable.ic_star_filled));

        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveSize((mDrawableSize * mMaxCount) + (mDrawableMargin * (mMaxCount - 1))/* + getPaddingLeft() + getPaddingRight()*/, widthMeasureSpec),
                resolveSize(mDrawableSize, heightMeasureSpec)/* + getPaddingBottom() + getPaddingTop()*/);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mIsIndicator)
            setOnTouchListener(this);
//        canvas.translate(getPaddingLeft(), getPaddingTop());
        if (mDrawableFilled != null && mDrawableHalf != null && mDrawableEmpty != null) {
            //set view size
            mRect.set(0, 0, mDrawableSize, mDrawableSize);

            int fullDrawablesCount = (int) mRating;
            int emptyDrawablesCount = mMaxCount - Math.round(mRating);

            if (mRating - fullDrawablesCount >= 0.75f)
                fullDrawablesCount++;

            //drawing full drawables
            for (int i = 0; i < fullDrawablesCount; i++) {
                canvas.drawBitmap(mDrawableFilled, null, mRect, null);
                mRect.offset(mDrawableSize + mDrawableMargin, 0);
            }

            //drawing half drawable if needed
            if (mRating - fullDrawablesCount >= 0.25f && mRating - fullDrawablesCount < 0.75f) {
                canvas.drawBitmap(mDrawableHalf, null, mRect, null);
                mRect.offset(mDrawableSize + mDrawableMargin, 0);
            }

            //drawing empty drawables
            for (int i = 0; i < emptyDrawablesCount; i++) {
                canvas.drawBitmap(mDrawableEmpty, null, mRect, null);
                mRect.offset(mDrawableSize + mDrawableMargin, 0);
            }

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                setRating(Math.round(event.getX() / getWidth() * mMaxCount + 0.5));
                return false;
            default: //do nothing
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Sets OnRatingChangedListener on this view, which will give you old and new rating.
     *
     * @param listener your listener
     */
    public void setOnRatingChangedListener(OnRatingChangedListener listener) {
        mListener = listener;
    }

    /**
     * Sets whether rating view is isIndicator or not
     *
     * @param isIndicator boolean, true - user can't interact the view
     */
    public void setIsIndicator(boolean isIndicator) {
        mIsIndicator = isIndicator;
        setOnTouchListener(mIsIndicator ? null : this);
    }

    /**
     * Sets the rating of this view
     *
     * @param rating custom rating
     */
    public void setRating(float rating) {
        float newRating = rating;
        if (newRating < 0) {
            newRating = 0;
        } else if (newRating > mMaxCount) {
            newRating = mMaxCount;
        }
        if (mListener != null)
            mListener.onRatingChange(mRating, newRating);
        mRating = newRating;
        invalidate();
    }

    /**
     * Sets empty drawable
     *
     * @param drawableEmpty bitmap of your drawable
     */
    public void setDrawableEmpty(Bitmap drawableEmpty) {
        this.mDrawableEmpty = drawableEmpty;
        invalidate();
    }

    /**
     * Sets half drawable
     *
     * @param drawableHalf bitmap of your drawable
     */
    public void setDrawableHalf(Bitmap drawableHalf) {
        this.mDrawableHalf = drawableHalf;
        invalidate();
    }

    /**
     * Sets filled drawable
     *
     * @param drawableFilled bitmap of your drawable
     */
    public void setDrawableFilled(Bitmap drawableFilled) {
        this.mDrawableFilled = drawableFilled;
        invalidate();
    }

    /**
     * @return is it indicator or not
     */
    public boolean isIndicator() {
        return mIsIndicator;
    }

    /**
     * @return current rating
     */
    public float getRating() {
        return mRating;
    }

    /**
     * @return current max count
     */
    public int getMaxCount() {
        return mMaxCount;
    }

    /**
     * @return drawable size in px
     */
    public int getDrawableSize() {
        return mDrawableSize;
    }

    /**
     * @return drawable margin in px
     */
    public int getDrawableMargin() {
        return mDrawableMargin;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.mRating = mRating;
        savedState.mIndicator = mIsIndicator;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            super.onRestoreInstanceState(savedState.getSuperState());
            mRating = savedState.mRating;
            mIsIndicator = savedState.mIndicator;
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    static class SavedState extends BaseSavedState {

        float mRating;
        boolean mIndicator;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.mRating = in.readFloat();
            this.mIndicator = in.readInt() == 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(this.mRating);
            out.writeInt(this.mIndicator ? 1 : 0);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
