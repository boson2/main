package org.tok.cust.common;

import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

/**
 * @author snoopy
 * @version 1.0.0
 * @since 1.0-SNAPSHOT
 */
public class BaseVo {
	public String getToken(String str, String sep, int order) {
		
		if(str == null || sep == null) return "";
		
		StringTokenizer stringTokenizer = new StringTokenizer(str, sep);
		if(stringTokenizer.countTokens() >= order) {
			String token = null;
			for (int i=0; i < order; i++) {
				token = stringTokenizer.nextToken();
			}
			return token;
		}
		
		return "";
	}
	public String getDateF( java.util.Date date, String format ) {
		if( date == null || format == null ) return "";

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public String getTrimStr(String str, int length, char type, String tail) {
		
		byte[] bytes = str.getBytes(); 
		int len = bytes.length; 
		int counter = 0; 
		if( length >= len ) { 
			StringBuffer sb = new StringBuffer(); 
			sb.append( str ); 
			for( int i=0; i<length-len; i++ ) { 
				sb.append(' '); 
			} 
			return sb.toString(); 
		}
		
		for( int i=length-1; i >= 0; i-- ) { 
			if(( (int)bytes[i] & 0x80 ) != 0 ) counter++; 
		} 
		String f_str = null; 
		if( type == '+' ) { 
			f_str = new String( bytes, 0, length + (counter % 2)); 
		} else if( type == '-' ) { 
			f_str = new String( bytes, 0, length - (counter % 2)); 
		} else { 
			f_str = new String( bytes, 0, length - (counter % 2)); 
		} 
		return f_str + tail; 
	}
}
