package www.qisu666.com.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.adapter.PoiSearchAdapter;
import www.qisu666.com.util.OnLoadRefreshCallBack;
import www.qisu666.com.widget.LoadingDialog;
import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.impl.DefaultLoadMoreView;

import java.util.ArrayList;
import java.util.List;

//地图信息点
public class PoiSearchActivity extends BaseActivity implements TextWatcher, PoiSearch.OnPoiSearchListener, OnLoadRefreshCallBack ,PoiSearchAdapter.OnItemClickListener, Inputtips.InputtipsListener {

    private PullRefreshLoadRecyclerView pullRefreshLoadRecyclerView;
    private PoiSearchAdapter adapter;
    private AutoCompleteTextView tv_keyword;

    private String keyWord = "";// 要输入的poi搜索关键字
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private int pageNum = 10;// 每页显示的数量
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private List<PoiItem> totalPoiItems = new ArrayList<>();
    private String city;// 城市

    private LoadingDialog loadingDialog;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_poi_search);
        city = getIntent().getStringExtra("city");
        initView();
        setAdapter();
        setListeners();
    }

    /**  初始化控件 */
    private void initView() {
        initTitleBar();
        pullRefreshLoadRecyclerView = (PullRefreshLoadRecyclerView) findViewById(R.id.pull_refresh_load_recycler_view);
        tv_keyword = (AutoCompleteTextView) findViewById(R.id.tv_keyword);
        tv_keyword.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        tv_keyword.setDropDownBackgroundResource(R.drawable.bg_input_tips);
    }

    /** 初始化标题栏 */
    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.poi_search_title));
        View leftBtn = findViewById(R.id.img_title_left);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /** 设置监听器 */
    private void setListeners() {
        tv_keyword.addTextChangedListener(this);
        tv_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){ // 先隐藏键盘
                    ((InputMethodManager) tv_keyword.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(PoiSearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    keyWord = tv_keyword.getText().toString();
                    if ("".equals(keyWord)) {
                        ToastUtil.showToast(R.string.toast_map_search_keyword_is_null);
                    } else {
                        doSearchQuery();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /** 设置适配器 */
    private void setAdapter(){
        pullRefreshLoadRecyclerView.setRefreshView(null);
        pullRefreshLoadRecyclerView.setLoadMoreView(null);
        adapter = new PoiSearchAdapter(this, this, this, totalPoiItems);
        pullRefreshLoadRecyclerView.setAdapter(adapter);
    }

    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (!TextUtils.isEmpty(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, city);
            Inputtips inputTips = new Inputtips(this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override public void afterTextChanged(Editable s) {

    }

    /** 开始进行poi搜索 */
    protected void doSearchQuery() {
        showLoadingDialog();
        if(pullRefreshLoadRecyclerView.getLoadMoreView()!=null){
            pullRefreshLoadRecyclerView.setLoadMoreView(null);
        }

        if(totalPoiItems.size()>0){
            adapter.notifyItemRangeRemoved(0, adapter.getItemCount());
            totalPoiItems.clear();
        }
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(pageNum);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        query.setCityLimit(true);

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override public void onPoiSearched(PoiResult result, int rCode) {
        hideLoadingDialog();
        if(pullRefreshLoadRecyclerView.getLoadMoreView()!=null){
            pullRefreshLoadRecyclerView.getLoadMoreView().setState(LoadMoreView.STATE_NORMAL);
        }

        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页

                    // 取得第一页的poiitem数据，页数从数字0开始
                    List<PoiItem> poiItems = poiResult.getPois();

                    if (poiItems != null && poiItems.size() > 0) {
                        int start = totalPoiItems.size();
                        totalPoiItems.addAll(poiItems);
                        adapter.notifyItemRangeInserted(start, totalPoiItems.size());
                        if(currentPage < poiResult.getPageCount()-1){
                            pullRefreshLoadRecyclerView.setLoadMoreView(new DefaultLoadMoreView(this));
                        }
                    } else {
                        ToastUtil.showToast(R.string.toast_map_search_no_data);
                    }
                }
            } else {
                ToastUtil.showToast(R.string.toast_map_search_no_data);
            }
        } else {
            LogUtils.d(String.valueOf(rCode));
        }
    }

    /** 显示loadingDialog */
    private void showLoadingDialog() {
        if(loadingDialog==null){
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.show();
    }

    /** 隐藏loadingDialog */
    private void hideLoadingDialog() {
        if(loadingDialog!=null && loadingDialog.isShowing()){
            loadingDialog.hide();
        }
    }

    @Override public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override public void onRefresh() {

    }

    @Override public void onLoadMore() {
        nextButton();
    }

    /** 点击下一页按钮 */
    public void nextButton() {
        if (query != null && poiSearch != null && poiResult != null) {
            if (poiResult.getPageCount() - 1 > currentPage) {
                currentPage++;
                query.setPageNum(currentPage);// 设置查后一页
                poiSearch.searchPOIAsyn();
            } else {
                ToastUtil.showToast(R.string.toast_map_search_no_data);
            }
        }
    }

    @Override public void onItemClick(int position) {
        Intent intent = new Intent();
        intent.putExtra("data", totalPoiItems.get(position));
        setResult(ConstantCode.RES_POI_SEARCH, intent);
        finish();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if(loadingDialog!=null){
            loadingDialog.dismiss();
        }
    }

    @Override public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == 1000) {// 正确返回
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.item_lv_input_tips, listString);
            tv_keyword.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
            LogUtils.e(rCode+"");
        }
    }
}
