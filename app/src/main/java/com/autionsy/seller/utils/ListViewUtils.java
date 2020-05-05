package com.autionsy.seller.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewUtils {
    /**
     * 根据listView内部item的高度动态决定大小
     *
     * @param listView 要动态的listView
     * @param count    几条之后滚动
     */
    public static void setListViewHeightBasedOnChildren(ListView listView, int count) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (listAdapter.getCount() < count) {
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                //listItem.notify();
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            params.height = totalHeight
                    + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        } else {
            View listItem = listAdapter.getView(0, null, listView);
            listItem.measure(0, 0);
            params.height = listItem.getMeasuredHeight() * count + listView.getDividerHeight() * count;
        }
        listView.setLayoutParams(params);
    }
}
