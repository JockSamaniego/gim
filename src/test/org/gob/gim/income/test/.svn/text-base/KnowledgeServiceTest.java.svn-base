package org.gob.gim.income.test;


import java.util.Properties;

import javax.naming.InitialContext;

import org.gob.gim.common.service.KnowledgeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KnowledgeServiceTest {

	private KnowledgeService knowledgeService;
	
	@Before
	public void setUp() throws Exception {
		Properties properties = new Properties();
		properties.put("java.naming.factory.initial",
			"org.jnp.interfaces.NamingContextFactory");
		properties.put("java.naming.factory.url.pkgs",
			"org.jboss.naming.org.jnp.interfaces");
		
		InitialContext ctx = new InitialContext(properties);
			knowledgeService = (KnowledgeService) ctx.lookup(
		"gim/"+"KnowledgeService"
		//+KnowledgeServiceBean.class.getSimpleName()
		+"/local");

		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testHello() throws Exception {
		
		
		
		/*InitialContext ctx;
        try {
            ctx = new InitialContext();
            String canonicalName = "KnowledgeServiceBean"+"/"+"local";
            Object result = ctx.lookup(canonicalName);
            System.out.print("---------------------------------------JNDI Found ----> "+canonicalName+" "+result);
            //return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            //return null;
        }*/
	}

}
