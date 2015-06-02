package net.masonliu.xrecycleview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by liumeng on 4/29/15.
 */
public abstract class XRecyclerViewHolder extends RecyclerView.ViewHolder {

    public XRecyclerViewHolder(View itemView, final XRecyclerItemClickListener xRecyclerItemClickListener) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xRecyclerItemClickListener.onWrappedItemClick(v, getPosition());
            }
        });
    }
}
