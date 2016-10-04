package com.example.xiaoran.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Date;

import com.example.xiaoran.bean.ChatMessage;
import com.example.xiaoran.bean.ChatMessage.Type;
import com.example.xiaoran.bean.Result;
import com.google.gson.Gson;

public class HttpUtils {
	private static final String URL="http://www.tuling123.com/openapi/api";
	private static final String ApiKey="";  //图灵机器人ApiKey
	public static ChatMessage sendMessage(String msg){
		ChatMessage chatMessage =new ChatMessage();
		String jsonRes =doGet(msg);
		Gson gson=new Gson();
		Result result= null;
		try {
			result=gson.fromJson(jsonRes, Result.class);
			chatMessage.setMsg(result.getText());
		} catch (Exception e) {
			chatMessage.setMsg("没联网就别跟我瞎BB~");
		}
		chatMessage.setDate(new Date());
		chatMessage.setType(Type.INCOMING);
		return chatMessage;
	}
	public static String doGet(String msg){
		String result="";
		String url=setParams(msg);
		InputStream is=null;
		int len;
		ByteArrayOutputStream baos=null;
		
		try {
			java.net.URL urlNet=new java.net.URL(url);
			HttpURLConnection conn=(HttpURLConnection) urlNet.openConnection();
			conn.setReadTimeout(5*1000);
			conn.setConnectTimeout(5*1000);
			conn.setRequestMethod("GET");
			 is=conn.getInputStream();
			len=-1;
			byte[] buf=new byte[128];
			baos=new ByteArrayOutputStream();
			while((len=is.read(buf))!=-1){
				baos.write(buf, 0, len);
			}
			baos.flush();
			result=new String(baos.toByteArray());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
				try {
					if(baos!=null)
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					if(is!=null){is.close();}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return result;	
	}
	private static String setParams(String msg) {
		String url="";
		try {
			url = URL+"?key="+ApiKey+"&info="+URLEncoder.encode(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
	}
}
