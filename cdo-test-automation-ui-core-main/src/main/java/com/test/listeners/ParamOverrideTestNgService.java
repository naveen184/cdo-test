package com.test.listeners;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.reflect.FieldUtils;

import com.epam.reportportal.listeners.ListenerParameters;
import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.ReportPortal;
import com.epam.reportportal.testng.TestNGService;
import com.epam.reportportal.utils.properties.PropertiesLoader;
import com.epam.ta.reportportal.ws.model.attribute.ItemAttributesRQ;
import com.epam.ta.reportportal.ws.model.launch.StartLaunchRQ;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.test.utils.EncryptDecrypt;
import com.test.utils.SystemProperties;


public class ParamOverrideTestNgService extends TestNGService {

	public static String REPORT_PORTAL_ENDPOINT = "rp.endpoint";
	public static String REPORT_PORTAL_UUID = "rp.uuid";
	public static String REPORT_PORTAL_LAUNCH = "rp.launch";
	public static String REPORT_PORTAL_PROJECT = "rp.project";
	public static String REPORT_PORTAL_ENABLE = "rp.enable";
	public static String REPORT_PORTAL_DESCRIPTION = "rp.description";
	public static String REPORT_PORTAL_ATTRIBUTES = "rp.attributes";
	public static String REPORT_PORTAL_PROXY_URL = "rp.http.proxy";
	
	public static String REPORT_PORTAL_LAUNCHER_UUID = "rp.launcherUUID";

	
	
	public ParamOverrideTestNgService() {
		super(getLaunchOverriddenProperties());
	}

	private static Supplier<Launch> getLaunchOverriddenProperties() {
		ListenerParameters parameters = new ListenerParameters(PropertiesLoader.load());
		String rpUUIDDecryptedText = null;

		
		String rpuuid;
		if (SystemProperties.isValueSet(REPORT_PORTAL_ENABLE)) {
			parameters.setEnable(SystemProperties.getBooleanValue(REPORT_PORTAL_ENABLE));
			parameters.setBaseUrl(SystemProperties.getStringValue(REPORT_PORTAL_ENDPOINT));
			rpuuid = SystemProperties.getStringValue(REPORT_PORTAL_UUID);
			parameters.setLaunchName(SystemProperties.getStringValue(REPORT_PORTAL_LAUNCH));
			parameters.setProjectName(SystemProperties.getStringValue(REPORT_PORTAL_PROJECT));
			String description = SystemProperties.getStringValue(REPORT_PORTAL_DESCRIPTION);
			if (!Strings.isNullOrEmpty(description)) {
				parameters.setDescription(description);
			}
			String rpproxy = SystemProperties.getStringValue(REPORT_PORTAL_PROXY_URL);
			if (!Strings.isNullOrEmpty(rpproxy)) {
				parameters.setProxyUrl(rpproxy);
			}else
				parameters.setProxyUrl(null);

			// attributes format Key1:Value1;Key2:Value2;.....;KeyN:ValueN
			String attributes = SystemProperties.getStringValue(REPORT_PORTAL_ATTRIBUTES);
			if (!Strings.isNullOrEmpty(attributes)) {
				parameters.setAttributes(itemAttributes(attributes));
			}
			parameters = setAttributes(parameters);
		} else {
			rpuuid = parameters.getApiKey();
		}


		try {
			rpUUIDDecryptedText = EncryptDecrypt.decrypt(rpuuid, EncryptDecrypt.PASSWORD);
			SystemProperties.putValue(REPORT_PORTAL_UUID,rpUUIDDecryptedText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		parameters.setApiKey(rpUUIDDecryptedText);


		// disable report portal if server is down
		if(!isReportPortalServerOnline(parameters)) {
			parameters.setEnable(false);
			SystemProperties.putValue(REPORT_PORTAL_ENABLE, "false");
		}
		
		
		ReportPortal reportPortal = ReportPortal.builder().withParameters(parameters).build();
		StartLaunchRQ rq = buildStartLaunch(reportPortal.getParameters());
		
		return new Supplier<Launch>() {
			@Override
			public Launch get() {
				Launch launch = reportPortal.newLaunch(rq);
				try {
					Object proxyOrigin = FieldUtils.readField(launch, "instanceUuid", true);
					String uuid = proxyOrigin.toString();
					SystemProperties.putValue(REPORT_PORTAL_LAUNCHER_UUID,uuid);
				} catch (Exception e) {
					e.getMessage();
				}
				return launch;
			}
		};
	}
	
	/**
	 * convert attribute string list to Set<ItemAttributesRQ> for report portal
	 * @return
	 */
	private static ListenerParameters setAttributes(ListenerParameters parameters) {
		String attributeString = SystemProperties.getStringValue(REPORT_PORTAL_ATTRIBUTES);
		
		if(attributeString.isEmpty()) {
			return parameters;
		}
		
		String[] attributes = attributeString.split(";");			
		Set<ItemAttributesRQ> attributeSet = new HashSet<>();
		
		ItemAttributesRQ items = null;
		for(String attribute: attributes) {
			 String[] keyValue = attribute.split(":");
			 String key = keyValue[0];
			 if(keyValue.length == 2) {
				 String value = keyValue[1];
				 items = new ItemAttributesRQ(key,value);

			 }else
				 items = new ItemAttributesRQ(key);
			 attributeSet.add(items);
		}
		parameters.setAttributes(attributeSet);
		return parameters;
	}

	private static StartLaunchRQ buildStartLaunch(ListenerParameters parameters) {
		StartLaunchRQ rq = new StartLaunchRQ();
		rq.setName(parameters.getLaunchName());
		rq.setStartTime(Calendar.getInstance().getTime());
		rq.setAttributes(parameters.getAttributes());
		rq.setMode(parameters.getLaunchRunningMode());
		if (!Strings.isNullOrEmpty(parameters.getDescription())) {
			rq.setDescription(parameters.getDescription());
		}
		return rq;
	}

	private static Map<String, String> arrayToMap(String arrayOfString) {
		return Arrays.stream(arrayOfString.split(";")).map(str -> str.split(":"))
				.collect(Collectors.toMap(str -> str[0], str -> str[1]));
	}

	private static Set<ItemAttributesRQ> itemAttributes(String attributes) {
		Set<ItemAttributesRQ> attrset = new HashSet<>();
		if(!attributes.contains(":"))
			return attrset;
		Map<String, String> map = arrayToMap(attributes);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			ItemAttributesRQ attribute = new ItemAttributesRQ(entry.getKey(), entry.getValue());
			attrset.add(attribute);
		}
		return attrset;
	}
	
	/**
	 * checks if report portal server is online
	 * @param parameter
	 * @return
	 */
	private static boolean isReportPortalServerOnline(ListenerParameters parameter){
	
		    if(!parameter.getEnable())
		    	return false;

			HttpURLConnection connection =  null;
		    try {
		    	if(parameter.getProxyUrl() != null) {
		    		 URL proxyUrl = new URL(parameter.getProxyUrl());
		    		 Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl.getHost(), proxyUrl.getPort()));
				     connection = (HttpURLConnection)new URL(parameter.getBaseUrl()).openConnection(proxy);
		    	}else
				     connection = (HttpURLConnection)new URL(parameter.getBaseUrl()).openConnection();
		        connection.setConnectTimeout(1000);
		        connection.connect();
		        return true;
		    } catch (Exception e) {
		        return false;
		    }
		}
		
}
