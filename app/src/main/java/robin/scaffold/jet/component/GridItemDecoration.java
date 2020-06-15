package robin.scaffold.jet.component;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexLine;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.List;


public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable verticalDivider;
    private Drawable horizontalDivider;
    private int spanCount;
    private int verticalSpaceSize;
    private int horizontalSpaceSize;
    private boolean includeVerticalEdge;//是否绘制左右边界，注意不绘制并不表示没有预留边界空间
    private boolean includeHorizontalEdge;//是否绘制上下边界，注意不绘制并不表示没有预留边界空间

    private GridItemDecoration(Builder builder) {
        verticalDivider = builder.verticalDivider;
        horizontalDivider = builder.horizontalDivider;
        spanCount = builder.spanCount;
        verticalSpaceSize = builder.verticalSpaceSize;
        horizontalSpaceSize = builder.horizontalSpaceSize;
        includeVerticalEdge = builder.includeVerticalEdge;
        includeHorizontalEdge = builder.includeHorizontalEdge;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column
        if (verticalSpaceSize > 0) {
            outRect.left = verticalSpaceSize - column * verticalSpaceSize / spanCount;
            outRect.right = (column + 1) * verticalSpaceSize / spanCount;
        } else {
            outRect.left = column * verticalSpaceSize / spanCount;
            outRect.right = verticalSpaceSize - (column + 1) * verticalSpaceSize / spanCount;
        }

        if (horizontalSpaceSize > 0) {
            if (position < spanCount) outRect.top = horizontalSpaceSize; // top edge
            outRect.bottom = horizontalSpaceSize; // item bottom
        } else {
            if (position >= spanCount) outRect.top = horizontalSpaceSize; // item top
        }

        log("确定边界" + outRect.left + "  " + outRect.right + "  " + outRect.top + "  " + outRect.bottom);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        log(toString());

        //在边界上绘制图形
        if (horizontalDivider != null && horizontalSpaceSize > 0) {
            drawHorizontalLineAtItemTop(c, parent);//在每一个item的上面绘制水平分割线
            drawLineAtTopAndBottom(c, parent);//绘制上下边界，包括倒数第二行中的部分item
        }
        if (verticalDivider != null && verticalSpaceSize > 0) {
            drawVerticalLineAtItemLeft(c, parent);//绘制竖直分割线
            if (includeVerticalEdge) drawLR(c, parent);//绘制左右边界
        }
    }

    //在每一个item的上面绘制水平分割线
    private void drawHorizontalLineAtItemTop(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int position = 0; position < childCount; position++) {
            if (position >= spanCount) {//第一行不绘制
                View child = parent.getChildAt(position);
                int left = child.getLeft();
                int right = (position % spanCount == spanCount - 1) ? child.getRight() : (child.getRight() + verticalSpaceSize);
                int top = child.getTop() - horizontalSpaceSize;
                int bottom = top + horizontalSpaceSize;
                log("绘制水平分割线" + left + "  " + right + "  " + top + "  " + bottom);
                horizontalDivider.setBounds(left, top, right, bottom);
                horizontalDivider.draw(c);
            }
        }
    }

    //绘制竖直分割线
    private void drawVerticalLineAtItemLeft(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int position = 0; position < childCount; position++) {//第一列不绘制
            if (position % spanCount != 0) {
                View child = parent.getChildAt(position);
                int left = child.getLeft() - verticalSpaceSize;
                int right = left + verticalSpaceSize;
                int top = child.getTop() /*- horizontalSpaceSize*/;
                int bottom = child.getBottom();
                log("绘制竖直分割线" + left + "  " + right + "  " + top + "  " + bottom);
                verticalDivider.setBounds(left, top, right, bottom);
                verticalDivider.draw(c);
            }
        }
    }

    //绘制左右边界
    private void drawLR(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int position = 0; position < childCount; position++) {
            View child = parent.getChildAt(position);

            int maxLines = (childCount % spanCount == 0) ? childCount / spanCount : (childCount / spanCount + 1);//一共有多少行
            boolean isLastLineItem = position / spanCount == maxLines - 1;//倒数第一行的元素
            //最左边那条线
            if (position % spanCount == 0) {
                int left = child.getLeft() - verticalSpaceSize;
                int right = left + verticalSpaceSize;
                int top = child.getTop() - horizontalSpaceSize;
                int bottom;
                if (isLastLineItem) {//【左下角】
                    bottom = child.getBottom() + horizontalSpaceSize;
                } else {
                    bottom = child.getBottom();
                }
                verticalDivider.setBounds(left, top, right, bottom);
                verticalDivider.draw(c);
                log("绘制最左边那条线" + left + "  " + right + "  " + top + "  " + bottom);
            }

            //最右边那条线
            if ((position + 1) % spanCount == 0) {
                int left = child.getRight();
                int right = left + verticalSpaceSize;
                int top = child.getTop() - horizontalSpaceSize;

                boolean isLastSecondLineItem = (position / spanCount == maxLines - 2) && (position + spanCount >= childCount);//倒数第二行的部分元素
                int bottom;
                if (isLastLineItem || isLastSecondLineItem) {
                    bottom = child.getBottom() + horizontalSpaceSize;//【右下角】
                } else {
                    bottom = child.getBottom();
                }
                verticalDivider.setBounds(left, top, right, bottom);
                verticalDivider.draw(c);
                log("绘制最右边那条线" + left + "  " + right + "  " + top + "  " + bottom);
            }
        }
    }

    //绘制上下边界，包括倒数第二行中的部分item
    private void drawLineAtTopAndBottom(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int position = 0; position < childCount; position++) {
            View child = parent.getChildAt(position);

            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

            if (layoutManager instanceof FlexboxLayoutManager) {
                List<FlexLine> lines = ((FlexboxLayoutManager) layoutManager).getFlexLines();
               // Log.i("loglog", "lines" + lines.size());
                if(lines.size() > 0) {
                    spanCount = lines.get(0).getItemCount();
                }
               // Log.i("loglog", "spanCount" + spanCount);
            } else if(layoutManager instanceof LinearLayoutManager
                    && ((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                spanCount = parent.getAdapter().getItemCount();
                if(spanCount == 0) {
                    spanCount = 1;
                }
            }
            //最上边那条线
            if (includeHorizontalEdge && position < spanCount) {
                int top = child.getTop() - horizontalSpaceSize;
                int bottom = top + horizontalSpaceSize;
                int left = child.getLeft();

                int right;
                boolean isRightEdgeItem = (position + 1) % spanCount == 0;//最右侧的元素
                boolean isFirstLineItem = childCount < spanCount && (position == childCount - 1);//只有一行的元素
                if (isRightEdgeItem || isFirstLineItem) {//【右上角】
                    right = child.getRight();
                } else {
                    right = child.getRight() + verticalSpaceSize;//如果同时有竖直分割线，则需要包含竖直分割线的宽度
                }

                horizontalDivider.setBounds(left, top, right, bottom);
                horizontalDivider.draw(c);
                log("绘制最上边那条线" + left + "  " + right + "  " + top + "  " + bottom);
            }

            //最下边那条线
            int maxLines = (childCount % spanCount == 0) ? childCount / spanCount : (childCount / spanCount + 1);//一共有多少行
            boolean isLastLineItem = position / spanCount == maxLines - 1;//倒数第一行的元素
            boolean isLastSecondLineItem = (position / spanCount == maxLines - 2) && (position + spanCount >= childCount);//倒数第二行的部分元素
            if ((includeHorizontalEdge && isLastLineItem) || isLastSecondLineItem) {
                int top = child.getBottom();
                int bottom = top + horizontalSpaceSize;
                int left = child.getLeft();
                int right;

                boolean isLastSecondLineEdgeItem = isLastSecondLineItem && ((position + 1) % spanCount == 0);//倒数第二行最后一个元素
                if (position == childCount - 1 || isLastSecondLineEdgeItem) {//【右下角】
                    right = child.getRight();
                } else {
                    right = child.getRight() + verticalSpaceSize;//如果同时有竖直分割线，则需要包含竖直分割线的宽度
                }

                horizontalDivider.setBounds(left, top, right, bottom);
                horizontalDivider.draw(c);
                log("绘制最下边那条线" + left + "  " + right + "  " + top + "  " + bottom);
            }
        }
    }

    public static final class Builder {
        private Drawable horizontalDivider;
        private Drawable verticalDivider;
        private int spanCount;
        private int horizontalSpaceSize;
        private int verticalSpaceSize;
        private boolean includeHorizontalEdge;
        private boolean includeVerticalEdge;

        private Builder() {
        }

        public Builder horizontalDivider(Drawable horizontalDivider, int horizontalSpaceSize, boolean includeHorizontalEdge) {
            this.horizontalDivider = horizontalDivider;
            this.horizontalSpaceSize = horizontalSpaceSize;
            this.includeHorizontalEdge = includeHorizontalEdge;
            return this;
        }

        public Builder verticalDivider(Drawable verticalDivider, int verticalSpaceSize, boolean includeVerticalEdge) {
            this.verticalDivider = verticalDivider;
            this.verticalSpaceSize = verticalSpaceSize;
            this.includeVerticalEdge = includeVerticalEdge;
            return this;
        }

        public Builder spanCount(int val) {
            spanCount = val;
            return this;
        }

        public GridItemDecoration build() {
            return new GridItemDecoration(this);
        }
    }

    private void log(String msg) {
       // Log.i("loglog", msg);
    }

}
