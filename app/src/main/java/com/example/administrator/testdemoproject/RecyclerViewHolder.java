package com.example.administrator.testdemoproject;

import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> mViews;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        this.mViews = new SparseArray<>();
    }


    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = this.itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }

        return (T) view;
    }

    public View getConvertView() {
        return itemView;
    }

    public RecyclerViewHolder setTextColorStateList(int viewId, ColorStateList color) {
        TextView view = getView(viewId);
        view.setTextColor(color);
        return this;
    }

    /**
     * 为TextView设置字符串
     */
    public RecyclerViewHolder setText(int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为TextView设置字符串
     */
    public RecyclerViewHolder setProgress(int viewId, int max, int progress) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    /**
     * 为标签多行文本换行设置字符串
     */
//    public RecyclerViewHolder setMultiText(int viewId, List<String> texts) {
//        MultipleTextView view = getView(viewId);
//        view.setTextViews(texts);
//        return this;
//    }

    public RecyclerViewHolder setText(int viewId, int textRes) {
        TextView view = getView(viewId);
        view.setText(textRes);
        return this;
    }

    public RecyclerViewHolder setTextColor(int viewId, int color) {
        TextView view = getView(viewId);
        view.setTextColor(color);
        return this;
    }

    public RecyclerViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        getView(viewId).setOnClickListener(listener);
        return this;
    }

    public RecyclerViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    public RecyclerViewHolder setButton(int viewId, String text, int resId) {
        Button view = getView(viewId);
        view.setBackgroundResource(resId);
        view.setText(text);
        return this;
    }

    public RecyclerViewHolder setButton(int viewId, String text, int resId, View.OnClickListener lsn) {
        Button view = getView(viewId);
        view.setBackgroundResource(resId);
        view.setText(text);
        view.setOnClickListener(lsn);
        return this;
    }

    public CheckBox getCheckBox(int viewid) {
        return getView(viewid);
    }

    public RecyclerViewHolder setCheckBox(int viewId) {
        CheckBox view = getView(viewId);
        view.toggle();
        return this;
    }

    public RecyclerViewHolder setCheckBox(int viewId, boolean isCheck) {
        CheckBox view = getView(viewId);
        view.setChecked(isCheck);
        return this;
    }




    public RecyclerViewHolder setViewVisablity(int viewId, int visablity) {
        getView(viewId).setVisibility(visablity);
        return this;
    }

    public RecyclerViewHolder setBackGround(int viewId, int resId) {
        getView(viewId).setBackgroundResource(resId);
        return this;
    }

    public RecyclerViewHolder setMovementMethod(int viewId) {
        ((TextView) getView(viewId)).setMovementMethod(LinkMovementMethod.getInstance());
        return this;
    }


}
