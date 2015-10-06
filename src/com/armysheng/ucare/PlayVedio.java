package com.armysheng.ucare;


import java.text.DateFormat;
import java.util.Date;

import com.armysheng.ucare.R;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class PlayVedio extends Activity  implements OnClickListener{

	String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
	ImageButton btnplay, btnstop, btnpause,btnopen,btnScreenshot;
	String vedioURL;
	SurfaceView surfaceView;
	MediaPlayer mediaPlayer;
	EditText editText;
	int position;
	//private Context self;
	
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		 Intent intent = this.getIntent();    //��õ�ǰ��Intent  
	     Bundle bundle = intent.getExtras();  //���ȫ�����  
	     vedioURL = bundle.getString("rtspAddr");  //�����ΪrtspAddr��ֵ 
	     
	     mediaPlayer=new MediaPlayer();
		 surfaceView=(SurfaceView) this.findViewById(R.id.surfaceView);
		
	 	     
//		btnplay=(ImageButton)this.findViewById(R.id.btnplay);
		btnstop=(ImageButton)this.findViewById(R.id.btnstop);
//		btnpause=(ImageButton)this.findViewById(R.id.btnpause);
//		btnopen=(ImageButton)this.findViewById(R.id.btnopen);
//		btnScreenshot=(ImageButton)this.findViewById(R.id.btnscreenshot);
		
		btnstop.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
		
//		btnScreenshot.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				saveScreenshot();
//			}
//			
//			String mScreenshotPath = Environment.getExternalStorageDirectory() + "/droidnova";
//
//			public void saveScreenshot() {
//			    if (ensureSDCardAccess()) {
//			        Bitmap bitmap = Bitmap.createBitmap(300, 405, Bitmap.Config.ARGB_8888);
//			        Canvas canvas = new Canvas(bitmap);
//			        canvas.drawBitmap(bitmap, 0, 0, null);
////			        doDraw(1, canvas);
//			        File file = new File(mScreenshotPath + "/" + currentDateTimeString + ".jpg");
//			        FileOutputStream fos;
//			        try {
//			            fos = new FileOutputStream(file);
//			            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//			            fos.close();
//			        } catch (FileNotFoundException e) {
//			            Log.e("Panel", "FileNotFoundException", e);
//			        } catch (IOException e) {
//			            Log.e("Panel", "IOEception", e);
//			        }
//			    }
//			}
//
//			private boolean ensureSDCardAccess() {
//			    File file = new File(mScreenshotPath);
//			    if (file.exists()) {
//			        return true;
//			    } else if (file.mkdirs()) {
//			        return true;
//			    }
//			    return false;
//			}
//		
//
//		});
		
		
	
//		btnplay.setOnClickListener(this);
//		btnpause.setOnClickListener(this);
//		btnopen.setOnClickListener(this);
		
		Toast.makeText(PlayVedio.this, "正在为你缓冲视频", Toast.LENGTH_LONG).show();
		
		//����SurfaceView�Լ�������Ļ�����
		surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);		
		surfaceView.getHolder().addCallback(new Callback() {		
			public void surfaceDestroyed(SurfaceHolder holder) {
		
			}
		
			public void surfaceCreated(SurfaceHolder holder) {
//				if (position>0) {
					try {
						//��ʼ����
						play();
						//��ֱ�Ӵ�ָ��λ�ÿ�ʼ����
						mediaPlayer.seekTo(position);
						position=0;						
					} catch (Exception e) {
						// TODO: handle exception
					}
//				}
			}			
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {

			}
		});		
//		 play();
	}

	
	
//	public void onClick(View v) {	
//		switch (v.getId()) {
//		case R.id.btnopen:
//			System.out.println("open");
//			open();
//			break;
//			
//		case R.id.btnplay:
//			play();
//			break;
//			
//		case R.id.btnpause:
//			if (mediaPlayer.isPlaying()) {
//				mediaPlayer.pause();
//			}
//			else{
//				mediaPlayer.start();
//			}
//			break;
//
//		case R.id.btnstop:
//			if (mediaPlayer.isPlaying()) {
//				mediaPlayer.stop();
//			}
//			
//			break;
//		default:
//			break;
//		}
//
//	}
//	@Override
//	protected void onPause() {	
//		//���ж��Ƿ����ڲ���
//		if (mediaPlayer.isPlaying()) {
//			//������ڲ������Ǿ��ȱ����������λ��
//			position=mediaPlayer.getCurrentPosition();
//			mediaPlayer.stop();
//		}
//		super.onPause();
//	}
//	private void open(){
//		try {
//			 
//			 new AlertDialog.Builder(this)
//			.setTitle("������rtsp����ַ�˿�,���磺rtsp://")
//			.setIcon(android.R.drawable.ic_dialog_info)
//			.setView(editText=new EditText(this))
//			.setPositiveButton("��", 
//				new DialogInterface.OnClickListener() {
//				
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					vedioURL=editText.getText().toString();
//					play();
//							}
//			})
//			.setNegativeButton("ȡ��", null)
//			.show();
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		
//	}

	private void play() {
		try {
			System.out.println("paly"+vedioURL);
			mediaPlayer.reset();
//			 mediaPlayer.setWakeMode(null, position) ; 
//			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			//������Ҫ���ŵ���Ƶ
//			  mediaPlayer.setDataSource(vedioURL);
			//mediaPlayer.setDataSource("rtsp://192.168.1.88/2?admin:admin");//ֱ�Ӷ������
//			  mediaPlayer.setDataSource("rtsp://172.31.6.7:8080/test.sdp");//��������
//			  mediaPlayer.setDataSource("rtsp://58.200.131.2/BTV-9");
//			  mediaPlayer.setDataSource("rtsp://211.139.194.251:554/live/2/13E6330A31193128/5iLd2iNl5nQ2s8r8.sdp");
			  mediaPlayer.setDataSource("rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp");

			
			//����Ƶ���������SurfaceView
			mediaPlayer.setDisplay(surfaceView.getHolder());
			mediaPlayer.prepare();
			//����
			mediaPlayer.start();		
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	
}
