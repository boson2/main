package org.tok.cust.user.dao.spi;

import java.security.Permission;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tok.cust.user.dao.AbstractSiteUserManagerDAO;
import org.tok.view.Talk;
import org.tok.view.components.dao.ConnectionContext;
import org.tok.view.components.dao.StandardDaoSupport;
import org.tok.view.decoration.dao.spi.ThemeMappingForMysql;
import org.tok.view.security.SecurityException;
import org.tok.view.util.TalkQuery;
import org.tok.view.util.TalkQueryFactory;
import org.tok.view.util.ResultSetList;


public class SiteUserManagerForMysql extends AbstractSiteUserManagerDAO
{
	private static final Log log = LogFactory.getLog( SiteUserManagerForMysql.class );
	
	public SiteUserManagerForMysql()
	{
	}
	
	
}
