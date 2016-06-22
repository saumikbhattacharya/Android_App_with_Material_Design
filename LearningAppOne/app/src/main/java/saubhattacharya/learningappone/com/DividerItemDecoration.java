package saubhattacharya.learningappone.com;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    Context context;
    int orientation;
    Drawable drawable;
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    private static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    public DividerItemDecoration(Context context, int orientation)
    {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        drawable = a.getDrawable(0);
        a.recycle();
        this.orientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView view, RecyclerView.State state)
    {
        if(orientation == VERTICAL_LIST)
        {
            drawVertical(c,view);
        }
        else
        {
            drawHorizontal(c,view);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView view) {
        int top = view.getPaddingTop();
        int bottom = view.getHeight() - view.getPaddingBottom();
        int childCount = view.getChildCount();
        for(int i = 0; i<childCount; i++)
        {
            View child = view.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = view.getRight() + params.rightMargin;
            int right = left + drawable.getIntrinsicHeight();
            drawable.setBounds(left,top,right,bottom);
            drawable.draw(c);
        }
    }

    private void drawVertical(Canvas c, RecyclerView view) {
        int left = view.getPaddingLeft();
        int right = view.getWidth() - view.getPaddingRight();
        int childCount = view.getChildCount();
        for(int i = 0; i<childCount; i++)
        {
            View child = view.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = view.getBottom() + params.bottomMargin;
            int bottom = top + drawable.getIntrinsicHeight();
            drawable.setBounds(left,top,right,bottom);
            drawable.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (orientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, drawable.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, drawable.getIntrinsicWidth(), 0);
        }
    }
}
