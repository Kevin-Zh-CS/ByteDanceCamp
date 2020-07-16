package com.bytedance.androidcamp.network.dou.recycler;

import com.bytedance.androidcamp.network.dou.R;

import java.util.ArrayList;
import java.util.List;

public class TestDataSet {

    public static List<TestData> getMessageSet() {
        List<TestData> messageSet = new ArrayList<>();
        messageSet.add(new TestData(R.mipmap.tiktok, "抖音助手", "上传你的第一支视频吧","刚刚"));
        messageSet.add(new TestData(R.mipmap.dalaoshi,"好兄弟","下午游泳吗？","0:37"));
        messageSet.add(new TestData(R.mipmap.nvshen,"女神","滚","12:40"));
        messageSet.add(new TestData(R.mipmap.hh,"妹妹","这题咋做啊QAQ","0:23"));
        messageSet.add(new TestData(R.mipmap.mo,"佚名","苟利国家生死以","3:27"));
        messageSet.add(new TestData(R.mipmap.messg,"消息助手","你因拍了拍张总已被移除群聊","10:22"));
        messageSet.add(new TestData(R.mipmap.moshen,"陌生人","[Hi]","7:11"));
        messageSet.add(new TestData(R.mipmap.tomato,"老番茄","巧了，我也是你粉丝","19:22"));
        return messageSet;
    }

}
