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
	 * @return English language representation for the given <code>word</code> argument. <p>
	 * If the <code>word</code> argument is in English then it will be returned. <p>
	 * If the <code>word</code> argument is not recognised then an empty translation is returned.
	 */
	// TODO the languages that are supported could be made configurable.
	// TODO where different non-English languages have the same word, the choice of 
	    // non-English language that is used in the translation could be made configurable.
	// TODO the English language version (e.g. UK English) that is translated to could be made configurable.
	// TODO in reality you can't possibly expect to write a service that can accept words in any language
		// ...there's just too many written languages in the world so really we'd need to also agree which 
		// languages we would support and create an Enum for this or provide support in a database table that
	    // is loaded into a cache upon application start-up.
	// TODO in reality you would access the 'Translator' class as a service over HTTP using perhaps 
		// HttpURLConnection.java but that is outside of the scope for the stated requirements
		// this is not a good candidate to be used as a library because I'd expect the service to be 
		// dynamic in terms of the languages and translations that it supports
	String translate(String word);
}
