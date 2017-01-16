/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.trackMe.web;

import com.trackMe.mapper.Alert;
import com.trackMe.mapper.DataObjectImpl;
import com.trackMe.mapper.DriverMaster;
import com.trackMe.mapper.Location;
import com.trackMe.mapper.Movement;
import com.trackMe.mapper.Route;
import com.trackMe.mapper.RouteSchedule;
import com.trackMe.mapper.VehicleMaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TrackMeServlet extends HttpServlet {
	private static final long serialVersionUID = 1;
	private static Map<String, Map<String, String>> configs;
	private static Map<Integer, String> statusCodes;

	public void init() {
		this.loadStatusCodes();
	}

	private void loadConfigs() {
		configs = new HashMap<String, Map<String, String>>();
		File file = new File("./conf/flow-config.xml");
		if (file.exists()) {
			try {
				FileInputStream in = new FileInputStream(file);
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.parse(in);
				Element element = (Element) document.getElementsByTagName("forms").item(0);
				NodeList formList = element.getElementsByTagName("form");
				int i = 0;
				while (i < formList.getLength()) {
					HashMap<String, String> formMap = new HashMap<String, String>();
					Element form = (Element) formList.item(i);
					formMap.put("action", form.getAttribute("action"));
					formMap.put("html", form.getAttribute("html"));
					formMap.put("model", form.getAttribute("model"));
					formMap.put("module", form.getAttribute("module"));
					formMap.put("validate", form.getAttribute("validate"));
					configs.put(form.getAttribute("action"), formMap);
					++i;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		try {
			if (action != null && action.equals("Login")) {
				if (this.validate(request)) {
					HttpSession httpSession = request.getSession();
					httpSession.setAttribute("username", (Object) request.getParameter("username"));
					response.setContentType("text/html");
					PrintWriter out = response.getWriter();
					out.println(this.getHtmlFile("Index"));
					out.flush();
				} else {
					response.setContentType("text/html");
					PrintWriter out = response.getWriter();
					out.println(this.getHtmlFile("LoginError"));
					out.flush();
				}
			} else if (action != null && action.equals("Save")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.saveVehicle(request);
				String html = this.editVehicle(request.getParameter("vehicleNo"));
				html = html.replaceAll("css/css.css", "html/css/css.css");
				html = html.replaceAll("jQueryAssets/", "html/jQueryAssets/");
				html = html.replaceAll("VehicleMasterView.html", "html/VehicleMasterView.html");
				out.println(html);
				out.flush();
			} else if (action != null && action.equals("Add Vehicle")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.addVehicle(request);
				String html = this.editVehicle(request.getParameter("vehicleNo"));
				html = html.replaceAll("css/css.css", "html/css/css.css");
				html = html.replaceAll("jQueryAssets/", "html/jQueryAssets/");
				html = html.replaceAll("VehicleMasterView.html", "html/VehicleMasterView.html");
				out.println(html);
				out.flush();
			} else if (action != null && action.equals("Add Alert")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.addAlert(request);
				String html = this.editAlert(request.getParameter("vehicleNo"));
				out.println(html);
				out.flush();
			} else if (action != null && (action.equals("Add Driver") || action.equals("Save Driver"))) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.addDriver(request);
				String html = this.getHtmlFile("Driver_master_view");
				html = html.replace("css/css.css", "html/css/css.css");
				html = html.replace("Driver_master_entry.html", "html/Driver_master_entry.html");
				out.println(html);
				out.flush();
			} else if (action != null && action.equals("Save Alert")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.saveAlert(request);
				String html = this.editAlert(request.getParameter("vehicleNo"));
				out.println(html);
				out.flush();
			} else if (action != null && action.equals("Save User")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.saveUser(request);
				String html = this.getHtmlFile("user_master_vew");
				html = html.replace("css/css.css", "html/css/css.css");
				out.println(html);
				out.flush();
			} else if (action != null && action.equals("Add Group")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.addGroup(request);
				String html = this.getHtmlFile("group_master_view");
				html = html.replace("css/css.css", "html/css/css.css");
				html = html.replace("group_master_entry.html", "html/group_master_entry.html");
				out.println(html);
				out.flush();
			} else if (action != null && action.equals("Save Group")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.saveGroup(request);
				String html = this.getHtmlFile("group_master_view");
				html = html.replace("css/css.css", "html/css/css.css");
				html = html.replace("group_master_entry.html", "html/group_master_entry.html");
				out.println(html);
				out.flush();
			} else if (action != null && action.equals("Add Movement")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.addMovement(request);
				String html = this.getHtmlFile("No_movements_view");
				html = html.replace("css/css.css", "html/css/css.css");
				html = html.replace("No_movements_entry.html", "html/No_movements_entry.html");
				out.println(html);
				out.flush();
			} else if (action != null && action.equals("Save Movement")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.saveMovement(request);
				String html = this.getHtmlFile("No_movements_view");
				html = html.replace("css/css.css", "html/css/css.css");
				html = html.replace("No_movements_entry.html", "html/No_movements_entry.html");
				out.println(html);
				out.flush();
			} else if (action != null && action.equals("Add Location")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.addLocation(request);
				String html = this.getHtmlFile("location_manage_entry");
				html = html.replace("css/css.css", "html/css/css.css");
				out.println(html);
				out.flush();
			} else if (action != null && action.equals("Save Location")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.saveLocation(request);
				String html = this.getHtmlFile("location_master_show");
				html = html.replace("css/css.css", "html/css/css.css");
				html = html.replace("location_master_show.html", "html/location_master_show.html");
				out.println(html);
				out.flush();
			} else if (action != null && action.equals("Add Route")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.addRoute(request);
				String html = this.getHtmlFile("route_master_view");
				html = html.replace("css/css.css", "html/css/css.css");
				html = html.replace("route_master_entry.html", "html/route_master_entry.html");
				html = html.replace("js/route_master_view.js", "html/js/route_master_view.js");
				out.println(html);
				out.flush();
			} else if (action != null && action.equals("Save Route")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.saveRoute(request);
				String html = this.getHtmlFile("route_master_view");
				html = html.replace("css/css.css", "html/css/css.css");
				html = html.replace("route_master_entry.html", "html/route_master_entry.html");
				html = html.replace("js/route_master_view.js", "html/js/route_master_view.js");
				out.println(html);
				out.flush();
			} else if (action != null && action.equals("Update Location")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.saveLocation(request);
				String html = this.getHtmlFile("location_manage_entry");
				html = html.replace("css/css.css", "html/css/css.css");
				out.println(html);
				out.flush();
			} else if (action != null && (action.equals("Delete Location") || action.equals("Manage Location"))) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.deleteLocation(request.getParameter("locationname"));
				String html = this.getHtmlFile("location_manage_entry");
				html = html.replace("css/css.css", "html/css/css.css");
				out.println(html);
				out.flush();
			} else if (action != null && action.equals("Add Schedule")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				this.addSchedule(request);
				String html = this.getHtmlFile("route_schedule_view");
				html = html.replace("css/css.css", "html/css/css.css");
				html = html.replace("route_scheduling_entry.html", "html/route_scheduling_entry.html");
				out.println(html);
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addSchedule(HttpServletRequest request) {
		try {
			RouteSchedule routeSchedule = this.getSchedule(request);
			DataObjectImpl dataObjectImpl = new DataObjectImpl(routeSchedule);
			routeSchedule.setCreatedby((String) request.getSession().getAttribute("username"));
			routeSchedule.setCreatedDate(new java.util.Date());
			try {
				Connection connection = this.getConnection();
				Statement statement = connection.createStatement();
				String query = dataObjectImpl.insert();
				statement.executeQuery(query);
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println(e.getMessage());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private RouteSchedule getSchedule(HttpServletRequest request) {
		RouteSchedule routeSchedule = new RouteSchedule();
		routeSchedule.setScheduleName(request.getParameter("scheduleName"));
		routeSchedule.setRouteName(request.getParameter("routename"));
		int count = Integer.parseInt(request.getParameter("rows"));
		String[] locations = new String[count];
		int i = 1;
		while (i <= count) {
			if (request.getParameter("vehicle" + i) != null) {
				locations[i - 1] = request.getParameter("vehicle" + i);
			}
			++i;
		}
		routeSchedule.setVehicleNo(locations);
		routeSchedule
				.setAlertbymail(request.getParameter("email") != null && request.getParameter("email").equals("true"));
		routeSchedule.setAlertbysms(request.getParameter("sms") != null && request.getParameter("sms").equals("true"));
		routeSchedule
				.setMonday(request.getParameter("monday") != null && request.getParameter("monday").equals("true"));
		routeSchedule
				.setTuesday(request.getParameter("tuesday") != null && request.getParameter("tuesday").equals("true"));
		routeSchedule.setWednesday(
				request.getParameter("wednesday") != null && request.getParameter("wednesday").equals("true"));
		routeSchedule.setThursday(
				request.getParameter("thursday") != null && request.getParameter("thursday").equals("true"));
		routeSchedule
				.setFriday(request.getParameter("friday") != null && request.getParameter("friday").equals("true"));
		routeSchedule.setSaturday(
				request.getParameter("saturday") != null && request.getParameter("saturday").equals("true"));
		routeSchedule
				.setSunday(request.getParameter("sunday") != null && request.getParameter("sunday").equals("true"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			routeSchedule
					.setEndDate(request.getParameter("endDate") != null && !request.getParameter("endDate").equals("")
							? formatter.parse(request.getParameter("endDate")) : null);
			routeSchedule.setStartDate(
					request.getParameter("startDate") != null && !request.getParameter("startDate").equals("")
							? formatter.parse(request.getParameter("startDate")) : null);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		routeSchedule.setEndTime(request.getParameter("endTime"));
		routeSchedule.setStartTime(request.getParameter("startTime"));
		return routeSchedule;
	}

	private void saveRoute(HttpServletRequest request) {
		int count = Integer.parseInt(request.getParameter("rows"));
		String[] locations = new String[count];
		int i = 1;
		while (i <= count) {
			if (request.getParameter("route" + i) != null) {
				locations[i - 1] = request.getParameter("route" + i);
			}
			++i;
		}
		Route route = new Route();
		route.setRouteName(request.getParameter("routename"));
		route.setLocations(locations);
		route.setUsername((String) request.getSession().getAttribute("username"));
		route.setModifiedBy((String) request.getSession().getAttribute("username"));
		route.setModifiedDate(new java.util.Date());
		DataObjectImpl dataObjectImpl = new DataObjectImpl(route);
		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			String query = dataObjectImpl.update(" routename = '" + request.getParameter("routename") + "' ;");
			query = query.replace(",  where", "where");
			statement.executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	private void addRoute(HttpServletRequest request) {
		int count = Integer.parseInt(request.getParameter("rows"));
		String[] locations = new String[count];
		int i = 1;
		while (i <= count) {
			if (request.getParameter("route" + i) != null) {
				locations[i - 1] = request.getParameter("route" + i);
			}
			++i;
		}
		Route route = new Route();
		route.setRouteName(request.getParameter("routename"));
		route.setLocations(locations);
		route.setUsername((String) request.getSession().getAttribute("username"));
		route.setCreatedBy((String) request.getSession().getAttribute("username"));
		route.setCreatedDate(new java.util.Date());
		DataObjectImpl dataObjectImpl = new DataObjectImpl(route);
		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			String query = dataObjectImpl.insert();
			statement.executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	private void addLocation(HttpServletRequest request) {
		try {
			Location location = this.getLocation(request);
			DataObjectImpl dataObjectImpl = new DataObjectImpl(location);
			try {
				Connection connection = this.getConnection();
				Statement statement = connection.createStatement();
				String query = dataObjectImpl.insert();
				statement.executeQuery(query);
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println(e.getMessage());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void saveLocation(HttpServletRequest request) {
		try {
			Location location = this.getLocation(request);
			DataObjectImpl dataObjectImpl = new DataObjectImpl(location);
			try {
				Connection connection = this.getConnection();
				Statement statement = connection.createStatement();
				String query = dataObjectImpl.update(" locationname = '" + request.getParameter("locationname") + "'");
				query = query.replace(",  where", " where ");
				statement.executeQuery(query);
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println(e.getMessage());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private Location getLocation(HttpServletRequest request) {
		Location location = new Location();
		location.setLatitude(request.getParameter("latitude"));
		location.setAddress(request.getParameter("address"));
		location.setLocationDescription(request.getParameter("locationdescription"));
		location.setLocationName(request.getParameter("locationname"));
		location.setLongitude(request.getParameter("longitude"));
		location.setRadiusLocation(request.getParameter("radiuslocation"));
		location.setRadiusReferLocation(request.getParameter("radiusreferlocation"));
		return location;
	}

	private void saveMovement(HttpServletRequest request) {
		try {
			Movement movement = this.getMovement(request);
			movement.setId(request.getParameter("primaryKey"));
			movement.setModifiedby((String) request.getSession().getAttribute("username"));
			movement.setModifiedDate(new java.util.Date());
			DataObjectImpl dataObjectImpl = new DataObjectImpl(movement);
			try {
				Connection connection = this.getConnection();
				Statement statement = connection.createStatement();
				String query = dataObjectImpl.update(" id='" + request.getParameter("primaryKey") + "'");
				query = query.replace(",  where", " where ");
				statement.executeQuery(query);
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println(e.getMessage());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private Movement getMovement(HttpServletRequest request) throws ParseException {
		Movement movement = new Movement();
		movement.setAlertbymail(request.getParameter("email") != null && request.getParameter("email").equals("true"));
		movement.setAlertbysms(request.getParameter("sms") != null && request.getParameter("sms").equals("true"));
		movement.setMonday(request.getParameter("monday") != null && request.getParameter("monday").equals("on"));
		movement.setTuesday(request.getParameter("tuesday") != null && request.getParameter("tuesday").equals("on"));
		movement.setWednesday(
				request.getParameter("wednesday") != null && request.getParameter("wednesday").equals("on"));
		movement.setThursday(request.getParameter("thursday") != null && request.getParameter("thursday").equals("on"));
		movement.setFriday(request.getParameter("friday") != null && request.getParameter("friday").equals("on"));
		movement.setSaturday(request.getParameter("saturday") != null && request.getParameter("saturday").equals("on"));
		movement.setSunday(request.getParameter("sunday") != null && request.getParameter("sunday").equals("on"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		movement.setEnddate(request.getParameter("endDate") != null && !request.getParameter("endDate").equals("")
				? formatter.parse(request.getParameter("endDate")) : null);
		movement.setStartdate(request.getParameter("startDate") != null && !request.getParameter("startDate").equals("")
				? formatter.parse(request.getParameter("startDate")) : null);
		movement.setEndTime(request.getParameter("endTime"));
		movement.setStartTime(request.getParameter("startTime"));
		movement.setSnoozetime(request.getParameter("snooze"));
		if (request.getParameter("select").equals("vehicle")) {
			movement.setVehicle(request.getParameter("vehicle"));
			movement.setGroupname("");
		} else {
			movement.setVehicle(null);
			movement.setGroupname(request.getParameter("vehicle"));
			movement.setGroups(true);
		}
		return movement;
	}

	private void addMovement(HttpServletRequest request) {
		try {
			Movement movement = this.getMovement(request);
			movement.setId(request.getParameter("vehicle"));
			DataObjectImpl dataObjectImpl = new DataObjectImpl(movement);
			try {
				Connection connection = this.getConnection();
				Statement statement = connection.createStatement();
				String query = dataObjectImpl.insert();
				statement.executeQuery(query);
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println(e.getMessage());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void saveUser(HttpServletRequest request) {
		if (request.getParameter("password").equals(request.getParameter("repassword"))) {
			try {
				Connection conn = this.getConnection();
				Statement stmt = conn.createStatement();
				String sql = "";
				sql = "update usermaster set password = '" + request.getParameter("password") + "', modifieddate ='"
						+ new java.util.Date() + "' , modifiedby ='" + request.getSession().getAttribute("username")
						+ "' where username = '" + request.getParameter("username") + "'";
				stmt.executeQuery(sql);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private void addDriver(HttpServletRequest request) {
		try {
			DriverMaster vehicleMaster = this.getDriverMaster(request);
			if (request.getParameter("action").equals("Add Driver")) {
				vehicleMaster.setCreatedBy((String) request.getSession().getAttribute("username"));
				vehicleMaster.setCreatedDate(new java.util.Date());
				DataObjectImpl dataObjectImpl = new DataObjectImpl(vehicleMaster);
				try {
					Connection connection = this.getConnection();
					Statement statement = connection.createStatement();
					String query = dataObjectImpl.insert();
					System.out.println(query);
					statement.executeQuery(query);
				} catch (ClassNotFoundException | SQLException e) {
					System.err.println(e.getMessage());
				}
			} else {
				vehicleMaster.setModifiedBy((String) request.getSession().getAttribute("username"));
				vehicleMaster.setModifiedDate(new java.util.Date());
				DataObjectImpl dataObjectImpl = new DataObjectImpl(vehicleMaster);
				try {
					Connection connection = this.getConnection();
					Statement statement = connection.createStatement();
					String query = dataObjectImpl.update(" id=" + Integer.parseInt(request.getParameter("driverNo")));
					query = query.replace(",  where", " where ");
					statement.executeQuery(query);
				} catch (ClassNotFoundException | SQLException e) {
					System.err.println(e.getMessage());
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private DriverMaster getDriverMaster(HttpServletRequest request) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM");
		DriverMaster driverMaster = new DriverMaster();
		driverMaster.setId(Integer.parseInt(request.getParameter("driverNo")));
		driverMaster.setDriverName(request.getParameter("driverName"));
		driverMaster.setAddress(request.getParameter("address"));
		driverMaster.setContact1(request.getParameter("contact1"));
		driverMaster.setContact2(request.getParameter("contact2"));
		driverMaster.setLicenseNo(request.getParameter("licenseno"));
		driverMaster.setLicenseExpiryDate(formatter.parse(request.getParameter("licenseexpirydate")));
		driverMaster.setRtoName(request.getParameter("rtoname"));
		driverMaster.setBloodGroup(request.getParameter("bloodgroup"));
		driverMaster.setUserName((String) request.getSession().getAttribute("username"));
		driverMaster.setDriverexp(request.getParameter("driverExp"));
		driverMaster.setRemark(request.getParameter("remark"));
		return driverMaster;
	}

	private void saveGroup(HttpServletRequest request) {
		// This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
		// org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 18[CATCHBLOCK]
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:394)
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:446)
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2869)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:817)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:220)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:165)
		// org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:91)
		// org.benf.cfr.reader.entities.Method.analyse(Method.java:354)
		// org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:751)
		// org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:683)
		// org.sf.feeling.decompiler.cfr.decompiler.CfrDecompiler.decompile(CfrDecompiler.java:86)
		// org.sf.feeling.decompiler.editor.BaseDecompilerSourceMapper.decompile(BaseDecompilerSourceMapper.java:342)
		// org.sf.feeling.decompiler.util.DecompileUtil.decompiler(DecompileUtil.java:71)
		// org.sf.feeling.decompiler.editor.JavaDecompilerClassFileEditor.doSetInput(JavaDecompilerClassFileEditor.java:235)
		// org.sf.feeling.decompiler.editor.JavaDecompilerClassFileEditor.doSetInput(JavaDecompilerClassFileEditor.java:327)
		// org.eclipse.ui.texteditor.AbstractTextEditor$19.run(AbstractTextEditor.java:3220)
		// org.eclipse.jface.operation.ModalContext.runInCurrentThread(ModalContext.java:463)
		// org.eclipse.jface.operation.ModalContext.run(ModalContext.java:371)
		// org.eclipse.ui.internal.WorkbenchWindow$14.run(WorkbenchWindow.java:2156)
		// org.eclipse.swt.custom.BusyIndicator.showWhile(BusyIndicator.java:70)
		// org.eclipse.ui.internal.WorkbenchWindow.run(WorkbenchWindow.java:2152)
		// org.eclipse.ui.texteditor.AbstractTextEditor.internalInit(AbstractTextEditor.java:3238)
		// org.eclipse.ui.texteditor.AbstractTextEditor.init(AbstractTextEditor.java:3265)
		// org.eclipse.ui.internal.EditorReference.initialize(EditorReference.java:361)
		// org.eclipse.ui.internal.e4.compatibility.CompatibilityPart.create(CompatibilityPart.java:319)
		// sun.reflect.GeneratedMethodAccessor52.invoke(Unknown Source)
		// sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
		// java.lang.reflect.Method.invoke(Unknown Source)
		// org.eclipse.e4.core.internal.di.MethodRequestor.execute(MethodRequestor.java:56)
		// org.eclipse.e4.core.internal.di.InjectorImpl.processAnnotated(InjectorImpl.java:898)
		// org.eclipse.e4.core.internal.di.InjectorImpl.processAnnotated(InjectorImpl.java:879)
		// org.eclipse.e4.core.internal.di.InjectorImpl.inject(InjectorImpl.java:121)
		// org.eclipse.e4.core.internal.di.InjectorImpl.internalMake(InjectorImpl.java:345)
		// org.eclipse.e4.core.internal.di.InjectorImpl.make(InjectorImpl.java:264)
		// org.eclipse.e4.core.contexts.ContextInjectionFactory.make(ContextInjectionFactory.java:162)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.createFromBundle(ReflectionContributionFactory.java:104)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.doCreate(ReflectionContributionFactory.java:73)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.create(ReflectionContributionFactory.java:55)
		// org.eclipse.e4.ui.workbench.renderers.swt.ContributedPartRenderer.createWidget(ContributedPartRenderer.java:129)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.createWidget(PartRenderingEngine.java:971)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.safeCreateGui(PartRenderingEngine.java:640)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.safeCreateGui(PartRenderingEngine.java:746)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.access$0(PartRenderingEngine.java:717)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine$2.run(PartRenderingEngine.java:711)
		// org.eclipse.core.runtime.SafeRunner.run(SafeRunner.java:42)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.createGui(PartRenderingEngine.java:695)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl$1.handleEvent(PartServiceImpl.java:99)
		// org.eclipse.e4.ui.services.internal.events.UIEventHandler$1.run(UIEventHandler.java:40)
		// org.eclipse.swt.widgets.Synchronizer.syncExec(Synchronizer.java:186)
		// org.eclipse.ui.internal.UISynchronizer.syncExec(UISynchronizer.java:145)
		// org.eclipse.swt.widgets.Display.syncExec(Display.java:4761)
		// org.eclipse.e4.ui.internal.workbench.swt.E4Application$1.syncExec(E4Application.java:211)
		// org.eclipse.e4.ui.services.internal.events.UIEventHandler.handleEvent(UIEventHandler.java:36)
		// org.eclipse.equinox.internal.event.EventHandlerWrapper.handleEvent(EventHandlerWrapper.java:197)
		// org.eclipse.equinox.internal.event.EventHandlerTracker.dispatchEvent(EventHandlerTracker.java:197)
		// org.eclipse.equinox.internal.event.EventHandlerTracker.dispatchEvent(EventHandlerTracker.java:1)
		// org.eclipse.osgi.framework.eventmgr.EventManager.dispatchEvent(EventManager.java:230)
		// org.eclipse.osgi.framework.eventmgr.ListenerQueue.dispatchEventSynchronous(ListenerQueue.java:148)
		// org.eclipse.equinox.internal.event.EventAdminImpl.dispatchEvent(EventAdminImpl.java:135)
		// org.eclipse.equinox.internal.event.EventAdminImpl.sendEvent(EventAdminImpl.java:78)
		// org.eclipse.equinox.internal.event.EventComponent.sendEvent(EventComponent.java:39)
		// org.eclipse.e4.ui.services.internal.events.EventBroker.send(EventBroker.java:85)
		// org.eclipse.e4.ui.internal.workbench.UIEventPublisher.notifyChanged(UIEventPublisher.java:59)
		// org.eclipse.emf.common.notify.impl.BasicNotifierImpl.eNotify(BasicNotifierImpl.java:374)
		// org.eclipse.e4.ui.model.application.ui.impl.ElementContainerImpl.setSelectedElement(ElementContainerImpl.java:171)
		// org.eclipse.e4.ui.internal.workbench.ModelServiceImpl.showElementInWindow(ModelServiceImpl.java:494)
		// org.eclipse.e4.ui.internal.workbench.ModelServiceImpl.bringToTop(ModelServiceImpl.java:458)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.delegateBringToTop(PartServiceImpl.java:724)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.bringToTop(PartServiceImpl.java:396)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.showPart(PartServiceImpl.java:1166)
		// org.eclipse.ui.internal.WorkbenchPage.busyOpenEditor(WorkbenchPage.java:3234)
		// org.eclipse.ui.internal.WorkbenchPage.access$25(WorkbenchPage.java:3149)
		// org.eclipse.ui.internal.WorkbenchPage$10.run(WorkbenchPage.java:3131)
		// org.eclipse.swt.custom.BusyIndicator.showWhile(BusyIndicator.java:70)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3126)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3090)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3080)
		// org.eclipse.jdt.internal.ui.javaeditor.EditorUtility.openInEditor(EditorUtility.java:373)
		// org.eclipse.jdt.internal.ui.javaeditor.EditorUtility.openInEditor(EditorUtility.java:179)
		// org.eclipse.jdt.ui.actions.OpenAction.run(OpenAction.java:268)
		// org.eclipse.jdt.ui.actions.OpenAction.run(OpenAction.java:233)
		// org.eclipse.jdt.ui.actions.SelectionDispatchAction.dispatchRun(SelectionDispatchAction.java:275)
		// org.eclipse.jdt.ui.actions.SelectionDispatchAction.run(SelectionDispatchAction.java:251)
		// org.eclipse.jdt.internal.ui.navigator.OpenAndExpand.run(OpenAndExpand.java:50)
		// org.eclipse.ui.actions.RetargetAction.run(RetargetAction.java:229)
		// org.eclipse.ui.navigator.CommonNavigatorManager$2.open(CommonNavigatorManager.java:191)
		// org.eclipse.ui.OpenAndLinkWithEditorHelper$InternalListener.open(OpenAndLinkWithEditorHelper.java:48)
		// org.eclipse.jface.viewers.StructuredViewer$2.run(StructuredViewer.java:854)
		// org.eclipse.core.runtime.SafeRunner.run(SafeRunner.java:42)
		// org.eclipse.ui.internal.JFaceUtil$1.run(JFaceUtil.java:50)
		// org.eclipse.jface.util.SafeRunnable.run(SafeRunnable.java:173)
		// org.eclipse.jface.viewers.StructuredViewer.fireOpen(StructuredViewer.java:851)
		// org.eclipse.jface.viewers.StructuredViewer.handleOpen(StructuredViewer.java:1168)
		// org.eclipse.ui.navigator.CommonViewer.handleOpen(CommonViewer.java:449)
		// org.eclipse.jface.viewers.StructuredViewer$6.handleOpen(StructuredViewer.java:1275)
		// org.eclipse.jface.util.OpenStrategy.fireOpenEvent(OpenStrategy.java:278)
		// org.eclipse.jface.util.OpenStrategy.access$2(OpenStrategy.java:272)
		// org.eclipse.jface.util.OpenStrategy$1.handleEvent(OpenStrategy.java:313)
		// org.eclipse.swt.widgets.EventTable.sendEvent(EventTable.java:84)
		// org.eclipse.swt.widgets.Display.sendEvent(Display.java:4362)
		// org.eclipse.swt.widgets.Widget.sendEvent(Widget.java:1113)
		// org.eclipse.swt.widgets.Display.runDeferredEvents(Display.java:4180)
		// org.eclipse.swt.widgets.Display.readAndDispatch(Display.java:3769)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine$4.run(PartRenderingEngine.java:1127)
		// org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:337)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.run(PartRenderingEngine.java:1018)
		// org.eclipse.e4.ui.internal.workbench.E4Workbench.createAndRunUI(E4Workbench.java:156)
		// org.eclipse.ui.internal.Workbench$5.run(Workbench.java:654)
		// org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:337)
		// org.eclipse.ui.internal.Workbench.createAndRunWorkbench(Workbench.java:598)
		// org.eclipse.ui.PlatformUI.createAndRunWorkbench(PlatformUI.java:150)
		// org.eclipse.ui.internal.ide.application.IDEApplication.start(IDEApplication.java:139)
		// org.eclipse.equinox.internal.app.EclipseAppHandle.run(EclipseAppHandle.java:196)
		// org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.runApplication(EclipseAppLauncher.java:134)
		// org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.start(EclipseAppLauncher.java:104)
		// org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:380)
		// org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:235)
		// sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		// sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
		// sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
		// java.lang.reflect.Method.invoke(Unknown Source)
		// org.eclipse.equinox.launcher.Main.invokeFramework(Main.java:669)
		// org.eclipse.equinox.launcher.Main.basicRun(Main.java:608)
		// org.eclipse.equinox.launcher.Main.run(Main.java:1515)
		throw new IllegalStateException("Decompilation failed");
	}

	private void addGroup(HttpServletRequest request) {
		// This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
		// org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 20[CATCHBLOCK]
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:394)
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:446)
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2869)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:817)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:220)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:165)
		// org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:91)
		// org.benf.cfr.reader.entities.Method.analyse(Method.java:354)
		// org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:751)
		// org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:683)
		// org.sf.feeling.decompiler.cfr.decompiler.CfrDecompiler.decompile(CfrDecompiler.java:86)
		// org.sf.feeling.decompiler.editor.BaseDecompilerSourceMapper.decompile(BaseDecompilerSourceMapper.java:342)
		// org.sf.feeling.decompiler.util.DecompileUtil.decompiler(DecompileUtil.java:71)
		// org.sf.feeling.decompiler.editor.JavaDecompilerClassFileEditor.doSetInput(JavaDecompilerClassFileEditor.java:235)
		// org.sf.feeling.decompiler.editor.JavaDecompilerClassFileEditor.doSetInput(JavaDecompilerClassFileEditor.java:327)
		// org.eclipse.ui.texteditor.AbstractTextEditor$19.run(AbstractTextEditor.java:3220)
		// org.eclipse.jface.operation.ModalContext.runInCurrentThread(ModalContext.java:463)
		// org.eclipse.jface.operation.ModalContext.run(ModalContext.java:371)
		// org.eclipse.ui.internal.WorkbenchWindow$14.run(WorkbenchWindow.java:2156)
		// org.eclipse.swt.custom.BusyIndicator.showWhile(BusyIndicator.java:70)
		// org.eclipse.ui.internal.WorkbenchWindow.run(WorkbenchWindow.java:2152)
		// org.eclipse.ui.texteditor.AbstractTextEditor.internalInit(AbstractTextEditor.java:3238)
		// org.eclipse.ui.texteditor.AbstractTextEditor.init(AbstractTextEditor.java:3265)
		// org.eclipse.ui.internal.EditorReference.initialize(EditorReference.java:361)
		// org.eclipse.ui.internal.e4.compatibility.CompatibilityPart.create(CompatibilityPart.java:319)
		// sun.reflect.GeneratedMethodAccessor52.invoke(Unknown Source)
		// sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
		// java.lang.reflect.Method.invoke(Unknown Source)
		// org.eclipse.e4.core.internal.di.MethodRequestor.execute(MethodRequestor.java:56)
		// org.eclipse.e4.core.internal.di.InjectorImpl.processAnnotated(InjectorImpl.java:898)
		// org.eclipse.e4.core.internal.di.InjectorImpl.processAnnotated(InjectorImpl.java:879)
		// org.eclipse.e4.core.internal.di.InjectorImpl.inject(InjectorImpl.java:121)
		// org.eclipse.e4.core.internal.di.InjectorImpl.internalMake(InjectorImpl.java:345)
		// org.eclipse.e4.core.internal.di.InjectorImpl.make(InjectorImpl.java:264)
		// org.eclipse.e4.core.contexts.ContextInjectionFactory.make(ContextInjectionFactory.java:162)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.createFromBundle(ReflectionContributionFactory.java:104)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.doCreate(ReflectionContributionFactory.java:73)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.create(ReflectionContributionFactory.java:55)
		// org.eclipse.e4.ui.workbench.renderers.swt.ContributedPartRenderer.createWidget(ContributedPartRenderer.java:129)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.createWidget(PartRenderingEngine.java:971)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.safeCreateGui(PartRenderingEngine.java:640)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.safeCreateGui(PartRenderingEngine.java:746)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.access$0(PartRenderingEngine.java:717)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine$2.run(PartRenderingEngine.java:711)
		// org.eclipse.core.runtime.SafeRunner.run(SafeRunner.java:42)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.createGui(PartRenderingEngine.java:695)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl$1.handleEvent(PartServiceImpl.java:99)
		// org.eclipse.e4.ui.services.internal.events.UIEventHandler$1.run(UIEventHandler.java:40)
		// org.eclipse.swt.widgets.Synchronizer.syncExec(Synchronizer.java:186)
		// org.eclipse.ui.internal.UISynchronizer.syncExec(UISynchronizer.java:145)
		// org.eclipse.swt.widgets.Display.syncExec(Display.java:4761)
		// org.eclipse.e4.ui.internal.workbench.swt.E4Application$1.syncExec(E4Application.java:211)
		// org.eclipse.e4.ui.services.internal.events.UIEventHandler.handleEvent(UIEventHandler.java:36)
		// org.eclipse.equinox.internal.event.EventHandlerWrapper.handleEvent(EventHandlerWrapper.java:197)
		// org.eclipse.equinox.internal.event.EventHandlerTracker.dispatchEvent(EventHandlerTracker.java:197)
		// org.eclipse.equinox.internal.event.EventHandlerTracker.dispatchEvent(EventHandlerTracker.java:1)
		// org.eclipse.osgi.framework.eventmgr.EventManager.dispatchEvent(EventManager.java:230)
		// org.eclipse.osgi.framework.eventmgr.ListenerQueue.dispatchEventSynchronous(ListenerQueue.java:148)
		// org.eclipse.equinox.internal.event.EventAdminImpl.dispatchEvent(EventAdminImpl.java:135)
		// org.eclipse.equinox.internal.event.EventAdminImpl.sendEvent(EventAdminImpl.java:78)
		// org.eclipse.equinox.internal.event.EventComponent.sendEvent(EventComponent.java:39)
		// org.eclipse.e4.ui.services.internal.events.EventBroker.send(EventBroker.java:85)
		// org.eclipse.e4.ui.internal.workbench.UIEventPublisher.notifyChanged(UIEventPublisher.java:59)
		// org.eclipse.emf.common.notify.impl.BasicNotifierImpl.eNotify(BasicNotifierImpl.java:374)
		// org.eclipse.e4.ui.model.application.ui.impl.ElementContainerImpl.setSelectedElement(ElementContainerImpl.java:171)
		// org.eclipse.e4.ui.internal.workbench.ModelServiceImpl.showElementInWindow(ModelServiceImpl.java:494)
		// org.eclipse.e4.ui.internal.workbench.ModelServiceImpl.bringToTop(ModelServiceImpl.java:458)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.delegateBringToTop(PartServiceImpl.java:724)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.bringToTop(PartServiceImpl.java:396)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.showPart(PartServiceImpl.java:1166)
		// org.eclipse.ui.internal.WorkbenchPage.busyOpenEditor(WorkbenchPage.java:3234)
		// org.eclipse.ui.internal.WorkbenchPage.access$25(WorkbenchPage.java:3149)
		// org.eclipse.ui.internal.WorkbenchPage$10.run(WorkbenchPage.java:3131)
		// org.eclipse.swt.custom.BusyIndicator.showWhile(BusyIndicator.java:70)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3126)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3090)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3080)
		// org.eclipse.jdt.internal.ui.javaeditor.EditorUtility.openInEditor(EditorUtility.java:373)
		// org.eclipse.jdt.internal.ui.javaeditor.EditorUtility.openInEditor(EditorUtility.java:179)
		// org.eclipse.jdt.ui.actions.OpenAction.run(OpenAction.java:268)
		// org.eclipse.jdt.ui.actions.OpenAction.run(OpenAction.java:233)
		// org.eclipse.jdt.ui.actions.SelectionDispatchAction.dispatchRun(SelectionDispatchAction.java:275)
		// org.eclipse.jdt.ui.actions.SelectionDispatchAction.run(SelectionDispatchAction.java:251)
		// org.eclipse.jdt.internal.ui.navigator.OpenAndExpand.run(OpenAndExpand.java:50)
		// org.eclipse.ui.actions.RetargetAction.run(RetargetAction.java:229)
		// org.eclipse.ui.navigator.CommonNavigatorManager$2.open(CommonNavigatorManager.java:191)
		// org.eclipse.ui.OpenAndLinkWithEditorHelper$InternalListener.open(OpenAndLinkWithEditorHelper.java:48)
		// org.eclipse.jface.viewers.StructuredViewer$2.run(StructuredViewer.java:854)
		// org.eclipse.core.runtime.SafeRunner.run(SafeRunner.java:42)
		// org.eclipse.ui.internal.JFaceUtil$1.run(JFaceUtil.java:50)
		// org.eclipse.jface.util.SafeRunnable.run(SafeRunnable.java:173)
		// org.eclipse.jface.viewers.StructuredViewer.fireOpen(StructuredViewer.java:851)
		// org.eclipse.jface.viewers.StructuredViewer.handleOpen(StructuredViewer.java:1168)
		// org.eclipse.ui.navigator.CommonViewer.handleOpen(CommonViewer.java:449)
		// org.eclipse.jface.viewers.StructuredViewer$6.handleOpen(StructuredViewer.java:1275)
		// org.eclipse.jface.util.OpenStrategy.fireOpenEvent(OpenStrategy.java:278)
		// org.eclipse.jface.util.OpenStrategy.access$2(OpenStrategy.java:272)
		// org.eclipse.jface.util.OpenStrategy$1.handleEvent(OpenStrategy.java:313)
		// org.eclipse.swt.widgets.EventTable.sendEvent(EventTable.java:84)
		// org.eclipse.swt.widgets.Display.sendEvent(Display.java:4362)
		// org.eclipse.swt.widgets.Widget.sendEvent(Widget.java:1113)
		// org.eclipse.swt.widgets.Display.runDeferredEvents(Display.java:4180)
		// org.eclipse.swt.widgets.Display.readAndDispatch(Display.java:3769)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine$4.run(PartRenderingEngine.java:1127)
		// org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:337)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.run(PartRenderingEngine.java:1018)
		// org.eclipse.e4.ui.internal.workbench.E4Workbench.createAndRunUI(E4Workbench.java:156)
		// org.eclipse.ui.internal.Workbench$5.run(Workbench.java:654)
		// org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:337)
		// org.eclipse.ui.internal.Workbench.createAndRunWorkbench(Workbench.java:598)
		// org.eclipse.ui.PlatformUI.createAndRunWorkbench(PlatformUI.java:150)
		// org.eclipse.ui.internal.ide.application.IDEApplication.start(IDEApplication.java:139)
		// org.eclipse.equinox.internal.app.EclipseAppHandle.run(EclipseAppHandle.java:196)
		// org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.runApplication(EclipseAppLauncher.java:134)
		// org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.start(EclipseAppLauncher.java:104)
		// org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:380)
		// org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:235)
		// sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		// sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
		// sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
		// java.lang.reflect.Method.invoke(Unknown Source)
		// org.eclipse.equinox.launcher.Main.invokeFramework(Main.java:669)
		// org.eclipse.equinox.launcher.Main.basicRun(Main.java:608)
		// org.eclipse.equinox.launcher.Main.run(Main.java:1515)
		throw new IllegalStateException("Decompilation failed");
	}

	private String editAlert(String parameter) {
		// This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
		// org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 9[CATCHBLOCK]
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:394)
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:446)
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2869)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:817)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:220)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:165)
		// org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:91)
		// org.benf.cfr.reader.entities.Method.analyse(Method.java:354)
		// org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:751)
		// org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:683)
		// org.sf.feeling.decompiler.cfr.decompiler.CfrDecompiler.decompile(CfrDecompiler.java:86)
		// org.sf.feeling.decompiler.editor.BaseDecompilerSourceMapper.decompile(BaseDecompilerSourceMapper.java:342)
		// org.sf.feeling.decompiler.util.DecompileUtil.decompiler(DecompileUtil.java:71)
		// org.sf.feeling.decompiler.editor.JavaDecompilerClassFileEditor.doSetInput(JavaDecompilerClassFileEditor.java:235)
		// org.sf.feeling.decompiler.editor.JavaDecompilerClassFileEditor.doSetInput(JavaDecompilerClassFileEditor.java:327)
		// org.eclipse.ui.texteditor.AbstractTextEditor$19.run(AbstractTextEditor.java:3220)
		// org.eclipse.jface.operation.ModalContext.runInCurrentThread(ModalContext.java:463)
		// org.eclipse.jface.operation.ModalContext.run(ModalContext.java:371)
		// org.eclipse.ui.internal.WorkbenchWindow$14.run(WorkbenchWindow.java:2156)
		// org.eclipse.swt.custom.BusyIndicator.showWhile(BusyIndicator.java:70)
		// org.eclipse.ui.internal.WorkbenchWindow.run(WorkbenchWindow.java:2152)
		// org.eclipse.ui.texteditor.AbstractTextEditor.internalInit(AbstractTextEditor.java:3238)
		// org.eclipse.ui.texteditor.AbstractTextEditor.init(AbstractTextEditor.java:3265)
		// org.eclipse.ui.internal.EditorReference.initialize(EditorReference.java:361)
		// org.eclipse.ui.internal.e4.compatibility.CompatibilityPart.create(CompatibilityPart.java:319)
		// sun.reflect.GeneratedMethodAccessor52.invoke(Unknown Source)
		// sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
		// java.lang.reflect.Method.invoke(Unknown Source)
		// org.eclipse.e4.core.internal.di.MethodRequestor.execute(MethodRequestor.java:56)
		// org.eclipse.e4.core.internal.di.InjectorImpl.processAnnotated(InjectorImpl.java:898)
		// org.eclipse.e4.core.internal.di.InjectorImpl.processAnnotated(InjectorImpl.java:879)
		// org.eclipse.e4.core.internal.di.InjectorImpl.inject(InjectorImpl.java:121)
		// org.eclipse.e4.core.internal.di.InjectorImpl.internalMake(InjectorImpl.java:345)
		// org.eclipse.e4.core.internal.di.InjectorImpl.make(InjectorImpl.java:264)
		// org.eclipse.e4.core.contexts.ContextInjectionFactory.make(ContextInjectionFactory.java:162)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.createFromBundle(ReflectionContributionFactory.java:104)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.doCreate(ReflectionContributionFactory.java:73)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.create(ReflectionContributionFactory.java:55)
		// org.eclipse.e4.ui.workbench.renderers.swt.ContributedPartRenderer.createWidget(ContributedPartRenderer.java:129)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.createWidget(PartRenderingEngine.java:971)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.safeCreateGui(PartRenderingEngine.java:640)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.safeCreateGui(PartRenderingEngine.java:746)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.access$0(PartRenderingEngine.java:717)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine$2.run(PartRenderingEngine.java:711)
		// org.eclipse.core.runtime.SafeRunner.run(SafeRunner.java:42)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.createGui(PartRenderingEngine.java:695)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl$1.handleEvent(PartServiceImpl.java:99)
		// org.eclipse.e4.ui.services.internal.events.UIEventHandler$1.run(UIEventHandler.java:40)
		// org.eclipse.swt.widgets.Synchronizer.syncExec(Synchronizer.java:186)
		// org.eclipse.ui.internal.UISynchronizer.syncExec(UISynchronizer.java:145)
		// org.eclipse.swt.widgets.Display.syncExec(Display.java:4761)
		// org.eclipse.e4.ui.internal.workbench.swt.E4Application$1.syncExec(E4Application.java:211)
		// org.eclipse.e4.ui.services.internal.events.UIEventHandler.handleEvent(UIEventHandler.java:36)
		// org.eclipse.equinox.internal.event.EventHandlerWrapper.handleEvent(EventHandlerWrapper.java:197)
		// org.eclipse.equinox.internal.event.EventHandlerTracker.dispatchEvent(EventHandlerTracker.java:197)
		// org.eclipse.equinox.internal.event.EventHandlerTracker.dispatchEvent(EventHandlerTracker.java:1)
		// org.eclipse.osgi.framework.eventmgr.EventManager.dispatchEvent(EventManager.java:230)
		// org.eclipse.osgi.framework.eventmgr.ListenerQueue.dispatchEventSynchronous(ListenerQueue.java:148)
		// org.eclipse.equinox.internal.event.EventAdminImpl.dispatchEvent(EventAdminImpl.java:135)
		// org.eclipse.equinox.internal.event.EventAdminImpl.sendEvent(EventAdminImpl.java:78)
		// org.eclipse.equinox.internal.event.EventComponent.sendEvent(EventComponent.java:39)
		// org.eclipse.e4.ui.services.internal.events.EventBroker.send(EventBroker.java:85)
		// org.eclipse.e4.ui.internal.workbench.UIEventPublisher.notifyChanged(UIEventPublisher.java:59)
		// org.eclipse.emf.common.notify.impl.BasicNotifierImpl.eNotify(BasicNotifierImpl.java:374)
		// org.eclipse.e4.ui.model.application.ui.impl.ElementContainerImpl.setSelectedElement(ElementContainerImpl.java:171)
		// org.eclipse.e4.ui.internal.workbench.ModelServiceImpl.showElementInWindow(ModelServiceImpl.java:494)
		// org.eclipse.e4.ui.internal.workbench.ModelServiceImpl.bringToTop(ModelServiceImpl.java:458)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.delegateBringToTop(PartServiceImpl.java:724)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.bringToTop(PartServiceImpl.java:396)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.showPart(PartServiceImpl.java:1166)
		// org.eclipse.ui.internal.WorkbenchPage.busyOpenEditor(WorkbenchPage.java:3234)
		// org.eclipse.ui.internal.WorkbenchPage.access$25(WorkbenchPage.java:3149)
		// org.eclipse.ui.internal.WorkbenchPage$10.run(WorkbenchPage.java:3131)
		// org.eclipse.swt.custom.BusyIndicator.showWhile(BusyIndicator.java:70)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3126)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3090)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3080)
		// org.eclipse.jdt.internal.ui.javaeditor.EditorUtility.openInEditor(EditorUtility.java:373)
		// org.eclipse.jdt.internal.ui.javaeditor.EditorUtility.openInEditor(EditorUtility.java:179)
		// org.eclipse.jdt.ui.actions.OpenAction.run(OpenAction.java:268)
		// org.eclipse.jdt.ui.actions.OpenAction.run(OpenAction.java:233)
		// org.eclipse.jdt.ui.actions.SelectionDispatchAction.dispatchRun(SelectionDispatchAction.java:275)
		// org.eclipse.jdt.ui.actions.SelectionDispatchAction.run(SelectionDispatchAction.java:251)
		// org.eclipse.jdt.internal.ui.navigator.OpenAndExpand.run(OpenAndExpand.java:50)
		// org.eclipse.ui.actions.RetargetAction.run(RetargetAction.java:229)
		// org.eclipse.ui.navigator.CommonNavigatorManager$2.open(CommonNavigatorManager.java:191)
		// org.eclipse.ui.OpenAndLinkWithEditorHelper$InternalListener.open(OpenAndLinkWithEditorHelper.java:48)
		// org.eclipse.jface.viewers.StructuredViewer$2.run(StructuredViewer.java:854)
		// org.eclipse.core.runtime.SafeRunner.run(SafeRunner.java:42)
		// org.eclipse.ui.internal.JFaceUtil$1.run(JFaceUtil.java:50)
		// org.eclipse.jface.util.SafeRunnable.run(SafeRunnable.java:173)
		// org.eclipse.jface.viewers.StructuredViewer.fireOpen(StructuredViewer.java:851)
		// org.eclipse.jface.viewers.StructuredViewer.handleOpen(StructuredViewer.java:1168)
		// org.eclipse.ui.navigator.CommonViewer.handleOpen(CommonViewer.java:449)
		// org.eclipse.jface.viewers.StructuredViewer$6.handleOpen(StructuredViewer.java:1275)
		// org.eclipse.jface.util.OpenStrategy.fireOpenEvent(OpenStrategy.java:278)
		// org.eclipse.jface.util.OpenStrategy.access$2(OpenStrategy.java:272)
		// org.eclipse.jface.util.OpenStrategy$1.handleEvent(OpenStrategy.java:313)
		// org.eclipse.swt.widgets.EventTable.sendEvent(EventTable.java:84)
		// org.eclipse.swt.widgets.Display.sendEvent(Display.java:4362)
		// org.eclipse.swt.widgets.Widget.sendEvent(Widget.java:1113)
		// org.eclipse.swt.widgets.Display.runDeferredEvents(Display.java:4180)
		// org.eclipse.swt.widgets.Display.readAndDispatch(Display.java:3769)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine$4.run(PartRenderingEngine.java:1127)
		// org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:337)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.run(PartRenderingEngine.java:1018)
		// org.eclipse.e4.ui.internal.workbench.E4Workbench.createAndRunUI(E4Workbench.java:156)
		// org.eclipse.ui.internal.Workbench$5.run(Workbench.java:654)
		// org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:337)
		// org.eclipse.ui.internal.Workbench.createAndRunWorkbench(Workbench.java:598)
		// org.eclipse.ui.PlatformUI.createAndRunWorkbench(PlatformUI.java:150)
		// org.eclipse.ui.internal.ide.application.IDEApplication.start(IDEApplication.java:139)
		// org.eclipse.equinox.internal.app.EclipseAppHandle.run(EclipseAppHandle.java:196)
		// org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.runApplication(EclipseAppLauncher.java:134)
		// org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.start(EclipseAppLauncher.java:104)
		// org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:380)
		// org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:235)
		// sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		// sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
		// sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
		// java.lang.reflect.Method.invoke(Unknown Source)
		// org.eclipse.equinox.launcher.Main.invokeFramework(Main.java:669)
		// org.eclipse.equinox.launcher.Main.basicRun(Main.java:608)
		// org.eclipse.equinox.launcher.Main.run(Main.java:1515)
		throw new IllegalStateException("Decompilation failed");
	}

	private void addVehicle(HttpServletRequest request) {
		try {
			VehicleMaster vehicleMaster = this.getVehicleMaster(request);
			vehicleMaster.setCreatedBy((String) request.getSession().getAttribute("username"));
			vehicleMaster.setCreatedDate(new java.util.Date());
			DataObjectImpl dataObjectImpl = new DataObjectImpl(vehicleMaster);
			try {
				Connection connection = this.getConnection();
				Statement statement = connection.createStatement();
				String query = dataObjectImpl.insert();
				statement.executeQuery(query);
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println(e.getMessage());
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}

	private void addAlert(HttpServletRequest request) {
		Alert alert = this.getAlert(request);
		alert.setCreatedBy((String) request.getSession().getAttribute("username"));
		alert.setCreatedDate(new java.util.Date());
		DataObjectImpl dataObjectImpl = new DataObjectImpl(alert);
		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			String query = dataObjectImpl.insert();
			statement.executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	private Alert getAlert(HttpServletRequest request) {
		Alert alert = new Alert();
		if (request.getParameter("select").equals("group")) {
			alert.setGroup(true);
		}
		if (request.getParameter("byMail") != null && request.getParameter("byMail").equals("true")) {
			alert.setAlertByMail(true);
		} else {
			alert.setAlertByMail(false);
		}
		if (request.getParameter("bySms") != null && request.getParameter("bySms").equals("true")) {
			alert.setAlertBySms(true);
		} else {
			alert.setAlertBySms(false);
		}
		if (request.getParameter("overSpeed") != null && request.getParameter("overSpeed").equals("true")) {
			alert.setOverSpeed(true);
		} else {
			alert.setOverSpeed(false);
		}
		if (request.getParameter("suddenBreak") != null && request.getParameter("suddenBreak").equals("true")) {
			alert.setSuddenBreak(true);
		} else {
			alert.setSuddenBreak(false);
		}
		if (request.getParameter("idleTime") != null && request.getParameter("idleTime").equals("true")) {
			alert.setIdleTime(true);
		} else {
			alert.setIdleTime(false);
		}
		if (request.getParameter("panic") != null && request.getParameter("panic").equals("true")) {
			alert.setPanic(true);
		} else {
			alert.setPanic(false);
		}
		if (request.getParameter("geoFency") != null && request.getParameter("geoFency").equals("true")) {
			alert.setGeofency(true);
		} else {
			alert.setGeofency(false);
		}
		alert.setContactNo(request.getParameter("contactNo"));
		alert.setVehicleNo(request.getParameter("vehicleNo"));
		alert.setSnoozeTime(request.getParameter("snoozeTime"));
		return alert;
	}

	private void saveAlert(HttpServletRequest request) {
		Alert alert = this.getAlert(request);
		alert.setModifiedBy((String) request.getSession().getAttribute("username"));
		alert.setModifiedDate(new java.util.Date());
		DataObjectImpl dataObjectImpl = new DataObjectImpl(alert);
		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			String query = dataObjectImpl.update("vehicleNo ='" + alert.getVehicleNo() + "'");
			query = query.replace(",  where", " where ");
			statement.executeQuery(query);
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	private void saveVehicle(HttpServletRequest request) {
		try {
			VehicleMaster vehicleMaster = this.getVehicleMaster(request);
			vehicleMaster.setModifiedBy((String) request.getSession().getAttribute("username"));
			vehicleMaster.setModifiedDate(new java.util.Date());
			DataObjectImpl dataObjectImpl = new DataObjectImpl(vehicleMaster);
			try {
				Connection connection = this.getConnection();
				Statement statement = connection.createStatement();
				String query = dataObjectImpl.update("vehicleNo ='" + vehicleMaster.getVehicleNo() + "'");
				query = query.replace(",  where", " where ");
				statement.executeQuery(query);
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println(e.getMessage());
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}

	private VehicleMaster getVehicleMaster(HttpServletRequest request) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		VehicleMaster vehicleMaster = new VehicleMaster();
		vehicleMaster.setVehicleNo(request.getParameter("vehicleNo"));
		vehicleMaster.setVehicleType(request.getParameter("vehicleType"));
		vehicleMaster.setVehicleMake(request.getParameter("vehicleMake"));
		vehicleMaster.setUnitNo(Integer.parseInt(request.getParameter("unitNo")));
		vehicleMaster.setNationalPermitNo(request.getParameter("vehicleNP"));
		if (request.getParameter("vehicleNPExp") != null && !request.getParameter("vehicleNPExp").equals("")) {
			vehicleMaster.setNationalPermitExpiryDate(formatter.parse(request.getParameter("vehicleNPExp")));
		}
		if (request.getParameter("insuranceDate") != null && !request.getParameter("insuranceDate").equals("")) {
			vehicleMaster.setInsuranceDate(formatter.parse(request.getParameter("insuranceDate")));
		}
		vehicleMaster.setInsuranceNo(request.getParameter("insuranceNo"));
		if (request.getParameter("insuranceExpiry") != null && !request.getParameter("insuranceExpiry").equals("")) {
			vehicleMaster.setInsuranceExpiryDate(formatter.parse(request.getParameter("insuranceExpiry")));
		}
		vehicleMaster.setInsuranceIssuedBy(request.getParameter("insuranceBy"));
		if (request.getParameter("odo") != null && !request.getParameter("odo").equals("")) {
			vehicleMaster.setCurrentOdiMeter(Integer.parseInt(request.getParameter("odo")));
		}
		if (request.getParameter("fuel") != null && !request.getParameter("fuel").equals("")) {
			vehicleMaster.setCurrentFuel(Integer.parseInt(request.getParameter("fuel")));
		}
		if (request.getParameter("service") != null && !request.getParameter("service").equals("")) {
			vehicleMaster.setServiceKm(Integer.parseInt(request.getParameter("service")));
		}
		if (request.getParameter("servicedt") != null && !request.getParameter("servicedt").equals("")) {
			vehicleMaster.setServiceDate(formatter.parse(request.getParameter("servicedt")));
		}
		vehicleMaster.setOwnerCompanyName(request.getParameter("owner"));
		vehicleMaster.setOwnerContact1(request.getParameter("owner1"));
		vehicleMaster.setOwnerContact2(request.getParameter("owner2"));
		vehicleMaster.setOwnerEmail(request.getParameter("email"));
		vehicleMaster.setOwnerAddress1(request.getParameter("address"));
		vehicleMaster.setOwnerCity(request.getParameter("city"));
		if (request.getParameter("pin") != null && !request.getParameter("pin").equals("")) {
			vehicleMaster.setOwnerPinCode(Integer.parseInt(request.getParameter("pin")));
		}
		vehicleMaster.setUserName((String) request.getSession().getAttribute("username"));
		vehicleMaster.setIssuedRTO(request.getParameter("issuedRto"));
		return vehicleMaster;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		try {
			if (action != null && action.equals("Login")) {
				if (this.validate(request)) {
					System.out.println("Context Path::: "+this.getServletContext().getContextPath());
					HttpSession httpSession = request.getSession();
					httpSession.setAttribute("username", (Object) request.getParameter("username"));
					response.setContentType("text/html");
					PrintWriter out = response.getWriter();
					out.println(this.getHtmlFile("Index"));
					out.flush();
				} else {
					response.setContentType("text/html");
					PrintWriter out = response.getWriter();
					out.println(this.getHtmlFile("LoginError"));
					out.flush();
				}
			} else if (action != null && action.equals("getLoginData")) {
				String username = (String) request.getSession().getAttribute("username");
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getSampleHtml(username));
				out.flush();
			} else if (action != null && action.equals("editVehicle")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.editVehicle(request.getParameter("id")));
				out.flush();
			} else if (action != null && action.equals("editAlert")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.editAlert(request.getParameter("id")));
				out.flush();
			} else if (action != null && action.equals("editDriver")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.editDriver(request.getParameter("id")));
				out.flush();
			} else if (action != null && action.equals("getVehicleData")) {
				String username = (String) request.getSession().getAttribute("username");
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getVehicleData(username));
				out.flush();
			} else if (action != null && action.equals("getDrivers")) {
				String username = (String) request.getSession().getAttribute("username");
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getDriverData(username));
				out.flush();
			} else if (action != null && action.equals("getVehicleLocations")) {
				String username = (String) request.getSession().getAttribute("username");
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getVehicleMapData(username));
				out.flush();
			} else if (action != null && action.equals("deleteVehicle")) {
				response.setContentType("text/html");
				this.deleteVehicle(request.getParameter("id"));
				PrintWriter out = response.getWriter();
				out.println(this.getVehicleData((String) request.getSession().getAttribute("username")));
				out.flush();
			} else if (action != null && action.equals("deleteDriver")) {
				response.setContentType("text/html");
				this.deleteDriver(request.getParameter("id"));
				PrintWriter out = response.getWriter();
				out.flush();
			} else if (action != null && action.equals("deleteAlert")) {
				response.setContentType("text/html");
				this.deleteAlert(request.getParameter("id"));
				PrintWriter out = response.getWriter();
				out.flush();
			} else if (action != null && action.equals("deleteGroup")) {
				response.setContentType("text/html");
				this.deleteGroup(request.getParameter("id"));
				PrintWriter out = response.getWriter();
				out.flush();
			} else if (action != null && action.equals("getVehicles")) {
				String username = (String) request.getSession().getAttribute("username");
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getVehicles(username));
				out.flush();
			} else if (action != null && action.equals("getRoute")) {
				String unitNo = request.getParameter("id");
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getRouteData(unitNo));
				out.flush();
			} else if (action != null && action.equals("getAlert")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getAlertData((String) request.getSession().getAttribute("username")));
				out.flush();
			} else if (action != null && action.equals("getGroups")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getGroups((String) request.getSession().getAttribute("username")));
				out.flush();
			} else if (action != null && action.equals("showGroup")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getGroupData(request));
				out.flush();
			} else if (action != null && action.equals("getUsers")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getUserData(request));
				out.flush();
			} else if (action != null && action.equals("addGroup")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getUngroupedVehicles(request.getParameter("groupName")));
				out.flush();
			} else if (action != null && action.equals("editUser")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.editUser(request.getParameter("id")));
				out.flush();
			} else if (action != null && action.equals("getMovements")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getMovements(request.getParameter("id")));
				out.flush();
			} else if (action != null && action.equals("editMovement")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.editMovement(request.getParameter("id")));
				out.flush();
			} else if (action != null && action.equals("deleteMovement")) {
				response.setContentType("text/html");
				this.deleteMovement(request.getParameter("id"));
				PrintWriter out = response.getWriter();
				out.flush();
			} else if (action != null && action.equals("getRoutes")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getRoutes(request));
				out.flush();
			} else if (action != null && action.equals("editRoute")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.editRoute(request.getParameter("id")));
				out.flush();
			} else if (action != null && action.equals("deleteRoute")) {
				response.setContentType("text/html");
				this.deleteRoute(request.getParameter("id"));
				PrintWriter out = response.getWriter();
				out.flush();
			} else if (action != null && action.equals("getLocationsForRoute")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getLocationsForRoute(request.getParameter("id")));
				out.flush();
			} else if (action != null && action.equals("getLocations")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getLocations());
				out.flush();
			} else if (action != null && action.equals("getLocation")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getLocationEdit(request.getParameter("id")));
				out.flush();
			} else if (action != null && action.equals("getSchedules")) {
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				out.println(this.getSchedules());
				out.flush();
			} else if (action != null && action.equals("deleteSchedule")) {
				response.setContentType("text/html");
				this.deleteSchedule(request.getParameter("id"));
				PrintWriter out = response.getWriter();
				out.flush();
			} else {
				System.out.println("Service not founds");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteSchedule(String parameter) {
		Connection conn = null;
		Statement stmt = null;
		try {
			try {
				Connection connection = this.getConnection();
				stmt = connection.createStatement();
				String sql = "delete from routesschedule where schedulename = '" + parameter + "'";
				stmt.executeQuery(sql);
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println(" Error logged " + e.getMessage());
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException var7_7) {
				}
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException var7_9) {
			}
		}
	}

	private String getSchedules() {
		StringBuffer buffer;
		block30: {
			Connection conn = null;
			Statement stmt = null;
			buffer = new StringBuffer();
			String sql = "SELECT * FROM routeschedule;";
			try {
				conn = this.getConnection();
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				buffer.append("<schedules>");
				while (rs.next()) {
					buffer.append("<schedule>");
					buffer.append("<schedulename>" + rs.getString("schedulename") + "</schedulename>" + "<routename>"
							+ rs.getString("routename") + "</routename>" + "<vehicleno>" + rs.getArray("vehicleno")
							+ "</vehicleno>" + "<startdate>" + rs.getDate("startdate") + "</startdate>" + "<starttime>"
							+ rs.getString("starttime") + "</starttime>" + "<enddate>" + rs.getDate("enddate")
							+ "</enddate>" + "<endtime>" + rs.getString("endtime") + "</endtime>" + "<createdby>"
							+ rs.getString("createdby") + "</createdby>" + "<createddate>" + rs.getDate("createddate")
							+ "</createddate>" + "<modifiedby>" + rs.getString("modifiedby") + "</modifiedby>"
							+ "<modifieddate>" + rs.getDate("modifieddate") + "</modifieddate>");
					buffer.append("</schedule>");
				}
				buffer.append("</schedules>");
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException var7_8) {
					// empty catch block
				}
				try {
					if (conn != null) {
						conn.close();
					}
					break block30;
				} catch (SQLException se6) {
					se6.printStackTrace();
				}
				break block30;
			} catch (Exception e) {
				try {
					e.printStackTrace();
				} catch (Throwable var6_16) {
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (SQLException se) {
					}
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}
				
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se) {
				// empty catch block
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return buffer.toString();
	}

	private String getLocationEdit(String id) {
		StringBuffer buffer;
		block30: {
			Connection conn = null;
			Statement stmt = null;
			buffer = new StringBuffer();
			String sql = "SELECT * FROM location where locationname = '" + id + "';";
			try {
				conn = this.getConnection();
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				buffer.append("<locations>");
				while (rs.next()) {
					buffer.append("<location>");
					buffer.append("<locationname>" + rs.getString("locationname") + "</locationname>"
							+ "<locationdescription>" + rs.getString("locationdescription") + "</locationdescription>"
							+ "<latitude>" + rs.getString("latitude") + "</latitude>" + "<longitude>"
							+ rs.getString("longitude") + "</longitude>" + "<radiuslocation>"
							+ rs.getString("radiuslocation") + "</radiuslocation>" + "<radiusreferlocation>"
							+ rs.getString("radiusreferlocation") + "</radiusreferlocation>" + "<address>"
							+ rs.getString("address") + "</address>");
					buffer.append("</location>");
				}
				buffer.append("</locations>");
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException var8_9) {
					// empty catch block
				}
				try {
					if (conn != null) {
						conn.close();
					}
					break block30;
				} catch (SQLException fg) {
					fg.printStackTrace();
				}
				break block30;
			} catch (Exception e) {
				try {
					e.printStackTrace();
				} catch (Throwable var7_17) {
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (SQLException se) {
					}
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}
				
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se) {
				// empty catch block
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return buffer.toString();
	}

	private String getLocations() {
		StringBuffer buffer;
		block30: {
			Connection conn = null;
			Statement stmt = null;
			buffer = new StringBuffer();
			String sql = "SELECT * FROM location;";
			try {
				conn = this.getConnection();
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				buffer.append("<locations>");
				while (rs.next()) {
					buffer.append("<location>");
					buffer.append("<locationname>" + rs.getString("locationname") + "</locationname>"
							+ "<locationdescription>" + rs.getString("locationdescription") + "</locationdescription>"
							+ "<latitude>" + rs.getString("latitude") + "</latitude>" + "<longitude>"
							+ rs.getString("longitude") + "</longitude>" + "<radiuslocation>"
							+ rs.getString("radiuslocation") + "</radiuslocation>" + "<radiusreferlocation>"
							+ rs.getString("radiusreferlocation") + "</radiusreferlocation>");
					buffer.append("</location>");
				}
				buffer.append("</locations>");
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException var7_8) {
					// empty catch block
				}
				try {
					if (conn != null) {
						conn.close();
					}
					break block30;
				} catch (SQLException se6) {
					se6.printStackTrace();
				}
				break block30;
			} catch (Exception e) {
				try {
					e.printStackTrace();
				} catch (Throwable var6_16) {
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (SQLException se) {
					}
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}
				
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se) {
				// empty catch block
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return buffer.toString();
	}

	private String getLocationsForRoute(String parameter) {
		StringBuffer buffer;
		block31: {
			Connection conn = null;
			Statement stmt = null;
			buffer = new StringBuffer();
			String sql = "SELECT locations FROM route where routename ='" + parameter + "';";
			try {
				conn = this.getConnection();
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				buffer.append("<routes>");
				while (rs.next()) {
					String[] nullable;
					Array array = rs.getArray("locations");
					String[] arrstring = nullable = (String[]) array.getArray();
					int n = arrstring.length;
					int n2 = 0;
					while (n2 < n) {
						String location = arrstring[n2];
						buffer.append("<route>" + location + "</route>");
						++n2;
					}
				}
				buffer.append("</routes>");
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException var14_15) {
					// empty catch block
				}
				try {
					if (conn != null) {
						conn.close();
					}
					break block31;
				} catch (SQLException se6) {
					se6.printStackTrace();
				}
				break block31;
			} catch (Exception e) {
				try {
					e.printStackTrace();
				} catch (Throwable var13_23) {
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (SQLException se) {
					}
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}
				
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se) {
				// empty catch block
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return buffer.toString();
	}

	private String editRoute(String parameter) {
		// This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
		// org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 14[CATCHBLOCK]
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:394)
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:446)
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2869)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:817)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:220)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:165)
		// org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:91)
		// org.benf.cfr.reader.entities.Method.analyse(Method.java:354)
		// org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:751)
		// org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:683)
		// org.sf.feeling.decompiler.cfr.decompiler.CfrDecompiler.decompile(CfrDecompiler.java:86)
		// org.sf.feeling.decompiler.editor.BaseDecompilerSourceMapper.decompile(BaseDecompilerSourceMapper.java:342)
		// org.sf.feeling.decompiler.util.DecompileUtil.decompiler(DecompileUtil.java:71)
		// org.sf.feeling.decompiler.editor.JavaDecompilerClassFileEditor.doSetInput(JavaDecompilerClassFileEditor.java:235)
		// org.sf.feeling.decompiler.editor.JavaDecompilerClassFileEditor.doSetInput(JavaDecompilerClassFileEditor.java:327)
		// org.eclipse.ui.texteditor.AbstractTextEditor$19.run(AbstractTextEditor.java:3220)
		// org.eclipse.jface.operation.ModalContext.runInCurrentThread(ModalContext.java:463)
		// org.eclipse.jface.operation.ModalContext.run(ModalContext.java:371)
		// org.eclipse.ui.internal.WorkbenchWindow$14.run(WorkbenchWindow.java:2156)
		// org.eclipse.swt.custom.BusyIndicator.showWhile(BusyIndicator.java:70)
		// org.eclipse.ui.internal.WorkbenchWindow.run(WorkbenchWindow.java:2152)
		// org.eclipse.ui.texteditor.AbstractTextEditor.internalInit(AbstractTextEditor.java:3238)
		// org.eclipse.ui.texteditor.AbstractTextEditor.init(AbstractTextEditor.java:3265)
		// org.eclipse.ui.internal.EditorReference.initialize(EditorReference.java:361)
		// org.eclipse.ui.internal.e4.compatibility.CompatibilityPart.create(CompatibilityPart.java:319)
		// sun.reflect.GeneratedMethodAccessor52.invoke(Unknown Source)
		// sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
		// java.lang.reflect.Method.invoke(Unknown Source)
		// org.eclipse.e4.core.internal.di.MethodRequestor.execute(MethodRequestor.java:56)
		// org.eclipse.e4.core.internal.di.InjectorImpl.processAnnotated(InjectorImpl.java:898)
		// org.eclipse.e4.core.internal.di.InjectorImpl.processAnnotated(InjectorImpl.java:879)
		// org.eclipse.e4.core.internal.di.InjectorImpl.inject(InjectorImpl.java:121)
		// org.eclipse.e4.core.internal.di.InjectorImpl.internalMake(InjectorImpl.java:345)
		// org.eclipse.e4.core.internal.di.InjectorImpl.make(InjectorImpl.java:264)
		// org.eclipse.e4.core.contexts.ContextInjectionFactory.make(ContextInjectionFactory.java:162)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.createFromBundle(ReflectionContributionFactory.java:104)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.doCreate(ReflectionContributionFactory.java:73)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.create(ReflectionContributionFactory.java:55)
		// org.eclipse.e4.ui.workbench.renderers.swt.ContributedPartRenderer.createWidget(ContributedPartRenderer.java:129)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.createWidget(PartRenderingEngine.java:971)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.safeCreateGui(PartRenderingEngine.java:640)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.safeCreateGui(PartRenderingEngine.java:746)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.access$0(PartRenderingEngine.java:717)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine$2.run(PartRenderingEngine.java:711)
		// org.eclipse.core.runtime.SafeRunner.run(SafeRunner.java:42)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.createGui(PartRenderingEngine.java:695)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl$1.handleEvent(PartServiceImpl.java:99)
		// org.eclipse.e4.ui.services.internal.events.UIEventHandler$1.run(UIEventHandler.java:40)
		// org.eclipse.swt.widgets.Synchronizer.syncExec(Synchronizer.java:186)
		// org.eclipse.ui.internal.UISynchronizer.syncExec(UISynchronizer.java:145)
		// org.eclipse.swt.widgets.Display.syncExec(Display.java:4761)
		// org.eclipse.e4.ui.internal.workbench.swt.E4Application$1.syncExec(E4Application.java:211)
		// org.eclipse.e4.ui.services.internal.events.UIEventHandler.handleEvent(UIEventHandler.java:36)
		// org.eclipse.equinox.internal.event.EventHandlerWrapper.handleEvent(EventHandlerWrapper.java:197)
		// org.eclipse.equinox.internal.event.EventHandlerTracker.dispatchEvent(EventHandlerTracker.java:197)
		// org.eclipse.equinox.internal.event.EventHandlerTracker.dispatchEvent(EventHandlerTracker.java:1)
		// org.eclipse.osgi.framework.eventmgr.EventManager.dispatchEvent(EventManager.java:230)
		// org.eclipse.osgi.framework.eventmgr.ListenerQueue.dispatchEventSynchronous(ListenerQueue.java:148)
		// org.eclipse.equinox.internal.event.EventAdminImpl.dispatchEvent(EventAdminImpl.java:135)
		// org.eclipse.equinox.internal.event.EventAdminImpl.sendEvent(EventAdminImpl.java:78)
		// org.eclipse.equinox.internal.event.EventComponent.sendEvent(EventComponent.java:39)
		// org.eclipse.e4.ui.services.internal.events.EventBroker.send(EventBroker.java:85)
		// org.eclipse.e4.ui.internal.workbench.UIEventPublisher.notifyChanged(UIEventPublisher.java:59)
		// org.eclipse.emf.common.notify.impl.BasicNotifierImpl.eNotify(BasicNotifierImpl.java:374)
		// org.eclipse.e4.ui.model.application.ui.impl.ElementContainerImpl.setSelectedElement(ElementContainerImpl.java:171)
		// org.eclipse.e4.ui.internal.workbench.ModelServiceImpl.showElementInWindow(ModelServiceImpl.java:494)
		// org.eclipse.e4.ui.internal.workbench.ModelServiceImpl.bringToTop(ModelServiceImpl.java:458)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.delegateBringToTop(PartServiceImpl.java:724)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.bringToTop(PartServiceImpl.java:396)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.showPart(PartServiceImpl.java:1166)
		// org.eclipse.ui.internal.WorkbenchPage.busyOpenEditor(WorkbenchPage.java:3234)
		// org.eclipse.ui.internal.WorkbenchPage.access$25(WorkbenchPage.java:3149)
		// org.eclipse.ui.internal.WorkbenchPage$10.run(WorkbenchPage.java:3131)
		// org.eclipse.swt.custom.BusyIndicator.showWhile(BusyIndicator.java:70)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3126)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3090)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3080)
		// org.eclipse.jdt.internal.ui.javaeditor.EditorUtility.openInEditor(EditorUtility.java:373)
		// org.eclipse.jdt.internal.ui.javaeditor.EditorUtility.openInEditor(EditorUtility.java:179)
		// org.eclipse.jdt.ui.actions.OpenAction.run(OpenAction.java:268)
		// org.eclipse.jdt.ui.actions.OpenAction.run(OpenAction.java:233)
		// org.eclipse.jdt.ui.actions.SelectionDispatchAction.dispatchRun(SelectionDispatchAction.java:275)
		// org.eclipse.jdt.ui.actions.SelectionDispatchAction.run(SelectionDispatchAction.java:251)
		// org.eclipse.jdt.internal.ui.navigator.OpenAndExpand.run(OpenAndExpand.java:50)
		// org.eclipse.ui.actions.RetargetAction.run(RetargetAction.java:229)
		// org.eclipse.ui.navigator.CommonNavigatorManager$2.open(CommonNavigatorManager.java:191)
		// org.eclipse.ui.OpenAndLinkWithEditorHelper$InternalListener.open(OpenAndLinkWithEditorHelper.java:48)
		// org.eclipse.jface.viewers.StructuredViewer$2.run(StructuredViewer.java:854)
		// org.eclipse.core.runtime.SafeRunner.run(SafeRunner.java:42)
		// org.eclipse.ui.internal.JFaceUtil$1.run(JFaceUtil.java:50)
		// org.eclipse.jface.util.SafeRunnable.run(SafeRunnable.java:173)
		// org.eclipse.jface.viewers.StructuredViewer.fireOpen(StructuredViewer.java:851)
		// org.eclipse.jface.viewers.StructuredViewer.handleOpen(StructuredViewer.java:1168)
		// org.eclipse.ui.navigator.CommonViewer.handleOpen(CommonViewer.java:449)
		// org.eclipse.jface.viewers.StructuredViewer$6.handleOpen(StructuredViewer.java:1275)
		// org.eclipse.jface.util.OpenStrategy.fireOpenEvent(OpenStrategy.java:278)
		// org.eclipse.jface.util.OpenStrategy.access$2(OpenStrategy.java:272)
		// org.eclipse.jface.util.OpenStrategy$1.handleEvent(OpenStrategy.java:313)
		// org.eclipse.swt.widgets.EventTable.sendEvent(EventTable.java:84)
		// org.eclipse.swt.widgets.Display.sendEvent(Display.java:4362)
		// org.eclipse.swt.widgets.Widget.sendEvent(Widget.java:1113)
		// org.eclipse.swt.widgets.Display.runDeferredEvents(Display.java:4180)
		// org.eclipse.swt.widgets.Display.readAndDispatch(Display.java:3769)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine$4.run(PartRenderingEngine.java:1127)
		// org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:337)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.run(PartRenderingEngine.java:1018)
		// org.eclipse.e4.ui.internal.workbench.E4Workbench.createAndRunUI(E4Workbench.java:156)
		// org.eclipse.ui.internal.Workbench$5.run(Workbench.java:654)
		// org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:337)
		// org.eclipse.ui.internal.Workbench.createAndRunWorkbench(Workbench.java:598)
		// org.eclipse.ui.PlatformUI.createAndRunWorkbench(PlatformUI.java:150)
		// org.eclipse.ui.internal.ide.application.IDEApplication.start(IDEApplication.java:139)
		// org.eclipse.equinox.internal.app.EclipseAppHandle.run(EclipseAppHandle.java:196)
		// org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.runApplication(EclipseAppLauncher.java:134)
		// org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.start(EclipseAppLauncher.java:104)
		// org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:380)
		// org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:235)
		// sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		// sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
		// sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
		// java.lang.reflect.Method.invoke(Unknown Source)
		// org.eclipse.equinox.launcher.Main.invokeFramework(Main.java:669)
		// org.eclipse.equinox.launcher.Main.basicRun(Main.java:608)
		// org.eclipse.equinox.launcher.Main.run(Main.java:1515)
		throw new IllegalStateException("Decompilation failed");
	}

	private void deleteRoute(String parameter) {
		Connection conn = null;
		Statement stmt = null;
		try {
			try {
				Connection connection = this.getConnection();
				stmt = connection.createStatement();
				String sql = "delete from route where routename = '" + parameter + "'";
				stmt.executeQuery(sql);
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println(" Error logged " + e.getMessage());
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException var7_7) {
				}
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException var7_9) {
			}
		}
	}

	private void deleteLocation(String parameter) {
		Connection conn = null;
		Statement stmt = null;
		try {
			try {
				Connection connection = this.getConnection();
				stmt = connection.createStatement();
				String sql = "delete from location where locationname = '" + parameter + "'";
				stmt.executeQuery(sql);
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println(" Error logged " + e.getMessage());
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException var7_7) {
				}
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException var7_9) {
			}
		}
	}

	private String getRoutes(HttpServletRequest request) {
		StringBuffer buffer;
		block30: {
			buffer = new StringBuffer();
			Connection conn = null;
			Statement stmt = null;
			String sql = "SELECT * FROM route where username ='" + request.getSession().getAttribute("username") + "';";
			try {
				conn = this.getConnection();
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				buffer.append("<routes>");
				while (rs.next()) {
					buffer.append("<route><routename>" + rs.getString("routename") + "</routename>" + "<locations>"
							+ rs.getArray("locations") + "</locations>" + "<createdby>" + rs.getString("createdby")
							+ "</createdby>" + "<createddate>" + rs.getDate("createddate") + "</createddate>"
							+ "<modifiedby>" + rs.getString("modifiedby") + "</modifiedby>" + "<modifieddate>"
							+ rs.getDate("modifieddate") + "</modifieddate>" + "</route>");
				}
				buffer.append("</routes>");
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException var8_9) {
					// empty catch block
				}
				try {
					if (conn != null) {
						conn.close();
					}
					break block30;
				} catch (SQLException seq) {
					seq.printStackTrace();
				}
				break block30;
			} catch (Exception e) {
				try {
					e.printStackTrace();
				} catch (Throwable var7_17) {
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (SQLException se) {
					}
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}
				
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se) {
				// empty catch block
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return buffer.toString();
	}

	private void deleteMovement(String parameter) {
		Connection conn = null;
		Statement stmt = null;
		try {
			try {
				Connection connection = this.getConnection();
				stmt = connection.createStatement();
				String sql = "delete from movement where id = '" + parameter + "'";
				stmt.executeQuery(sql);
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println(" Error logged " + e.getMessage());
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException var7_7) {
				}
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException var7_9) {
			}
		}
	}

	private String editMovement(String parameter) {
		// This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
		// org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 14[CATCHBLOCK]
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:394)
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:446)
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2869)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:817)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:220)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:165)
		// org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:91)
		// org.benf.cfr.reader.entities.Method.analyse(Method.java:354)
		// org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:751)
		// org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:683)
		// org.sf.feeling.decompiler.cfr.decompiler.CfrDecompiler.decompile(CfrDecompiler.java:86)
		// org.sf.feeling.decompiler.editor.BaseDecompilerSourceMapper.decompile(BaseDecompilerSourceMapper.java:342)
		// org.sf.feeling.decompiler.util.DecompileUtil.decompiler(DecompileUtil.java:71)
		// org.sf.feeling.decompiler.editor.JavaDecompilerClassFileEditor.doSetInput(JavaDecompilerClassFileEditor.java:235)
		// org.sf.feeling.decompiler.editor.JavaDecompilerClassFileEditor.doSetInput(JavaDecompilerClassFileEditor.java:327)
		// org.eclipse.ui.texteditor.AbstractTextEditor$19.run(AbstractTextEditor.java:3220)
		// org.eclipse.jface.operation.ModalContext.runInCurrentThread(ModalContext.java:463)
		// org.eclipse.jface.operation.ModalContext.run(ModalContext.java:371)
		// org.eclipse.ui.internal.WorkbenchWindow$14.run(WorkbenchWindow.java:2156)
		// org.eclipse.swt.custom.BusyIndicator.showWhile(BusyIndicator.java:70)
		// org.eclipse.ui.internal.WorkbenchWindow.run(WorkbenchWindow.java:2152)
		// org.eclipse.ui.texteditor.AbstractTextEditor.internalInit(AbstractTextEditor.java:3238)
		// org.eclipse.ui.texteditor.AbstractTextEditor.init(AbstractTextEditor.java:3265)
		// org.eclipse.ui.internal.EditorReference.initialize(EditorReference.java:361)
		// org.eclipse.ui.internal.e4.compatibility.CompatibilityPart.create(CompatibilityPart.java:319)
		// sun.reflect.GeneratedMethodAccessor52.invoke(Unknown Source)
		// sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
		// java.lang.reflect.Method.invoke(Unknown Source)
		// org.eclipse.e4.core.internal.di.MethodRequestor.execute(MethodRequestor.java:56)
		// org.eclipse.e4.core.internal.di.InjectorImpl.processAnnotated(InjectorImpl.java:898)
		// org.eclipse.e4.core.internal.di.InjectorImpl.processAnnotated(InjectorImpl.java:879)
		// org.eclipse.e4.core.internal.di.InjectorImpl.inject(InjectorImpl.java:121)
		// org.eclipse.e4.core.internal.di.InjectorImpl.internalMake(InjectorImpl.java:345)
		// org.eclipse.e4.core.internal.di.InjectorImpl.make(InjectorImpl.java:264)
		// org.eclipse.e4.core.contexts.ContextInjectionFactory.make(ContextInjectionFactory.java:162)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.createFromBundle(ReflectionContributionFactory.java:104)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.doCreate(ReflectionContributionFactory.java:73)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.create(ReflectionContributionFactory.java:55)
		// org.eclipse.e4.ui.workbench.renderers.swt.ContributedPartRenderer.createWidget(ContributedPartRenderer.java:129)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.createWidget(PartRenderingEngine.java:971)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.safeCreateGui(PartRenderingEngine.java:640)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.safeCreateGui(PartRenderingEngine.java:746)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.access$0(PartRenderingEngine.java:717)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine$2.run(PartRenderingEngine.java:711)
		// org.eclipse.core.runtime.SafeRunner.run(SafeRunner.java:42)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.createGui(PartRenderingEngine.java:695)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl$1.handleEvent(PartServiceImpl.java:99)
		// org.eclipse.e4.ui.services.internal.events.UIEventHandler$1.run(UIEventHandler.java:40)
		// org.eclipse.swt.widgets.Synchronizer.syncExec(Synchronizer.java:186)
		// org.eclipse.ui.internal.UISynchronizer.syncExec(UISynchronizer.java:145)
		// org.eclipse.swt.widgets.Display.syncExec(Display.java:4761)
		// org.eclipse.e4.ui.internal.workbench.swt.E4Application$1.syncExec(E4Application.java:211)
		// org.eclipse.e4.ui.services.internal.events.UIEventHandler.handleEvent(UIEventHandler.java:36)
		// org.eclipse.equinox.internal.event.EventHandlerWrapper.handleEvent(EventHandlerWrapper.java:197)
		// org.eclipse.equinox.internal.event.EventHandlerTracker.dispatchEvent(EventHandlerTracker.java:197)
		// org.eclipse.equinox.internal.event.EventHandlerTracker.dispatchEvent(EventHandlerTracker.java:1)
		// org.eclipse.osgi.framework.eventmgr.EventManager.dispatchEvent(EventManager.java:230)
		// org.eclipse.osgi.framework.eventmgr.ListenerQueue.dispatchEventSynchronous(ListenerQueue.java:148)
		// org.eclipse.equinox.internal.event.EventAdminImpl.dispatchEvent(EventAdminImpl.java:135)
		// org.eclipse.equinox.internal.event.EventAdminImpl.sendEvent(EventAdminImpl.java:78)
		// org.eclipse.equinox.internal.event.EventComponent.sendEvent(EventComponent.java:39)
		// org.eclipse.e4.ui.services.internal.events.EventBroker.send(EventBroker.java:85)
		// org.eclipse.e4.ui.internal.workbench.UIEventPublisher.notifyChanged(UIEventPublisher.java:59)
		// org.eclipse.emf.common.notify.impl.BasicNotifierImpl.eNotify(BasicNotifierImpl.java:374)
		// org.eclipse.e4.ui.model.application.ui.impl.ElementContainerImpl.setSelectedElement(ElementContainerImpl.java:171)
		// org.eclipse.e4.ui.internal.workbench.ModelServiceImpl.showElementInWindow(ModelServiceImpl.java:494)
		// org.eclipse.e4.ui.internal.workbench.ModelServiceImpl.bringToTop(ModelServiceImpl.java:458)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.delegateBringToTop(PartServiceImpl.java:724)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.bringToTop(PartServiceImpl.java:396)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.showPart(PartServiceImpl.java:1166)
		// org.eclipse.ui.internal.WorkbenchPage.busyOpenEditor(WorkbenchPage.java:3234)
		// org.eclipse.ui.internal.WorkbenchPage.access$25(WorkbenchPage.java:3149)
		// org.eclipse.ui.internal.WorkbenchPage$10.run(WorkbenchPage.java:3131)
		// org.eclipse.swt.custom.BusyIndicator.showWhile(BusyIndicator.java:70)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3126)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3090)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3080)
		// org.eclipse.jdt.internal.ui.javaeditor.EditorUtility.openInEditor(EditorUtility.java:373)
		// org.eclipse.jdt.internal.ui.javaeditor.EditorUtility.openInEditor(EditorUtility.java:179)
		// org.eclipse.jdt.ui.actions.OpenAction.run(OpenAction.java:268)
		// org.eclipse.jdt.ui.actions.OpenAction.run(OpenAction.java:233)
		// org.eclipse.jdt.ui.actions.SelectionDispatchAction.dispatchRun(SelectionDispatchAction.java:275)
		// org.eclipse.jdt.ui.actions.SelectionDispatchAction.run(SelectionDispatchAction.java:251)
		// org.eclipse.jdt.internal.ui.navigator.OpenAndExpand.run(OpenAndExpand.java:50)
		// org.eclipse.ui.actions.RetargetAction.run(RetargetAction.java:229)
		// org.eclipse.ui.navigator.CommonNavigatorManager$2.open(CommonNavigatorManager.java:191)
		// org.eclipse.ui.OpenAndLinkWithEditorHelper$InternalListener.open(OpenAndLinkWithEditorHelper.java:48)
		// org.eclipse.jface.viewers.StructuredViewer$2.run(StructuredViewer.java:854)
		// org.eclipse.core.runtime.SafeRunner.run(SafeRunner.java:42)
		// org.eclipse.ui.internal.JFaceUtil$1.run(JFaceUtil.java:50)
		// org.eclipse.jface.util.SafeRunnable.run(SafeRunnable.java:173)
		// org.eclipse.jface.viewers.StructuredViewer.fireOpen(StructuredViewer.java:851)
		// org.eclipse.jface.viewers.StructuredViewer.handleOpen(StructuredViewer.java:1168)
		// org.eclipse.ui.navigator.CommonViewer.handleOpen(CommonViewer.java:449)
		// org.eclipse.jface.viewers.StructuredViewer$6.handleOpen(StructuredViewer.java:1275)
		// org.eclipse.jface.util.OpenStrategy.fireOpenEvent(OpenStrategy.java:278)
		// org.eclipse.jface.util.OpenStrategy.access$2(OpenStrategy.java:272)
		// org.eclipse.jface.util.OpenStrategy$1.handleEvent(OpenStrategy.java:313)
		// org.eclipse.swt.widgets.EventTable.sendEvent(EventTable.java:84)
		// org.eclipse.swt.widgets.Display.sendEvent(Display.java:4362)
		// org.eclipse.swt.widgets.Widget.sendEvent(Widget.java:1113)
		// org.eclipse.swt.widgets.Display.runDeferredEvents(Display.java:4180)
		// org.eclipse.swt.widgets.Display.readAndDispatch(Display.java:3769)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine$4.run(PartRenderingEngine.java:1127)
		// org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:337)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.run(PartRenderingEngine.java:1018)
		// org.eclipse.e4.ui.internal.workbench.E4Workbench.createAndRunUI(E4Workbench.java:156)
		// org.eclipse.ui.internal.Workbench$5.run(Workbench.java:654)
		// org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:337)
		// org.eclipse.ui.internal.Workbench.createAndRunWorkbench(Workbench.java:598)
		// org.eclipse.ui.PlatformUI.createAndRunWorkbench(PlatformUI.java:150)
		// org.eclipse.ui.internal.ide.application.IDEApplication.start(IDEApplication.java:139)
		// org.eclipse.equinox.internal.app.EclipseAppHandle.run(EclipseAppHandle.java:196)
		// org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.runApplication(EclipseAppLauncher.java:134)
		// org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.start(EclipseAppLauncher.java:104)
		// org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:380)
		// org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:235)
		// sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		// sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
		// sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
		// java.lang.reflect.Method.invoke(Unknown Source)
		// org.eclipse.equinox.launcher.Main.invokeFramework(Main.java:669)
		// org.eclipse.equinox.launcher.Main.basicRun(Main.java:608)
		// org.eclipse.equinox.launcher.Main.run(Main.java:1515)
		throw new IllegalStateException("Decompilation failed");
	}

	private String getMovements(String parameter) {
		StringBuffer buffer;
		block30: {
			buffer = new StringBuffer();
			Connection conn = null;
			Statement stmt = null;
			String sql = "SELECT * FROM Movement ;";
			try {
				conn = this.getConnection();
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				buffer.append("<movements>");
				while (rs.next()) {
					buffer.append("<movement><vehicle>" + rs.getString("vehicle") + "</vehicle>" + "<groupname>"
							+ rs.getString("groupname") + "</groupname>" + "<startdate>" + rs.getDate("startdate")
							+ "</startdate>" + "<enddate>" + rs.getDate("enddate") + "</enddate>" + "<starttime>"
							+ rs.getString("starttime") + "</starttime>" + "<endtime>" + rs.getString("endtime")
							+ "</endtime>" + "<snoozetime>" + rs.getString("snoozetime") + "</snoozetime>"
							+ "<modifiedby>" + rs.getString("modifiedby") + "</modifiedby>" + "<modifieddate>"
							+ rs.getDate("modifieddate") + "</modifieddate>" + "<id>" + rs.getString("id") + "</id>"
							+ "</movement>");
				}
				buffer.append("</movements>");
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException var8_9) {
					// empty catch block
				}
				try {
					if (conn != null) {
						conn.close();
					}
					break block30;
				} catch (SQLException se6) {
					se6.printStackTrace();
				}
				break block30;
			} catch (Exception e) {
				try {
					e.printStackTrace();
				} catch (Throwable var7_17) {
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (SQLException se) {
					}
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}
				
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se) {
				// empty catch block
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return buffer.toString();
	}

	private String editUser(String parameter) {
		String html = this.getHtmlFile("User_master_entry");
		html = html.replaceAll("%username%", parameter);
		return html;
	}

	private String getUserData(HttpServletRequest request) {
		StringBuffer buffer;
		block30: {
			buffer = new StringBuffer();
			Connection conn = null;
			Statement stmt = null;
			String sql = "SELECT * FROM UserMaster ;";
			try {
				conn = this.getConnection();
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				buffer.append("<users>");
				while (rs.next()) {
					buffer.append("<user><username>" + rs.getString("username") + "</username>" + "<createdby>"
							+ rs.getString("createdby") + "</createdby>" + "<createddate>" + rs.getDate("createddate")
							+ "</createddate>" + "<modifiedby>" + rs.getString("modifiedby") + "</modifiedby>"
							+ "<modifieddate>" + rs.getDate("modifieddate") + "</modifieddate>" + "</user>");
				}
				buffer.append("</users>");
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException var8_9) {
					// empty catch block
				}
				try {
					if (conn != null) {
						conn.close();
					}
					break block30;
				} catch (SQLException se5) {
					se5.printStackTrace();
				}
				break block30;
			} catch (Exception e) {
				try {
					e.printStackTrace();
				} catch (Throwable var7_17) {
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (SQLException se) {
					}
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}
			
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se) {
				// empty catch block
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return buffer.toString();
	}

	private String editDriver(String parameter) {
		// This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
		// org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 14[CATCHBLOCK]
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:394)
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:446)
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2869)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:817)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:220)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:165)
		// org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:91)
		// org.benf.cfr.reader.entities.Method.analyse(Method.java:354)
		// org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:751)
		// org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:683)
		// org.sf.feeling.decompiler.cfr.decompiler.CfrDecompiler.decompile(CfrDecompiler.java:86)
		// org.sf.feeling.decompiler.editor.BaseDecompilerSourceMapper.decompile(BaseDecompilerSourceMapper.java:342)
		// org.sf.feeling.decompiler.util.DecompileUtil.decompiler(DecompileUtil.java:71)
		// org.sf.feeling.decompiler.editor.JavaDecompilerClassFileEditor.doSetInput(JavaDecompilerClassFileEditor.java:235)
		// org.sf.feeling.decompiler.editor.JavaDecompilerClassFileEditor.doSetInput(JavaDecompilerClassFileEditor.java:327)
		// org.eclipse.ui.texteditor.AbstractTextEditor$19.run(AbstractTextEditor.java:3220)
		// org.eclipse.jface.operation.ModalContext.runInCurrentThread(ModalContext.java:463)
		// org.eclipse.jface.operation.ModalContext.run(ModalContext.java:371)
		// org.eclipse.ui.internal.WorkbenchWindow$14.run(WorkbenchWindow.java:2156)
		// org.eclipse.swt.custom.BusyIndicator.showWhile(BusyIndicator.java:70)
		// org.eclipse.ui.internal.WorkbenchWindow.run(WorkbenchWindow.java:2152)
		// org.eclipse.ui.texteditor.AbstractTextEditor.internalInit(AbstractTextEditor.java:3238)
		// org.eclipse.ui.texteditor.AbstractTextEditor.init(AbstractTextEditor.java:3265)
		// org.eclipse.ui.internal.EditorReference.initialize(EditorReference.java:361)
		// org.eclipse.ui.internal.e4.compatibility.CompatibilityPart.create(CompatibilityPart.java:319)
		// sun.reflect.GeneratedMethodAccessor52.invoke(Unknown Source)
		// sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
		// java.lang.reflect.Method.invoke(Unknown Source)
		// org.eclipse.e4.core.internal.di.MethodRequestor.execute(MethodRequestor.java:56)
		// org.eclipse.e4.core.internal.di.InjectorImpl.processAnnotated(InjectorImpl.java:898)
		// org.eclipse.e4.core.internal.di.InjectorImpl.processAnnotated(InjectorImpl.java:879)
		// org.eclipse.e4.core.internal.di.InjectorImpl.inject(InjectorImpl.java:121)
		// org.eclipse.e4.core.internal.di.InjectorImpl.internalMake(InjectorImpl.java:345)
		// org.eclipse.e4.core.internal.di.InjectorImpl.make(InjectorImpl.java:264)
		// org.eclipse.e4.core.contexts.ContextInjectionFactory.make(ContextInjectionFactory.java:162)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.createFromBundle(ReflectionContributionFactory.java:104)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.doCreate(ReflectionContributionFactory.java:73)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.create(ReflectionContributionFactory.java:55)
		// org.eclipse.e4.ui.workbench.renderers.swt.ContributedPartRenderer.createWidget(ContributedPartRenderer.java:129)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.createWidget(PartRenderingEngine.java:971)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.safeCreateGui(PartRenderingEngine.java:640)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.safeCreateGui(PartRenderingEngine.java:746)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.access$0(PartRenderingEngine.java:717)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine$2.run(PartRenderingEngine.java:711)
		// org.eclipse.core.runtime.SafeRunner.run(SafeRunner.java:42)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.createGui(PartRenderingEngine.java:695)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl$1.handleEvent(PartServiceImpl.java:99)
		// org.eclipse.e4.ui.services.internal.events.UIEventHandler$1.run(UIEventHandler.java:40)
		// org.eclipse.swt.widgets.Synchronizer.syncExec(Synchronizer.java:186)
		// org.eclipse.ui.internal.UISynchronizer.syncExec(UISynchronizer.java:145)
		// org.eclipse.swt.widgets.Display.syncExec(Display.java:4761)
		// org.eclipse.e4.ui.internal.workbench.swt.E4Application$1.syncExec(E4Application.java:211)
		// org.eclipse.e4.ui.services.internal.events.UIEventHandler.handleEvent(UIEventHandler.java:36)
		// org.eclipse.equinox.internal.event.EventHandlerWrapper.handleEvent(EventHandlerWrapper.java:197)
		// org.eclipse.equinox.internal.event.EventHandlerTracker.dispatchEvent(EventHandlerTracker.java:197)
		// org.eclipse.equinox.internal.event.EventHandlerTracker.dispatchEvent(EventHandlerTracker.java:1)
		// org.eclipse.osgi.framework.eventmgr.EventManager.dispatchEvent(EventManager.java:230)
		// org.eclipse.osgi.framework.eventmgr.ListenerQueue.dispatchEventSynchronous(ListenerQueue.java:148)
		// org.eclipse.equinox.internal.event.EventAdminImpl.dispatchEvent(EventAdminImpl.java:135)
		// org.eclipse.equinox.internal.event.EventAdminImpl.sendEvent(EventAdminImpl.java:78)
		// org.eclipse.equinox.internal.event.EventComponent.sendEvent(EventComponent.java:39)
		// org.eclipse.e4.ui.services.internal.events.EventBroker.send(EventBroker.java:85)
		// org.eclipse.e4.ui.internal.workbench.UIEventPublisher.notifyChanged(UIEventPublisher.java:59)
		// org.eclipse.emf.common.notify.impl.BasicNotifierImpl.eNotify(BasicNotifierImpl.java:374)
		// org.eclipse.e4.ui.model.application.ui.impl.ElementContainerImpl.setSelectedElement(ElementContainerImpl.java:171)
		// org.eclipse.e4.ui.internal.workbench.ModelServiceImpl.showElementInWindow(ModelServiceImpl.java:494)
		// org.eclipse.e4.ui.internal.workbench.ModelServiceImpl.bringToTop(ModelServiceImpl.java:458)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.delegateBringToTop(PartServiceImpl.java:724)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.bringToTop(PartServiceImpl.java:396)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.showPart(PartServiceImpl.java:1166)
		// org.eclipse.ui.internal.WorkbenchPage.busyOpenEditor(WorkbenchPage.java:3234)
		// org.eclipse.ui.internal.WorkbenchPage.access$25(WorkbenchPage.java:3149)
		// org.eclipse.ui.internal.WorkbenchPage$10.run(WorkbenchPage.java:3131)
		// org.eclipse.swt.custom.BusyIndicator.showWhile(BusyIndicator.java:70)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3126)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3090)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3080)
		// org.eclipse.jdt.internal.ui.javaeditor.EditorUtility.openInEditor(EditorUtility.java:373)
		// org.eclipse.jdt.internal.ui.javaeditor.EditorUtility.openInEditor(EditorUtility.java:179)
		// org.eclipse.jdt.ui.actions.OpenAction.run(OpenAction.java:268)
		// org.eclipse.jdt.ui.actions.OpenAction.run(OpenAction.java:233)
		// org.eclipse.jdt.ui.actions.SelectionDispatchAction.dispatchRun(SelectionDispatchAction.java:275)
		// org.eclipse.jdt.ui.actions.SelectionDispatchAction.run(SelectionDispatchAction.java:251)
		// org.eclipse.jdt.internal.ui.navigator.OpenAndExpand.run(OpenAndExpand.java:50)
		// org.eclipse.ui.actions.RetargetAction.run(RetargetAction.java:229)
		// org.eclipse.ui.navigator.CommonNavigatorManager$2.open(CommonNavigatorManager.java:191)
		// org.eclipse.ui.OpenAndLinkWithEditorHelper$InternalListener.open(OpenAndLinkWithEditorHelper.java:48)
		// org.eclipse.jface.viewers.StructuredViewer$2.run(StructuredViewer.java:854)
		// org.eclipse.core.runtime.SafeRunner.run(SafeRunner.java:42)
		// org.eclipse.ui.internal.JFaceUtil$1.run(JFaceUtil.java:50)
		// org.eclipse.jface.util.SafeRunnable.run(SafeRunnable.java:173)
		// org.eclipse.jface.viewers.StructuredViewer.fireOpen(StructuredViewer.java:851)
		// org.eclipse.jface.viewers.StructuredViewer.handleOpen(StructuredViewer.java:1168)
		// org.eclipse.ui.navigator.CommonViewer.handleOpen(CommonViewer.java:449)
		// org.eclipse.jface.viewers.StructuredViewer$6.handleOpen(StructuredViewer.java:1275)
		// org.eclipse.jface.util.OpenStrategy.fireOpenEvent(OpenStrategy.java:278)
		// org.eclipse.jface.util.OpenStrategy.access$2(OpenStrategy.java:272)
		// org.eclipse.jface.util.OpenStrategy$1.handleEvent(OpenStrategy.java:313)
		// org.eclipse.swt.widgets.EventTable.sendEvent(EventTable.java:84)
		// org.eclipse.swt.widgets.Display.sendEvent(Display.java:4362)
		// org.eclipse.swt.widgets.Widget.sendEvent(Widget.java:1113)
		// org.eclipse.swt.widgets.Display.runDeferredEvents(Display.java:4180)
		// org.eclipse.swt.widgets.Display.readAndDispatch(Display.java:3769)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine$4.run(PartRenderingEngine.java:1127)
		// org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:337)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.run(PartRenderingEngine.java:1018)
		// org.eclipse.e4.ui.internal.workbench.E4Workbench.createAndRunUI(E4Workbench.java:156)
		// org.eclipse.ui.internal.Workbench$5.run(Workbench.java:654)
		// org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:337)
		// org.eclipse.ui.internal.Workbench.createAndRunWorkbench(Workbench.java:598)
		// org.eclipse.ui.PlatformUI.createAndRunWorkbench(PlatformUI.java:150)
		// org.eclipse.ui.internal.ide.application.IDEApplication.start(IDEApplication.java:139)
		// org.eclipse.equinox.internal.app.EclipseAppHandle.run(EclipseAppHandle.java:196)
		// org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.runApplication(EclipseAppLauncher.java:134)
		// org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.start(EclipseAppLauncher.java:104)
		// org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:380)
		// org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:235)
		// sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		// sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
		// sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
		// java.lang.reflect.Method.invoke(Unknown Source)
		// org.eclipse.equinox.launcher.Main.invokeFramework(Main.java:669)
		// org.eclipse.equinox.launcher.Main.basicRun(Main.java:608)
		// org.eclipse.equinox.launcher.Main.run(Main.java:1515)
		throw new IllegalStateException("Decompilation failed");
	}

	private void deleteDriver(String parameter) {
		Connection conn = null;
		Statement stmt = null;
		try {
			try {
				Connection connection = this.getConnection();
				stmt = connection.createStatement();
				String sql = "delete from drivermaster where id = '" + parameter + "'";
				stmt.executeQuery(sql);
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println(" Error logged " + e.getMessage());
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException var7_7) {
				}
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException var7_9) {
			}
		}
	}

	private String getDriverData(String username) {
		StringBuffer buffer;
		block30: {
			buffer = new StringBuffer();
			Connection conn = null;
			Statement stmt = null;
			String sql = "SELECT * FROM drivermaster where username='" + username + "'";
			try {
				conn = this.getConnection();
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				buffer.append("<drivers>");
				while (rs.next()) {
					buffer.append("<driver><drivername>" + rs.getString("drivername") + "</drivername>" + "<address>"
							+ rs.getString("address") + "</address>" + "<contact1>" + rs.getString("contact1")
							+ "</contact1>" + "<licenseno>" + rs.getString("licenseno") + "</licenseno>"
							+ "<licenseexpirydate>" + rs.getDate("licenseexpirydate") + "</licenseexpirydate>"
							+ "<rtoname>" + rs.getString("rtoname") + "</rtoname>" + "<bloodgroup>"
							+ rs.getString("bloodgroup") + "</bloodgroup>" + "<createdby>" + rs.getString("createdby")
							+ "</createdby>" + "<createddate>" + rs.getDate("createddate") + "</createddate>"
							+ "<modifiedby>" + rs.getString("modifiedby") + "</modifiedby>" + "<modifieddate>"
							+ rs.getDate("modifieddate") + "</modifieddate>" + "<id>" + rs.getInt("id") + "</id>"
							+ "</driver>");
				}
				buffer.append("</drivers>");
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException var8_9) {
					// empty catch block
				}
				try {
					if (conn != null) {
						conn.close();
					}
					break block30;
				} catch (SQLException se6) {
					se6.printStackTrace();
				}
				break block30;
			} catch (Exception e) {
				try {
					e.printStackTrace();
				} catch (Throwable var7_17) {
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (SQLException se) {
					}
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}
				
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se) {
				// empty catch block
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return buffer.toString();
	}

	private String getUngroupedVehicles(String groupName) {
		StringBuffer buffer;
		block33: {
			buffer = new StringBuffer();
			Connection conn = null;
			Statement stmt = null;
			String sql = "";
			ResultSet rs = null;
			try {
				conn = this.getConnection();
				stmt = conn.createStatement();
				buffer.append("<vehicles>");
				if (groupName != null) {
					sql = "SELECT vehicleno from vehiclemaster where groupname ='" + groupName + "'";
					rs = stmt.executeQuery(sql);
					while (rs.next()) {
						buffer.append("<vehicle>");
						buffer.append(rs.getString("vehicleno"));
						buffer.append("</vehicle>");
					}
				}
				if (rs != null) {
					rs.close();
				}
				sql = "SELECT vehicleno from vehiclemaster where groupname IS  NULL";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					buffer.append("<vehicle>");
					buffer.append(rs.getString("vehicleno"));
					buffer.append("</vehicle>");
				}
				buffer.append("</vehicles>");
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException var9_9) {
					// empty catch block
				}
				try {
					if (conn != null) {
						conn.close();
					}
					break block33;
				} catch (SQLException se6) {
					se6.printStackTrace();
				}
				break block33;
			} catch (Exception e) {
				try {
					e.printStackTrace();
				} catch (Throwable var8_17) {
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (SQLException se) {
					}
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}
				
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se) {
				// empty catch block
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return buffer.toString();
	}

	private String getGroupData(HttpServletRequest request) {
		String html;
		StringBuffer buffer;
		block33: {
			html = this.getHtmlFile("group_view");
			String id = request.getParameter("id");
			html = html.replaceAll("%groupName%", id);
			buffer = new StringBuffer();
			Connection conn = null;
			Statement stmt = null;
			String sql = "SELECT vehicleno from vehiclemaster where groupname IS  NULL";
			sql = "SELECT vehicleno from vehiclemaster where groupname ='" + id + "'";
			ArrayList<String> groups = new ArrayList<String>();
			try {
				String vehicleno;
				conn = this.getConnection();
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				sql = "SELECT vehicleno from vehiclemaster where groupname ='" + id + "'";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					vehicleno = rs.getString("vehicleno");
					groups.add(vehicleno);
					buffer.append("<tr class='leftMenu'><td><input type='checkbox' checked value='true' id=" + vehicleno
							+ " name=" + vehicleno + "></td><td>" + vehicleno + "</td></tr>");
				}
				if (rs != null) {
					rs.close();
				}
				sql = "SELECT vehicleno from vehiclemaster where groupname IS  NULL";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					vehicleno = rs.getString("vehicleno");
					groups.add(vehicleno);
					buffer.append("<tr class='leftMenu'><td><input type='checkbox' value='true' id=" + vehicleno
							+ " name=" + vehicleno + "></td><td>" + vehicleno + "</td></tr>");
				}
				if (rs != null) {
					rs.close();
				}
				request.getSession().setAttribute("groups", groups);
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException var12_13) {
					// empty catch block
				}
				try {
					if (conn != null) {
						conn.close();
					}
					break block33;
				} catch (SQLException se6) {
					se6.printStackTrace();
				}
				break block33;
			} catch (Exception e) {
				try {
					e.printStackTrace();
				} catch (Throwable var11_21) {
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (SQLException se) {
					}
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}
				
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se) {
				// empty catch block
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		html = html.replaceAll("%data%", buffer.toString());
		return html;
	}

	private String getGroups(String parameter) {
		StringBuffer buffer;
		block32: {
			buffer = new StringBuffer();
			Connection conn = null;
			Statement stmt = null;
			String sql = "SELECT * from vehiclegroup where createdby = '" + parameter + "';";
			HashMap<String, String> groups = new HashMap<String, String>();
			try {
				conn = this.getConnection();
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					groups.put(rs.getString("id"),
							String.valueOf(rs.getString("createdby")) + " - " + rs.getDate("createddate"));
				}
				buffer.append("<groups>");
				for (String group : groups.keySet()) {
					sql = "select count(groupname) as groups from vehiclemaster where groupname = '" + group + "';";
					rs = stmt.executeQuery(sql);
					while (rs.next()) {
						String groupdata = (String) groups.get(group);
						String count = rs.getString("groups");
						buffer.append("<group><groupname>" + group + "</groupname>" + "<count>" + count + "</count>"
								+ "<createdby>" + groupdata.substring(0, groupdata.indexOf("-")) + "</createdby>"
								+ "<createddate>" + groupdata.substring(groupdata.indexOf("-") + 1) + "</createddate>"
								+ "</group>");
					}
				}
				buffer.append("</groups>");
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException var13_14) {
					// empty catch block
				}
				try {
					if (conn != null) {
						conn.close();
					}
					break block32;
				} catch (SQLException sed) {
					sed.printStackTrace();
				}
				break block32;
			} catch (Exception e) {
				try {
					e.printStackTrace();
				} catch (Throwable var12_22) {
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (SQLException se) {
					}
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}
				
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se) {
				// empty catch block
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return buffer.toString();
	}

	private String getAlertData(String username) {
		StringBuffer buffer;
		block30: {
			buffer = new StringBuffer();
			Connection conn = null;
			Statement stmt = null;
			String sql = "SELECT alert.vehicleno, overspeed , suddenbreak,idletime , panic , geofency , snoozetime , alertbysms , alertbymail , contactno , alert.modifiedby , alert.createdby , alert.createddate, alert.group , alert.modifieddate FROM alert, vehiclemaster where alert.vehicleno = vehiclemaster.vehicleno AND vehiclemaster.username='"
					+ username + "'";
			try {
				conn = this.getConnection();
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				buffer.append("<vehicles>");
				while (rs.next()) {
					buffer.append("<alert><group>" + rs.getBoolean("group") + "</group>" + "<vehicleNo>"
							+ rs.getString("vehicleNo") + "</vehicleNo>" + "<name>Nikhil</name>" + "<overspeed>"
							+ rs.getBoolean("overspeed") + "</overspeed>" + "<suddenbreak>"
							+ rs.getBoolean("suddenbreak") + "</suddenbreak>" + "<idletime>" + rs.getBoolean("idletime")
							+ "</idletime>" + "<panic>" + rs.getBoolean("panic") + "</panic>" + "<geofency>"
							+ rs.getBoolean("geofency") + "</geofency>" + "<snoozetime>" + rs.getString("snoozetime")
							+ "</snoozetime>" + "<alertbysms>" + rs.getBoolean("alertbysms") + "</alertbysms>"
							+ "<alertbymail>" + rs.getBoolean("alertbymail") + "</alertbymail>" + "<contactno>"
							+ rs.getString("contactno") + "</contactno>" + "<modifiedby>" + rs.getString("modifiedby")
							+ "</modifiedby>" + "<modifieddate>" + rs.getDate("modifieddate") + "</modifieddate>"
							+ "<createdby>" + rs.getString("createdby") + "</createdby>" + "<createddate>"
							+ rs.getDate("createddate") + "</createddate>" + "</alert>");
				}
				buffer.append("</vehicles>");
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException var8_9) {
					// empty catch block
				}
				try {
					if (conn != null) {
						conn.close();
					}
					break block30;
				} catch (SQLException sed) {
					sed.printStackTrace();
				}
				break block30;
			} catch (Exception e) {
				try {
					e.printStackTrace();
				} catch (Throwable var7_17) {
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (SQLException se) {
					}
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}
				
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se) {
				// empty catch block
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return buffer.toString();
	}

	private String editVehicle(String parameter) {
		// This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
		// org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 9[CATCHBLOCK]
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:394)
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:446)
		// org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:2869)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:817)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:220)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:165)
		// org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:91)
		// org.benf.cfr.reader.entities.Method.analyse(Method.java:354)
		// org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:751)
		// org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:683)
		// org.sf.feeling.decompiler.cfr.decompiler.CfrDecompiler.decompile(CfrDecompiler.java:86)
		// org.sf.feeling.decompiler.editor.BaseDecompilerSourceMapper.decompile(BaseDecompilerSourceMapper.java:342)
		// org.sf.feeling.decompiler.util.DecompileUtil.decompiler(DecompileUtil.java:71)
		// org.sf.feeling.decompiler.editor.JavaDecompilerClassFileEditor.doSetInput(JavaDecompilerClassFileEditor.java:235)
		// org.sf.feeling.decompiler.editor.JavaDecompilerClassFileEditor.doSetInput(JavaDecompilerClassFileEditor.java:327)
		// org.eclipse.ui.texteditor.AbstractTextEditor$19.run(AbstractTextEditor.java:3220)
		// org.eclipse.jface.operation.ModalContext.runInCurrentThread(ModalContext.java:463)
		// org.eclipse.jface.operation.ModalContext.run(ModalContext.java:371)
		// org.eclipse.ui.internal.WorkbenchWindow$14.run(WorkbenchWindow.java:2156)
		// org.eclipse.swt.custom.BusyIndicator.showWhile(BusyIndicator.java:70)
		// org.eclipse.ui.internal.WorkbenchWindow.run(WorkbenchWindow.java:2152)
		// org.eclipse.ui.texteditor.AbstractTextEditor.internalInit(AbstractTextEditor.java:3238)
		// org.eclipse.ui.texteditor.AbstractTextEditor.init(AbstractTextEditor.java:3265)
		// org.eclipse.ui.internal.EditorReference.initialize(EditorReference.java:361)
		// org.eclipse.ui.internal.e4.compatibility.CompatibilityPart.create(CompatibilityPart.java:319)
		// sun.reflect.GeneratedMethodAccessor52.invoke(Unknown Source)
		// sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
		// java.lang.reflect.Method.invoke(Unknown Source)
		// org.eclipse.e4.core.internal.di.MethodRequestor.execute(MethodRequestor.java:56)
		// org.eclipse.e4.core.internal.di.InjectorImpl.processAnnotated(InjectorImpl.java:898)
		// org.eclipse.e4.core.internal.di.InjectorImpl.processAnnotated(InjectorImpl.java:879)
		// org.eclipse.e4.core.internal.di.InjectorImpl.inject(InjectorImpl.java:121)
		// org.eclipse.e4.core.internal.di.InjectorImpl.internalMake(InjectorImpl.java:345)
		// org.eclipse.e4.core.internal.di.InjectorImpl.make(InjectorImpl.java:264)
		// org.eclipse.e4.core.contexts.ContextInjectionFactory.make(ContextInjectionFactory.java:162)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.createFromBundle(ReflectionContributionFactory.java:104)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.doCreate(ReflectionContributionFactory.java:73)
		// org.eclipse.e4.ui.internal.workbench.ReflectionContributionFactory.create(ReflectionContributionFactory.java:55)
		// org.eclipse.e4.ui.workbench.renderers.swt.ContributedPartRenderer.createWidget(ContributedPartRenderer.java:129)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.createWidget(PartRenderingEngine.java:971)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.safeCreateGui(PartRenderingEngine.java:640)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.safeCreateGui(PartRenderingEngine.java:746)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.access$0(PartRenderingEngine.java:717)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine$2.run(PartRenderingEngine.java:711)
		// org.eclipse.core.runtime.SafeRunner.run(SafeRunner.java:42)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.createGui(PartRenderingEngine.java:695)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl$1.handleEvent(PartServiceImpl.java:99)
		// org.eclipse.e4.ui.services.internal.events.UIEventHandler$1.run(UIEventHandler.java:40)
		// org.eclipse.swt.widgets.Synchronizer.syncExec(Synchronizer.java:186)
		// org.eclipse.ui.internal.UISynchronizer.syncExec(UISynchronizer.java:145)
		// org.eclipse.swt.widgets.Display.syncExec(Display.java:4761)
		// org.eclipse.e4.ui.internal.workbench.swt.E4Application$1.syncExec(E4Application.java:211)
		// org.eclipse.e4.ui.services.internal.events.UIEventHandler.handleEvent(UIEventHandler.java:36)
		// org.eclipse.equinox.internal.event.EventHandlerWrapper.handleEvent(EventHandlerWrapper.java:197)
		// org.eclipse.equinox.internal.event.EventHandlerTracker.dispatchEvent(EventHandlerTracker.java:197)
		// org.eclipse.equinox.internal.event.EventHandlerTracker.dispatchEvent(EventHandlerTracker.java:1)
		// org.eclipse.osgi.framework.eventmgr.EventManager.dispatchEvent(EventManager.java:230)
		// org.eclipse.osgi.framework.eventmgr.ListenerQueue.dispatchEventSynchronous(ListenerQueue.java:148)
		// org.eclipse.equinox.internal.event.EventAdminImpl.dispatchEvent(EventAdminImpl.java:135)
		// org.eclipse.equinox.internal.event.EventAdminImpl.sendEvent(EventAdminImpl.java:78)
		// org.eclipse.equinox.internal.event.EventComponent.sendEvent(EventComponent.java:39)
		// org.eclipse.e4.ui.services.internal.events.EventBroker.send(EventBroker.java:85)
		// org.eclipse.e4.ui.internal.workbench.UIEventPublisher.notifyChanged(UIEventPublisher.java:59)
		// org.eclipse.emf.common.notify.impl.BasicNotifierImpl.eNotify(BasicNotifierImpl.java:374)
		// org.eclipse.e4.ui.model.application.ui.impl.ElementContainerImpl.setSelectedElement(ElementContainerImpl.java:171)
		// org.eclipse.e4.ui.internal.workbench.ModelServiceImpl.showElementInWindow(ModelServiceImpl.java:494)
		// org.eclipse.e4.ui.internal.workbench.ModelServiceImpl.bringToTop(ModelServiceImpl.java:458)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.delegateBringToTop(PartServiceImpl.java:724)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.bringToTop(PartServiceImpl.java:396)
		// org.eclipse.e4.ui.internal.workbench.PartServiceImpl.showPart(PartServiceImpl.java:1166)
		// org.eclipse.ui.internal.WorkbenchPage.busyOpenEditor(WorkbenchPage.java:3234)
		// org.eclipse.ui.internal.WorkbenchPage.access$25(WorkbenchPage.java:3149)
		// org.eclipse.ui.internal.WorkbenchPage$10.run(WorkbenchPage.java:3131)
		// org.eclipse.swt.custom.BusyIndicator.showWhile(BusyIndicator.java:70)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3126)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3090)
		// org.eclipse.ui.internal.WorkbenchPage.openEditor(WorkbenchPage.java:3080)
		// org.eclipse.jdt.internal.ui.javaeditor.EditorUtility.openInEditor(EditorUtility.java:373)
		// org.eclipse.jdt.internal.ui.javaeditor.EditorUtility.openInEditor(EditorUtility.java:179)
		// org.eclipse.jdt.ui.actions.OpenAction.run(OpenAction.java:268)
		// org.eclipse.jdt.ui.actions.OpenAction.run(OpenAction.java:233)
		// org.eclipse.jdt.ui.actions.SelectionDispatchAction.dispatchRun(SelectionDispatchAction.java:275)
		// org.eclipse.jdt.ui.actions.SelectionDispatchAction.run(SelectionDispatchAction.java:251)
		// org.eclipse.jdt.internal.ui.navigator.OpenAndExpand.run(OpenAndExpand.java:50)
		// org.eclipse.ui.actions.RetargetAction.run(RetargetAction.java:229)
		// org.eclipse.ui.navigator.CommonNavigatorManager$2.open(CommonNavigatorManager.java:191)
		// org.eclipse.ui.OpenAndLinkWithEditorHelper$InternalListener.open(OpenAndLinkWithEditorHelper.java:48)
		// org.eclipse.jface.viewers.StructuredViewer$2.run(StructuredViewer.java:854)
		// org.eclipse.core.runtime.SafeRunner.run(SafeRunner.java:42)
		// org.eclipse.ui.internal.JFaceUtil$1.run(JFaceUtil.java:50)
		// org.eclipse.jface.util.SafeRunnable.run(SafeRunnable.java:173)
		// org.eclipse.jface.viewers.StructuredViewer.fireOpen(StructuredViewer.java:851)
		// org.eclipse.jface.viewers.StructuredViewer.handleOpen(StructuredViewer.java:1168)
		// org.eclipse.ui.navigator.CommonViewer.handleOpen(CommonViewer.java:449)
		// org.eclipse.jface.viewers.StructuredViewer$6.handleOpen(StructuredViewer.java:1275)
		// org.eclipse.jface.util.OpenStrategy.fireOpenEvent(OpenStrategy.java:278)
		// org.eclipse.jface.util.OpenStrategy.access$2(OpenStrategy.java:272)
		// org.eclipse.jface.util.OpenStrategy$1.handleEvent(OpenStrategy.java:313)
		// org.eclipse.swt.widgets.EventTable.sendEvent(EventTable.java:84)
		// org.eclipse.swt.widgets.Display.sendEvent(Display.java:4362)
		// org.eclipse.swt.widgets.Widget.sendEvent(Widget.java:1113)
		// org.eclipse.swt.widgets.Display.runDeferredEvents(Display.java:4180)
		// org.eclipse.swt.widgets.Display.readAndDispatch(Display.java:3769)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine$4.run(PartRenderingEngine.java:1127)
		// org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:337)
		// org.eclipse.e4.ui.internal.workbench.swt.PartRenderingEngine.run(PartRenderingEngine.java:1018)
		// org.eclipse.e4.ui.internal.workbench.E4Workbench.createAndRunUI(E4Workbench.java:156)
		// org.eclipse.ui.internal.Workbench$5.run(Workbench.java:654)
		// org.eclipse.core.databinding.observable.Realm.runWithDefault(Realm.java:337)
		// org.eclipse.ui.internal.Workbench.createAndRunWorkbench(Workbench.java:598)
		// org.eclipse.ui.PlatformUI.createAndRunWorkbench(PlatformUI.java:150)
		// org.eclipse.ui.internal.ide.application.IDEApplication.start(IDEApplication.java:139)
		// org.eclipse.equinox.internal.app.EclipseAppHandle.run(EclipseAppHandle.java:196)
		// org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.runApplication(EclipseAppLauncher.java:134)
		// org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher.start(EclipseAppLauncher.java:104)
		// org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:380)
		// org.eclipse.core.runtime.adaptor.EclipseStarter.run(EclipseStarter.java:235)
		// sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		// sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
		// sun.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source)
		// java.lang.reflect.Method.invoke(Unknown Source)
		// org.eclipse.equinox.launcher.Main.invokeFramework(Main.java:669)
		// org.eclipse.equinox.launcher.Main.basicRun(Main.java:608)
		// org.eclipse.equinox.launcher.Main.run(Main.java:1515)
		throw new IllegalStateException("Decompilation failed");
	}

	private String getRouteData(String unitNo) throws ParseException {
		StringBuffer buffer;
		block19: {
			Statement stmt;
			Connection conn;
			conn = null;
			stmt = null;
			buffer = new StringBuffer();
			try {
				try {
					Connection connection = this.getConnection();
					stmt = connection.createStatement();
					String sql = "select latitude, longitude, datetime, datetimedate, dir from gsmmaster where datetime = ( select max( datetime ) from gsmmaster where status = 1 and unitno = '"
							+ unitNo + "' and datetimedate = "
							+ "( select max( datetimedate ) from gsmmaster where status = 1 and unitno = '" + unitNo
							+ "' ) )";
					ResultSet rs = stmt.executeQuery(sql);
					buffer.append("<vehicles>");
					while (rs.next()) {
						SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
						java.util.Date today = df.parse(rs.getString("dateTime"));
						Date startTime = rs.getDate("datetimedate");
						System.out.println(today +""+ startTime);
						rs.close();
						sql = "select * from gsmmaster where datetimedate >= '" + startTime + "' and unitno = '"
								+ unitNo + "'";
						rs = stmt.executeQuery(sql);
						while (rs.next()) {
							df = new SimpleDateFormat("HH:mm:ss");
							java.util.Date time = df.parse(rs.getString("dateTime"));
							if (!time.after(today))
								continue;
							buffer.append("<vehicle>");
							buffer.append("<latitude>" + rs.getString("latitude") + "</latitude>");
							buffer.append("<longitude>" + rs.getString("longitude") + "</longitude>");
							buffer.append("<dir>" + rs.getInt("dir") + "</dir>");
							buffer.append("</vehicle>");
						}
					}
					buffer.append("</vehicles>");
				} catch (ClassNotFoundException | SQLException e) {
					System.err.println(" Error logged " + e.getMessage());
					try {
						if (stmt != null) {
							stmt.close();
						}
						if (conn != null) {
							conn.close();
						}
						break block19;
					} catch (SQLException var13_13) {
					}
					break block19;
				}
			} catch (Throwable var12_16) {
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException var13_14) {
					// empty catch block
				}
				throw var12_16;
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException var13_15) {
				// empty catch block
			}
		}
		System.out.println(buffer.toString());
		return buffer.toString();
	}

	private void deleteVehicle(String vehicleNo) {
		Connection conn = null;
		Statement stmt = null;
		try {
			try {
				Connection connection = this.getConnection();
				stmt = connection.createStatement();
				String sql = "delete from vehiclemaster where vehicleno = '" + vehicleNo + "'";
				stmt.executeQuery(sql);
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println(" Error logged " + e.getMessage());
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException var7_7) {
				}
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException var7_9) {
			}
		}
	}

	private void deleteAlert(String alert) {
		Connection conn = null;
		Statement stmt = null;
		try {
			try {
				Connection connection = this.getConnection();
				stmt = connection.createStatement();
				String sql = "delete from alert where vehicleno = '" + alert + "'";
				stmt.executeQuery(sql);
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println(" Error logged " + e.getMessage());
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException var7_7) {
				}
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException var7_9) {
			}
		}
	}

	private void deleteGroup(String group) {
		Connection conn = null;
		Statement stmt = null;
		String sql = "";
		try {
			try {
				Connection connection = this.getConnection();
				stmt = connection.createStatement();
				sql = "update vehiclemaster set groupname = NULL  where groupname = '" + group + "'";
				try {
					stmt.executeQuery(sql);
				} catch (Exception e) {
					System.err.println(" Error logged " + e.getMessage());
				}
				sql = "delete from vehiclegroup where id = '" + group + "'";
				try {
					stmt.executeQuery(sql);
				} catch (Exception e) {
					System.err.println(" Error logged " + e.getMessage());
				}
			} catch (ClassNotFoundException | SQLException e) {
				System.err.println(" Error logged " + e.getMessage());
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException var8_9) {
				}
			}
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException var8_11) {
			}
		}
	}

	private String getVehicles(String username) {
		StringBuffer buffer;
		block22: {
			buffer = new StringBuffer();
			Connection conn = null;
			Statement stmt = null;
			try {
				conn = this.getConnection();
				stmt = conn.createStatement();
				String sql = "SELECT vehicleNo, unitNo, ownercontact1, owneremail, owneraddress1,owneraddress2,owneraddress3, ownercity, createdby, createddate, modifiedby, modifieddate FROM  VehicleMaster where VehicleMaster.username = '"
						+ username + "';";
				ResultSet rs = stmt.executeQuery(sql);
				buffer.append("<vehicles>");
				while (rs.next()) {
					String vehicleNo = rs.getString("vehicleNo");
					int unitNo = rs.getInt("unitNo");
					String ownercontact1 = rs.getString("ownercontact1");
					String owneremail = rs.getString("owneremail");
					String owneraddress1 = rs.getString("owneraddress1");
					String ownercity = rs.getString("ownercity");
					String createdby = rs.getString("createdby");
					String modifiedby = rs.getString("modifiedby");
					Date modifieddate = rs.getDate("modifieddate");
					Date createddate = rs.getDate("createddate");
					buffer.append("<vehicle><vehicleNo>" + vehicleNo + "</vehicleNo>" + "<unitNo>" + unitNo
							+ "</unitNo>" + "<ownercontact>" + ownercontact1 + "</ownercontact>" + "<owneremail>"
							+ owneremail + "</owneremail>" + "<owneraddress>" + owneraddress1 + "</owneraddress>"
							+ "<ownercity>" + ownercity + "</ownercity>" + "<createdby>" + createdby + "</createdby>"
							+ "<createddate>" + createddate + "</createddate>" + "<modifiedby>" + modifiedby
							+ "</modifiedby>" + "<modifieddate>" + modifieddate + "</modifieddate>" + "</vehicle>");
				}
				buffer.append("</vehicles>");
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
					break block22;
				} catch (SQLException var18_19) {
				}
				break block22;
			} catch (Exception e) {
				try {
					e.printStackTrace();
				} catch (Throwable var17_23) {
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException var18_20) {
					}
				}
				
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException var18_22) {
				// empty catch block
			}
		}
		return buffer.toString();
	}

	private String getVehicleMapData(String username) {
		StringBuffer buffer;
		block22: {
			buffer = new StringBuffer();
			Connection conn = null;
			Statement stmt = null;
			try {
				conn = this.getConnection();
				stmt = conn.createStatement();
				String sql = "SELECT GsmStatus.unitNo, GsmStatus.status, GsmStatus.datetime, GsmStatus.datetimedate, GsmStatus.latitude, GsmStatus.speed, GsmStatus.longitude, GsmStatus.location, VehicleMaster.vehicleno, VehicleMaster.vehicletype FROM GsmStatus , VehicleMaster where GsmStatus.unitno=VehicleMaster.unitno and VehicleMaster.username = '"
						+ username + "';";
				ResultSet rs = stmt.executeQuery(sql);
				buffer.append("<vehicles>");
				while (rs.next()) {
					String vehicleNo = rs.getString("vehicleNo");
					int unitNo = rs.getInt("unitNo");
					String location = rs.getString("location");
					String longitude = rs.getString("longitude");
					String latitude = rs.getString("latitude");
					buffer.append("<vehicle><vehicleNo>" + vehicleNo + "</vehicleNo>" + "<fuel>0</fuel>" + "<status>"
							+ statusCodes.get(rs.getInt("status")) + "</status>" + "<speed>" + rs.getInt("speed")
							+ "</speed>" + "<ac>ON</ac>" + "<odo>235</odo>" + "<location>" + location + "</location>"
							+ "<movement>YES</movement>" + "<dateTime>" + rs.getDate("datetimedate") + " "
							+ rs.getString("dateTime") + "</dateTime>" + "<unitNo>" + unitNo + "</unitNo>"
							+ "<longitude>" + longitude + "</longitude>" + "<latitude>" + latitude + "</latitude>"
							+ "</vehicle>");
				}
				buffer.append("</vehicles>");
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
					break block22;
				} catch (SQLException var13_14) {
				}
				break block22;
			} catch (Exception e) {
				try {
					e.printStackTrace();
				} catch (Throwable var12_18) {
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException var13_15) {
					}
				}
				
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException var13_17) {
				// empty catch block
			}
		}
		return buffer.toString();
	}

	private boolean validate(HttpServletRequest httpRequest) {
		String username = httpRequest.getParameter("username");
		String password = httpRequest.getParameter("password");
		System.out.println(password);
		try {
			Connection connection = this.getConnection();
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM userMaster where username='" + username.toString() + "'";
			System.out.println(sql);
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs != null && rs.next()) {
				String serverPass = rs.getString("password");
				System.out.println(serverPass);
				if (serverPass == null || password == null)
					continue;
				return serverPass.equals(password);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private String getVehicleData(String username) {
		StringBuffer buffer;
		block30: {
			buffer = new StringBuffer();
			Connection conn = null;
			Statement stmt = null;
			String sql = "SELECT vehicleno,insuranceexpirydate, insuranceno, currentfuel, vehicletype ,username FROM vehiclemaster where username='"
					+ username + "'";
			try {
				conn = this.getConnection();
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				buffer.append("<vehicles>");
				while (rs.next()) {
					String vehicleNo = rs.getString("vehicleNo");
					String insuranceexpirydate = rs.getString("insuranceexpirydate");
					String vehicletype = rs.getString("vehicletype");
					String insuranceno = rs.getString("insuranceno");
					String currentfuel = rs.getString("currentfuel");
					buffer.append("<vehicle><vehicleNo>" + vehicleNo + "</vehicleNo>" + "<currentodo>"
							+ insuranceexpirydate + "</currentodo>" + "<vehicletype>" + vehicletype + "</vehicletype>"
							+ "<insuranceno>" + insuranceno + "</insuranceno>" + "<currentfuel>" + currentfuel
							+ "</currentfuel>" + "<username>" + username + "</username>" + "</vehicle>");
				}
				buffer.append("</vehicles>");
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					if (stmt != null) {
						stmt.close();
					}
				} catch (SQLException var13_14) {
					// empty catch block
				}
				try {
					if (conn != null) {
						conn.close();
					}
					break block30;
				} catch (SQLException se1) {
					se1.printStackTrace();
				}
				break block30;
			} catch (Exception e) {
				try {
					e.printStackTrace();
				} catch (Throwable var12_22) {
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
					} catch (SQLException se) {
					}
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}
				
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException se) {
				// empty catch block
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return buffer.toString();
	}

	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection conn=null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/trackMe", "trackMe", "root");
			System.out.println("connection object"+conn);
			return conn;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("connection failed"+e.getMessage());
		}
		return conn;
	}

	private String getHtmlFile(String fileName) {
		StringBuilder contentBuilder = new StringBuilder();
		try { ;
			System.out.println(getServletContext().getRealPath("")+"//html//"
					+ fileName + ".html");
			File file = new File(getServletContext().getRealPath("")+"//html//"
					+ fileName + ".html");
			System.out.println("path::::::::"+this.getServletContext().getContextPath());
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

	private Double calculateFuel(int currentfuel, String formula) {
		Double fuelLevel = new Double(currentfuel);
		return null;
	}

	private String getSampleHtml(String username) {
		StringBuffer data;
		block29: {
			data = new StringBuffer();
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				conn = this.getConnection();
				stmt = conn.createStatement();
				String sql = "SELECT vehicleno FROM vehiclemaster where username='" + username + "'";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				ArrayList<String> vehicleNos = new ArrayList<String>();
				while (rs.next()) {
					vehicleNos.add(rs.getString("vehicleno"));
				}
				HashMap<String, String> fuelFormulas = new HashMap<String, String>();
				if (rs != null) {
					rs.close();
				}
				for (String vehicle : vehicleNos) {
					sql = "SELECT fuelformula from vehiclemastermapping where vehicleno = '" + vehicle + "'";
					rs = stmt.executeQuery(sql);
					while (rs.next()) {
						fuelFormulas.put(vehicle, rs.getString("fuelformula").trim());
					}
				}
				if (rs != null) {
					rs.close();
				}
				sql = "SELECT GsmStatus.unitNo, VehicleMaster.vehicleNo, GsmStatus.speed, GsmStatus.status, GsmStatus.latitude, GsmStatus.longitude, GsmStatus.location, GsmStatus.dateTime, GsmStatus.dateTimeDate, VehicleMaster.currentFuel, VehicleMaster.currentOdiMeter FROM GsmStatus , VehicleMaster where GsmStatus.unitno=VehicleMaster.unitno and VehicleMaster.username = '"
						+ username + "';";
				rs = stmt.executeQuery(sql);
				data.append("<vehicles>");
				while (rs.next()) {
					data.append("<vehicle>");
					String vehicleNo = rs.getString("vehicleNo");
					String formula = (String) fuelFormulas.get(vehicleNo);
					int speed = rs.getInt("speed");
					int status = rs.getInt("status");
					int fuel = rs.getInt("currentFuel");
					Double fuelValue = this.calculateFuel(fuel, formula);
					int odo = rs.getInt("currentOdiMeter");
					String location = rs.getString("location");
					SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
					SimpleDateFormat df1 = new SimpleDateFormat("yyyy-mm-dd");
					try {
						java.util.Date today = df.parse(rs.getString("dateTime"));
						java.util.Date todayDate = df1.parse(rs.getString("dateTimeDate"));
						data.append("<td>" + vehicleNo + "</td>" + "<td>" + statusCodes.get(status) + "</td>" + "<td>"
								+ speed + "</td>" + "<td>" + fuelValue + "</td>" + "<td> ON </td>" + "<td>" + odo
								+ "</td>" + "<td> Moving </td>" + "<td>" + location + "</td>" + "<td>"
								+ df1.format(todayDate) + "</td>" + "<td>" + df.format(today) + "</td>");
					} catch (ParseException e) {
						e.printStackTrace();
					}
					data.append("</vehicle>");
				}
				data.append("</vehicles>");
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
				try {
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
					break block29;
				} catch (SQLException var22_24) {
				}
				break block29;
			} catch (Exception e) {
				try {
					e.printStackTrace();
				} catch (Throwable var21_28) {
				} finally {
					try {
						if (stmt != null) {
							stmt.close();
						}
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException var22_25) {
					}
				}
				
			}
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException var22_27) {
				// empty catch block
			}
		}
		return data.toString();
	}

	private void loadStatusCodes() {
		statusCodes = new HashMap<Integer, String>();
		statusCodes.put(0, "Ignition Off");
		statusCodes.put(1, "Ignition On");
		statusCodes.put(2, "Moving");
		statusCodes.put(3, "Normal Distance");
		statusCodes.put(4, "Moving");
		statusCodes.put(5, "Harsh Brake");
		statusCodes.put(6, "Harsh Acceleration");
		statusCodes.put(7, "Overspeed");
		statusCodes.put(8, "Idle Excess");
		statusCodes.put(9, "Ignition Off Poll");
		statusCodes.put(10, "Normal Poll");
		statusCodes.put(11, "Battery Low");
		statusCodes.put(12, "Battery Disconnect");
		statusCodes.put(13, "Panic");
		statusCodes.put(14, "Health Check");
		statusCodes.put(15, "Overspeed Start");
		statusCodes.put(16, "Overspeed Highest Speed");
		statusCodes.put(17, "Overspeed End");
		statusCodes.put(18, "Idling Start");
		statusCodes.put(19, "Idling End");
		statusCodes.put(23, "Illegal Movement");
		statusCodes.put(68, "Poll Reboot");
		statusCodes.put(99, "Reset");
	}

	private void callValidate(String validator) {
		if (validator != null && !validator.equals("")) {
			try {
				Class cls = Class.forName(validator);
				Object obj = cls.newInstance();
				Method method = cls.getDeclaredMethod("validate", new Class[0]);
				method.invoke(obj, null);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
}