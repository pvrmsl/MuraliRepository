package org.mycompany.TestPackage;

import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.HttpCommandProcessor;

public class GitTest {
	DefaultSelenium selenium;
	SeleniumServer server;
	HttpCommandProcessor proc;
	
	
		@BeforeSuite(alwaysRun = true)
		public void setupBeforeSuite(ITestContext context) {
		  String seleniumHost = context.getCurrentXmlTest().getParameter("selenium.host");
		  String seleniumPort = context.getCurrentXmlTest().getParameter("selenium.port");
		  String seleniumBrowser = context.getCurrentXmlTest().getParameter("selenium.browser");
		  String seleniumUrl = context.getCurrentXmlTest().getParameter("selenium.url");
		  
		  RemoteControlConfiguration rcc = new RemoteControlConfiguration();
		  rcc.setSingleWindow(true);
		  rcc.setPort(Integer.parseInt(seleniumPort));
		  
		  try {
		    server = new SeleniumServer(false, rcc);
		    server.boot();
		  } catch (Exception e) {
		    throw new IllegalStateException("Can't start selenium server", e);
		  }
		  
		  proc = new HttpCommandProcessor(seleniumHost, Integer.parseInt(seleniumPort),
		      seleniumBrowser, seleniumUrl);
		  selenium = new DefaultSelenium(proc);
		  selenium.start();
		}
		  
		@AfterSuite(alwaysRun = true)
		public void setupAfterSuite() {
		  selenium.stop();
		  server.stop();
		}
		
		
		@Test(description="Launches the WordPress site")
		public void launchSite(){
		  selenium.open("");
		  selenium.waitForPageToLoad("30000");
		  Assert.assertTrue(selenium.getTitle().contains("Demo | Just another WordPress site"), "Demo | Just another WordPress site");
		}
		  
		@Test(description="Navigates to the admin page")
		  public void openAdminPage() {
		  selenium.open("wp-admin");
		  selenium.waitForPageToLoad("30000");
		  //selenium.getTitle(), "Demo › Log In");
		}
		  
		@Test(description="Enters valid login data")
		  public void loginAsAdmin() {
		  selenium.type("user_login", "admin");
		  selenium.type("user_pass", "demo123");
		  selenium.click("wp-submit");
		  selenium.waitForPageToLoad("30000");
		  selenium.isTextPresent("Howdy, admin");
		}
		  
		@Test(description="Navigates to the New Post screen")
		public void navigateNewPost() {
		  selenium.click("//a[contains(text(),'Posts')]/following::a[contains(text(),'Add New')][1]");
		  selenium.waitForPageToLoad("30000");
		  selenium.isTextPresent("Add New Post");
		}
		  
		@Test(description="Writes the new post")
		public void writeBlogPost() {
		  selenium.type("title", "New Blog Post");
		  selenium.click("edButtonHTML");
		  selenium.type("content", "This is a new post");
		  //TODO:Assert
		}
		  
		@Test(description="Publishes the post")
		public void publishBlogPost() {
		  selenium.click("submitdiv");
		  selenium.click("publish");
		  selenium.waitForPageToLoad("30000");
		  selenium.isTextPresent("Post published.");
		}
		  
		@Test(description="Verifies the post")
		public void verifyBlogPost() {
		  selenium.click("//a[contains(text(),'Posts') and contains(@class,'wp-first-item')]");
		  selenium.waitForPageToLoad("30000");
		  selenium.isElementPresent("//a[text()='New Blog Post']");
		}
		  
		@Test(description="Logs out")
		public void logout() {
		  selenium.click("//a[text()='Log Out']");
		  //TODO:Assert
		}
		
		
		
	}
	

	


