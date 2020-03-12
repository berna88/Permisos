package com.consistent.cuervo.permisos.email;

import java.io.File;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailServiceUtil;

public class SendMail {
	
	public static boolean mail(String para, String de, String asunto, File file){
		try{
			InternetAddress fromAddress = new InternetAddress(de);
			InternetAddress toAddress = new InternetAddress(para);
			
			MailMessage mailMessage = new  MailMessage();
			mailMessage.setFrom(fromAddress);
			mailMessage.setTo(toAddress);
			mailMessage.setSubject(asunto);
			mailMessage.setHTMLFormat(true);
			mailMessage.setBody("Adjunto encontrara su solicitud.");
			mailMessage.addFileAttachment(file);
			MailServiceUtil.sendEmail(mailMessage);
			
		}catch (AddressException e) {
			e.getMessage();
			return false;
		}
		
		return true;
	}

}
