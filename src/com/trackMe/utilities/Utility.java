/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.trackMe.utilities;

import com.trackMe.mapper.ConnectionClass;
import com.trackMe.mapper.DataObjectImpl;
import com.trackMe.mapper.DriverConf;
import com.trackMe.mapper.GsmMaster;
import com.trackMe.mapper.GsmStatus;
import com.trackMe.mapper.UnitMaster;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Date;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.postgresql.util.PSQLException;

public class Utility {
	private static final String COMMA = ",";

	public void lookup(String inputStr) {
		// This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
		// org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
		// org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:143)
		// org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:385)
		// org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
		// org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:425)
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
		// sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
		// sun.reflect.NativeMethodAccessorImpl.invoke(Unknown Source)
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

	private void updateGsmStatus(String[] tokens) {
		GsmStatus gsmMaster = new GsmStatus();
		if (!tokens[0].equals("M") && !tokens[0].equals("K")) {
			gsmMaster.setFuel(tokens[10]);
		}
		gsmMaster.setDateTime(new Date(Long.parseLong(tokens[7]) * 1000));
		gsmMaster.setFix(Integer.parseInt(tokens[6]));
		gsmMaster.setLatitude(tokens[2]);
		gsmMaster.setLongitude(tokens[3]);
		gsmMaster.setAltitude(tokens[8]);
		gsmMaster.setDir(Integer.parseInt(tokens[5]));
		try {
			gsmMaster.setLocation(this.getLocation(tokens[2], tokens[3]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (tokens[0].equals("K")) {
			gsmMaster.setDigitalIO(tokens[9]);
			gsmMaster.setStatus(99);
		} else if (!tokens[0].equals("K")) {
			gsmMaster.setStatus(Integer.parseInt(tokens[9]));
		}
		gsmMaster.setSpeed(Integer.parseInt(tokens[4].trim()));
		gsmMaster.setUnitNo(Integer.parseInt(tokens[1]));
		DataObjectImpl dataObjectImpl = new DataObjectImpl(gsmMaster);
		String query = dataObjectImpl.insert();
		System.out.println(query);
		ConnectionClass connectionClass = new ConnectionClass();
		try {
			connectionClass.getQuery(query);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			dataObjectImpl = new DataObjectImpl(gsmMaster);
			String whereClause = " unitno = " + Integer.parseInt(tokens[1]);
			String query1 = dataObjectImpl.update(whereClause);
			query1 = query1.replace(",  where ", " where ");
			System.out.println(query1);
			try {
				connectionClass.getQuery(query1);
				System.out.println("updated");
			} catch (Exception e1) {
				System.err.println("GsmStatus update failed" + e.getMessage());
			}
		}
	}

	private void tracking(String[] tokens) {
		try {
			GsmMaster gsmMaster = new GsmMaster();
			gsmMaster.setFuel(tokens[10]);
			gsmMaster.setDateTime(new Date(Long.parseLong(tokens[7]) * 1000));
			gsmMaster.setFix(Integer.parseInt(tokens[6]));
			gsmMaster.setIoValues(Integer.parseInt(tokens[1]));
			gsmMaster.setLatitude(tokens[2]);
			gsmMaster.setLongitude(tokens[3]);
			gsmMaster.setDir(Integer.parseInt(tokens[5].trim()));
			try {
				gsmMaster.setLocation(this.getLocation(tokens[2], tokens[3]));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			gsmMaster.setSpeed(Integer.parseInt(tokens[4].trim()));
			gsmMaster.setStatus(Integer.parseInt(tokens[9]));
			gsmMaster.setUnitNo(Integer.parseInt(tokens[1]));
			gsmMaster.setAltitude(tokens[8]);
			if (tokens.length > 11) {
				gsmMaster.setIdleTime(tokens[11]);
				gsmMaster.setDistance(tokens[12]);
			}
			DataObjectImpl dataObjectImpl = new DataObjectImpl(gsmMaster);
			String query = dataObjectImpl.insert();
			System.out.println(query);
			ConnectionClass connectionClass = new ConnectionClass();
			try {
				connectionClass.getQuery(query);
				this.updateGsmStatus(tokens, connectionClass);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateGsmStatus(String[] tokens, ConnectionClass connectionClass) {
		try {
			GsmStatus gsmMaster = new GsmStatus();
			gsmMaster.setFuel(tokens[10]);
			gsmMaster.setDateTime(new Date(Long.parseLong(tokens[7]) * 1000));
			gsmMaster.setFix(Integer.parseInt(tokens[6]));
			gsmMaster.setIoValues(Integer.parseInt(tokens[1]));
			gsmMaster.setLatitude(tokens[2]);
			gsmMaster.setLongitude(tokens[3]);
			gsmMaster.setDir(Integer.parseInt(tokens[5].trim()));
			try {
				gsmMaster.setLocation(this.getLocation(tokens[2], tokens[3]));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			gsmMaster.setSpeed(Integer.parseInt(tokens[4].trim()));
			gsmMaster.setStatus(Integer.parseInt(tokens[9]));
			gsmMaster.setUnitNo(Integer.parseInt(tokens[1]));
			gsmMaster.setAltitude(tokens[8]);
			if (tokens.length > 11) {
				gsmMaster.setIdleTime(tokens[11]);
				gsmMaster.setDistance(tokens[12]);
			}
			DataObjectImpl dataObjectImpl = new DataObjectImpl(gsmMaster);
			try {
				String query = dataObjectImpl.insert();
				System.out.println(query);
				connectionClass.getQuery(query);
			} catch (PSQLException e) {
				dataObjectImpl = new DataObjectImpl(gsmMaster);
				String whereClause = " unitno = " + Integer.parseInt(tokens[1]);
				String query1 = dataObjectImpl.update(whereClause);
				query1 = query1.replace(",  where ", " where ");
				System.out.println(query1);
				connectionClass.getQuery(query1);
				System.out.println("updated");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dataUsage(String[] tokens) {
	}

	private void confirmationAT(String[] tokens) {
		DriverConf conf = new DriverConf();
		DataObjectImpl dataObjectImpl = new DataObjectImpl(conf);
		dataObjectImpl.insert();
	}

	private void generalTracking(String[] tokens) {
		GsmMaster gsmMaster = new GsmMaster();
		if (!tokens[0].equals("M") && !tokens[0].equals("K")) {
			gsmMaster.setFuel(tokens[10]);
		}
		gsmMaster.setDateTime(new Date(Long.parseLong(tokens[7]) * 1000));
		gsmMaster.setFix(Integer.parseInt(tokens[6]));
		gsmMaster.setLatitude(tokens[2]);
		gsmMaster.setLongitude(tokens[3]);
		gsmMaster.setAltitude(tokens[8]);
		gsmMaster.setDir(Integer.parseInt(tokens[5]));
		try {
			gsmMaster.setLocation(this.getLocation(tokens[2], tokens[3]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		gsmMaster.setSpeed(Integer.parseInt(tokens[4].trim()));
		if (tokens[0].equals("K")) {
			gsmMaster.setDigitalIO(tokens[9]);
			gsmMaster.setStatus(99);
		}
		if (!tokens[0].equals("K")) {
			gsmMaster.setStatus(Integer.parseInt(tokens[9]));
		}
		gsmMaster.setUnitNo(Integer.parseInt(tokens[1]));
		DataObjectImpl dataObjectImpl = new DataObjectImpl(gsmMaster);
		String query = dataObjectImpl.insert();
		System.out.println(query);
		ConnectionClass connectionClass = new ConnectionClass();
		try {
			connectionClass.getQuery(query);
		} catch (Exception var6_7) {
			// empty catch block
		}
	}

	private String getLocation(String lat, String log) throws ParseException {
		InputStream inputStream = null;
		String json = "";
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(
					"http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + log + "&sensor=true");
			HttpResponse response = client.execute((HttpUriRequest) post);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
		} catch (Exception client) {
			// empty catch block
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
			StringBuilder sbuild = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sbuild.append(line);
			}
			inputStream.close();
			json = sbuild.toString();
		} catch (Exception reader) {
			// empty catch block
		}
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(json);
		JSONObject jb = (JSONObject) obj;
		JSONArray jsonObject1 = (JSONArray) jb.get((Object) "results");
		JSONObject jsonObject2 = (JSONObject) jsonObject1.get(0);
		return jsonObject2.get((Object) "formatted_address").toString().replaceAll(",", "").trim().toString();
	}

	public String[] getTokens(String inputStr) {
		return inputStr.split(",");
	}

	public void unitRegistration(String[] inputStr) {
		try {
			UnitMaster unitMaster = new UnitMaster();
			unitMaster.setCreatedDate(new Date());
			unitMaster.setGpsNo(inputStr[2]);
			unitMaster.setOnOffStatus(true);
			unitMaster.setUnitIMEINo(inputStr[1].trim());
			unitMaster.setUnitNo(Integer.parseInt(inputStr[3]));
			DataObjectImpl dataObjectImpl = new DataObjectImpl(unitMaster);
			String query = dataObjectImpl.insert();
			System.out.println(query);
			ConnectionClass connectionClass = new ConnectionClass();
			connectionClass.getQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}