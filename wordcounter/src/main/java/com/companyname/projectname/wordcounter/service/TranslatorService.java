package com.companyname.projectname.wordcounter.service;

/**
 * Service interface for word translation.
 */
public interface TranslatorService {
	
	/**
	 * Translate a non-English language word into and English word. 
	 * English language words are returned as is.
	 * @param word The word that will be translated into English. 
	 * English language words are returned as is.
	 * @return English language representation for the given word. If the provided word 
	 * is in English then it will be returned.
	 */
	// TODO the languages that are supported can be made configurable.
	// TODO where different non-English languages have the same word, the choice of non-English language that is used in the translation can be made configurable.
	// TODO the English language version (e.g. UK English) that is translated to can be made configurable.
	// TODO in reality you can't possibly expect to write a service that can accept words in any language...there's just too many written languages in the world 
		// so really we'd need to also agree which languages we would support and create an Enum for this
	// TODO in reality you would access the 'Translator' class as a service over HTTP using perhaps HttpURLConnection.java
		// this is not a good candidate to be used as a library because I'd expect the service to be dynamic in terms of the languages and translations that it supports
	// TODO there is nothing in the requirements concerning what the 'Translator' service will do about words that contain only alphabetic characters but don't exist in any language.
		// - I would expect it to return some sort of Exception or error code or an empty translation
	String translate(String word);

}
