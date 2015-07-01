package net.masonliu.xrecycleview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by liumeng on 4/29/15.
 */
public abstract class XRecyclerViewHolder extends RecyclerView.ViewHolder {

    public XRecyclerViewHolder(View itemView, final XRecyclerViewAdapter xRecyclerViewAdapter) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (xRecyclerViewAdapter != null) {
                    xRecyclerViewAdapter.onWrappedItemClickInXRecyclerViewAdapter(v, getPosition());
                }
            }
        });
    }
}
