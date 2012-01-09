package com.emergya.aplicaciones.standardbreadcrumb.xmlbreadcrumb;

import java.util.HashMap;
import java.util.Map;

import com.emergya.aplicaciones.standardbreadcrumb.AbstractCrumb;

public class XMLCrumb extends AbstractCrumb{
	
	public XMLCrumb() {
		super();
		
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> globlalParameters = new HashMap<String, String>();
		
		super.setParams(params);
		super.setGlobalParams(globlalParameters);

		
	}

}
