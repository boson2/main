package org.tok.cust.tool.control;

import java.util.ArrayList;
import java.util.List;

import org.tok.cust.tool.dao.ToolManagerDAO;
import org.tok.cust.tool.dao.ToolManagerDAOFactory;
import org.tok.view.Talk;
import org.tok.view.components.dao.ConnectionContext;
import org.tok.view.components.dao.ConnectionContextForRdbms;


public class ToolManagerImpl implements ToolManager
{
//    private static final Log log = LogFactory.getLog(ToolManagerImpl.class);
    
    private ToolManagerDAO toolManagerDAO = null;
    
    public ToolManagerImpl() {
    	String dbType = Talk.getConfiguration().getString("tok.db.type");
    	this.toolManagerDAO = ToolManagerDAOFactory.getDAO( dbType );
    }
    
	public List getZipCodes(String dong, String langKnd) {
		ConnectionContext connCtx = null;
		List zipCodes = new ArrayList();
		List tempList = new ArrayList();
    	try {
    		connCtx = new ConnectionContextForRdbms( false );
    		tempList.addAll(toolManagerDAO.getZipCodes(connCtx, dong, langKnd));
    		if(!tempList.isEmpty()){
    			zipCodes.add(tempList.get(tempList.size()-1));
        		for(int i = 0 ; i < tempList.size()-1 ; i++){
        			zipCodes.add(tempList.get(i));
        		}	
    		}
    		connCtx.commit();
    	}
    	catch (Exception e)
        {
            e.printStackTrace();
            if( connCtx != null ) connCtx.rollback();
        }
    	finally
        {
            try
            {
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    	return zipCodes;
	}
	
	public List getUserList(String searchName, int searchType, String userId) {
		ConnectionContext connCtx = null;
		List userList = new ArrayList();
    	try {
    		connCtx = new ConnectionContextForRdbms( false );
    		int group_id = toolManagerDAO.getGroupId(connCtx, userId);
    		userList.addAll(toolManagerDAO.getUserList(connCtx, searchName, searchType, userId, group_id));
    		connCtx.commit();
    	}catch (Exception e){
            e.printStackTrace();
            if( connCtx != null ) connCtx.rollback();
        }finally{
            try{
            	if( connCtx != null ) connCtx.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    	return userList;
	}
}
