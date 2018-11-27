package www.qisu666.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.com.config.Config;
import www.qisu666.common.utils.ToastUtil;
import www.qisu666.com.R;
import www.qisu666.com.adapter.PayByOthersManageAdapter;
import www.qisu666.com.event.LoginEvent;
import www.qisu666.com.logic.AbstractResponseCallBack;
import www.qisu666.com.logic.HttpLogic;
import www.qisu666.com.logic.PageLoadResponseCallBack;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.util.OnLoadRefreshCallBack;
import www.qisu666.com.util.UserParams;
import www.qisu666.com.widget.AlertDialog;
import www.qisu666.com.widget.LoadingDialog;
import com.linfaxin.recyclerview.PullRefreshLoadRecyclerView;
import com.linfaxin.recyclerview.headfoot.LoadMoreView;
import com.linfaxin.recyclerview.headfoot.impl.DefaultLoadMoreView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//被代付人管理
public class PayByOthersManageActivity extends BaseActivity implements OnLoadRefreshCallBack, PayByOthersManageAdapter.OnItemLongClickListener, PayByOthersManageAdapter.OnDeleteClickListener {

    private PullRefreshLoadRecyclerView pull_refresh_load_recycler_view;
    private LoadMoreView loadMoreView;
    private PayByOthersManageAdapter adapter;
    private List<Map<String,Object>> list;

    private int currentPage = 1;//当前数据分页
    private static final int PAGE_NUM = 10;//每页数据条数
    private UserParams user = UserParams.INSTANCE;

    @Subscribe public void onEventMainThread(LoginEvent event) {
        connToServer();
    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_pay_by_others_manage);
        initViews();
        setAdapter();
        connToServer();
        EventBus.getDefault().register(this);
    }

    /** 初始化控件 */
    private void initViews() {
        initTitleBar();
        pull_refresh_load_recycler_view = (PullRefreshLoadRecyclerView) findViewById(R.id.pull_refresh_load_recycler_view);
        pull_refresh_load_recycler_view.setLoadMoreView(null);
        loadMoreView = new DefaultLoadMoreView(this);
        pull_refresh_load_recycler_view.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
    }

    /**  初始化标题栏 */
    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(getResources().getString(R.string.pay_by_others_manage_title));
        View leftBtn = findViewById(R.id.img_title_left);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView right_btn = (ImageView) findViewById(R.id.img_title_right);
        right_btn.setImageResource(R.mipmap.ic_pay_by_others_add_person);
        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PayByOthersManageActivity.this, PayByOthersManageAddActivity.class), ConstantCode.REQ_ADD_BE_PAYING_AGENT);
            }
        });
    }

    /** 设置适配器 */
    private void setAdapter() {
        list = new ArrayList<Map<String, Object>>();
        //demo代码，添加静态数据
//        for (int i=0;i<10;i++){
//            list.add(new HashMap<String, Object>());
//        }
        adapter = new PayByOthersManageAdapter(this, this, list);
        adapter.setOnDeleteClickListener(this);
        adapter.setOnItemLongClickListener(this);
        adapter.setMode(Attributes.Mode.Single);
        pull_refresh_load_recycler_view.setAdapter(adapter);
    }

    /** 刷新列表 */
    @Override public void onRefresh() {
        currentPage = 1;
        pull_refresh_load_recycler_view.setLoadMoreView(null);
        connToServer();
    }

    /**  加载更多 */
    @Override  public void onLoadMore() {
        currentPage++;
        connToServer();
    }

    /**  发送 I104 请求，获取被代付人列表 */
    private void connToServer() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_code", "I104");
            jsonObject.put("s_token", user.getS_token());
            jsonObject.put("user_id", user.getUser_id());
            jsonObject.put("cur_page_no", String.valueOf(currentPage));
            jsonObject.put("page_size", "500");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new HttpLogic(this).sendRequest(Config.REQUEST_URL, jsonObject, new PageLoadResponseCallBack(currentPage, PAGE_NUM, pull_refresh_load_recycler_view, loadMoreView, adapter, list, "data_list"));
    }

    /** 发送 I105 请求，删除被代付人 */
    private void connToServerForDelete(final int position, final SwipeLayout swipeLayout) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("req_code", "I105");
            jsonObject.put("s_token", user.getS_token());
            jsonObject.put("user_id", user.getUser_id());
            jsonObject.put("mobile_no", list.get(position).get("mobile_no").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new HttpLogic(this).sendRequest(Config.REQUEST_URL, jsonObject, true, LoadingDialog.TYPE_ROTATE, new AbstractResponseCallBack() {
            @Override
            public void onResponse(Map<String, Object> map, String tag) {
//                removeItem(position, swipeLayout);
                ToastUtil.showToast(R.string.toast_I105);
                adapter.closeAllItems();
                onRefresh();
            }
        });
    }

    @Override public void onDeleteClick(int position, SwipeLayout swipeLayout) { showDeleteDialog(position, swipeLayout);  }
    @Override public void onItemLongClick(final int position, final SwipeLayout swipeLayout) { showDeleteDialog(position, swipeLayout);  }

    private void showDeleteDialog(final int position, final SwipeLayout swipeLayout) {
        DialogHelper.confirmDialog(this, getString(R.string.dialog_prompt_delete_be_paying_agent), new AlertDialog.OnDialogButtonClickListener() {
            @Override public void onConfirm() {
                connToServerForDelete(position, swipeLayout);
            }

            @Override public void onCancel() {

            }
        });
    }

//    private void removeItem(int position, SwipeLayout swipeLayout) {
//        adapter.removeShownLayouts(swipeLayout);
//        adapter.notifyItemRemoved(position);
//        list.remove(position);
//        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
//        adapter.closeAllItems();
//        ToastUtil.showToast(R.string.toast_I105);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ConstantCode.REQ_ADD_BE_PAYING_AGENT && resultCode==ConstantCode.RES_ADD_BE_PAYING_AGENT){
            onRefresh();
        }
    }

    @Override  public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
