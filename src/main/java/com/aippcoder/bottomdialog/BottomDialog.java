package com.aippcoder.bottomdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.aippcoder.bottomdialog.model.BottomModel;

import java.util.List;

/**
 * 底部弹窗
 * 实现类似于苹果的弹窗效果
 * 默认是一个内嵌一个ListView
 * <p>
 * 1. 可以选择普通样式或者圆角样式
 * 2. 可以自定义View
 * 3. 可以自定义点击事件
 * <p>
 * Created by san on 2017/3/18.
 */

public class BottomDialog extends Dialog {

    public static final int LIST = 0;

    public static final int GRID = 1;

    @IntDef({LIST, GRID})
    public @interface ShowType {
    }

    public static final int NORMAL = 0;

    public static final int CIRCLE = 1;

    @IntDef({NORMAL, CIRCLE})
    public @interface ShowStyle {
    }

    //弹窗显示类型，默认提供List和Grid两种
    private
    @ShowType
    int showType = LIST;

    private
    @ShowStyle
    int showStyle = NORMAL;
    //自定义的View
    private View customView;

    private List<BottomModel> data;

    private OnBottomItemClickListener itemClickListener;
    private OnBottomItemLongClickListener itemLongClickListener;

    public BottomDialog(@NonNull Context context, List<BottomModel> models) {
        super(context);
        this.data = models;
        init(context);
    }

    public BottomDialog(@NonNull Context context, @StyleRes int themeResId, List<BottomModel> data) {
        super(context, themeResId);
        setCanceledOnTouchOutside(true);
        this.data = data;
        init(context);
    }

    public BottomDialog(@NonNull Context context, @StyleRes int themeResId, @ShowType int showType, @ShowStyle int showStyle, List<BottomModel> models) {
        super(context, themeResId);
        setCanceledOnTouchOutside(true);
        this.showType = showType;
        this.showStyle = showStyle;
        this.data = models;
        init(context);
    }

    public BottomDialog(@NonNull Context context, @StyleRes int themeResId, View view) {
        super(context, themeResId);
        setCanceledOnTouchOutside(true);
        this.customView = view;
        init(context);
    }

    public BottomDialog(@NonNull Context context, @StyleRes int themeResId, @ShowStyle int showStyle, View view) {
        super(context, themeResId);
        setCanceledOnTouchOutside(true);
        this.showStyle = showStyle;
        this.customView = view;
        init(context);
    }

    private void init(Context context) {

        LinearLayout container = new LinearLayout(context);
        container.setOrientation(LinearLayout.VERTICAL);

//
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        container.setLayoutParams(layoutParams);

        if (showStyle == NORMAL)
            container.setBackgroundResource(R.drawable.shape_bottom_dialog_normal);
        else
            container.setBackgroundResource(R.drawable.shape_bottom_dialog_circle);

        if(customView == null) {
            if(showType == LIST) {
                ListView listView = new ListView(context);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(itemClickListener != null) itemClickListener.onItemClick(parent,view,position,id);
                    }
                });
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        return itemLongClickListener != null && itemLongClickListener.onItemLongClick(parent,view,position,id);
                    }
                });
                listView.setAdapter(new BottomListAdapter(data));

                container.addView(listView);
            } else if(showType == GRID) {
                GridView gridView = new GridView(context);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(itemClickListener != null) itemClickListener.onItemClick(parent,view,position,id);
                    }
                });
                gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        return itemLongClickListener != null && itemLongClickListener.onItemLongClick(parent,view,position,id);
                    }
                });
                gridView.setNumColumns(4);
                gridView.setAdapter(new BottomGridAdapter(data));
                container.addView(gridView);
            }
        } else {
            container.addView(customView);
        }

        if (showStyle == NORMAL) {
            ViewGroup.LayoutParams params = container.getLayoutParams();
            params.width = context.getResources().getDisplayMetrics().widthPixels;
            setContentView(container, params);
        } else {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) container.getLayoutParams();
            params.width = context.getResources().getDisplayMetrics().widthPixels - DensityUtils.dip2px(context, 16);
            params.bottomMargin = DensityUtils.dip2px(context, 8);
            setContentView(container, params);
        }

        setBottomLayout();

    }

    public void setOnBottomItemClickListener(OnBottomItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setOnBottomItemLongClickListener(OnBottomItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public interface OnBottomItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    public interface OnBottomItemLongClickListener {
        boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id);
    }

    /**
     * 设置 dialog 位于屏幕底部，并且设置出入动画
     */
    private void setBottomLayout() {
        Window win = getWindow();
        if (win != null) {
            // dialog 布局位于底部
            win.setGravity(Gravity.BOTTOM);
            // 设置进出场动画
            win.setWindowAnimations(R.style.BottomDialog_Animation);
        }
    }


    private class BottomListAdapter extends BaseAdapter {
        private List<BottomModel> data;

        private BottomListAdapter(List<BottomModel> data) {
            this.data = data;
        }

        public void setData(List<BottomModel> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return this.data == null ? 0 : this.data.size();
        }

        @Override
        public BottomModel getItem(int position) {
            return this.data == null ? null : this.data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BottomModel model = data.get(position);
            Drawable leftDrawable = parent.getResources().getDrawable(model.resId);
//            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());

            TextView view = new TextView(parent.getContext());
//            view.setCompoundDrawables(leftDrawable, null, null, null);
            view.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
            view.setCompoundDrawablePadding(DensityUtils.dip2px(parent.getContext(), 16));
            view.setGravity(Gravity.CENTER_VERTICAL);
            view.setTextColor(parent.getResources().getColor(R.color.dark_gray));
            view.setTextSize(14);
            view.setText(model.title);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            view.setPadding(DensityUtils.dip2px(parent.getContext(), 16),
                    DensityUtils.dip2px(parent.getContext(), 16),
                    DensityUtils.dip2px(parent.getContext(), 16),
                    DensityUtils.dip2px(parent.getContext(), 16));
            return view;
        }

    }

    private class BottomGridAdapter extends BaseAdapter {
        private List<BottomModel> data;

        private BottomGridAdapter(List<BottomModel> data) {
            this.data = data;
        }

        public void setData(List<BottomModel> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return this.data == null ? 0 : this.data.size();
        }

        @Override
        public BottomModel getItem(int position) {
            return this.data == null ? null : this.data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BottomModel model = data.get(position);
            Drawable topDrawable = parent.getResources().getDrawable(model.resId);
//            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());

            TextView view = new TextView(parent.getContext());
//            view.setCompoundDrawables(leftDrawable, null, null, null);
            view.setCompoundDrawablesWithIntrinsicBounds(null, topDrawable, null, null);
            view.setCompoundDrawablePadding(DensityUtils.dip2px(parent.getContext(), 16));
            view.setGravity(Gravity.CENTER_HORIZONTAL);
            view.setTextColor(parent.getResources().getColor(R.color.dark_gray));
            view.setTextSize(14);
            view.setText(model.title);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            view.setPadding(DensityUtils.dip2px(parent.getContext(), 16),
                    DensityUtils.dip2px(parent.getContext(), 16),
                    DensityUtils.dip2px(parent.getContext(), 16),
                    DensityUtils.dip2px(parent.getContext(), 16));
            return view;
        }

    }

}
