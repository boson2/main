package org.tok.cust.tool.dao;

import java.util.Collection;

import org.tok.view.components.dao.ConnectionContext;


public interface ToolManagerDAO
{
	public Collection getZipCodes(ConnectionContext connCtx, String dong, String langKnd);

	public Collection getUserList(ConnectionContext connCtx, String searchName, int searchType, String userId, int groupId);

	public int getGroupId(ConnectionContext connCtx, String userId);

}
