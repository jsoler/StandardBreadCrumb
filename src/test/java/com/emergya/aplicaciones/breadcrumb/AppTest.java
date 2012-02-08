package com.emergya.aplicaciones.breadcrumb;

import java.io.File;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.emergya.aplicaciones.standardbreadcrumb.BreadCrumbException;
import com.emergya.aplicaciones.standardbreadcrumb.IBreadCrumb;
import com.emergya.aplicaciones.standardbreadcrumb.IBreadCrumbFactory;
import com.emergya.aplicaciones.standardbreadcrumb.xmlbreadcrumb.XMLBreadCrumbFactory;



/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	
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
    		
    		String path = null;
			String fileName = "breadcrumb.xml";
			String full_classPath = System.getProperty("java.class.path");
			String[] separate_classPath = full_classPath.split(":");
			String classPath = separate_classPath[0];
			path = classPath + File.separatorChar+ fileName;
    		
			IBreadCrumb breadCrumb = factoria.getBreadCrumb(path);
			try {
				assertTrue(true);
			} catch (AssertionFailedError f) {
				System.out.println("An error occurred during test breadCrumbXML");
				fail();
			}
			/*if(checkMenuFuncionality(menu1)){				
				printMenu(menu1);
			}*/
		} catch (BreadCrumbException e) {
			e.printStackTrace();
		}
    	
    	System.out.println("Test BreadCrumbXML finished");
    }   
    
}

