package com.emergya.aplicaciones.standardbreadcrumb;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;




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



/**
 * The abstract Trace.
 */
public abstract class AbstractTrace implements ITrace{

	private String text;
	private String description;
	private String urlpattern;
	private Integer priority;
	private Collection<ICrumb> children;
	private IBreadCrumb parent;
	private Collection<String> profiles;
	private boolean enabled;
	
	
	
	/**
	 * {@inheritDoc}
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setText(String text) {
		this.text = text;
	}
	

	/**
	 * {@inheritDoc}
	 */
	public String getUrlPattern() {
		return urlpattern;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setUrlPattern(String urlpattern) {
		this.urlpattern = urlpattern;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getPriority() {
		return this.priority;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setPriority(Integer priority){
		this.priority = priority;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<ICrumb> getChildren() {
		return children;
	}

	public void setChildren(Collection<ICrumb> children) {
		this.children = children;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public IBreadCrumb getParent() {
		return parent;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setParent(IBreadCrumb parent){
		this.parent = parent;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<String> getProfiles() {
		return profiles;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setProfiles(Collection<String> profiles) {
		this.profiles = profiles;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean getEnabled() {
		return enabled;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public int compareTo(ITrace trace) {
		int cmp = 0;
		
		if(trace != null){			
			if(this.priority != trace.getPriority()){
				cmp = new Integer(this.priority).compareTo(new Integer(trace.getPriority()));
			}else{
				cmp = this.getText().compareToIgnoreCase(trace.getText());
			}
		}
		
		return cmp;
	}
	
}
