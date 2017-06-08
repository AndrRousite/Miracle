package com.letion.geetionlib.base;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.letion.geetionlib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu-feng on 2017/6/8.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> {
    // 布局 viewType
    @IntDef({NEITHER, ONLY_HEADER, ONLY_FOOTER, BOTH_HEADER_FOOTER})
    protected @interface MODE {
    }

    public static final int NEITHER = 0;  // 默认没有头部局和尾布局
    public static final int ONLY_HEADER = 1;
    public static final int ONLY_FOOTER = 2;
    public static final int BOTH_HEADER_FOOTER = 3;
    protected static final int VIEW_TYPE_NORMAL = 0;
    protected static final int VIEW_TYPE_HEADER = -1;
    protected static final int VIEW_TYPE_FOOTER = -2;
    protected int BEHAVIOR_MODE = NEITHER;
    // footer view 的状态
    protected int mState = STATE_HIDE;  // 默认隐藏底部视图
    protected static final int STATE_NO_MORE = 1;
    protected static final int STATE_LOAD_MORE = 2;
    protected static final int STATE_INVALID_NETWORK = 3;
    protected static final int STATE_HIDE = 5;
    protected static final int STATE_REFRESHING = 6;
    protected static final int STATE_LOAD_ERROR = 7;
    protected static final int STATE_LOADING = 8;

    protected Context mContext;
    protected List<T> mItems;
    protected LayoutInflater mInflater;

    // 事件 监听
    private OnLoadingHeaderCallBack onLoadingHeaderCallBack;

    public BaseRecyclerAdapter(Context context, @MODE int mode) {
        super();
        BEHAVIOR_MODE = mode;
        this.mContext = context;
        this.mItems = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
    }

    /**
     * 创建Hodler
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseHolder<T> onCreateViewHolder(ViewGroup parent, final int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                if (onLoadingHeaderCallBack != null)
                    return onLoadingHeaderCallBack.onCreateHeaderHolder(parent);
                else
                    throw new IllegalArgumentException("you have to impl the interface when using" +
                            " this viewType");
            case VIEW_TYPE_FOOTER:
                return new FooterViewHolder(mInflater.inflate(R.layout.layout_footer_view,
                        parent, false));
            default:
                BaseHolder holder = onCreateDefaultViewHolder(parent, viewType);
                if (holder != null) {
                    holder.itemView.setTag(holder);
                }
                return holder;
        }
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseHolder<T> holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_HEADER:
                if (onLoadingHeaderCallBack != null)
                    onLoadingHeaderCallBack.onBindHeaderHolder(holder, position);
                break;
            case VIEW_TYPE_FOOTER:
                FooterViewHolder fvh = (FooterViewHolder) holder;
                fvh.itemView.setVisibility(View.VISIBLE);
                switch (mState) {
                    case STATE_INVALID_NETWORK:
                        fvh.tv_footer.setText(mContext.getResources().getString(R.string
                                .footer_type_net_error));
                        fvh.pb_footer.setVisibility(View.GONE);
                        break;
                    case STATE_LOAD_MORE:
                    case STATE_LOADING:
                        fvh.tv_footer.setText(mContext.getResources().getString(R.string
                                .footer_type_loading));
                        fvh.pb_footer.setVisibility(View.VISIBLE);
                        break;
                    case STATE_NO_MORE:
                        fvh.tv_footer.setText(mContext.getResources().getString(R.string
                                .footer_type_not_more));
                        fvh.pb_footer.setVisibility(View.GONE);
                        break;
                    case STATE_REFRESHING:
                        fvh.tv_footer.setText(mContext.getResources().getString(R.string
                                .footer_type_refreshing));
                        fvh.pb_footer.setVisibility(View.GONE);
                        break;
                    case STATE_LOAD_ERROR:
                        fvh.tv_footer.setText(mContext.getResources().getString(R.string
                                .footer_type_error));
                        fvh.pb_footer.setVisibility(View.GONE);
                        break;
                    case STATE_HIDE:
                        fvh.itemView.setVisibility(View.GONE);
                        break;
                }
                break;
            default:
                holder.setData(getItem(getIndex(position)), position);
                break;
        }
    }

    /**
     * 当添加到RecyclerView时获取GridLayoutManager布局管理器，修正header和footer显示整行
     *
     * @param recyclerView the mRecyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == VIEW_TYPE_HEADER || getItemViewType
                            (position) == VIEW_TYPE_FOOTER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    /**
     * 当RecyclerView在windows活动时获取StaggeredGridLayoutManager布局管理器，修正header和footer显示整行
     *
     * @param holder the RecyclerView.ViewHolder
     */
    @Override
    public void onViewAttachedToWindow(BaseHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams)
                    lp;
            if (BEHAVIOR_MODE == ONLY_HEADER) {
                p.setFullSpan(holder.getLayoutPosition() == 0);
            } else if (BEHAVIOR_MODE == ONLY_FOOTER) {
                p.setFullSpan(holder.getLayoutPosition() == mItems.size() + 1);
            } else if (BEHAVIOR_MODE == BOTH_HEADER_FOOTER) {
                if (holder.getLayoutPosition() == 0 || holder.getLayoutPosition() == mItems.size
                        () + 1) {
                    p.setFullSpan(true);
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && (BEHAVIOR_MODE == ONLY_HEADER || BEHAVIOR_MODE == BOTH_HEADER_FOOTER))
            return VIEW_TYPE_HEADER;
        if (position + 1 == getItemCount() && (BEHAVIOR_MODE == ONLY_FOOTER || BEHAVIOR_MODE ==
                BOTH_HEADER_FOOTER))
            return VIEW_TYPE_FOOTER;
        else return VIEW_TYPE_NORMAL;
    }

    /**
     * recycler 中 View的个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (BEHAVIOR_MODE == ONLY_FOOTER || BEHAVIOR_MODE == ONLY_HEADER) {
            return mItems.size() + 1;
        } else if (BEHAVIOR_MODE == BOTH_HEADER_FOOTER) {
            return mItems.size() + 2;
        } else return mItems.size();
    }

    private int getIndex(int position) {
        return BEHAVIOR_MODE == ONLY_HEADER || BEHAVIOR_MODE == BOTH_HEADER_FOOTER ? position - 1
                : position;
    }

    public List<T> getItems() {
        return mItems;
    }

    /**
     * 获得item的数据
     *
     * @param position
     * @return
     */
    public T getItem(int position) {
        return mItems == null ? null : mItems.get(position);
    }

    public final void addAll(List<T> items) {
        if (items != null) {
            this.mItems.addAll(items);
            notifyItemRangeInserted(this.mItems.size(), items.size());
        }
    }

    public final void addItem(T item) {
        if (item != null) {
            this.mItems.add(item);
            notifyItemChanged(mItems.size());
        }
    }


    public final void addItem(int position, T item) {
        if (item != null) {
            this.mItems.add(getIndex(position), item);
            notifyItemInserted(position);
        }
    }

    public void replaceItem(int position, T item) {
        if (item != null) {
            this.mItems.set(getIndex(position), item);
            notifyItemChanged(position);
        }
    }

    public void updateItem(int position) {
        if (getItemCount() > position) {
            notifyItemChanged(position);
        }
    }


    public final void removeItem(T item) {
        if (this.mItems.contains(item)) {
            int position = mItems.indexOf(item);
            this.mItems.remove(item);
            notifyItemRemoved(position);
        }
    }

    public final void removeItem(int position) {
        if (this.getItemCount() > position) {
            this.mItems.remove(getIndex(position));
            notifyItemRemoved(position);
        }
    }

    public final void clearAll() {
        this.mItems.clear();
        notifyDataSetChanged();
    }

    public void setState(int mState, boolean isUpdate) {
        this.mState = mState;
        if (isUpdate)
            updateItem(getItemCount() - 1);
    }

    public int getState() {
        return mState;
    }

    /**
     * 子类实现提供holder
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract BaseHolder<T> onCreateDefaultViewHolder(ViewGroup parent, int viewType);

    /**
     * 遍历所有hodler,释放他们需要释放的资源
     *
     * @param recyclerView
     */
    public static void releaseAllHolder(RecyclerView recyclerView) {
        if (recyclerView == null) return;
        for (int i = recyclerView.getChildCount() - 1; i >= 0; i--) {
            final View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder != null && viewHolder instanceof BaseHolder) {
                ((BaseHolder) viewHolder).release();
            }
        }
    }

    public void setOnLoadingHeaderCallBack(OnLoadingHeaderCallBack listener) {
        onLoadingHeaderCallBack = listener;
    }

    /**
     * for load header view
     */
    public interface OnLoadingHeaderCallBack {
        BaseHolder onCreateHeaderHolder(ViewGroup parent);

        void onBindHeaderHolder(RecyclerView.ViewHolder holder, int position);
    }

    static class FooterViewHolder extends BaseHolder {
        public ProgressBar pb_footer;
        public TextView tv_footer;

        public FooterViewHolder(View view) {
            super(view);
            pb_footer = (ProgressBar) view.findViewById(R.id.pb_footer);
            tv_footer = (TextView) view.findViewById(R.id.tv_footer);
        }

        @Override
        public void setData(Object data, int position) {

        }
    }
}
