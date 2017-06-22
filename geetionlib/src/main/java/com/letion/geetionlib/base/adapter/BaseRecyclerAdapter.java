package com.letion.geetionlib.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu-feng on 2017/6/22.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {
    protected List<T> mInfos = new ArrayList<>();
    private BaseViewHolder<T> mHolder;
    protected OnItemClickListener mOnItemClickListener = null;
    protected OnItemLongClickListener mOnItemLongClickListener = null;

    public BaseRecyclerAdapter(List<T> infos) {
        super();
        if (infos != null)
            this.mInfos.addAll(infos);
    }

    /**
     * 创建Hodler
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        mHolder = getViewHolder(view, viewType);
        mHolder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null && mInfos.size() > 0) {
                mOnItemClickListener.onClick(mInfos.get(mHolder.getAdapterPosition()), viewType, mHolder.getAdapterPosition());
            }
        });
        mHolder.itemView.setOnLongClickListener(v -> {
            if (mOnItemLongClickListener != null && mInfos.size() > 0) {
                mOnItemLongClickListener.onLongClick(mInfos.get(mHolder.getAdapterPosition()), viewType, mHolder.getAdapterPosition());
                return true;
            }
            return false;
        });
        return mHolder;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseViewHolder<T> holder, int position) {
        holder.bindViewHolder(mInfos.get(position), position);
    }

    /**
     * 遍历所有hodler,释放他们需要释放的资源
     *
     * @param recyclerView
     */
    public static void releaseAllViewHolder(RecyclerView recyclerView) {
        if (recyclerView == null) return;
        for (int i = recyclerView.getChildCount() - 1; i >= 0; i--) {
            final View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder != null && viewHolder instanceof BaseViewHolder) {
                ((BaseViewHolder) viewHolder).releaseViewHolder();
            }
        }
    }

    /**
     * 子类实现提供holder
     *
     * @param v
     * @param viewType
     * @return
     */
    public abstract BaseViewHolder<T> getViewHolder(View v, int viewType);

    /**
     * 提供Item的布局
     *
     * @param viewType
     * @return
     */
    public abstract int getLayoutId(int viewType);

    public final void addAll(List<T> items) {
        if (items != null) {
            this.mInfos.addAll(items);
            notifyItemRangeInserted(this.mInfos.size(), items.size());
        }
    }

    public final void addItem(T item) {
        if (item != null) {
            this.mInfos.add(item);
            notifyItemChanged(mInfos.size());
        }
    }


    public final void addItem(int position, T item) {
        if (item != null) {
            this.mInfos.add(position, item);
            notifyItemInserted(position);
        }
    }

    public void replaceItem(int position, T item) {
        if (item != null) {
            this.mInfos.set(position, item);
            notifyItemChanged(position);
        }
    }

    public void updateItem(int position) {
        if (getItemCount() > position) {
            notifyItemChanged(position);
        }
    }


    public final void removeItem(T item) {
        if (this.mInfos.contains(item)) {
            int position = mInfos.indexOf(item);
            this.mInfos.remove(item);
            notifyItemRemoved(position);
        }
    }

    public final void removeItem(int position) {
        if (this.getItemCount() > position) {
            this.mInfos.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

}
