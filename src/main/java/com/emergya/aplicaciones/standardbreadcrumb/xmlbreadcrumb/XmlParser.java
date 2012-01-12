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
	private static final String ICONURL = "iconUrl";
	private static final String ENABLED = "enabled";
	private static final String ACTIVE = "active";
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
				
				//Tomamos los hijos de forma recursiva
				//SortedSet<INodebreadCrumb> children = getChildren(doc,n,nm);
				//nm.setChildren(children);
				
				//añadimos el nodo de nivel 0 al breadcrumb
				//childrenList.add(nm);
			}
			breadCrumb.setChildren(childrenList);
		}
		
		return breadCrumb;
	}
	
	/**
	 * Recursive method that will cover every node in the breadCrumb
	 * @param n, actual node
	 * @param doc, Document generated from breadCrumb xml
	 * @return INodebreadCrumb list
	 */	
	public SortedSet<ITrace> getChildren(Document doc, Node node, ITrace parent){
		
		//Lista de Hijos a devolver
		SortedSet<ITrace> children = new TreeSet<ITrace>();
		
		XmlParserUtil xpu = new XmlParserUtil();

		if(xpu.nodeHasChildren(doc, node)){
			
			//Lista de nodos del nivel inferior al padre -> variable parent
			List<Node> list = xpu.getListNodes(doc,node);
			
			ITrace child = null;
			
			for(int i=0; i<list.size();i++){
				//Parsear el hijo y convertirlo a nodo
				Node childNode = list.get(i);
				//child = parseNode(doc,childNode,parent);
				children.add(child);
				
				SortedSet<ITrace> hijos = getChildren(doc, childNode, child);
				
				//child.setChildren(hijos);
			}

		}
		
		return children;
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
		
		String text = parser.getValueLeafNode(doc, TEXT, n);
		String url = parser.getValueLeafNode(doc, URL, n);
		String iconUrl = parser.getValueLeafNode(doc, ICONURL, n);
		SortedSet<ICrumb> children = null;
		List<String> profiles = parser.getProfiles(doc, n);
		boolean enabled = parser.getBoolean(doc, ENABLED, n); 		
		boolean active = parser.getBoolean(doc, ACTIVE, n);
		
		// Insercion valores en el nodo
		nodeTrace.setText(text);
		
		nodeTrace.setChildren(children);
		nodeTrace.setParent(father);
		
		
		nodeTrace.setProfiles(profiles);
		nodeTrace.setEnabled(enabled);
		
		return nodeTrace;
	}
	
}
