package www.qisu666.com.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import www.qisu666.common.activity.BaseActivity;
import www.qisu666.common.utils.Cn2pyUtil;
import www.qisu666.common.utils.ConstantCode;
import www.qisu666.common.utils.LogUtils;
import www.qisu666.common.utils.PinyinComparator;
import www.qisu666.com.R;
import www.qisu666.com.adapter.BrandAdapter;
import www.qisu666.com.adapter.LetterAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//选择品牌
public class ChooseBrandActivity extends BaseActivity {

    private ListView lv_letter;
    private ListView lv_brand;
    private BrandAdapter brandAdapter;
    private List<ArrayList<String>> brandList;
    private ArrayList<String> typeList;
    private String currentBrand;//当前选择的品牌

    private PinyinComparator pinyinComparator;//根据拼音来排列ListView里面的数据类

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_choose_brand);
        initTitleBar();
        initData();
        initViews();
        setListeners();
    }

    private void initTitleBar() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.choose_brand_title);
        View left_btn = findViewById(R.id.img_title_left);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        pinyinComparator = new PinyinComparator();
        lv_brand = (ListView) findViewById(R.id.lv_brand);
        Collections.sort(brandList, pinyinComparator);
        brandAdapter = new BrandAdapter(this, brandList);
        lv_brand.setAdapter(brandAdapter);
        lv_letter = (ListView) findViewById(R.id.lv_letter);
        lv_letter.setAdapter(new LetterAdapter(this));
    }

    /**
     * 设置监听器
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setListeners() {
        lv_brand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentBrand = ((ArrayList<String>)parent.getItemAtPosition(position)).get(2);
                Intent intent = new Intent(ChooseBrandActivity.this, ChooseBrandTypeActivity.class);
                intent.putExtra("types", (ArrayList<String>)parent.getItemAtPosition(position));
                startActivityForResult(intent, ConstantCode.REQ_CHOOSE_BRAND_TYPE);
            }
        });

        lv_letter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lv_brand.setSelection(brandAdapter.getPositionForSection(String.valueOf(parent.getItemAtPosition(position))));
            }
        });

        lv_letter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
//                    float y = event.getY() - DensityUtil.dip2px(ChooseBrandActivity.this, 50) - getStatusBarHeight();

                    //getY()获取相对坐标
                    float y = event.getY();
                    int position = (int)(y/(lv_letter.getChildAt(0).getHeight()));
                    String letter = String.valueOf(lv_letter.getItemAtPosition(position));
                    lv_brand.setSelection(brandAdapter.getPositionForSection(letter));
                }
                return false;
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //品牌数据
        XmlResourceParser parser = null;
        if(getIntent().getIntExtra("power", 1) == 1){
            parser = getResources().getXml(R.xml.brands_hybird);
        } else if(getIntent().getIntExtra("power", 1) == 2){
            parser = getResources().getXml(R.xml.brands_electric);
        }

        try {
            int eventType = parser.getEventType();
            LogUtils.d("eventType:"+eventType);
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        brandList = new ArrayList<ArrayList<String>>();
                        break;
                    case XmlPullParser.START_TAG:
                        if(parser.getName().equals("brand")){
                            typeList = new ArrayList<>();
                        } else if(parser.getName().equals("name")){
                            String name = parser.nextText();
                            String pinyin = Cn2pyUtil.getInstance().getSelling(name);
                            String firstLetter = pinyin.substring(0,1).toUpperCase();
                            if(firstLetter.matches("[A-Z]")){
                                typeList.add(firstLetter);
                            } else {
                                typeList.add("#");
                            }
                            typeList.add(pinyin);
                            typeList.add(name);
                        } else if(parser.getName().equals("type")){
                            typeList.add(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("brand")){
                            brandList.add(typeList);
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            parser.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ConstantCode.REQ_CHOOSE_BRAND_TYPE && resultCode==ConstantCode.RES_CHOOSE_BRAND_TYPE){
            data.putExtra("brand", currentBrand);
            setResult(ConstantCode.RES_CHOOSE_BRAND_TYPE, data);
            finish();
        }
    }
}
