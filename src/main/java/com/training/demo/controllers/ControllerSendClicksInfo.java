package com.training.demo.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.training.demo.entity.Click;
import com.training.demo.entity.MousePositionData;
import com.training.demo.entity.Response;
import com.training.demo.entity.User;

@RestController
@RequestMapping("/clickManage")
public class ControllerSendClicksInfo {
	
	private static final String urlDb = "jdbc:clickhouse://localhost:8123/clicksDB";
	private Connection connect;
	
	@GetMapping(value = "/getMoveMouseData")
	public String getMoveMouseInfo() throws SQLException
	{
		connect = DriverManager.getConnection(urlDb);
		String query = "SELECT * FROM moveMouse";
		Statement statement = connect.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		return "Данные по кликам были занесены в логовый файл";
	}
	
	@GetMapping(value = "/getClicksData")
	public String getClicksInfo() throws SQLException
	{
		connect = DriverManager.getConnection(urlDb);
		String query = "SELECT * FROM clicks";
		Statement statement = connect.createStatement();
		ResultSet resultSet = statement.executeQuery(query);	
		return "Данные по кликам были занесены в логовый файл";
	}
	
	@GetMapping(value = "/getUserData")
	public String getUserInfo() throws SQLException
	{
		connect = DriverManager.getConnection(urlDb);
		String query = "SELECT * FROM usrData";
		Statement statement = connect.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		return "Данные по пользователям скопированы в логовый файл";
	}
	
	@PostMapping(value = "/sendClickData")
	public Response sendInfo(@RequestBody Click click) throws SQLException
	{
		Response response = new Response("Done", click);

		connect = DriverManager.getConnection(urlDb);
		String query = "insert into clicks values (toDate(now())," +
				" toDateTime(now()), '" + click.getNamePage() + "'," +
				" '" + click.getNameBanner() + "', " + click.getHeightBanner() + 
				", " + click.getWidthBanner() + ", " + click.getClickedCoordX() + 
				", " + click.getClickedCoordY() + "," +
				" '" + click.getIpClient() + "');";
		Statement statement = connect.createStatement();
		statement.executeQuery(query);
		System.out.println("Info about click sended to db");
		return response;
	}
	
	@PostMapping(value = "/sendUserData")
	public Response sendUserData(@RequestBody User user) throws SQLException
	{
		Response response = new Response("Done", user);
		int browserOnline = user.isBrowserOnline() ? 1 : 0;
		int cookieEnabled = user.isCookiesEnabled() ? 1 : 0;
		
		connect = DriverManager.getConnection(urlDb);
		String query = "insert into usrData values (toDate(now())," +
					" '" + user.getIpUser() + "', " + "'" + user.getBrowserCodeName() + "', " +
					"'" + user.getBrowserName() + "', " + "'" + user.getBrouserVersion() + "', " +
					cookieEnabled + ", '" + user.getBrowserLanguage() + "', " +
					browserOnline + ", '" + user.getPlatform() + "', '" + 
					user.getUserAgentHeader() + "');";
		Statement statement = connect.createStatement();
		statement.executeQuery(query);
		System.out.println("Info about user sended to db");
		return response;
	}
	
	@PostMapping(value = "/sendMoveMouseData")
	public Response sendMoveMousePosition(@RequestBody MousePositionData mousePositionData) throws SQLException
	{
		Response response = new Response("Done", mousePositionData);
		
		connect = DriverManager.getConnection(urlDb);
		String query = "insert into moveMouse values (toDate(now()), '" + mousePositionData.getPageName() +
				"', " + mousePositionData.getMouseCoordX() + ", " + mousePositionData.getMouseCoordY() + ", '" + 
				mousePositionData.getIpClient() + "');";
		Statement statement = connect.createStatement();
		statement.executeQuery(query);
		System.out.println("Mouse position added to database");
		return response;
	}
	
}
