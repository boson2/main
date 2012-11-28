package org.tok.cust.user.model;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.tok.cust.common.BaseVo;



public class UserVo extends BaseVo{

	private int	 credentialKey;
	public 	int	 getCredentialKey(){return credentialKey;}
	public	void setCredentialKey(int credentialKey){this.credentialKey = credentialKey;}
	
    private String columnValue;
	public  String getColumnValue() {return columnValue;}
	public  void   setColumnValue(String columnValue) {this.columnValue = columnValue;}
	
	private String password;
	public  String getPassword() {return password;}
	public  void   setPassword(String password) {this.password = password;}
	
	private String passwordNew;
	public  String getPasswordNew() {return passwordNew;}
	public  void   setPasswordNew(String passwordNew) {this.passwordNew = passwordNew;}
	
	private String passwordConfirm;
	public  String getPasswordConfirm() {return passwordConfirm;}
	public  void   setPasswordConfirm(String passwordConfirm) {this.passwordConfirm = passwordConfirm;}
	/**
	 * for getting the value of record sequence
	 */
	private String rnum;					
	public  String getRnum () {return this.rnum;}
	public  void   setRnum (String rnum) {this.rnum = rnum;}
	/**
	 * for getting the value of principal_id from the table 'SecurityPrincipal'
	 */
	private int	 principalKey;					
	public  int  getPrincipalKey () {return this.principalKey;}
	public  void setPrincipalKey(int principalKey) {this.principalKey = principalKey;}
	
	
	private String principalId;					
	public  String getPrincipalId () {return this.principalId;}
	public  void   setPrincipalId (String principalId) {this.principalId = principalId;}
	
	private String destination;					
	public  String getDestination () {return this.destination;}
	public  void   setDestination (String destination) {this.destination = destination;}
	
	private String current;					
	public  String getCurrent () {return this.current;}
	public  void   setCurrent (String current) {this.current = current;}
	
	private String langKnd;					
	public  String getLangKnd () {return this.langKnd;}
	public  void   setLangKnd (String langKnd) {this.langKnd = langKnd;}
	
	private List langKndList;					
	public  List getLangKndList () {return this.langKndList;}
	public  void   setLangKndList (List langKndList) {this.langKndList = langKndList;}
	
	
	/*
	 * for sports product category in KISS
	 */
	private String	cate1;
	private String	cate2;
	private String	cate3;
	public void setCate1( String value ) { this.cate1 = value; }
	public void setCate2( String value ) { this.cate2 = value; }
	public void setCate3( String value ) { this.cate3 = value; }
	public String	getCate1() { return cate1; }
	public String	getCate2() { return cate2; }
	public String	getCate3() { return cate3; }

	private String	userId;					
	private String	regNo;					
	private String	comNo;					
	private String	orgCd;					
	private String	empNo;					
	private String	nmKor;					
	private String	stateFlag;				
	private String	orgFlag;				
	private String	levelCd;				
	private String	kindCd;					
	private String	typeCd;					
	private int	  	themeId;			
	private String	mailFlag;				
	private String	blogFlag;				
	private String	memoFlag;				
	private String	diskFlag;				
	private String	rcmdUserId;			
	private long	mileTot;
	private String	nmChr;					
	private String	nmEng;					
	private String	nmNic;					
	private String	sexFlag;				
	private String	homeTel;				
	private String	offcTel;				
	private String	mobileTel;				
	private String	faxNo;					
	private Timestamp birthYmd;				
	private String	luorsunFlag;			
	private Timestamp marryYmd;				
	private String	emailAddr;				
	private String	openFlag;			
	private String	homeZip;				
	private String	homeAddr1;				
	private String	homeAddr2;				
	private String	offcZip;				
	private String	offcAddr1;				
	private String	offcAddr2;				
	private String	userIcon;				
	private long	userInfo01;				
	private long	userInfo02;				
	private long	userInfo03;				
	private String	userInfo04;				
	private String	userInfo05;				
	private String	userInfo06;				
	private String	userInfo07;				
	private String	userInfo08;				
	private String	userInfo09;				
	private String	userInfo10;				
	private Timestamp lastLogon;				
	private String	lastIp;					
	private Timestamp regDatim;				
	private String	updUserId;				
	private Timestamp updDatim;				
	private String	page1stFlag;			
	private String	page1stUrl;				
	private String	picPath;				
	private String	intro;
	
