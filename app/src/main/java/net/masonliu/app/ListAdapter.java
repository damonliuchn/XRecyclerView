package net.masonliu.app;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.masonliu.xrecycleview.XRecyclerItemClickListener;
import net.masonliu.xrecycleview.XRecyclerViewAdapter;
import net.masonliu.xrecycleview.XRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liumeng on 4/26/15.
 */
public class ListAdapter extends XRecyclerViewAdapter<ListAdapter.MyViewHolder> {

    private List<String> mDataset;

    public ListAdapter(List<String> dataset, Context context) {
        mDataset = dataset;
    }

    public void setmDateSet(List<String> dataset) {
        mDataset = dataset;
        notifyDataSetChanged();
    }

    public List<String> getmDataset() {
        return mDataset;
    }

    @Override
    public void onWrappedItemClickChild(View view, int wrappedPosition) {

    }

    @Override
    public MyViewHolder onCreateWrappedViewHolderChild(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_item, parent, false);
        return new MyViewHolder(view, this);
    }

    @Override
    public void onBindWrappedViewHolderChild(MyViewHolder holder, int wrappedPosition) {
        holder.mTextView.setText(mDataset.get(wrappedPosition));
    }

    @Override
    public int getWrappedItemCount() {
        return mDataset.size();
    }

    @Override
    public int getWrappedItemViewType(int wrappedPosition) {
        return 0;
    }

    @Override
    public void onLoadMore() {
        Log.e("loadmore","loadmore");
        //模拟网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Handler mHandler=new Handler(Looper.getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        getmDataset().addAll(getData());
                        notifyDataSetChanged();
                        autoLoadingFinish();
                    }
                });
            }
        }).start();
    }


    private List<String> getData() {
        List<String> datas = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            datas.add("load" + i);
        }
        return datas;
    }

    public class MyViewHolder extends XRecyclerViewHolder {

        public TextView mTextView;

        public MyViewHolder(View itemView, XRecyclerItemClickListener okRecyclerItemClickListener) {
            super(itemView, okRecyclerItemClickListener);
            mTextView = (TextView) itemView.findViewById(R.id.text1);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {

                    return false;
                }
            });
        }

    }

}
