/**
 *  Copyright 2014 Diego Ceccarelli
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.europeana.querylog.learn.measure;

import java.util.LinkedList;
import java.util.List;

import eu.europeana.querylog.QueryAssessment;
import eu.europeana.querylog.learn.measure.filter.Filter;

/**
 * Represents an abstract measure, implements the filters mechanism. To add a
 * new measure just extend this class the implements the match method, without
 * caring of filters.
 * 
 * @author Diego Ceccarelli <diego.ceccarelli@isti.cnr.it>
 * 
 *         Created on Mar 11, 2014
 */
public abstract class AbstractMeasure {

	List<Filter> filters = new LinkedList<Filter>();
	protected String name = "noname";

	public void addFilter(Filter f) {
		filters.add(f);
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public double getScore(List<String> results, QueryAssessment assessment) {
		filter(results, assessment);
		return match(results, assessment);
	}

	private void filter(List<String> results, QueryAssessment assessment) {
		for (Filter f : filters) {
			f.filter(results, assessment);
		}
	}

	/**
	 * returns a score reporting how much a ranking function is good, given a
	 * goldentruth assessment. The higher is the score the better the ranking
	 * function.
	 * 
	 * @param results
	 *            the ranked document-id returned by the ranking function that
	 *            is evaluated
	 * @param assessment
	 *            a query assessment;
	 * @return a score representing how much the ranking function is good for
	 *         the given query assessment, given a goldentruth assessment. The
	 *         higher is the score the better the ranking function.
	 * 
	 */
	public abstract double match(List<String> results,
			QueryAssessment assessment);

	public String getName() {
		String str = name;
		for (Filter f : filters) {
			str += "[" + f.getName() + "]";
		}
		return str;
	}

}