	private boolean isAgree;
	private String	user_id;
	private String	user_name;
	private String	user_jumin1;
	private String	user_jumin2;
	private String	user_hp;
	private String	user_hp1;
	private String	user_hp2;
	private String	user_hp3;
	private String	user_email;
	private String	user_email1;
	private String	user_email2;
	
	public String	getUserId     () { return userId;		}
	public String	getRegNo      () { return regNo;		}
	public String	getRegNo1     () { return StringUtils.substring(regNo,0,6); }
	public String	getRegNo2     () { return StringUtils.substring(regNo,6); }
	public String	getComNo      () { return comNo;		}
	public String	getOrgCd      () { return orgCd;		}
	public String	getEmpNo      () { return empNo;		}
	public String	getNmKor      () { return nmKor;		}
	public String	getStateFlag  () { return stateFlag;	}
	public String	getOrgFlag    () { return orgFlag;		}
	public String	getLevelCd    () { return levelCd;		}
	public String	getKindCd     () { return kindCd;		}
	public String	getTypeCd     () { return typeCd;		}
	public int	  	getThemeId    () { return themeId;		}
	public String	getMailFlag   () { return mailFlag;		}
	public String	getBlogFlag   () { return blogFlag;		}
	public String	getMemoFlag   () { return memoFlag;		}
	public String	getDiskFlag   () { return diskFlag;		}
	public String	getRcmdUserId () { return rcmdUserId;	}
	public long		getMileTot    () { return mileTot;		}
	public String	getNmChr      () { return nmChr;		}
	public String	getNmEng      () { return nmEng;		}
	public String	getNmNic      () { return nmNic;		}
	public String	getSexFlag    () { return sexFlag;		}
	public String	getHomeTel    () { return homeTel;		}
	public String	getHomeTel1   () { return getToken(homeTel, "-", 1); }
	public String	getHomeTel2   () { return getToken(homeTel, "-", 2); }
	public String	getHomeTel3   () { return getToken(homeTel, "-", 3); }
	public String	getOffcTel    () { return offcTel;		}
	public String	getOffcTel1   () { return getToken(offcTel, "-", 1); }
	public String	getOffcTel2   () { return getToken(offcTel, "-", 2); }
	public String	getOffcTel3   () { return getToken(offcTel, "-", 3); }
	public String	getMobileTel  () { return mobileTel;	}
	public String	getMobileTel1 () { return getToken(mobileTel, "-", 1); }
	public String	getMobileTel2 () { return getToken(mobileTel, "-", 2); }
	public String	getMobileTel3 () { return getToken(mobileTel, "-", 3); }
	public String	getFaxNo      () { return faxNo;		}
	public String	getFaxNo1     () { return getToken(faxNo, "-", 1); }
	public String	getFaxNo2     () { return getToken(faxNo, "-", 2); }
	public String	getFaxNo3     () { return getToken(faxNo, "-", 3); }
	public Timestamp getBirthYmd  () { return birthYmd;		}
	public String	getBirthYear  () { return getDateF(getBirthYmd(), "yyyy"); }
	public String	getBirthMonth () { return getDateF(getBirthYmd(), "MM");   }
	public String	getBirthDay   () { return getDateF(getBirthYmd(), "dd");   }
	public String	getBirthYmdF  () { return getDateF(birthYmd, "yyyy.MM.dd"); }
	public String	getLuorsunFlag() { return luorsunFlag;	}
	public Timestamp getMarryYmd  () { return marryYmd;		}
	public String	getMarryYear  () { return getDateF(getMarryYmd(), "yyyy"); }
	public String	getMarryMonth () { return getDateF(getMarryYmd(), "MM");   }
	public String	getMarryDay   () { return getDateF(getMarryYmd(), "dd");   }
	public String	getMarryYmdF  () { return getDateF(marryYmd, "yyyy.MM.dd"); }
	public String	getEmailAddr  () { return emailAddr;	}
	public String	getEmailAddr1 () { return getToken(emailAddr, "@", 1); }
	public String	getEmailAddr2 () { return getToken(emailAddr, "@", 2); }
	public String	getOpenFlag   () { return openFlag;		}
	public String	getHomeZip    () { return homeZip;		}
	public String	getHomeZip1   () { return StringUtils.substring(homeZip,0,3); }
	public String	getHomeZip2   () { return StringUtils.substring(homeZip,3);  }
	public String	getHomeAddr1  () { return homeAddr1;	}
	public String	getHomeAddr2  () { return homeAddr2;	}
	public String	getOffcZip    () { return offcZip;		}
	public String	getOffcZip1   () { return StringUtils.substring(offcZip,0,3); }
	public String	getOffcZip2   () { return StringUtils.substring(offcZip,3);	  }
	public String	getOffcAddr1  () { return offcAddr1;	}
	public String	getOffcAddr2  () { return offcAddr2;	}
	public String	getUserIcon   () { return userIcon;		}
	public long		getUserInfo01 () { return userInfo01;	}
	public long		getUserInfo02 () { return userInfo02;	}
	public long		getUserInfo03 () { return userInfo03;	}
	public String	getUserInfo04 () { return userInfo04;	}
	public String	getUserInfo05 () { return userInfo05;	}
	public String	getUserInfo06 () { return userInfo06;	}
	public String	getUserInfo07 () { return userInfo07;	}
	public String	getUserInfo08 () { return userInfo08;	}
	public String	getUserInfo09 () { return userInfo09;	}
	public String	getUserInfo10 () { return userInfo10;	}
	public Timestamp getLastLogon () { return lastLogon;	}
	public String	getLastIp     () { return lastIp;		}
	public Timestamp getRegDatim  () { return regDatim;		}
	public String	getRegDatimF  () { return getDateF(regDatim, "yyyy.MM.dd"); }
	public String	getUpdUserId  () { return updUserId;	}
	public Timestamp getUpdDatim  () { return updDatim;		}
	public String	getUpdDatimF  () { return getDateF(updDatim, "yyyy.MM.dd"); }
	public String	getPage1stFlag() { return page1stFlag;	}
	public String	getPage1stUrl () { return page1stUrl;	}
	public String	getPicPath    () { return picPath;		}
	public String	getIntro      () { return intro;		}

