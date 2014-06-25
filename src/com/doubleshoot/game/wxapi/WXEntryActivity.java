package com.doubleshoot.game.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.doubleshoot.game.GameStartActivity;
import com.doubleshoot.game.R;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private IWXAPI mApi;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	mApi = WXAPIFactory.createWXAPI(this, getResources().getString(R.string.wx_app_id), false);
        mApi.handleIntent(getIntent(), this);
    }
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        mApi.handleIntent(intent, this);
	}
		
	@Override
	public void onReq(BaseReq arg0) {
		Intent intent = new Intent(this, GameStartActivity.class);
		startActivity(intent);
		Log.d(WXEntryActivity.class.getSimpleName(), "Finished");
		finish();
	}

	@Override
	public void onResp(BaseResp resp) {
		int result = 0;

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		finish();
	}

}