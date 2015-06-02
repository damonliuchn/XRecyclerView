package net.masonliu.xrecycleview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liumeng on 4/26/15.
 */
public abstract class XRecyclerViewAdapter<VH extends XRecyclerViewHolder> extends RecyclerView.Adapter<XRecyclerViewHolder> implements XRecyclerItemClickListener {

    private XRecyclerItemClickListener xRecyclerItemClickListener;

    public XRecyclerItemClickListener getxRecyclerItemClickListener() {
        return xRecyclerItemClickListener;
    }

    public void setxRecyclerItemClickListener(XRecyclerItemClickListener xRecyclerItemClickListener) {
        this.xRecyclerItemClickListener = xRecyclerItemClickListener;
    }

    @Override
    public void onWrappedItemClick(View view, int position) {
        if (xRecyclerItemClickListener != null) {
            xRecyclerItemClickListener.onWrappedItemClick(view, wrapPosition(position));
        }
        onWrappedItemClickChild(view, wrapPosition(position));
    }

    public abstract void onWrappedItemClickChild(View view, int wrappedPosition);

    public abstract VH onCreateWrappedViewHolderChild(ViewGroup parent, int viewType);

    public abstract void onBindWrappedViewHolderChild(VH holder, int wrappedPosition);

    /**
     * @return The item count in the underlying adapter
     */
    public abstract int getWrappedItemCount();

    public abstract int getWrappedItemViewType(int wrappedPosition);

    ///////////////////////////////////////////////////////////
    private static final int HEADERS_START = Integer.MIN_VALUE;
    private static final int FOOTERS_START = Integer.MIN_VALUE + 100;


    private List<View> mHeaderViews = new ArrayList<>();
    private List<View> mFooterViews = new ArrayList<>();

    @Override
    public int getItemViewType(int position) {
        int hCount = getHeaderCount();
        int wrappedItemCount = getWrappedItemCount();
        if (position < hCount) {
            return HEADERS_START + position;
        } else if (position < hCount + wrappedItemCount) {
            return getWrappedItemViewType(wrapPosition(position));
        } else {
            return FOOTERS_START + (position - hCount - wrappedItemCount);
        }
    }

    @Override
    public XRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType < HEADERS_START + getHeaderCount())
            return new StaticViewHolder(mHeaderViews.get(viewType - HEADERS_START));
        else if (viewType < FOOTERS_START + getFooterCount())
            return new StaticViewHolder(mFooterViews.get(viewType - FOOTERS_START));
        else {
            return onCreateWrappedViewHolderChild(viewGroup, viewType);
        }
    }

    @Override
    public void onBindViewHolder(XRecyclerViewHolder viewHolder, int position) {
        int hCount = getHeaderCount();
        if (position >= hCount && position < hCount + getWrappedItemCount())
            onBindWrappedViewHolderChild((VH) viewHolder, wrapPosition(position));
    }

    /**
     * Add a static view to appear at the start of the RecyclerView. Headers are displayed in the
     * order they were added.
     *
     * @param view The header view to add
     */
    public void addHeaderView(View view) {
        mHeaderViews.add(view);
    }

    /**
     * Add a static view to appear at the end of the RecyclerView. Footers are displayed in the
     * order they were added.
     *
     * @param view The footer view to add
     */
    public void addFooterView(View view) {
        if(haveLoadMoreView){
            mFooterViews.add(mFooterViews.size()-1,view);
        }else{
            mFooterViews.add(view);
        }
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getFooterCount() + getWrappedItemCount();
    }

    public int wrapPosition(int position) {
        return position - getHeaderCount();
    }

    /**
     * @return The number of header views added
     */
    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    /**
     * @return The number of footer views added
     */
    public int getFooterCount() {
        return mFooterViews.size();
    }

    private static class StaticViewHolder extends XRecyclerViewHolder {

        public StaticViewHolder(View itemView) {
            super(itemView, null);
        }
    }

    public void notifyWrappedItemRangeChanged(int wrappedPositionStart, int itemCount) {
        notifyItemRangeChanged(wrappedPositionStart + getHeaderCount(), itemCount);
    }

    public void notifyWrappedItemRangeInserted(int wrappedPositionStart, int itemCount) {
        notifyItemRangeInserted(wrappedPositionStart + getHeaderCount(), itemCount);
    }

    public void notifyWrappedItemRangeRemoved(int wrappedPositionStart, int itemCount) {
        notifyItemRangeRemoved(wrappedPositionStart + getHeaderCount(), itemCount);
    }

    //loadmore

    protected XRecyclerView recyclerView;
    protected boolean haveLoadMoreView;

    public void addEndlessView(XRecyclerView recyclerView,View view,boolean isAutoLoadMore){
        this.recyclerView = recyclerView;
        if(isAutoLoadMore){
            recyclerView.setOnLoadMoreScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                    //lastVisibleItem >= totalItemCount - 1 表示剩下1个item自动加载
                    // dy>0 表示向下滑动
                    if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                        onLoadMore();
                    }
                }
            });
        }
        addFooterView(view);
        haveLoadMoreView = true;
    }

    public abstract void onLoadMore();

    public void removeEndlessView(){
        if(recyclerView!=null){
            recyclerView.setOnLoadMoreScrollListener(null);
        }
        if(haveLoadMoreView){
            mFooterViews.remove(mFooterViews.size()-1);
            haveLoadMoreView = false;
        }
    }

    public View getEndlessView(){
        if(haveLoadMoreView){
            return mFooterViews.get(mFooterViews.size() - 1);
        }
        return null;
    }
}
