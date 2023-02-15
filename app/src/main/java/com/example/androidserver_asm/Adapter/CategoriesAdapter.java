package com.example.androidserver_asm.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.androidserver_asm.Models.Categories;
import com.example.androidserver_asm.R;

import java.util.List;

public class CategoriesAdapter extends BaseAdapter {
    private Context context;
    private List<Categories> list;

    public CategoriesAdapter(Context _context, List<Categories> list) {
        this.context = _context;
        this.list = list;
    }

    public CategoriesAdapter() {
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int _i, View _view, ViewGroup _viewGroup) {
        View view = _view;
        if (view == null) {
            view = View.inflate(_viewGroup.getContext(), R.layout.layout_category_item, null);
            TextView spinner = view.findViewById(R.id.tvSpinner);
            ConstraintLayout layout = view.findViewById(R.id.layout_category_one_item);
            ViewHolder holder = new ViewHolder(spinner, layout);
            view.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        Categories category = (Categories) getItem(_i);
        holder.tvSpinner.setText(category.getName());

        return view;
    }

    private static class ViewHolder {
        final TextView tvSpinner;
        final ConstraintLayout layout;

        public ViewHolder(TextView tvSpinner, ConstraintLayout layout) {
            this.tvSpinner = tvSpinner;
            this.layout = layout;
        }
    }
}
