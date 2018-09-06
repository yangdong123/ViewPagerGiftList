package com.example.administrator.testdemoproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private List<List<String>> lists = null;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        pager = findViewById(R.id.pager);
        initData();
        initPagerData();
    }

    private void initData() {
        lists = new ArrayList<>();

        dataList1 = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            dataList1.add(String.valueOf(i)+ " a");
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


    private void initPagerData() {
        int page1 = getPageSum(dataList1.size(), 10);
        int page2 = getPageSum(dataList2.size(), 10);
        int page3 = getPageSum(dataList3.size(), 10);
        int page4 = getPageSum(dataList4.size(), 10);
        int page5 = getPageSum(dataList5.size(), 10);
        int totalPageSize = page1 + page2 + page3 + page4 + page5;//计算出ViewPager 总页数

        List<List<String>> lists1 = partList(dataList1, 10);
        List<List<String>> lists2 = partList(dataList2, 10);
        List<List<String>> lists3 = partList(dataList3, 10);
        List<List<String>> lists4 = partList(dataList4, 10);
        List<List<String>> lists5 = partList(dataList5, 10);

        for (int i = 0; i <lists1.size() ; i++) {
            lists.add(lists1.get(i));
        }
        for (int i = 0; i <lists2.size() ; i++) {
            lists.add(lists2.get(i));
        }
        for (int i = 0; i <lists3.size() ; i++) {
            lists.add(lists3.get(i));
        }
        for (int i = 0; i <lists4.size() ; i++) {
            lists.add(lists4.get(i));
        }
        for (int i = 0; i <lists5.size() ; i++) {
            lists.add(lists5.get(i));
        }

        MyAdapter adapter = new MyAdapter(this, this.lists, totalPageSize);
        pager.setAdapter(adapter);
    }


    /**
     * @param rows     总条数
     * @param pageSize 每页显示条数
     * @return
     */
    private int getPageSum(int rows, int pageSize) {
        return (rows - 1) / pageSize + 1;
    }


/**
 * @param source
 * @param n     每页显示个数
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

}