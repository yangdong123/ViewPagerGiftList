package com.example.administrator.testdemoproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private List<String> dataList1 = null;
    private List<String> dataList2 = null;
    private List<String> dataList3 = null;
    private List<String> dataList4 = null;
    private List<String> dataList5 = null;
    private List<List<String>> lists = null; //存放切割数据后的集合
    private ViewPager pager;
    private LinearLayout llPointLayout;
    private List<PointBean> pointList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        pager = findViewById(R.id.pager);
        llPointLayout = findViewById(R.id.ll_point_layout);
        initData();
        initPagerData();
    }

    private void initData() {
        lists = new ArrayList<>();


        dataList1 = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            dataList1.add(String.valueOf(i) + " a");
        }
        dataList2 = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            dataList2.add(String.valueOf(i) + " b");
        }
        dataList3 = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            dataList3.add(String.valueOf(i) + " c");
        }
        dataList4 = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            dataList4.add(String.valueOf(i) + " d");
        }
        dataList5 = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            dataList5.add(String.valueOf(i) + " f");
        }


    }

    int pagerIndex = 0;

    private void initPagerData() {
        pointList = new ArrayList<>();

        final int page1 = getPageSum(dataList1.size(), 10); // 10是每页显示数据个数
        int page2 = getPageSum(dataList2.size(), 10);
        int page3 = getPageSum(dataList3.size(), 10);
        int page4 = getPageSum(dataList4.size(), 10);
        int page5 = getPageSum(dataList5.size(), 10);
        final int totalPageSize = page1 + page2 + page3 + page4 + page5;//计算出ViewPager 总页数
        //切割每页显示数据
        List<List<String>> lists1 = partList(dataList1, 10);
        List<List<String>> lists2 = partList(dataList2, 10);
        List<List<String>> lists3 = partList(dataList3, 10);
        List<List<String>> lists4 = partList(dataList4, 10);
        List<List<String>> lists5 = partList(dataList5, 10);

        for (int i = 0; i < lists1.size(); i++) {
            processData(lists1, i);
        }
        for (int i = 0; i < lists2.size(); i++) {
            processData(lists2, i);
        }
        for (int i = 0; i < lists3.size(); i++) {
            processData(lists3, i);
        }
        for (int i = 0; i < lists4.size(); i++) {
            processData(lists4, i);
        }
        for (int i = 0; i < lists5.size(); i++) {
            processData(lists5, i);
        }

        MyAdapter adapter = new MyAdapter(this, this.lists, totalPageSize);
        pager.setAdapter(adapter);
        updatePoint(page1, 0);


        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (pointList.size() > 0) {
                    PointBean pointBean = pointList.get(position);
                    updatePoint(pointBean.points, pointBean.pagerIndex);
                    Log.e("yd", " position  == " + position + "  points == " + pointBean.points + "  pagerIndex== " + pointBean.pagerIndex);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void processData(List<List<String>> list, int index) {
        lists.add(list.get(index));

        //设置每页点的属性
        PointBean pointBean = new PointBean();
        if (index == 0) {
            pointBean.isNextPage = true;
        }
        pointBean.points = list.size();
        pointBean.pagerIndex = index;
        pointList.add(pointBean);
    }

    /**
     * @param rows     总条数
     * @param pageSize 每页显示条数
     * @return
     */
    private int getPageSum(int rows, int pageSize) {
        return (rows - 1) / pageSize + 1;
    }


    /**切割集合
     * @param source
     * @param n      每页显示个数
     */
    public static <T> List<List<T>> partList(List<T> source, int n) {

        if (source == null) {
            return null;
        }

        if (n == 0) {
            return null;
        }
        List<List<T>> result = new ArrayList<List<T>>();
        // 集合长度
        int size = source.size();
        // 余数
        int remaider = size % n;
        System.out.println("余数:" + remaider);
        // 商
        int number = size / n;
        System.out.println("商:" + number);

        for (int i = 0; i < number; i++) {
            List<T> value = source.subList(i * n, (i + 1) * n);
            result.add(value);
        }

        if (remaider > 0) {
            List<T> subList = source.subList(size - remaider, size);
            result.add(subList);
        }
        return result;
    }

    /**
     * 更新点个数和焦点
     * @param ponits
     * @param selectIndex
     */
    private void updatePoint(int ponits, int selectIndex) {
        llPointLayout.removeAllViews();
        for (int i = 0; i < ponits; i++) {
            View view = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.rightMargin = 20;
            view.setLayoutParams(params);
            if (i == selectIndex) {
                view.setBackgroundResource(R.drawable.viewpager_point_p_shape);
            } else {
                view.setBackgroundResource(R.drawable.viewpager_point_shape);
            }
            llPointLayout.addView(view);
        }
    }


}