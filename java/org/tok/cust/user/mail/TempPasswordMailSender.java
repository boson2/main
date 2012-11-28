package org.tok.cust.user.mail;


import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.tok.cust.mail.AbstractMessageSender;
import org.tok.cust.user.dao.UserException;


public class TempPasswordMailSender extends AbstractMessageSender {
	protected final Log logger = LogFactory.getLog(getClass());
	
	public TempPasswordMailSender() {
	}
	
	public void sendMessage(String userId, String regDatimF, String tempPassword, String to) throws UserException{
		try {

			MimeMessage msg = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg);
			helper.setTo(to);
			helper.setFrom(from);
			helper.setSubject("[enview]임시 비밀번호가 발송되었습니다.");

			StringBuffer sb = new StringBuffer();
			sb.append("<html><head></head><body>");
			sb.append("<h3>'");
			sb.append(userId.subSequence(0, 4) + "****");
			sb.append("'님(");
			sb.append(regDatimF);
			sb.append(" 가입)의");
			sb.append(" 임시 비밀번호는 [ ");
			sb.append("<font color='red'>");
			sb.append(tempPassword);
			sb.append("</font>");
			sb.append(" ] 입니다.");
			sb.append("</h3><br/>");
			sb.append("<h4>보안을 위해서 아이디는 8자리로 통일되어 앞의 4자리만 보여집니다.<br>");
			sb.append("</h4><br/>");
			sb.append("<a href='http://localhost:8080/enview/>");
			sb.append("enview 바로가기");
			sb.append("</a>");
			sb.append("</body></html>");
	
			helper.setText(sb.toString());
			if( logger.isErrorEnabled() ) {
				logger.debug("Try to send E-mail to " + to);
				logger.debug("MimeMessage=" + msg.getContent());
			}
			sender.send(msg);
			if( logger.isErrorEnabled() ) {
				logger.debug("Complete to send e-mail to " + to + ", From " + from);
			}
		}catch (Exception ex) {
			if( logger.isErrorEnabled() ) {
				logger.error("Fail to send E-mail!!");
				logger.error(ex);
			}
			throw new UserException("pt.ev.mail.Error.FailToSendEmail");
		}
	}

}
