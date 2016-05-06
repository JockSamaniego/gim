/**
 * 
 */
package org.gob.gim.income.test;


import javax.naming.InitialContext;

import junit.framework.Assert;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.KnowledgeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author wilman
 *
 */

public class Payment {

	/**
	 * @throws java.lang.Exception
	 */
	private KnowledgeService knowledgeService; 
	
	@Before
	public void setUp() throws Exception {
		//Get the Initial Context for the JNDI lookup for a local EJB
		InitialContext ic = new InitialContext();
		//Retrieve the Home interface using JNDI lookup
		knowledgeService = (KnowledgeService)ic.lookup("java:comp/env/ejb/KnowledgeServiceBean");
		//knowledgeService = (KnowledgeService)ic.lookup("java:comp/env/ejb/knowledgeServiceBean");
		// = (KnowledgeService)ctx.lookup("knowledgeServiceRemote");
		//knowledgeService = ServiceLocator.getInstance().findResource("java:comp/env/KnowledgeService/local");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		knowledgeService = null;
	}
	
	@Test
	public void testerUp(){
		Assert.assertNotNull("No se cargo el EJB", knowledgeService);
	}

}
