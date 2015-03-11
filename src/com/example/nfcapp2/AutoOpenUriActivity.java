package com.example.nfcapp2;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.widget.Toast;

public class AutoOpenUriActivity extends Activity{
	
	/*
	 * ��Ա��������
	 */
	private NfcAdapter mNfcAdapter;
	private PendingIntent mPendingIntent;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auto_open_uri);
		
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		//һ���ػ�NFC����Ϣ���͵���PendingIntent�������
		mPendingIntent = PendingIntent.getActivity(this , 0 , new Intent(this , getClass()) , 0);
	}
	
	/*
	 * ��NFC��ǩ��д������Ҫ�ڸ÷���������
	 */
	private void writeNFCTag(Tag tag){
		if(tag == null){
			return;
		}
		
		NdefMessage ndefMessage = new NdefMessage( new NdefRecord[]{NdefRecord.createUri(Uri.parse("http://www.baidu.com"))} );
		int size = ndefMessage.toByteArray().length;
		
		try {
			Ndef ndef = Ndef.get(tag);
			//���ж�һ�������ǩ�ǲ���NDEF��
			if(ndef != null){ //�����NDEF��ʽ��
				ndef.connect();
				//�����ж������ǩ�Ƿ��ǿ�д��
				if( ! ndef.isWritable()){ //����ǲ���д�ģ�ֱ�ӾͿ��Խ�����
					Toast.makeText(this , "��NFC��ǩ����д!" , Toast.LENGTH_SHORT).show();
					return;
				}
				//�����жϵ�ǰ��ǩ����������Ƿ���װ������Ҫд�����Ϣ
				if(ndef.getMaxSize() < size){
					Toast.makeText(this , "��NFC��ǩ������д����̫С!" , Toast.LENGTH_SHORT).show();
					return;
				}
				//����Ϊֹ���Ϳ��Է��ĵİѶ���д��NFC��ǩ����
				ndef.writeNdefMessage(ndefMessage);
				Toast.makeText(this , "NFC��ǩд�����ݳɹ�" , Toast.LENGTH_SHORT).show();
			}
			else{ //�������NDEF��ʽ��
				//���Խ������NDEF��ǩ��ʽ����NDEF��ʽ��
				NdefFormatable format = NdefFormatable.get(tag);
				//��Ϊ��Щ��ǩ��ֻ���ģ�����������Ҫ�ж�һ��
				//���format��Ϊnull����ʾ�����ǩ�ǿ��Խ��ܸ�ʽ����
				if(format != null){
					format.connect();
					format.format(ndefMessage); //ͬʱ����˸�ʽ����д����Ϣ�Ĳ���
					Toast.makeText(this , "NFC��ǩ��ʽ��д��ɹ�" , Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(this , "��NFC��ǩ�޷�����ʽ��" , Toast.LENGTH_SHORT).show();
				}
			}
		} 
		catch (Exception e) {
			Toast.makeText(this , "�޷���ȡ��NFC��ǩ" , Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	/*
	 * ����launchMode����Ϊ��singleTop����singleTask����˶�δ򿪳����ʱ��onCreateֻ��ִ��һ�飬����
	 * ����NFC��Ϣ�Ĵ��ھ������onNewIntent()���������
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		//���Tag����
		Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		writeNFCTag(detectedTag);
	}
	
	
	
	
	
	
	
	
	/*
	 * ������Ҫʵ�ֵĻ���Ҫ����NFC�����ع��˻���
	 * �����RunApplicationActivity��������Ϊ��߽���NFC��Ϣ�����ȼ�
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		if(mNfcAdapter != null){
			//�����RunApplicationActivity��������Ϊ���ȼ����������ܴ���NFC��ǩ�Ĵ��ڣ�Ҳ���ǽ�RunApplicationActivity������Ϊջ��
			mNfcAdapter.enableForegroundDispatch(this , mPendingIntent , null , null);
		}
	}
	
	/*
	 * ����������������ʱ�򣬾Ͳ�����������ö���
	 */
	@Override
	protected void onPause() {
		super.onPause();
		
		if(mNfcAdapter != null){
			mNfcAdapter.disableForegroundDispatch(this);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
