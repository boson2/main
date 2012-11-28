package org.tok.cust.user.dao;

import org.tok.cust.user.dao.spi.SiteUserManagerForCubrid;
import org.tok.cust.user.dao.spi.SiteUserManagerForMysql;

public class SiteUserManagerDAOFactory 
{
	public SiteUserManagerDAOFactory() 
	{
		
	}
	
	public static SiteUserManagerDAO getDAO(String dbType) {
		
		if( dbType.equals("mysql") ) {
			return new SiteUserManagerForMysql();
		}
		else if( dbType.equals("cubrid") ) {
			return new SiteUserManagerForCubrid();
		}
		return null;
	}
}
