package www.qisu666.com.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


import java.util.HashMap;

import www.qisu666.com.activity.MainActivity;
import www.qisu666.common.utils.JsonUtils;

public class PushChatUtils {

	private static int serverNotifyId = 0;

	/**
	 * 分发推送
	 */
	public static synchronized void pushChat(Context context, String notificationTitle, String notificationMessage) {

		Log.d("PushChatUtils", "notificationTitle=" + notificationTitle);
		Log.d("PushChatUtils", "notificationMessage=" + notificationMessage);

		try {
			int command = getConmand(notificationTitle);
//			@SuppressWarnings("unchecked")
//			HashMap<String, Object> map = (HashMap<String, Object>) JsonUtils.jsonToMap(notificationMessage);
			Log.e("YYQ", "notificationTitle:" + notificationTitle);
			Intent caIntent = new Intent(context, MainActivity.class);
//			caIntent.putExtra("consultationId", map.get("bizId").toString());// 咨询的id
//			caIntent.putExtra("jumpMark", "NotificationReceiver");// 跳转标记
			NotificationUtil.sendNotification(context, caIntent, notificationTitle, notificationMessage,
					1);
			// 处理
//			switch (command) {
//			case Constant.COMMAND_CONSULTATION_ANSWER:// 咨询回复
//				Intent caIntent = new Intent(context, ConsultDetailActivity.class);
//				caIntent.putExtra("consultationId", map.get("bizId").toString());// 咨询的id
//				caIntent.putExtra("jumpMark", "NotificationReceiver");// 跳转标记
//				NotificationUtil.sendNotification(context, caIntent, "咨询回复", map.get("bizUserName") + "对您咨询进行了回复...",
//						Constant.COMMAND_CONSULTATION_ANSWER);
//				break;
//			case Constant.COMMAND_SEND_GUIDANCES:// 健康指导
//				Intent sgIntent = new Intent(context, GuideDetailActivity.class);
//				sgIntent.putExtra("studioId", map.get("bizUserId").toString());
//				sgIntent.putExtra("healthGuidanceId", map.get("bizId").toString());
//				NotificationUtil.sendNotification(context, sgIntent, "健康指导", map.get("bizUserName") + "给您发了健康指导...",
//						Constant.COMMAND_SEND_GUIDANCES);
//				break;
//			case Constant.COMMAND_CONSULTATION_REMIND:// 咨询提醒
//				Intent adIntent = new Intent(context, ConsultDetailActivity.class);
//				adIntent.putExtra("consultationId", map.get("bizId").toString());// 咨询的id
//				adIntent.putExtra("jumpMark", "NotificationReceiver");// 跳转标记
//				NotificationUtil.sendNotification(context, adIntent, "咨询评价", map.get("bizUserName")
//						+ "提醒您对对本次咨询进行评价...", Constant.COMMAND_CONSULTATION_REMIND);
//				break;
//			case Constant.COMMAND_NEW_MESSAGES:// 站内信
//					Intent nmIntent = new Intent(context, MessageRecvDetailsActivity.class);
//					nmIntent.putExtra("senderUserId", map.get("bizUserId").toString());
//					nmIntent.putExtra("receiveMessageId", map.get("bizId").toString());
//					NotificationUtil.sendNotification(context, nmIntent, "站内信", map.get("bizUserName") + "给您发了站内信...",
//							Constant.COMMAND_NEW_MESSAGES);
//					break;
//			case Constant.COMMAND_VISIT_REQUEST:// 文字回访邀请
//				Intent vrIntent;
//				vrIntent = new Intent(context, CallbackEvaluateActivity.class);
//				vrIntent.putExtra("recordId", map.get("bizId").toString());// 回访记录id
//				NotificationUtil.sendNotification(context, vrIntent, "回访邀请", map.get("bizUserName") + "邀请您进行信息回访...",
//						Constant.COMMAND_VISIT_REQUEST);
//				break;
//			case Constant.COMMAND_VISIT_QUEST:// 问卷回访邀请
//				Intent vqIntent;
//				vqIntent = new Intent(context, CallbackNaireActivity.class);
//				vqIntent.putExtra("recordId", map.get("bizId").toString());// 回访记录id
//				if(map.get("bizUserName").toString().equals("医院职工调查")){
//					NotificationUtil.sendNotification(context, vqIntent, "医院职工意见反馈调查", "", Constant.COMMAND_VISIT_QUEST_FOR_HOSP);
//				} else {
//					NotificationUtil.sendNotification(context, vqIntent, "回访邀请", map.get("bizUserName") + "邀请您进行问卷回访...",
//							Constant.COMMAND_VISIT_QUEST);
//				}
//				break;
//			case Constant.COMMAND_SERVER_NOTIFY:// 服务通知
//				Intent snIntent = new Intent(context, NotifyDetailActivity.class);
//				snIntent.putExtra("noticeId", map.get("bizId").toString());// 服务通知id
//				NotificationUtil.sendNotification(context, snIntent, "服务通知", map.get("bizUserName") + "给您发了一个通知...",
//						getServerId() + 10000);
//				break;
//			case Constant.COMMAND_QUEST_FOLLOW:// 随访推送: 问卷
//				Intent fi = new Intent(context, RecordInfoActivity.class);
//				fi.putExtra("type", 1);
//				fi.putExtra("followId", (String) map.get("bizId"));// 服务通知id
//				NotificationUtil.sendNotification(context, fi, "即时随访", map.get("bizUserName") + "给您发送了即时随访...",
//						getServerId() + 10000);
//				break;
//				case Constant.COMMAND_VOICE_FOLLOW:// 随访推送: 语音
//					Intent fiIntent = new Intent(context, RecordInfoActivity.class);
//					fiIntent.putExtra("type", 2);
//					fiIntent.putExtra("followId", (String) map.get("bizId"));// 服务通知id
//					NotificationUtil.sendNotification(context, fiIntent, "语音随访", map.get("bizUserName") + "给您发送了语音随访...",
//							getServerId() + 10000);
//					break;
//				case Constant.COMMAND_EXAM_REPORT:// 体检报告
//					Intent erIntent = new Intent(context, ExamReportAvtivity.class);
//					erIntent.putExtra("url", (String) map.get("url"));
//					NotificationUtil.sendNotification(context, erIntent, "体检报告", map.get("bizUserName") + "给您发送了体检报告...",
//							getServerId() + 10000);
//					break;
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 请求分发
	 * 
	 * @param title
	 * @return
	 */
	public static int getConmand(String title) {
		int result = 0;

//		if (title.equals("consultationAnswer")) {// 咨询回复
//			result = Constant.COMMAND_CONSULTATION_ANSWER;
//		} else if (title.equals("sendGuidances")) {// 健康指导
//			result = Constant.COMMAND_SEND_GUIDANCES;
//		} else if (title.equals("finishConsultation")) {// 咨询提醒
//			result = Constant.COMMAND_CONSULTATION_REMIND;
//		}else if (title.equals("visitMessages")) {// 回访邀请
//			result = Constant.COMMAND_VISIT_REQUEST;
//		} else if (title.equals("visitQuest")) {// 问卷回访
//			result = Constant.COMMAND_VISIT_QUEST;
//		} else if (title.equals("serverNotify")) {// 服务通知
//			result = Constant.COMMAND_SERVER_NOTIFY;
//		} else if (title.equals("questFollow")) {// 随访推送: 问卷
//			result = Constant.COMMAND_QUEST_FOLLOW;
//		} else if (title.equals("voiceFollow")) {// 随访推送: 语音
//			result = Constant.COMMAND_VOICE_FOLLOW;
//		} else if (title.equals("examReport")) {// 体检报告
//			result = Constant.COMMAND_EXAM_REPORT;
//		} else if (title.equals("newMessages")) {// 站内信
//			result = Constant.COMMAND_NEW_MESSAGES;
//		}
		return result;
	}

	private static int getServerId() {
		serverNotifyId++;
		return serverNotifyId > 10000 ? 0 : serverNotifyId;
	}

}
