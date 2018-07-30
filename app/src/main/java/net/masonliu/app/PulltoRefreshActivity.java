package net.masonliu.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.masonliu.xrecycleview.XRecyclerItemClickListener;
import net.masonliu.xrecycleview.XRecyclerView;
import net.masonliu.xrecycleview.sticky.SimpleTextHeader;
import net.masonliu.xrecycleview.sticky.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


public class PulltoRefreshActivity extends AppCompatActivity {
    private StickyHeaderDecoration mDecoration;
    private PtrClassicFrameLayout mPtrFrame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);

        XRecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ListAdapter mAdapter = new ListAdapter(getData(), PulltoRefreshActivity.this);
        mAdapter.setxRecyclerItemClickListener(new XRecyclerItemClickListener() {
            @Override
            public void onWrappedItemClick(View view, int wrappedPosition) {

            }
        });
        recyclerView.setAdapter(mAdapter);

        TextView footerView = (TextView) LayoutInflater.from(this).inflate(android.R.layout.simple_list_item_1, null, false);
        footerView.setText("footer");
        mAdapter.addFooterView(footerView);

        TextView view = (TextView) LayoutInflater.from(this).inflate(android.R.layout.simple_list_item_1, null, false);
        view.setText("header");
        mAdapter.addHeaderView(view);

        TextView view2 = (TextView) LayoutInflater.from(this).inflate(android.R.layout.simple_list_item_1, null, false);
        view2.setText("header2");
        mAdapter.addHeaderView(view2);

        TextView endlessView = (TextView) LayoutInflater.from(this).inflate(android.R.layout.simple_list_item_1, null, false);
        endlessView.setText("endlessView");
        mAdapter.addEndlessView(recyclerView, endlessView, true);

        //sticky
        mDecoration = new StickyHeaderDecoration(new SimpleTextHeader() {
            @Override
            protected CharSequence getHeaderContent(int childPos) {
                return "Header " + getStickyHeaderId(childPos);
            }

            @Override
            public long getStickyHeaderId(int itemPosition) {
                //不管是 ItemView 还是 ItemDecoration 来实现 Header，正确的数据分组永远是第一步
                //它的 getItemOffsets() 方法。我们组与组之间的间隔设置成为一个 Header 的高度，然后组内的 ItemView 之间的间距是指定的间距值，通常为 1 px 或者 2 px
                Log.e("aaaaaaaaaaaa","ss"+ itemPosition);
                return itemPosition / 14;
            }
        });
        recyclerView.addItemDecoration(mDecoration);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //pull to refresh
        // the following are default settings
        mPtrFrame = findViewById(R.id.store_house_ptr_frame);
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(true);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }
            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        frame.refreshComplete();
                    }
                }, 2000);
            }
        });
    }

    private List<String> getData() {
        List<String> datas = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            datas.add("" + i);
        }
        return datas;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
