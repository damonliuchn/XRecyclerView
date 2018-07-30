package net.masonliu.xrecycleview.sticky;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * https://blog.csdn.net/briblue/article/details/70211942
 */
@SuppressWarnings("WeakerAccess")
public class StickyHeaderDecoration extends RecyclerView.ItemDecoration {

    private LongSparseArray<RecyclerView.ViewHolder> mHeaderCache;

    private StickyHeaderAdapter mAdapter;

    /**
     * @param adapter the sticky header adapter to use
     */
    public StickyHeaderDecoration(StickyHeaderAdapter adapter) {
        mAdapter = adapter;
        mHeaderCache = new LongSparseArray<>();
    }

    /**
     * Clear header cache. This is useful for adapter data set changes.
     */
    public void invalidate() {
        mHeaderCache.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        int position = parent.getChildAdapterPosition(view);
        int headerHeight = 0;

        if (position != RecyclerView.NO_POSITION
                && hasStickyHeaderInThatGroup(position) && firstPositionInThatGroup(position)) {
            headerHeight = getStickyHeader(parent, position).itemView.getHeight();
        }

        outRect.set(0, headerHeight, 0, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        final int count = parent.getChildCount();
        long previousHeaderId = -1;

        for (int layoutPos = 0; layoutPos < count; layoutPos++) {
            final View child = parent.getChildAt(layoutPos);
            final int adapterPos = parent.getChildAdapterPosition(child);

            if (adapterPos != RecyclerView.NO_POSITION && hasStickyHeaderInThatGroup(adapterPos)) {
                long headerId = mAdapter.getStickyHeaderId(adapterPos);

                if (headerId != previousHeaderId) {
                    View header = getStickyHeader(parent, adapterPos).itemView;
                    canvas.save();

                    final int left = child.getLeft();
                    final int top = getStickyHeaderTop(parent, child, header, adapterPos, layoutPos);
                    canvas.translate(left, top);

                    header.setTranslationX(left);
                    header.setTranslationY(top);
                    header.draw(canvas);
                    canvas.restore();
                    previousHeaderId = headerId;
                }
            }
        }
    }

    /**
     * 该分组是否要显示stickyheader
     */
    public boolean hasStickyHeaderInThatGroup(int itemPosition) {
        return mAdapter.getStickyHeaderId(itemPosition) != StickyHeaderAdapter.NO_HEADER;
    }

    /**
     * 在item所在分组里，该item是否是第一个位置
     */
    protected boolean firstPositionInThatGroup(int itemPosition) {
        if (itemPosition == 0) {
            return true;
        }
        return mAdapter.getStickyHeaderId(itemPosition) != mAdapter.getStickyHeaderId(itemPosition - 1);
    }


    private RecyclerView.ViewHolder getStickyHeader(RecyclerView parent, int adapterPosition) {
        final long key = mAdapter.getStickyHeaderId(adapterPosition);

        RecyclerView.ViewHolder holder = mHeaderCache.get(key);
        if (holder == null) {
            holder = mAdapter.onCreateStickyHeaderHolder(parent);
            final View header = holder.itemView;

            //noinspection unchecked
            mAdapter.onBindStickyHeaderHolder(holder, adapterPosition);

            int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(), View
                    .MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getMeasuredHeight(), View
                    .MeasureSpec.UNSPECIFIED);

            int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                    parent.getPaddingLeft() + parent.getPaddingRight(), header.getLayoutParams()
                            .width);
            int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                    parent.getPaddingTop() + parent.getPaddingBottom(), header.getLayoutParams()
                            .height);

            header.measure(childWidth, childHeight);
            header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());

            mHeaderCache.put(key, holder);
        }

        return holder;
    }

    private int getStickyHeaderTop(RecyclerView parent, View child, View header, int adapterPos, int layoutPos) {
        int headerHeight = header.getHeight();
        int top = ((int) child.getY()) - headerHeight;
        if (layoutPos == 0) {
            final int count = parent.getChildCount();
            final long currentId = mAdapter.getStickyHeaderId(adapterPos);
            // find next view with header and compute the offscreen push if needed
            for (int i = 1; i < count; i++) {
                int adapterPosHere = parent.getChildAdapterPosition(parent.getChildAt(i));
                if (adapterPosHere != RecyclerView.NO_POSITION) {
                    long nextId = mAdapter.getStickyHeaderId(adapterPosHere);
                    if (nextId != currentId) {
                        final View next = parent.getChildAt(i);
                        final int offset = ((int) next.getY()) - (headerHeight + getStickyHeader
                                (parent, adapterPosHere).itemView.getHeight());
                        if (offset < 0) {
                            return offset;
                        } else {
                            break;
                        }
                    }
                }
            }

            top = Math.max(0, top);
        }

        return top;
    }

}
