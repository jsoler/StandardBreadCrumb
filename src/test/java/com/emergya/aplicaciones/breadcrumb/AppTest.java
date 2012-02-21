package com.emergya.aplicaciones.breadcrumb;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.emergya.aplicaciones.standardbreadcrumb.BreadCrumbException;
import com.emergya.aplicaciones.standardbreadcrumb.IBreadCrumb;
import com.emergya.aplicaciones.standardbreadcrumb.IBreadCrumbFactory;
import com.emergya.aplicaciones.standardbreadcrumb.ICrumb;
import com.emergya.aplicaciones.standardbreadcrumb.ITrace;
import com.emergya.aplicaciones.standardbreadcrumb.xmlbreadcrumb.XMLBreadCrumbFactory;



/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	
	
	private static final String NAME = "BreadCrumb";
	private static final String DESCRIPTION = "BreadCrumb del portal web";
	private static final String ALL = "All";
	private static final String OWNER = "Owner";
	private static final String GPARAM1 = "clave";
	private static final String GVALUE1 = "valor";
	private static final String GPARAM2 = "clave1";
	private static final String GVALUE2 = "valor1";
	private static final String URL = "/appBase/pagina.xhtml&gparam1=gvalor1&param1=valor&param2=valor2";
	
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp(){
    	System.out.println("Starting BreadCrumbXML test\n");
    	
    	IBreadCrumbFactory factoria = new XMLBreadCrumbFactory();
    	try {
    		
			String fileName = "breadcrumb.xml";
    		
			IBreadCrumb bread1 = factoria.getBreadCrumb(fileName);
			try {
				assertTrue(checkBreadCrumbFuncionality(bread1));
			} catch (AssertionFailedError f) {
				System.out.println("An error occurred during test breadCrumbXML");
				fail();
			}
							
			printBreadCrumb(bread1);
						
		} catch (BreadCrumbException e) {
			e.printStackTrace();
		}
    	
    	System.out.println("Test BreadCrumbXML finished");
    }
    
    
    /**
	 * Check a breadcrumb. If the breadcrumb is well built, it will return true
	 * @param breadcrumb
	 * @return boolean
	 */
	public boolean checkBreadCrumbFuncionality(IBreadCrumb bread){
		boolean checked = true;
		// Check leaf node
		if(!(bread.getName().equals(NAME))){
			return false;
		}
		if(!(bread.getDescription().equals(DESCRIPTION))){
			return false;
		}
		
		// Check profiles
		Collection<String> profiles = bread.getProfiles();
		Iterator<String> proIt = profiles.iterator();
		String pro1 = proIt.next();
		String pro2 = proIt.next();
		if(!(pro1.equals(ALL)) || !(pro2.equals(OWNER))){
			return false;
		}
		// Check global parameters
		String gp = bread.getGlobalParam(GPARAM1);
		if(!gp.equals(GVALUE1)){
			return false;
		}
		String gp2 = bread.getGlobalParam(GPARAM2);
		if(!gp2.equals(GVALUE2)){
			return false;
		}
		
		// Check children
		Collection<ITrace> children = bread.getChildren();
		// Check num children
		if(children.size() != 5){
			return false;
		}
		/* Check a child
		Iterator<ITrace> itCh = children.iterator();
		ITrace ch1 = itCh.next();
		if(!(ch1.getGETUrl().equals(URL))){
			return false;
		}*/
		
		return checked;
		
	}
	
	/**
	 * Print a IBreadCrumb
	 * @param bread
	 */
	public void printBreadCrumb(IBreadCrumb bread){
		
		System.out.println("\n<-Printing info main bread->\n");
		System.out.println("Bread name: " + bread.getName());
		
		if(bread.getDescription() != null){    		
			System.out.println("Bread description: " +bread.getDescription());
		}
		
		Collection<String> profiles = bread.getProfiles();
		if(!profiles.isEmpty()){    		
			String proString = "";
			for(String profile : profiles){
				proString += "[" + profile + "]";
			}
			System.out.println("Profiles: " + proString);
		}
		
		String globalParams = "";
		Iterator<Map.Entry<String, String>> itGParam = bread.getGlobalParamskeys();
		while(itGParam.hasNext()){
			Map.Entry<String, String> gParamEntry = itGParam.next();
			globalParams += "[" + gParamEntry.getKey() + "," + gParamEntry.getValue() + "]";
		}
		if(globalParams != ""){    		
			System.out.println("Global Parameters: " + globalParams );
		}
		
		System.out.println("Base url: " + bread.getBaseUrl());
		
		System.out.println("\n<-Printing info children->\n");
		Collection<ITrace> children = bread.getChildren();
		for (ITrace trace : children) {
			printInfoTrace(trace);
		}
		
	}
	
	
	
	
	/**
	 * Print info ITrace
	 * @param child
	 */
	public void printInfoTrace(ITrace child){
		
		System.out.println("Name: " + child.getText());
		System.out.println("Description: " + child.getDescription());
		System.out.println("Weight: " + child.getPriority());
		System.out.println("Url-pattern: " + child.getUrlPattern());
		
		
		//IBreadCrumb parent = child.getParent();
		//System.out.println("Parent: " + parent.getName());
	
		
		Collection<String> profiles = child.getProfiles();
		if(!profiles.isEmpty()){    		
			String proString = "";
			for(String profile : profiles){
				proString += "[" + profile + "]";
			}
			System.out.println("Profiles: " + proString);
		}
		
		Collection<ICrumb> crumbs = child.getChildren();
		for (ICrumb crumb : crumbs) {
			printInfoCrumb(crumb);
		}
		
		
		System.out.println("Enabled: " + child.getEnabled());
	}
	
	
	/**
	 * Print info ICrumb
	 * @param crumb
	 */
	public void printInfoCrumb(ICrumb crumb){
		
		System.out.println("Name: " + crumb.getText());
		System.out.println("Url: " + crumb.getUrl());
		
		ITrace parent = crumb.getParent();
		System.out.println("Parent: " + crumb.getParent().getText());
	
		String globalParams = "";
    	Iterator<Map.Entry<String, String>> itGParam = crumb.getGlobalParams();
    	while(itGParam.hasNext()){
    		Map.Entry<String, String> gParamEntry = itGParam.next();
    		globalParams += "[" + gParamEntry.getKey() + "," + gParamEntry.getValue() + "]";
    	}
    	if(globalParams != ""){    		
    		System.out.println("Global Parameters: " + globalParams );
    	}
    	String params ="";
    	
		
    	for(Map.Entry<String, String> itParam : crumb.getParams().entrySet()){
    		params += "[" + itParam.getKey() + "," + itParam.getValue() + "]";
    	}

    	if(params != ""){    		
    		System.out.println("Parameters: " + params );
    	}

		
	}

    
}

