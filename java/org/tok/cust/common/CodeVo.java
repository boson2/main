package org.tok.cust.common;

/**
 * enview 코드 테이블 객체.
 * 테이블명 : CODEBASE
 * 
 * @author snoopy
 * @version 1.0.0
 * @since 1.0-SNAPSHOT
 */
public class CodeVo extends BaseVo {

	/**
	 * Private field members.
	 */
	private String systemCode;
    private String codeId;
    private String code;
    private String codeTag1;
    private String codeTag2;
    private String codeName;
    private String codeName2;
    private String remark;

    /**
     * Getters. 
     */
    public String getSystemCode() { return systemCode; }
    public String getCodeId    () { return codeId;     }
    public String getCode      () { return code;       }
    public String getCodeTag1  () { return codeTag1;   }
    public String getCodeTag2  () { return codeTag2;   }
    public String getCodeName  () { return codeName;   }
    public String getCodeName2 () { return codeName2;  }
    public String getRemark    () { return remark;     }

    /**
     * Setters. 
     */
    public void setSystemCode(String value) { this.systemCode = value; }
    public void setCodeId    (String value) { this.codeId     = value; }
    public void setCode      (String value) { this.code       = value; }
    public void setCodeTag1  (String value) { this.codeTag1   = value; }
    public void setCodeTag2  (String value) { this.codeTag2   = value; }
    public void setCodeName  (String value) { this.codeName   = value; }
    public void setCodeName2 (String value) { this.codeName2  = value; }
    public void setRemark    (String value) { this.remark     = value; }
}
