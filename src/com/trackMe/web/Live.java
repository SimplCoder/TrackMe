package com.trackMe.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Live extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Context Path::: "+this.getServletContext().getContextPath());
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("username", (Object) request.getParameter("username"));
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(getHtmlFile("Index"));
		out.flush();
		System.out.println("Live_Success");

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("Context Path::: "+this.getServletContext().getContextPath());
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("username", (Object) request.getParameter("username"));
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(getHtmlFile("Index"));
		out.flush();
		System.out.println("Live_Success");
		
		
	}
	
	private String getHtmlFile(String fileName) {
		StringBuilder contentBuilder = new StringBuilder();
		try {
			File file = new File(getServletContext().getRealPath("")+"//html//"
					+ fileName + ".html");
			//File file=new File(this.getServletContext().getContextPath());
			if (file.exists()) {
				String str;
				System.out.println();
				BufferedReader in = new BufferedReader(new FileReader(file));
				while ((str = in.readLine()) != null) {
					contentBuilder.append(str);
				}
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String str = contentBuilder.toString();
		str = str.replace("%data%", "<p style='color:red'>Invalid UserName or Password, Please try again </p><br>");
		return contentBuilder.toString();
	}

}
