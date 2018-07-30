package net.masonliu.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.masonliu.xrecycleview.XRecyclerItemClickListener;
import net.masonliu.xrecycleview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        XRecyclerView recyclerView = (XRecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ListAdapter mAdapter = new ListAdapter(getData(), MainActivity.this);
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

        TextView endlessView = (TextView) LayoutInflater.from(this).inflate(android.R.layout.simple_list_item_1, null, false);
        endlessView.setText("endlessView");
        mAdapter.addEndlessView(recyclerView, endlessView, true);
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
