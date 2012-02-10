package com.emergya.aplicaciones.standardbreadcrumb.xmlbreadcrumb;

/**
 * Copyright (C) 2011, Emergya (http://www.emergya.es)
 *
 * @author <a href="mailto:eserrano@emergya.com">Eduardo Serrano Luque</a>
 * @author <a href="mailto:jsoler@emergya.com">Jaime Soler</a>
 * @author <a href="mailto:jariera@emergya.com">José Alfonso Riera</a>
 * @author <a href="mailto:frodriguez@emergya.com">Francisco Rodríguez Mudarra</a>
 *
 * This file is Component StandardBreadCrumb
 *
 * This software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 */

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.emergya.aplicaciones.standardbreadcrumb.BreadCrumbException;
import com.emergya.aplicaciones.standardbreadcrumb.IBreadCrumb;
import com.emergya.aplicaciones.standardbreadcrumb.ICrumb;
import com.emergya.aplicaciones.standardbreadcrumb.ITrace;

public class XmlParser {
	
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String BASE_URL = "base-url";
	private static final String TEXT = "text";
	private static final String PRIORITY = "priority";
	private static final String URL = "url";
	private static final String URLPATTERN = "url-pattern";
	private static final String ENABLE = "enabled";
	private static final String MSG = "There was an error in createbreadCrumb";
	
	/**
	 * Using a xml it will generate a IBreadCrumb
	 * @param path, xml path
	 * @return IbreadCrumb, generated breadCrumb
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws BreadCrumbException 
	 */
	public IBreadCrumb createBreadCrumb(String path) throws BreadCrumbException{
		
		IBreadCrumb breadCrumb = null;
		Document doc = null;
		File f;
		
		try {
			f = new File(path);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(f);
			breadCrumb = readBreadCrumb(doc);
		} catch (Exception e) {
			throw new BreadCrumbException(e, MSG, new Integer(0), "");
		}
		
		return breadCrumb;
		
	}

	/**
	 * Xml reader
	 * @param doc, Document generated from breadCrumb xml
	 * @return IbreadCrumb
	 */
	public IBreadCrumb readBreadCrumb(Document doc){
		
		// Establecemos los valores del breadCrumb principal
		IBreadCrumb breadCrumb = new XMLBreadCrumb();
		XmlParserUtil xpu = new XmlParserUtil();
		Node root = doc.getDocumentElement();
		
		breadCrumb.setName(xpu.getValueLeafNode(doc, NAME, root));
		breadCrumb.setDescripction(xpu.getValueLeafNode(doc, DESCRIPTION, root));
		breadCrumb.setBaseUrl(xpu.getValueLeafNode(doc, BASE_URL, root));
		breadCrumb.setProfiles(xpu.getProfiles(doc, root));
		
		Map<String, String> globalParams = xpu.getGloblalParameters(doc,root);
		Iterator<Map.Entry<String, String>> it = globalParams.entrySet().iterator();
		
		while(it.hasNext()){
			Map.Entry<String, String> mapEntry = (Map.Entry<String, String>)it.next();
			breadCrumb.addGlobalParam(mapEntry.getKey(), mapEntry.getValue());
		}
		
		// Recursividad que recorrera los hijos
		if(xpu.rootHasNodes(doc)){
			//Devuelve una lista de nodos de nivel 0
			List<Node> nodeList = xpu.getListRootNodes(doc);
			
			// Lista de hijos del breadCrumb
			SortedSet<ITrace> childrenList = new TreeSet<ITrace>();
			
			for(int i=0; i<nodeList.size();i++){
				Node n = nodeList.get(i);
				//Construimos la información del nodo y la pasamos al objeto leyendo del xml
				ITrace nm = parseNode(doc, n, null);
				
				//añadimos el nodo de nivel 0 al breadcrumb
				childrenList.add(nm);
			}
			breadCrumb.setChildren(childrenList);
		}
		
		return breadCrumb;
	}
	
	/**
	 * Sets node properties
	 * @param doc, Document generated from breadCrumb xml
	 * @param n, Node that contais info node
	 * @param father, INodebreadCrumb father
	 * @return INodebreadCrumb
	 */
	public ITrace parseNode(Document doc, Node n, IBreadCrumb father){
		
		ITrace nodeTrace = new XMLTrace();
		// Parseo directo desde el xml
		XmlParserUtil parser = new XmlParserUtil();
		
		String text = parser.getValueLeafNode(doc, NAME, n);
		String urlPattern = parser.getValueLeafNode(doc, URLPATTERN, n);
		String description = parser.getValueLeafNode(doc, DESCRIPTION, n);
		Boolean enabled = parser.getBoolean(doc,ENABLE, n);
		String priority  = parser.getValueLeafNode(doc, PRIORITY, n);
		
		//Lista de Hijos a devolver
		List<ICrumb> children = new LinkedList<ICrumb>();
		if(parser.nodeHasChildren(doc, n)){

			//Lista de nodos del nivel inferior al padre -> variable parent
			List<Node> list = parser.getListNodes(doc,n);
		
			ICrumb child = null;
			for(int i=0; i<list.size();i++){
				//Parsear el hijo y convertirlo a nodo
				Node childNode = list.get(i);
				child = parseCrumbNode(doc,childNode,nodeTrace);
				children.add(child);
				
			}
		}	
		List<String> profiles = parser.getProfiles(doc, n);
		
		// Insercion valores en el nodo
		nodeTrace.setText(text);
		nodeTrace.setDescription(description);
		nodeTrace.setUrlPattern(urlPattern);
		nodeTrace.setEnabled(enabled);
		nodeTrace.setPriority(Integer.decode(priority));
		
		nodeTrace.setChildren(children);
		nodeTrace.setParent(father);
		
		
		nodeTrace.setProfiles(profiles);
		
		return nodeTrace;
	}
	
	
	
	/**
	 * Sets node properties
	 * @param doc, Document generated from breadCrumb xml
	 * @param n, Node that contais info node
	 * @param father, INodebreadCrumb father
	 * @return INodebreadCrumb
	 */
	public ICrumb parseCrumbNode(Document doc, Node n, ITrace father){
		
		ICrumb nodeCrumb = new XMLCrumb();
		// Parseo directo desde el xml
		XmlParserUtil parser = new XmlParserUtil();
		
		String text = parser.getValueLeafNode(doc, TEXT, n);
		String url = parser.getValueLeafNode(doc, URL, n);
		
		// Insercion valores en el nodo
		nodeCrumb.setText(text);
		nodeCrumb.setUrl(url);
		
		Map<String, String> globalParams = parser.getGloblalParameters(doc,n);
		Iterator<Map.Entry<String, String>> it = globalParams.entrySet().iterator();
		
		while(it.hasNext()){
			Map.Entry<String, String> mapEntry = (Map.Entry<String, String>)it.next();
			nodeCrumb.addGlobalParam(mapEntry.getKey(), mapEntry.getValue());
		}
		
		Map<String, String> params = parser.getParameters(doc, n);
		Iterator<Map.Entry<String, String>> it2 = params.entrySet().iterator();
		while(it2.hasNext()){
			Map.Entry<String, String> mapEntry2 = (Map.Entry<String, String>)it2.next();
			nodeCrumb.addParam(mapEntry2.getKey(), mapEntry2.getValue());
		}
		
		
		
				
		nodeCrumb.setParent(father);
		
		
		
		return nodeCrumb;
	}

	
}
