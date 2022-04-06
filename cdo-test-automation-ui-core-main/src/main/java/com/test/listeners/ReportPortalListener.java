package com.test.listeners;

import com.epam.reportportal.testng.BaseTestNGListener;

public class ReportPortalListener extends BaseTestNGListener {
	public ReportPortalListener() {
		super(new ParamOverrideTestNgService());
	}
}



