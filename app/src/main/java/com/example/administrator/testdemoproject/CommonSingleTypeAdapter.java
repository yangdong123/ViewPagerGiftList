package com.example.administrator.testdemoproject;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public abstract class CommonSingleTypeAdapter<E> extends RecyclerView.Adapter<RecyclerViewHolder> {

    public static final int VIEW_TYPES_HEADER = 99930;
    public static final int VIEW_TYPES_FOOTER = 99931;
    public static final int VIEW_TYPES_LOADMODE = 99932;

    protected final int DEFAULT_VALUE = -1;
    private final RecyclerView.LayoutManager mLayoutManager;
    public List<E> mTypeUser;
    protected View customHeaderView = null;
    protected View customFooterView = null;
    protected View customLoadMoreView = null;
    protected boolean loadMore, loadingMore;
    protected int layoutId = DEFAULT_VALUE;
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener<E> mOnItemLongClickListener;
    protected Context context;
    List<E> dataList;
    LayoutInflater inflate;
    LoadMoreListener loadMoreListener;
    AnimationDrawable drawable;
    public static int hotSize = -1;
    private TextView loding_more;

    /**
     * 默认构造方法
     *
     * @param context 上下文
     * @param layoutManager manager 一定要和recyclerView设置的manager是一致的
     * @param layoutId 要设置的itemlayout
     */
    public CommonSingleTypeAdapter(Context context, int layoutId, RecyclerView.LayoutManager layoutManager) {
        this.context = context;

        this.inflate = LayoutInflater.from(context);
        this.dataList = new ArrayList<>();
        mLayoutManager = layoutManager;
        if (mLayoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = ((GridLayoutManager) mLayoutManager);
            final GridLayoutManager.SpanSizeLookup lookup = gridLayoutManager.getSpanSizeLookup();
            final int spanCount = gridLayoutManager.getSpanCount();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    final int type = getItemViewType(position);
                    if (type == VIEW_TYPES_HEADER || type == VIEW_TYPES_FOOTER || type == VIEW_TYPES_LOADMODE) {
                        return spanCount;
                    } else {
                        return lookup.getSpanSize(position - getHeaderCount());
                    }
                }
            });
        }
        this.layoutId = layoutId;
    }

    /**
     * 带有下拉加载更多的adapter
     *
     * @param context 上下文
     * @param layoutId layoutId
     * @param recyclerView 当前adapter绑定的recyclerView
     * @param listener 下拉刷新的事件,在时间结束后一定要调用adapter的 {@link #onLoadSuccess()}才能将下面的加载更多去掉
     */
    public CommonSingleTypeAdapter(Context context, int layoutId, RecyclerView recyclerView, LoadMoreListener listener) {
        this(context, layoutId, recyclerView.getLayoutManager());
        this.loadMoreListener = listener;
        recyclerView.setAdapter(this);
        recyclerView.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollLoad() {
                if (!loadingMore && getCount() > 0) {
                    loadingMore = true;
                    loadMore = true;
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onScrollDown() {
            }

            @Override
            public void onBottom() {
                if (loadingMore && getCount() > 0) {
                    loadMoreListener.onLodingMore();
                }
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {

            }
        });
    }

    public void onLoadSuccess() {
        loadMore = false;
        loadingMore = false;
        if (drawable != null) {
            drawable.stop();
        }
        notifyDataSetChanged();
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 头和尾的holder
        if (viewType == VIEW_TYPES_HEADER && customHeaderView != null) {
            return new RecyclerViewHolder(customHeaderView);
        } else if (viewType == VIEW_TYPES_FOOTER && customFooterView != null) {
            return new RecyclerViewHolder(customFooterView);
        } else if (viewType == VIEW_TYPES_LOADMODE) {
            if (customLoadMoreView == null) {
                customLoadMoreView = inflate.inflate(R.layout.mint_loading_more_layout, null);
//                loding_more = (TextView) customLoadMoreView.findViewById(R.id.loding_more);
//                loding_more.setVisibility(View.VISIBLE);
                setFulSpan(customLoadMoreView, true);
            }
            return new RecyclerViewHolder(customLoadMoreView);
        } else {
            return new RecyclerViewHolder(inflate.inflate(layoutId, parent, false));
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        final int type = getItemViewType(position);
        if (type != VIEW_TYPES_HEADER && type != VIEW_TYPES_FOOTER && type != VIEW_TYPES_LOADMODE) {
            convert(holder, dataList.get(position - getHeaderCount()));
            convert(holder, dataList.get(position - getHeaderCount()), position);
            if (mOnItemClickListener != null) {
                final int finalPosition = position;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(holder, finalPosition);
                    }
                });

                if (mOnItemLongClickListener != null) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            mOnItemLongClickListener.onItemLongClick(holder, dataList.get(position - getHeaderCount()));
                            return true;
                        }
                    });
                }
            } else if (type == VIEW_TYPES_LOADMODE && drawable != null) {
                drawable.start();
            }
        }
    }


    public abstract void convert(final RecyclerViewHolder helper, final E item);

    protected void convert(final RecyclerViewHolder helper, final E item, int position) {

    }

    void setFulSpan(View view, boolean isloadMore) {
        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setFullSpan(true);
            if (isloadMore) {
                layoutParams.height = dip2px(context, 40);
            }
            view.setLayoutParams(layoutParams);
        }
    }

    private int dip2px(Context context, float dpValue) {
        float scale = 0;
        if(context != null) {
            scale = context.getResources().getDisplayMetrics().density;
        }
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 返回adapter中总共的item数目，包括头部和底部
     */
    @Override
    public int getItemCount() {
        int headerOrFooter = 0;
        if (customHeaderView != null) {
            headerOrFooter++;
        }
        if (customFooterView != null) {
            headerOrFooter++;
        }
        if (loadMore) {
            headerOrFooter++;
        }
        return getCount() + headerOrFooter;
    }

    @Override
    public int getItemViewType(int position) {
        if (customFooterView != null && position == getItemCount() - 1 - getLoadMoreCount()) {
            return VIEW_TYPES_FOOTER;
        } else if (customHeaderView != null && position == 0) {
            return VIEW_TYPES_HEADER;
        } else if (loadMore && position == getItemCount() - 1) {
            return VIEW_TYPES_LOADMODE;
        } else {
            return super.getItemViewType(position - getHeaderCount());
        }
    }

    public int getHeaderCount() {
        return customHeaderView != null ? 1 : 0;
    }

    public int getLoadMoreCount() {
        return loadMore ? 1 : 0;
    }

    public int getFooterCount() {
        return customFooterView != null ? 1 : 0;
    }

    public View getCustomHeaderView() {
        return customHeaderView;
    }

    public void setCustomHeaderView(View customHeaderView) {
        this.customHeaderView = customHeaderView;
        setFulSpan(customHeaderView, false);
    }

    public View getCustomFooterView() {
        return customFooterView;
    }

    public void setCustomFooterView(View customFooterView) {
        this.customFooterView = customFooterView;
        setFulSpan(customFooterView, false);
    }

    public boolean isLoadMore() {
        return loadMore;
    }

    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
        notifyDataSetChanged();
    }

    public void removeHeaderView() {
        customHeaderView = null;
        // notifyItemRemoved(0);如果这里需要做头部的删除动画，
        // 可以复写这个方法，然后进行改写
        notifyDataSetChanged();
    }

    public void removeFooterView() {
        customFooterView = null;
        notifyItemRemoved(getItemCount());
        // 这里因为删除尾部不会影响到前面的pos的改变，所以不用刷新了。
    }

    public int getCount() {
        if (dataList == null) {
            return 0;
        }
        if (hotSize != -1) {
            if (dataList.size() > hotSize) {
                this.getmDatas().remove(dataList.get(dataList.size() - 1));
                return hotSize;
            } else {
                return dataList.size();
            }
        }
        return dataList.size();
    }

    public List<E> getmDatas() {
        return dataList;
    }

    public void setmDatas(List<E> mDatas) {
        dataList = mDatas;
        notifyDataSetChanged();
    }

    public void setmDatas(List<E> mDatas, List mTypeUser) {
        dataList = mDatas;
        this.mTypeUser = mTypeUser;
        notifyDataSetChanged();
    }

    public void addItemAtFront(E item) {
        dataList.add(0, item);
        notifyItemInserted(1);
    }

    public void addmDatas(List<E> mDatas) {
        dataList.addAll(mDatas);
    }

    public E getLastData() {
        if (getCount() == 0) {
            return null;
        }
        return getmDatas().get(getCount() - 1);
    }

    public E getItem(int position) {
        return getmDatas().get(position);
    }

    public void remove(E entity) {
        dataList.remove(entity);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<E> mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerViewHolder holder, int position);
    }

    public interface OnItemLongClickListener<E> {
        void onItemLongClick(RecyclerViewHolder holder, E item);
    }

    public interface LoadMoreListener {
        void onLodingMore();
    }

    public void clearnAdapter() {
        if (dataList != null) {
            this.dataList.clear();
        }
        notifyDataSetChanged();
    }
}