	public void setUserId     ( String userId	  ) { this.userId	   = userId;	  }
	public void setRegNo      ( String regNo	  ) { this.regNo	   = regNo;		  }
	public void setComNo      ( String comNo	  ) { this.comNo	   = comNo;		  }
	public void setOrgCd      ( String orgCd	  ) { this.orgCd	   = orgCd;		  }
	public void setEmpNo      ( String empNo	  ) { this.empNo	   = empNo;		  }
	public void setNmKor      ( String nmKor	  ) { this.nmKor	   = nmKor;		  }
	public void setStateFlag  ( String stateFlag  ) { this.stateFlag   = stateFlag;	  }
	public void setOrgFlag    ( String orgFlag	  ) { this.orgFlag	   = orgFlag;	  }
	public void setLevelCd    ( String levelCd	  ) { this.levelCd	   = levelCd;	  }
	public void setKindCd     ( String kindCd	  ) { this.kindCd	   = kindCd;	  }
	public void setTypeCd     ( String typeCd	  ) { this.typeCd	   = typeCd;	  }
	public void setThemeId    ( int	   themeId	  ) { this.themeId	   = themeId;	  }
	public void setMailFlag   ( String mailFlag	  ) { this.mailFlag	   = mailFlag;	  }
	public void setBlogFlag   ( String blogFlag	  ) { this.blogFlag	   = blogFlag;	  }
	public void setMemoFlag   ( String memoFlag	  ) { this.memoFlag	   = memoFlag;	  }
	public void setDiskFlag   ( String diskFlag	  ) { this.diskFlag	   = diskFlag;	  }
	public void setRcmdUserId ( String rcmdUserId ) { this.rcmdUserId  = rcmdUserId;  }
	public void setMileTot    ( long   mileTot	  ) { this.mileTot	   = mileTot;	  }
	public void setNmChr      ( String nmChr	  ) { this.nmChr	   = nmChr;		  }
	public void setNmEng      ( String nmEng	  ) { this.nmEng	   = nmEng;		  }
	public void setNmNic      ( String nmNic	  ) { this.nmNic	   = nmNic;		  }
	public void setSexFlag    ( String sexFlag	  ) { this.sexFlag	   = sexFlag;	  }
	public void setHomeTel    ( String homeTel	  ) { this.homeTel	   = homeTel;	  }
	public void setOffcTel    ( String offcTel	  ) { this.offcTel	   = offcTel;	  }
	public void setMobileTel  ( String mobileTel  ) { this.mobileTel   = mobileTel;	  }
	public void setFaxNo      ( String faxNo	  ) { this.faxNo	   = faxNo;		  }
	public void setBirthYmd   ( Timestamp birthYmd) { this.birthYmd	   = birthYmd;	  }
	public void setLuorsunFlag( String luorsunFlag) { this.luorsunFlag = luorsunFlag; }
	public void setMarryYmd   ( Timestamp marryYmd) { this.marryYmd	   = marryYmd;	  }
	public void setEmailAddr  ( String emailAddr  ) { this.emailAddr   = emailAddr;	  }
	public void setOpenFlag   ( String openFlag	  ) { this.openFlag	   = openFlag;	  }
	public void setHomeZip    ( String homeZip	  ) { this.homeZip	   = homeZip;	  }
	public void setHomeAddr1  ( String homeAddr1  ) { this.homeAddr1   = homeAddr1;	  }
	public void setHomeAddr2  ( String homeAddr2  ) { this.homeAddr2   = homeAddr2;	  }
	public void setOffcZip    ( String offcZip	  ) { this.offcZip	   = offcZip;	  }
	public void setOffcAddr1  ( String offcAddr1  ) { this.offcAddr1   = offcAddr1;	  }
	public void setOffcAddr2  ( String offcAddr2  ) { this.offcAddr2   = offcAddr2;	  }
	public void setUserIcon   ( String userIcon	  ) { this.userIcon	   = userIcon;	  }
	public void setUserInfo01 ( long   userInfo01 ) { this.userInfo01  = userInfo01;  }
	public void setUserInfo02 ( long   userInfo02 ) { this.userInfo02  = userInfo02;  }
	public void setUserInfo03 ( long   userInfo03 ) { this.userInfo03  = userInfo03;  }
	public void setUserInfo04 ( String userInfo04 ) { this.userInfo04  = userInfo04;  }
	public void setUserInfo05 ( String userInfo05 ) { this.userInfo05  = userInfo05;  }
	public void setUserInfo06 ( String userInfo06 ) { this.userInfo06  = userInfo06;  }
	public void setUserInfo07 ( String userInfo07 ) { this.userInfo07  = userInfo07;  }
	public void setUserInfo08 ( String userInfo08 ) { this.userInfo08  = userInfo08;  }
	public void setUserInfo09 ( String userInfo09 ) { this.userInfo09  = userInfo09;  }
	public void setUserInfo10 ( String userInfo10 ) { this.userInfo10  = userInfo10;  }
	public void setLastLogon  ( Timestamp lastLogon){ this.lastLogon   = lastLogon;	  }
	public void setLastIp     ( String lastIp	  ) { this.lastIp	   = lastIp;	  }
	public void setRegDatim   ( Timestamp regDatim) { this.regDatim	   = regDatim;	  }
	public void setUpdUserId  ( String updUserId  ) { this.updUserId   = updUserId;	  }
	public void setUpdDatim   ( Timestamp updDatim) { this.updDatim	   = updDatim;	  }
	public void setPage1stFlag( String page1stFlag) { this.page1stFlag = page1stFlag; }
	public void setPage1stUrl ( String page1stUrl ) { this.page1stUrl  = page1stUrl;  }
	public void setPicPath    ( String picPath	  ) { this.picPath	   = picPath;	  }
	public void setIntro      ( String intro	  ) { this.intro	   = intro;		  }
	
	
	public boolean getIsAgree() {
		return isAgree;
	}
	public void setIsAgree(boolean isAgree) {
		this.isAgree = isAgree;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_email1() {
		return user_email1;
	}
	public void setUser_email1(String user_email1) {
		this.user_email1 = user_email1;
	}
	public String getUser_email2() {
		return user_email2;
	}
	public void setUser_email2(String user_email2) {
		this.user_email2 = user_email2;
	}
	public String getUser_hp() {
		return user_hp;
	}
	public void setUser_hp(String user_hp) {
		this.user_hp = user_hp;
	}
	public String getUser_hp1() {
		return user_hp1;
	}
	public void setUser_hp1(String user_hp1) {
		this.user_hp1 = user_hp1;
	}
	public String getUser_hp2() {
		return user_hp2;
	}
	public void setUser_hp2(String user_hp2) {
		this.user_hp2 = user_hp2;
	}
	public String getUser_hp3() {
		return user_hp3;
	}
	public void setUser_hp3(String user_hp3) {
		this.user_hp3 = user_hp3;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_jumin1() {
		return user_jumin1;
	}
	public void setUser_jumin1(String user_jumin1) {
		this.user_jumin1 = user_jumin1;
	}
	public String getUser_jumin2() {
		return user_jumin2;
	}
	public void setUser_jumin2(String user_jumin2) {
		this.user_jumin2 = user_jumin2;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
}
