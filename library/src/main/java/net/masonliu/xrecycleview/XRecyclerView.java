package net.masonliu.xrecycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by liumeng on 4/29/15.
 */
public class XRecyclerView extends RecyclerView {
    public XRecyclerView(Context context) {
        super(context);
        init();
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        setOnScrollListener(null);
    }

    private OnScrollListener onLoadMoreScrollListener;

    @Override
    public void setOnScrollListener(final OnScrollListener listener) {
        super.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(listener!=null){
                    listener.onScrolled(recyclerView, dx, dy);
                }
                if(getOnLoadMoreScrollListener()!=null){
                    getOnLoadMoreScrollListener().onScrolled(recyclerView, dx, dy);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(listener!=null){
                    listener.onScrollStateChanged(recyclerView, newState);
                }
                if(getOnLoadMoreScrollListener()!=null){
                    getOnLoadMoreScrollListener().onScrollStateChanged(recyclerView, newState);
                }
            }
        });
    }

    public OnScrollListener getOnLoadMoreScrollListener() {
        return onLoadMoreScrollListener;
    }

    public void setOnLoadMoreScrollListener(OnScrollListener onLoadMoreScrollListener) {
        this.onLoadMoreScrollListener = onLoadMoreScrollListener;
    }
}
