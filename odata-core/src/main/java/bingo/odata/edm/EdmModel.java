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
package bingo.odata.edm;

import bingo.odata.edm.annotations.EdmDirectValueAnnotationsManager;
import bingo.odata.edm.annotations.EdmVocabularyAnnotation;

public interface EdmModel extends EdmElement {

	EdmDirectValueAnnotationsManager getDirectValueAnnotationsManager();
	
	Iterable<EdmModel> getReferencedModels();
	
	Iterable<EdmSchemaElement> getSchemaElements();
	
	Iterable<EdmVocabularyAnnotation> getVocabularyAnnotations();
	
	EdmEntityContainer findDeclaredEntityContainer(String name);
	
	Iterable<EdmFunction> findDeclaredFunctions(String qualifiedName);
	
	EdmSchemaType findDeclaredType(String qualifiedName);
	
	EdmValueTerm findDeclaredValueTerm(String qualifiedName);
	
	Iterable<EdmVocabularyAnnotation> findDeclaredVocabularyAnnotations(EdmVocabularyAnnotatable element);
	
	Iterable<EdmStructuredType> findDirectlyDerivedTypes(EdmStructuredType baseType);
	
}