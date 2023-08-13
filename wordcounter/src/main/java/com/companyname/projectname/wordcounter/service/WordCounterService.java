package com.companyname.projectname.wordcounter.service;

import java.util.List;

import com.companyname.projectname.wordcounter.exception.EmptyWordListException;

/**
 * Service interface for word translation and count functionality.
 */
public interface WordCounterService {
	
	String EMPTYWORDLIST_MESSAGE = "The list of words provided for this request is empty";
	
	/**
	 * Adds one or more words to the word counter.
	 * @param words {@link java.util.List} of words to add to the word counter.
	 * @return {@link java.util.List} of {@link java.lang.String} where each entry 
	 * is a word that has been added to the library of words.
	 * @throws EmptyWordListException
	 */
	List<String> addWords(List<String> words) throws EmptyWordListException;
	
	/**
	 * Returns the count of how many times the <code>word</code> argument 
	 * was added to the library of words.
	 * @param word Given word for which the count is required.
	 * @return returns the count of how many times the <code>word</code> argument 
	 * was added to the library of words.
	 */
	long countWord(String word);
	
	/**
	 * Clears the library of words.
	 */
	void clearWordStore();

}
