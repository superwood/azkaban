package azkaban.test.utils;

import azkaban.utils.EmailMessage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by superwood
 * Product: IntelliJ IDEA
 * Date: 16/4/19
 * Time: 15:19
 */
public class EmailMessageTest2 {

	@org.junit.Test
	public void testSendEmail() throws Exception {
		String host = "smtp.exmail.qq.com";
		String mailSender = "monitor.dataPlatform@mobvista.com";
		String mailUser = "monitor.dataPlatform@mobvista.com";
		String mailPasswd = "dataPlatform1";
		List<String> mailToList = new ArrayList<String>();//"walt@mobvista.com";
		mailToList.add("walt@mobvista.com");
		int port = 465;

		EmailMessage emailMessage = new EmailMessage(host, -1, mailUser, mailPasswd);
		emailMessage.setFromAddress(mailSender);
		EmailMessage.setConnectionTimeout(10000);
		emailMessage.addAllToAddress(mailToList);
		emailMessage.setMimeType("text/html");
		emailMessage.setSubject("test for mobvista");
		emailMessage.println("a email test for mobvista");
		emailMessage.sendEmail();

	}
}