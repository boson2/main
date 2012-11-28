package org.tok.cust.tool.dao;

import org.tok.cust.tool.dao.spi.ToolManagerForCubrid;
import org.tok.cust.tool.dao.spi.ToolManagerForMysql;

public class ToolManagerDAOFactory 
{
	public ToolManagerDAOFactory() 
	{
		
	}
	
	public static ToolManagerDAO getDAO(String dbType) {
		
		if( dbType.equals("mysql") ) {
			return new ToolManagerForMysql();
		}
		else if( dbType.equals("cubrid") ) {
			return new ToolManagerForCubrid();
		}
		return null;
	}
}
