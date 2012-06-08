/*
 * Copyright 2012 the original author or authors.
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
package bingo.odata.edm.csdl;

import java.util.List;


public class CsdlAnnotations {

	private final List<CsdlVocabularyAnnotationBase> annotations;
	
	private final String qualifier;
	
	private final String target;
	
	public CsdlAnnotations(Iterable<CsdlVocabularyAnnotationBase> annotations,String qualifier,String target){
		this.annotations = null;
		this.qualifier   = qualifier;
		this.target      = target;
	}

	public List<CsdlVocabularyAnnotationBase> getAnnotations() {
    	return annotations;
    }

	public String getQualifier() {
    	return qualifier;
    }

	public String getTarget() {
    	return target;
    }
}
