package com.telus.demo.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.test.ui.actions.WebDriverSession;

/**
 ****************************************************************************
 * > DESCRIPTION: Support for DOWNLOAD Page Object Mapping > AUTHOR: x223006 > USER STORY: DEMO
 ****************************************************************************
 */
public class DemoPage extends WebDriverSession {

    @FindBy(css = "input[name='q']") public WebElement mainInputBar;
    @FindBy(css = "input[name='NOT EXISTING']") public WebElement notExistingElement;
    @FindBy(css = "input[name='NOT EXISTING']") public WebElement delayedElement;

    public DemoPage() {

        PageFactory.initElements(getWebDriverSession(), this);

    }
}
