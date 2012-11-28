package org.tok.cust.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class TalkValidator implements Validator{

	protected final Log logger;

	public TalkValidator() {
		this.logger = LogFactory.getLog(super.getClass());
	}
	
	public boolean supports(Class paramClass) {	return false; }

	public void validate(Object paramObject, Errors paramErrors) {}
}
