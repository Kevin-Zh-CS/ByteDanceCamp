package com.bytedance.androidcamp.network.dou.util;

import android.os.Environment;

import java.io.File;

public class FormatUtil {

    /**
     * 将缓存文件夹的数据转存到vedio文件下
     * @param recAudioFile
     */
    public static void videoRename(File recAudioFile,String fileName) {
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath()+ "/DCIM/Camera/";
        File out = new File(path);
        if (!out.exists()) {
            out.mkdirs();
        }
        out = new File(path, fileName);
        if (recAudioFile.exists())
            recAudioFile.renameTo(out);
    }

    /**
     * 用以计时操作的相关方法
     * @param num
     * @return
     */
    public static String format(int num){
        String s = num + "";
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }
}