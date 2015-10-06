package com.armysheng.ucare;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

import com.armysheng.ucare.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

//�ͻ��˵�ʵ��
public class login_socket extends BaseActivity {
protected static final int MSG_FAILURE = 0;
protected static final int MSG_SUCCESS = 1;
//	private TextView text1;
	private Button loginbtn,WBAuthBtn,QQauthBtn;
	private EditText usernameEditText;
	private final String DEBUG_TAG="mySocketAct";
	private String[] mstrSplit;
	private RelativeLayout RLay;
	private WeiboAuth mWeiboAuth;
	private SsoHandler mSsoHandler;
	private String serverIP;

    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;
    private TextView mTokenText;


	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ui);
        
        checkNetAll();

        loginbtn=(Button)findViewById(R.id.login_btn);
        usernameEditText=(EditText)findViewById(R.id.username_tx);
        RLay = (RelativeLayout) findViewById(R.id.RelativeLayout1);
        RLay.setBackgroundColor(00000000);
        
        WBAuthBtn=(Button)findViewById(R.id.btn_weibo);
        // 创建微博实例
        mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        
        // SSO 授权
        findViewById(R.id.btn_weibo).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
          
                mSsoHandler = new SsoHandler(login_socket.this, mWeiboAuth);
                mSsoHandler.authorize(new AuthListener());
               }
        });
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
            updateTokenView(true);
            Intent intent = new Intent();  
            intent.setClass(login_socket.this, PlayVedio.class); 
            intent.putExtra("rtspAddr", "from weibo");  //�������  
            finish();
            startActivity(intent);
        }
        
        
     // Tencent login
        QQauthBtn = (Button)findViewById(R.id.btn_qq);

        QQauthBtn.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {

        		initTencent();
        		login();
        	}
        });
        
        
//        本地按钮
        
        loginbtn.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v) {
        		
				new Thread(loginTd).start();			//

			}        	
        });
    }
    Runnable loginTd = new Runnable() {
		public void run() {
			try {
			    serverIP="222.199.230.30";
				String mesg=usernameEditText.getText().toString()+"\r\n";
				Socket socket=null;
//				socket=new Socket("121.195.143.120",12121);
				socket=new Socket(serverIP,12121);
//				socket=new Socket("222.199.230.218",12121);
//				socket=new Socket("172.31.6.156",12121);
				//�������������Ϣ
				PrintWriter out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
				out.println(mesg);
				
				//���ܷ���������Ϣ
				BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String mstr=br.readLine();
				System.out.println(mstr);
				 mstrSplit = mstr.split(",");
				if(mstrSplit[0].equals("login_success"))
				{
					mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
					
				}else
				{
					mHandler.obtainMessage(MSG_FAILURE).sendToTarget();
					
				}
				out.close();
				br.close();
				socket.close();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch(Exception e)
			{
				Log.e(DEBUG_TAG,e.toString());
			}
			
		}
	};
    


    

	 public boolean checkNetwork() {
			
			// ʵ��ConnectivityManager
			
			ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			// ��õ�ǰ������Ϣ
			NetworkInfo info = manager.getActiveNetworkInfo();
			// �ж��Ƿ�����
			if (info == null || !info.isConnected()) {
				return false;
				}
			return true;
				}
	 private void checkNetAll(){
		 if(checkNetwork()==false){
	    	    Toast.makeText(login_socket.this, "没有网络呦，亲", Toast.LENGTH_LONG).show();   //������ֻ����һ����ʾ���ɸ��������ó�������ʽ��
	    	  }
	    	  
	    	}
	private Handler mHandler = new Handler() {  
	        public void handleMessage (Message msg) {//�˷�����ui�߳�����  
	            switch(msg.what) {  
	            case MSG_SUCCESS:   
	            	Toast.makeText(login_socket.this, "登陆成功，跳转中...", Toast.LENGTH_LONG).show();
	            	Intent intent = new Intent();  
	                intent.setClass(login_socket.this, PlayVedio.class);  //��login_socket��ת��MainActivity  
	                intent.putExtra("rtspAddr", mstrSplit[1]);  //�������  
	                finish();
	                startActivity(intent);  //��ʼ��ת  
	                break;  
	  
	            case MSG_FAILURE:  
	            	Toast.makeText(login_socket.this, "没有该号码，请检查你的手机号~~", Toast.LENGTH_LONG).show();  
	                break;  
	            }  
	        }  
	    };  

   

/**
 * 微博认证授权回调类。
 * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
 *    该回调才会被执行。
 * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
 * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
 */
class AuthListener implements WeiboAuthListener {
    
    public void onComplete(Bundle values) {
        // 从 Bundle 中解析 Token
        mAccessToken = Oauth2AccessToken.parseAccessToken(values);
        if (mAccessToken.isSessionValid()) {
            // 显示 Token
            updateTokenView(false);
            
            // 保存 Token 到 SharedPreferences
            AccessTokenKeeper.writeAccessToken(login_socket.this, mAccessToken);
            Toast.makeText(login_socket.this, 
                    R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();
        } else {
            // 以下几种情况，您会收到 Code：
            // 1. 当您未在平台上注册的应用程序的包名与签名时；
            // 2. 当您注册的应用程序包名与签名不正确时；
            // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
            String code = values.getString("code");
            String message = getString(R.string.weibosdk_demo_toast_auth_failed);
            if (!TextUtils.isEmpty(code)) {
                message = message + "\nObtained the code: " + code;
            }
            Toast.makeText(login_socket.this, message, Toast.LENGTH_LONG).show();
        }
    }

    public void onCancel() {
        Toast.makeText(login_socket.this, 
                R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
    }

    public void onWeiboException(WeiboException e) {
        Toast.makeText(login_socket.this, 
                "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
    }
}
/**
 * 显示当前 Token 信息。
 * 
 * @param hasExisted 配置文件中是否已存在 token 信息并且合法
 */
private void updateTokenView(boolean hasExisted) {
    String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
            new java.util.Date(mAccessToken.getExpiresTime()));
    String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
    mTokenText.setText(String.format(format, mAccessToken.getToken(), date));
    
    String message = String.format(format, mAccessToken.getToken(), date);
    if (hasExisted) {
        message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
    }
    mTokenText.setText(message);
}

///**
// * 当 SSO 授权 Activity 退出时，该函数被调用。
// * 
// * @see {@link Activity#onActivityResult}
// */
//@Override
//public void onActivityResult(int requestCode, int resultCode, Intent data) {
//    super.onActivityResult(requestCode, resultCode, data);
//    
//    // SSO 授权回调
//    // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
//    if (mSsoHandler != null) {
//        mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
//        Intent intent = new Intent();  
//        intent.setClass(login_socket.this, PlayVedio.class); 
//        intent.putExtra("rtspAddr", "from weibo");  //�������  
//        finish();
//        startActivity(intent);
//    }
//}
@Override
public void showMessage(String msg) {
	// TODO Auto-generated method stub
	
}


}