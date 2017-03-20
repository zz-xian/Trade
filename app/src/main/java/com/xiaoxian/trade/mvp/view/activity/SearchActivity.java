package com.xiaoxian.trade.mvp.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoxian.trade.R;
import com.xiaoxian.trade.base.BaseActivity;
import com.xiaoxian.trade.util.CacheUtil;
import com.xiaoxian.trade.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {
    private static final int DO_SEARCH = 1;

    @BindView(R.id.search_back)
    ImageView searchBack;
    @BindView(R.id.search_content)
    EditText searchContent;
    @BindView(R.id.clear_content)
    ImageView clearContent;
    @BindView(R.id.clear_history)
    ImageView clearHistory;
    @BindView(R.id.history_list)
    ListView historyList;
    @BindView(R.id.search_history)
    LinearLayout searchHistory;
    @BindView(R.id.result_list)
    ListView resultList;
    @BindView(R.id.search_result)
    LinearLayout searchResult;

    ArrayAdapter<String> adapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        //使用TextWatcher实时筛选
        searchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {//搜索结果隐藏，搜索历史显示
                    searchResult.setVisibility(View.GONE);
                    searchHistory.setVisibility(View.VISIBLE);
                } else {//搜索结果显示，搜索历史隐藏
                    if (searchResult.getVisibility() == View.GONE) {
                        searchResult.setVisibility(View.VISIBLE);
                    }
                    if (searchHistory.getVisibility() == View.VISIBLE) {
                        searchHistory.setVisibility(View.GONE);
                    }
                    if (clearContent.getVisibility() == View.GONE) {
                        clearContent.setVisibility(View.VISIBLE);
                    }
                    //根据输入值模糊查询历史记录中的数据
                    String input = searchContent.getText().toString();
                    queryHistory(input);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (handler.hasMessages(DO_SEARCH)) {
                    handler.removeMessages(DO_SEARCH);
                }
                if (TextUtils.isEmpty(s)) {
                    clearContent.setVisibility(View.GONE);
                } else {
                    handler.sendEmptyMessageDelayed(DO_SEARCH, 2000);
                }
            }
        });
        searchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //隐藏键盘
                    ((InputMethodManager) searchContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    handler.sendEmptyMessage(DO_SEARCH);
                    return true;
                }
                return false;
            }
        });
        initSearchHistory();
    }

    private void queryHistory(String str) {
        String cache = CacheUtil.getCache(SearchActivity.this);
        if (cache != null) {
            List<String> list = new ArrayList<>();
            //split()-将字符串分割为子字符串，返回字符串数组
            //如果使用“.”和“|”都是转义字符，必须得加"\\"
            for (String history : cache.split(",")) {
                if (history.contains(str)) {
                    list.add(history);
                }
            }
            adapter = new ArrayAdapter<>(SearchActivity.this, R.layout.item_history, list);
            if (list.size() > 0) {
                resultList.setAdapter(adapter);
                resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        searchContent.setText("");
                        searchContent.setText(adapter.getItem(position));
                    }
                });
            }
        } else {
            searchResult.setVisibility(View.GONE);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            doSearch();
        }
    };

    //初始化搜索历史记录显示
    private void initSearchHistory() {
        String cache = CacheUtil.getCache(SearchActivity.this);
        if (cache != null) {
            List<String> list = new ArrayList<>();
            for (String history : cache.split(",")) {
                list.add(history);
            }
            adapter = new ArrayAdapter<>(SearchActivity.this, R.layout.item_history, list);
            if (list.size() > 0) {
                historyList.setAdapter(adapter);
                historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        searchContent.setText("");
                        searchContent.setText(adapter.getItem(position));
                    }
                });
            }
        } else {
            searchHistory.setVisibility(View.GONE);
        }
    }

    //执行搜索
    public void doSearch() {
        String text = searchContent.getText().toString();
        if (TextUtils.isEmpty(text)) {
            return;
        }
        saveHistory(text);
        Bundle bundle = new Bundle();
        bundle.putString("Key", text);
        bundle.putString("Result", "KeyWords");
        UIUtil.nextPage(this, ResultActivity.class, bundle);
    }

    //缓存搜索历史
    private void saveHistory(String str) {
        String oldCache = CacheUtil.getCache(this);
        StringBuilder sb = new StringBuilder(str);
        if (oldCache == null) {
            CacheUtil.setCache(this, sb.toString());
            updateHistory();
        } else {
            sb.append("," + oldCache);
            if (!oldCache.contains(str)) {//避免缓存重复数据
                CacheUtil.setCache(this, sb.toString());
                updateHistory();
            }
        }
    }

    //动态更新搜索历史列表
    private void updateHistory() {
        String cache = CacheUtil.getCache(this);
        String[] data = new String[]{};
        if (cache != null) {
            data = cache.split(",");
        }
        adapter = new ArrayAdapter<>(this, R.layout.item_history, data);
        historyList.setAdapter(adapter);
        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchContent.setText("");
                searchContent.setText(adapter.getItem(position));
            }
        });
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.search_back, R.id.clear_content, R.id.clear_history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_back:
                finish();
                break;
            case R.id.clear_content:
                clearContent.setVisibility(View.GONE);
                searchContent.setText("");
                break;
            case R.id.clear_history:
                CacheUtil.ClearCache(this);
                updateHistory();
                break;
        }
    }
}
