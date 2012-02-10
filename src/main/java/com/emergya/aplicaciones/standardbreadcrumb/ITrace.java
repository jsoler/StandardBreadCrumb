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
 * The Interface ITrace.
 */
public interface ITrace  extends Comparable<ITrace>{
	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText();
	
	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public void setText(String text);
	
	
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription();
	
	/**
	 * Sets the description.
	 *
	 * @param text the new description
	 */
	public void setDescription(String description);
	

	
	/**
	 * Gets the urlpattern.
	 *
	 * @return the urlpattern
	 */
	public String getUrlPattern();
	
	/**
	 * Sets the urlpattern.
	 *
	 * @param url the new urlpattern
	 */
	public void setUrlPattern(String urlPattern);
	

	/**
	 * Gets the priority.
	 *
	 * @return the priority
	 */
	public Integer getPriority();
	
	/**
	 * Sets the priority.
	 *
	 * @param url the new priority
	 */
	public void setPriority(Integer priority);

	
	
	/**
	 * Gets the children.
	 *
	 * @return the children
	 */
	public Collection<ICrumb> getChildren();
	
	/**
	 * Sets the children.
	 *
	 * @param children the new children
	 */
	public void setChildren(Collection<ICrumb> children);
	
	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public IBreadCrumb getParent();
	
	/**
	 * Sets the parent.
	 *
	 * @param parent the new parent
	 */
	public void setParent(IBreadCrumb parent);
	
	/**
	 * Gets the profiles.
	 *
	 * @return the profiles
	 */
	public Collection<String> getProfiles();
	
	/**
	 * Sets the profiles.
	 *
	 * @param profiles the new profiles
	 */
	public void setProfiles(Collection<String> profiles);
	
	/**
	 * Gets the enabled.
	 *
	 * @return the enabled
	 */
	public boolean getEnabled();
	
	/**
	 * Sets the enabled.
	 *
	 * @param enabled the new enabled
	 */
	public void setEnabled(boolean enabled);
	
}
