package www.qisu666.sdk.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import org.xutils.common.util.LogUtil;

import java.util.List;

/**
 * 717219917@qq.com 2018/4/20 14:08.
 */
public class Update {


  public  Update(Activity context, String url2){
      Intent intent = new Intent();
//      intent.setAction("android.intent.action.MAIN");
//      intent.addCategory("android.intent.category.APP_MARKET");
      PackageManager pm = context.getPackageManager();
      List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
      int size = infos.size();
      String url="";
      for (int i = 0; i < size; i++) {
          ActivityInfo activityInfo = infos.get(i).activityInfo;
          String packageName = activityInfo.packageName;
          LogUtil.e("获取到的包名："+packageName);     //获取应用市场的包名
          if (packageName.equals("com.tencent.android.qqdownloader")){//应用宝
             url=packageName;break;
          }else if (packageName.equals("com.oppo.market")){           //oppo应用商店
              url=packageName;break;
          }else if (packageName.equals("com.huawei.appmarket")){      //华为应用市场
              url=packageName;break;
          }
//          else if (packageName.equals("com.baidu.appsearch")){      //百度手机市场
//              url=packageName;break;
//          }else if (packageName.equals("com.xiaomi.market")){        //小米应用市场
//              url=packageName;break;
//          }
          else if (packageName.equals("com.pp.assistant")){        //pp助手
              url=packageName;break;
          }else if (packageName.equals("com.taobao.appcenter")){    //淘宝手机助手
              url=packageName;break;
          }else if (packageName.equals("com.wandoujia.phoenix2")){  //豌豆荚
              url=packageName;break;
          }else if (packageName.equals("cn.goapk.market")){         //安智市场
              url=packageName;break;
          }else if (packageName.contains("com.sec.android.app.samsungapps")){ //三星应用市场
              url=packageName;break;
          }
      }



//      Intent intent2 = new Intent(Intent.ACTION_VIEW);
//      intent2.setData(Uri.parse("market://details?id=www.qisu666.com"));Uri.parse("market://details?id=www.qisu666.com");
//      intent2.setPackage("com.oppo.market");//应用市场包名
//      context.startActivity(intent2);

      if (url.equals("")){
          url=url2;
          Intent intent_my = new Intent(Intent.ACTION_VIEW);
          try {
              intent_my.setData(Uri.parse(url));
              context.startActivity(intent_my);}catch (Throwable t){
              intent_my.setData(Uri.parse("http://android.myapp.com/myapp/detail.htm?apkName=www.qisu666.com"));
               context.startActivity(intent_my);
          }

      }else {

          if(url.contains("samsung")){//三星特别处理
              goToSamsungappsMarket(context,url);
          }else{
              Intent intent2 = new Intent(Intent.ACTION_VIEW);
              Uri uri2 = Uri.parse("market://details?id=www.qisu666.com");//app包名
              intent2.setData(uri2);
              intent2.setPackage(url);//应用市场包名
              context.startActivity(intent2);
          }





      }




  }

    public static void goToSamsungappsMarket(Context context, String packageName) {
        Uri uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=www.qisu666.com");
        Intent goToMarket = new Intent();
        goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
        goToMarket.setData(uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public  Update(Activity context,String url2,boolean finish){
        Intent intent = new Intent();
//      intent.setAction("android.intent.action.MAIN");
//      intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
//        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        List<ApplicationInfo> infos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);// GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的
        int size = infos.size();
        String url="";
        for (int i = 0; i < size; i++) {
            String packageName = infos.get(i).packageName;
            LogUtil.e("获取到的包名："+packageName);     //获取应用市场的包名
            if (packageName.equals("com.tencent.android.qqdownloader")){//应用宝
                url=packageName;break;
            }else if (packageName.contains("com.oppo.market")){           //oppo应用商店
                url=packageName;break;
            } else if (packageName.contains("com.huawei.appmarket")){      //华为应用市场
                url=packageName;break;
            }
//            else if (packageName.contains("com.baidu.appsearch")){      //百度手机市场
//                url=packageName;break;
//            }
//            else if (packageName.contains("com.xiaomi.market")){        //小米应用市场
//                url=packageName;break;
//            }
            else if (packageName.contains("com.pp.assistant")){        //pp助手
                url=packageName;break;
            }else if (packageName.contains("com.taobao.appcenter")){    //淘宝手机助手
                url=packageName;break;
            }else if (packageName.contains("com.wandoujia.phoenix2")){  //豌豆荚
                url=packageName;break;
            }else if (packageName.contains("cn.goapk.market")){         //安智市场
                url=packageName;break;
            }else if (packageName.contains("com.sec.android.app.samsungapps")){ //三星应用市场
                url=packageName;break;
            }
        }

///Instrumentation: checkStartActivityResult() : Intent { act=android.intent.action.VIEW dat=market://details?id=www.qisu666.com pkg=com.sec.android.app.samsungapps }
//            checkStartActivityResult() : intent is instance of [Intent].

//      Intent intent2 = new Intent(Intent.ACTION_VIEW);
//      intent2.setData(Uri.parse("market://details?id=www.qisu666.com"));Uri.parse("market://details?id=www.qisu666.com");
//      intent2.setPackage("com.oppo.market");//应用市场包名
//      context.startActivity(intent2);

        if (url.equals("")){
            url=url2;
            Intent intent_my = new Intent(Intent.ACTION_VIEW);
            try {
                intent_my.setData(Uri.parse(url));
                context.startActivity(intent_my);
            }catch (Throwable t){
                intent_my.setData(Uri.parse("http://android.myapp.com/myapp/detail.htm?apkName=www.qisu666.com"));
                context.startActivity(intent_my);
            }

        }else {

          if(url.contains("samsung")){//三星特别处理

              goToSamsungappsMarket(context,url);

          }else{
              Intent intent2 = new Intent(Intent.ACTION_VIEW);
              Uri uri2 = Uri.parse("market://details?id=www.qisu666.com");//app包名
              intent2.setData(uri2);
              intent2.setPackage(url);//应用市场包名
              context.startActivity(intent2);
          }

//            Intent intent2 = new Intent(Intent.ACTION_VIEW);
//            intent2.setData(Uri.parse("market://details?id=www.qisu666.com"));//app包名
////          intent2.setPackage(infos.get(0).activityInfo.packageName);//应用市场包名
//            intent2.setPackage(url.replace(" ",""));//应用市场包名
//            context.startActivity(inteeeernt2);
        }

        if(finish){context.finish();}


    }


}
