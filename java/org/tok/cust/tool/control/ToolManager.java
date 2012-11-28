package org.tok.cust.tool.control;

import java.util.List;

public interface ToolManager {

	public List getZipCodes(String dong, String langKnd);

	public List getUserList(String searchName, int searchType, String userId);
	
}
