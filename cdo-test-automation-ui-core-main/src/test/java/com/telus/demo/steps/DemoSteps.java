package com.telus.demo.steps;

import org.openqa.selenium.WebElement;

import com.telus.demo.pages.DemoPage;
import com.test.ui.actions.BaseSteps;

public class DemoSteps extends BaseSteps {

    public static WebElement getDelayedWebElement() {
        // Reporting.logReporter(Status.INFO, "[DESKTOP APP] STEP === CALCULATOR --> clickButtonEquals ===");
        DemoPage app = new DemoPage();
        return app.delayedElement;
    }

    public static WebElement getExistingWebElement() {
        // Reporting.logReporter(Status.INFO, "[DESKTOP APP] STEP === CALCULATOR --> clickButtonEquals ===");
        DemoPage app = new DemoPage();
        return app.mainInputBar;
    }

    public static WebElement getNotExistingWebElement() {
        // Reporting.logReporter(Status.INFO, "[DESKTOP APP] STEP === CALCULATOR --> clickButtonEquals ===");
        DemoPage app = new DemoPage();
        return app.notExistingElement;
    }
}
