package com.example.words;

import android.provider.BaseColumns;

public class words {
    public words() {
    }
    public static abstract class Word implements BaseColumns {
        public static final String T="b";
        public static final String W="name";//列：单词
        public static final String M="hanyi";//列：单词含义
        public static final String L="example";//单词示例
    }

}
