package com.szy.weather.util;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.szy.weather.domain.Index;
import com.szy.weather.domain.Results;
import com.szy.weather.domain.WeatherData;
import com.szy.weather.domain.WeatherResult;


public class WeatherUtils {

	/**
	 * get请求
	 * @param url:请求地址
	 * @return
	 */
	public static JSONObject dogetString(String url){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		//
		httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);
		
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);//执行get请求
			HttpEntity entity = response.getEntity();//获取请求结果,类型是HttpEntity
			if(entity != null){
				String result = EntityUtils.toString(entity,"UTF-8");//entity转为String
				jsonObject = JSONObject.fromObject(result);//String转为json格式
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httpGet.releaseConnection();//释放连接
		}
		return jsonObject;
	}

	
	/**
	 * post请求
	 * @param url:请求地址
	 * @param outStr:请求参数
	 * @return
	 */
	public static JSONObject dopostString(String url, String outStr){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		try {
			httpPost.setEntity(new StringEntity(outStr,"UTF-8"));//设置请求参数,参数类型为HttpEntity
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if(entity != null){
				String result = EntityUtils.toString(entity);
				jsonObject = JSONObject.fromObject(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			httpPost.releaseConnection();//释放连接
		}
		return jsonObject;
	}


	
	
	//天气查询
	public static String getWeather(String city){
		String weather_url = "http://api.map.baidu.com/telematics/v3/weather?location=CITY&output=json&ak=AK";//ak不等于Secret Key
		String url = weather_url.replace("CITY",city).replace("AK", "kgl3w0hL9BLBB8Gpc5QqGSF7");
		JSONObject jsonResult = dogetString(url);	
		String status = jsonResult.getString("status");
		StringBuffer sb = new StringBuffer();
		if("success".equals(status)){ 
			WeatherResult weatherResult = (WeatherResult) JSONObject.toBean(jsonResult, WeatherResult.class);
			Results results = weatherResult.getResults()[0];
			Index[] index = results.getIndex();
			WeatherData[] weather_data = results.getWeather_data();
			String currentCity = results.getCurrentCity();  
			 
			String radiation = index[5].getDes();//紫外线建议
			String pm25 = results.getPm25();
			String pm25Des = "";
			if(!"".equals(pm25)){
				int pm25Num = Integer.parseInt(pm25);
				pm25Des = pm25Num<50?"一级,优,绿色":pm25Num<100?"二级,良,黄色":pm25Num<150?"三级,轻度污染,橙色":pm25Num<200?"四级,中度污染 ,红色":pm25Num<300?"五级,重度污染 ,紫色":"六级,严重污染, 褐红色";
			}
			
			//今天：
			String weather0 = weather_data[0].getWeather();
			String temeperature0 = weather_data[0].getTemperature();
			String wind0 = weather_data[0].getWind();
			sb.append("["+currentCity+"]<br>今　天:　").append(weather0+";").append(temeperature0+";")
			  .append(wind0);
			if(!"".equals(pm25Des)){
				sb.append("<br>　　　　&nbsp;PM2.5指数:　"+pm25+","+pm25Des+"<br>");    
			}else{
				sb.append("<br>");  
			}
			//明天
			String weather1 = weather_data[1].getWeather();
			String temeperature1 = weather_data[1].getTemperature();
			String wind1 = weather_data[1].getWind(); 
			sb.append("明　天:　"+weather1+";"+temeperature1+";"+wind1+"<br>");
			//后天
			String weather2 = weather_data[2].getWeather();
			String temeperature2 = weather_data[2].getTemperature();
			String wind2 = weather_data[2].getWind(); 
			sb.append("后　天:　"+weather2+";"+temeperature2+";"+wind2+"<br>");
			
			//大后天
			if(weather_data.length==4){
				String weather3 = weather_data[3].getWeather();
				String temeperature3 = weather_data[3].getTemperature();
				String wind3 = weather_data[3].getWind(); 
				sb.append("大后天:　"+weather3+";"+temeperature3+";"+wind3+"<br>");
			}
			
			//今日建议
			sb.append("<br>今日建议:　"+radiation);      
		}
		return sb.toString();
	}
	
	
	public static String getRealTimeWeatherWeather(String city){
		String weather_url = "http://api.map.baidu.com/telematics/v3/weather?location=CITY&output=json&ak=AK";//ak不等于Secret Key
		String url = weather_url.replace("CITY",city).replace("AK", "kgl3w0hL9BLBB8Gpc5QqGSF7");
		JSONObject jsonResult = dogetString(url);	
		String status = jsonResult.getString("status");
		StringBuffer sb = new StringBuffer();
		if("success".equals(status)){ 
			WeatherResult weatherResult = (WeatherResult) JSONObject.toBean(jsonResult, WeatherResult.class);
			Results results = weatherResult.getResults()[0];
			WeatherData[] weather_data = results.getWeather_data();
			String pm25 = results.getPm25();
			String temeperature0 = weather_data[0].getTemperature();
			
			int indexL = temeperature0.indexOf("~");
			int indexH = temeperature0.indexOf("℃");
			String temeperatureL = temeperature0.substring(0, indexL);
			String temeperatureH = temeperature0.substring(indexL+1, indexH);
			float l = Float.parseFloat(temeperatureL);
			float h = Float.parseFloat(temeperatureH);
			String temp = (l+h)/2+"";
			
			sb.append("当前实时温度:　").append(temp+"℃<br>")
			  .append("　　实时P&nbsp;M:　").append(pm25);
			
		}
		return sb.toString();
	}
	
	
	
}
