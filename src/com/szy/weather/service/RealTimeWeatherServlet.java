package com.szy.weather.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.szy.weather.util.WeatherUtils;

@WebServlet(urlPatterns={"/realtimeWeatherServlet"})    
public class RealTimeWeatherServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");  
		PrintWriter out = response.getWriter();
		
		String realTimeWeatherData = getRealTimeWeather();
System.out.println(realTimeWeatherData);		
		out.print(realTimeWeatherData);
	}
	
	/**��ȡʵʱ������*/
	public String getRealTimeWeather(){
		String realTimeWatherData = WeatherUtils.getRealTimeWeatherWeather("������");
		return realTimeWatherData;
	}
	
	
}
