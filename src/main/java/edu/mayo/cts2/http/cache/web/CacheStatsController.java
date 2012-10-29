/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mayo.cts2.http.cache.web;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Statistics;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.mayo.cts2.framework.webapp.rest.extensions.controller.ControllerProvider;

/**
 * The Class CacheStatsController.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class CacheStatsController implements ControllerProvider {

	private static final String CACHE_NAME = "SimplePageCachingFilter";

	/**
	 * Gets the stats.
	 *
	 * @return the stats
	 */
	@RequestMapping(value = { "/system/cache/stats" }, method = RequestMethod.GET)
	@ResponseBody
	public String getStats() {
		return this.cacheStatsDisplay(CacheManager.getInstance()
				.getEhcache(CACHE_NAME).getStatistics());
	}
	
	/**
	 * Purge cache.
	 */
	@RequestMapping(value = { "/system/cache" }, method = RequestMethod.DELETE)
	@ResponseBody
	public void purgeCache() {
		CacheManager.getInstance().clearAll();
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.webapp.rest.extensions.controller.ControllerProvider#getController()
	 */
	@Override
	public Object getController() {
		return this;
	}

	/**
	 * Cache stats display.
	 *
	 * @param stats the stats
	 * @return the string
	 */
	private String cacheStatsDisplay(Statistics stats) {
		StringBuilder dump = new StringBuilder();

		dump.append("<html><body><pre>")
				.append("Stats:").append("\n name = ")
				.append(stats.getAssociatedCacheName())
				.append("\n cacheHits = ").append(stats.getCacheHits())
				.append("\n onDiskHits = ").append(stats.getOnDiskHits())
				.append("\n offHeapHits = ").append(stats.getOffHeapHits())
				.append("\n inMemoryHits = ").append(stats.getInMemoryHits())
				.append("\n misses = ").append(stats.getCacheMisses())
				.append("\n onDiskMisses = ").append(stats.getOnDiskMisses())
				.append("\n offHeapMisses = ").append(stats.getOffHeapMisses())
				.append("\n inMemoryMisses = ")
				.append(stats.getInMemoryMisses()).append("\n size = ")
				.append(stats.getObjectCount()).append("\n averageGetTime = ")
				.append(stats.getAverageGetTime())
				.append("\n evictionCount = ").append(stats.getEvictionCount())
				.append("</pre></body></html>");


		return dump.toString();
	}
}