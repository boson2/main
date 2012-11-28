package org.tok.cust.user.validator;

import org.springframework.validation.Errors;
import org.tok.cust.user.dao.UserException;
import org.tok.cust.user.model.UserVo;
import org.tok.cust.validator.TalkValidator;

public class TalkUserValidator extends TalkValidator {
	
	public void validateRegistryNumber(Object command) throws UserException{
		validateRegistryNumber(command, null);
	}
	
	public void validateRegistryNumber(Object command, Errors errors) throws UserException{
		UserVo user = (UserVo)command;
		validateRegistryNumber(user.getUser_jumin1(), user.getUser_jumin2(), errors);
	}
	
	public void validateRegistryNumber(String user_jumin1, String user_jumin2, Errors errors) throws UserException{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s validateRegistryNumber' method...");
			logger.debug("user_jumin1= " + user_jumin1 + ", user_jumin2= " + user_jumin2);
		 }
		if(!user_jumin1.equals("") && !user_jumin2.equals("")){
			char reg_no[] = (user_jumin1 + user_jumin2).toCharArray();
			int sum = 0;
			int lastNumber = -1;
			for(int i = 0 ; i <= 7 ; i++){
				sum += (reg_no[i]-48) * (i+2);
			}
			for(int i = 0 ; i <= 3 ; i++){
				sum += (reg_no[i+8]-48) * (i+2);
			}
			
			lastNumber = (11 - (sum%11))%10;	
			if(lastNumber == reg_no[12]-48){
				if( logger.isErrorEnabled() ) {
					logger.debug("RegNo(" + user_jumin1 + "-" + user_jumin2 + ") is validated.");
				}
			}
			else{
				if( logger.isErrorEnabled() ) {
					logger.debug("RegNo(" + user_jumin1 + "-" + user_jumin2 + ") is invalid Value.");
				}
				throw new UserException("pt.ev.user.label.InvalidRegNo");
			}
		}
		else{
			if( logger.isErrorEnabled() ) {
				logger.debug("RegNo(" + user_jumin1 + "-" + user_jumin2 + ") is invalid Value.");
			}
			throw new UserException("pt.ev.user.label.InvalidRegNo");
		}
	}
	
	public void validateUserId(Object command, Errors errors) throws UserException{
		UserVo user = (UserVo)command;
		validateUserId(user.getUser_id(), errors);
	}
	public void validateUserId(String userId, Errors errors) throws UserException{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s validateUserId' method...");
		 }
		if(userId == null || userId.equals("") || 5 > userId.length() || userId.length() > 12){
			throw new UserException("pt.ev.user.label.InvalidUserId");
		}
	}
	
	public void validateUserHp(Object command) throws UserException{
		validateUserHp(command, null);
	}
	
	public void validateUserHp(Object command, Errors errors) throws UserException{
		UserVo user = (UserVo)command;
		validateUserHp(user.getUser_hp2(), user.getUser_hp3(), errors);
	}
	
	public void validateUserHp(String user_hp2, String user_hp3, Errors errors) throws UserException{
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s validateUserHp' method...");
			logger.debug("user_hp2=" + user_hp2 + ", user_hp3=" + user_hp3 );
		 }
		if( user_hp2.length() > 4 || user_hp3.length() < 3  ||
				user_hp3.length() != 4){
			throw new UserException("pt.ev.user.label.InvalidUserHp");
		}
	}
	
	
	public void validateUserEmail(Object command) throws UserException{
		validateUserEmail(command, null);
	}
	
	public void validateUserEmail(Object command, Errors errors) throws UserException{
		UserVo user = (UserVo)command;
		validateUserEmail(user.getUser_email2(), errors);
	}
	
	public void validateUserEmail(String user_email2, Errors errors) throws UserException{
		String[]  suffixList = {".com", ".net", ".co.kr", "or.kr", "ac.kr", ".kr"};
		boolean isValid = false;
		if (logger.isDebugEnabled()) {
			logger.debug("entering '" + this.getClass().getName() + "'s validateUserEmail' method...");
		 }
		if(user_email2.length() <= 6){
			throw new UserException("pt.ev.user.label.InvalidUserEmail");
		}
		else {
			for(int i = 0 ; i < suffixList.length ; i++){
				if(user_email2.endsWith(suffixList[i].toString())){
					isValid = true;
					break;
				}
			}
			if(!isValid){
				throw new UserException("pt.ev.user.label.InvalidUserEmail");
			}
		}
	}
	
	public boolean supports(Class clazz) {
		return UserVo.class.isAssignableFrom(clazz);
	}
	public void validate(Object command, Errors errors) {
	}
}
