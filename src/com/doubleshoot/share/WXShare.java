package com.doubleshoot.share;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.doubleshoot.game.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.platformtools.Util;

public class WXShare {
	private static final int THUMB_SIZE = 150;
	private IWXAPI mApi;
	private Activity mContext;
	
	public WXShare(Activity pContext) {
		mContext = pContext;
		mApi = WXAPIFactory.createWXAPI(pContext, getString(R.string.wx_app_id), true);
	}
	
	private String getString(int stringid) {
		return mContext.getResources().getString(stringid);
	}
	
	public void share(Bitmap bmp, int leftScore, int rightScore) throws IOException {
		if (!mApi.isWXAppInstalled()) {
			onWXNotInstalled();
			return;
		}
		//
		mApi.registerApp(getString(R.string.wx_app_id));
		//
		WXMediaMessage msg = new WXMediaMessage();
		WXImageObject imgObj = new WXImageObject(bmp);
		msg.mediaObject = imgObj;
		msg.title = getString(R.string.share_title);
		msg.description = createText(leftScore, rightScore);

		Bitmap thumbBmp = Bitmap.createScaledBitmap(
				bmp, THUMB_SIZE, THUMB_SIZE, true);
		msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("image");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		mApi.sendReq(req);
	}
	
	private void onWXNotInstalled() {
		mContext.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(mContext, "未安装微信", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private String createText(int left, int right) {
		if (left <= Constants.LOW_SCORE && right >= Constants.HIGH_SCORE) {
			return "我的左手无能(" + left + ")，但是我的右手有啊(" + right + ")。" ;
		}
		
		if (left >= Constants.HIGH_SCORE && right <= Constants.LOW_SCORE) {
			return "我的右手无能(" + right + ")，但是我的左手有啊(" + left + ")。" ;
		}
		
		if (left <= Constants.LOW_SCORE && right <= Constants.LOW_SCORE) {
			return "打成这样，我也不想啊。(L:" + left + ", R:" + right + ")";
		}
		
		if (left >= Constants.HIGH_SCORE && right >= Constants.HIGH_SCORE) {
			return "就算你夸我，我也不会高兴的。(L:" + left + ", R:" + right + ")";
		}
		
		if (left == 0 && right == 0) {
			return "发生了什么？！！！请叫我双零射击！";
		}
		
		return "我是来晒战斗力的(L：" + left + "R：" + right + ")";
	}
	
	private String buildTransaction(final String type) {
		return type + System.currentTimeMillis();
	}
}
