package com.example.administrator.testdemoproject;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

class MyAdapter extends PagerAdapter {
    PagingScrollHelper scrollHelper = new PagingScrollHelper();
    private Context context;
    private LayoutInflater layoutInflater;
    private List<List<String>> list;
    private int totalPageSize;

    public MyAdapter(Context context,List<List<String>> list,int totalPageSize) {
        this.context = context;
        this.list = list;
        this.totalPageSize = totalPageSize;
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return totalPageSize;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_viewpager, null);
        RecyclerView recycle_view = (RecyclerView) view.findViewById(R.id.recycle_view);
        scrollHelper.setUpRecycleView(recycle_view);
        setRecycleViewData(recycle_view,position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void setRecycleViewData(RecyclerView recycleView,int position) {
        HorizontalPageLayoutManager layoutManager =new HorizontalPageLayoutManager(2,5);
        recycleView.setLayoutManager(layoutManager);
        CommonSingleTypeAdapter adapter = new CommonSingleTypeAdapter<String>(context,R.layout.item,layoutManager) {

            @Override
            public void convert(final RecyclerViewHolder helper, final String item) {
                helper.setText(R.id.text,item);
                helper.setOnClickListener(R.id.text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context," item "  + item  +"  " +helper.getAdapterPosition()  ,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        List<String> list1 = list.get(position);

        adapter.addmDatas(list1);
        recycleView.setAdapter(adapter);
    }

}