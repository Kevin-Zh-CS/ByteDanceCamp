package com.bytedance.androidcamp.network.dou.recycler2;

import com.bytedance.androidcamp.network.dou.R;

import java.util.ArrayList;
import java.util.List;

public class TestDataSet2 {
    public static List<TestData2> getMessageSet2() {
        List<TestData2> hintlist = new ArrayList<>();
        hintlist.add(new TestData2(R.mipmap.fans,"粉丝"));
        hintlist.add(new TestData2(R.mipmap.like,"赞"));
        hintlist.add(new TestData2(R.mipmap.at,"@我的"));
        hintlist.add(new TestData2(R.mipmap.comment,"评论"));
        return hintlist;
    }
}
