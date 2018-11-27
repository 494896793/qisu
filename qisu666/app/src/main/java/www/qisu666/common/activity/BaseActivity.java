package www.qisu666.common.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import www.qisu666.com.util.DialogHelper;
import www.qisu666.com.widget.LoadingDialog;

/**
 * Activity 基类
 *
 * @author Zed
 *
 */

/**
 *
 * 2015-6-27 Edit by
 * @author Coang
 *
 * 继承FragmentActivity
 *
 * */

/**
 *
 *
 * Activity退出
 *
 * Add 2015.08.03.
 *
 * */

public class BaseActivity extends FragmentActivity {

	private int mStatusBarColor = Color.parseColor("#1B2130");

	private ActivityController activityController = ActivityController.getInstance();

	private View mStatusBar;

	/**
	 * 当前沉浸模式，默认为布局沉浸式
	 */
	private String immersionType = TYPE_LAYOUT;

	/**
	 * 仅仅改变状态栏颜色的沉浸模式
	 */
	protected static final String TYPE_LAYOUT = "type_layout";

	/**
	 * 将原布局背景扩散至状态栏的沉浸模式
	 */
	protected static final String TYPE_BACKGROUND = "type_background";

	/**
	 * 直接将布局扩散至状态栏，不做任何处理
	 */
	protected static final String TYPE_NULL = "type_null";

	protected Context mContext;


	protected LoadingDialog mLoadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext = this;
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		activityController.putActivity(this);
		mLoadingDialog = DialogHelper.loadingAletDialog(mContext, "正在加载中...");
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
//		if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
////			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		}else {
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		}
////		MobclickAgent.onResume(this);
	}






	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityController.removeActivity(this);
		if(mLoadingDialog!=null){
			mLoadingDialog.dismiss();
			mLoadingDialog = null;
		}
	}

	/**
	 * 将状态栏添加至布局中
	 * @param viewGroup
	 */
	private void addStatusBar(ViewGroup viewGroup) {
		mStatusBar = new View(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight());
		mStatusBar.setLayoutParams(lp);
//		if(immersionType == TYPE_LAYOUT) {
		mStatusBar.setBackgroundColor(mStatusBarColor);
//		}else if(immersionType == TYPE_BACKGROUND){
//			//当浸入模式为背景浸入时，状态栏设为透明色
//			mStatusBar.setBackgroundColor(Color.TRANSPARENT);
//		}
		viewGroup.addView(mStatusBar);
	}

	/**
	 * 获取状态栏高度
	 * @return
	 */
	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	protected void setView(View contentView){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//去掉状态栏布局
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//当API>=21时，状态栏会自动增加一块半透明色块，这段代码将其设为全透明
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				Window window = getWindow();
				window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
						| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
				window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
				window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
				window.setStatusBarColor(Color.TRANSPARENT);
			}
			if (immersionType.equals(TYPE_LAYOUT)) {
				LinearLayout ll_content = new LinearLayout(this);
				ll_content.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
				ll_content.setOrientation(LinearLayout.VERTICAL);
				addStatusBar(ll_content);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				ll_content.addView(contentView, lp);
				setContentView(ll_content);
			} else if (immersionType.equals(TYPE_BACKGROUND)) {
				contentView.setPadding(0, getStatusBarHeight(), 0, 0);
				setContentView(contentView);
			} else if (immersionType.equals(TYPE_NULL)){
				setContentView(contentView);
			}

		}else{
			setContentView(contentView);
		}
	}

	/**
	 * 子类设置布局时应调用该方法
	 * @param resId 布局id
	 */

	protected void setView(int resId){
		View contentView = View.inflate(this, resId, null);
		setView(contentView);
		ButterKnife.bind(this);
	}

	/**
	 * 子类设置布局时应调用该方法
	 * @param resId 布局id
	 * @param color 状态栏颜色
	 */
	protected void setView(int resId, int color){
		mStatusBarColor = color;
		setView(resId);
	}

	/**
	 * 子类设置布局时应调用该方法
	 * @param contentView 布局View
	 * @param color 状态栏颜色
	 */
	protected void setView(View contentView, int color){
		mStatusBarColor = color;
		setView(contentView);
	}

	protected void setStatusBarColor(int color){
		 if(mStatusBar != null) {
			mStatusBar.setBackgroundColor(color);
		}
	}

	/**
	 * 子类设置布局时应调用该方法
	 * @param resId 布局id
	 * @param color 状态栏颜色
	 * @param type 浸入模式:TYPE_BACKGROUND或者TYPE_LAYOUT
	*/
	protected void setView(int resId, int color, String type){
		immersionType = type;
		mStatusBarColor = color;
		setView(resId);
	}

	/**
	 * 子类设置布局时应调用该方法
	 * @param contentView 布局View
	 * @param color 状态栏颜色
	 * @param type 浸入模式:TYPE_BACKGROUND或者TYPE_LAYOUT
	 */
	protected void setView(View contentView, int color, String type){
		immersionType = type;
		mStatusBarColor = color;
		setView(contentView);
	}

	/**
	 *
	 * @param contentView 布局View
	 * @param type 浸入模式:TYPE_BACKGROUND或者TYPE_LAYOUT
	 */
	protected void setView(View contentView, String type){
		immersionType = type;
		setView(contentView);
	}

	/**
	 *
	 * @param resId 布局ID
	 * @param type 浸入模式:TYPE_BACKGROUND或者TYPE_LAYOUT
	 */
	protected void setView(int resId, String type){
		immersionType = type;
		setView(resId);
	}


}