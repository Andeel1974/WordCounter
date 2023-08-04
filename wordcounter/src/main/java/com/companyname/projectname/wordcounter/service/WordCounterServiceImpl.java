package com.companyname.projectname.wordcounter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.companyname.projectname.wordcounter.exception.EmptyWordListException;

@Service
public class WordCounterServiceImpl implements WordCounterService {
	
	// TODO - If the words and their counts were to be stored in a database instead then the RDBMS in conjunction 
	// with an application-tier @Transactional annotation (with the necessary 'isolation' level set as an attribute) 
	// on this class would provide the necessary multi-user environment protection instead
	// TODO - Functionality will need to be developed to housekeep this data-structure or else a Memory Leak will ensue
	// Choice of 'static', 'ConcurrentMap' & 'AtomicLong' are to keep a consistent & minimal state in a multi-user environment where 
	// each request must work with the same 'wordStore' instance. Refer to comments further above regarding refactoring 
	// this solution to use a RDBMS instead.
	private static ConcurrentMap<String, AtomicLong> wordStore = new ConcurrentHashMap<>();
	
	@Autowired
	TranslatorService translatorService;
	
	Logger logger = LoggerFactory.getLogger(WordCounterServiceImpl.class);

	@Override
	public List<String> addWords(List<String> words) throws EmptyWordListException {
		
		if (this.isNullOrEmptyList(words)) {
			logger.error("EmptyWordListException");
			throw new EmptyWordListException();
		}
		
		List<String> translatedWords = new ArrayList<>();
		
		for (String currentWord : words) {
			if (!this.isNullOrEmptyWord(currentWord)) {
				currentWord = currentWord.trim();
				if (this.validateWord(currentWord)) {
					String translatedWord = translatorService.translate(currentWord);
					if (!this.isNullOrEmptyWord(translatedWord)) {
						translatedWord = translatedWord.toLowerCase();
						translatedWords.add(translatedWord);
						
						StringBuilder tranlatedWordMessage = new StringBuilder();
						tranlatedWordMessage.append(currentWord);
						tranlatedWordMessage.append(" translated into ");
						tranlatedWordMessage.append(translatedWord);
						logger.info(tranlatedWordMessage.toString());
						
						if (wordStore.computeIfPresent(translatedWord, (key, val) -> {
								StringBuilder wordCountIncrementMessage = new StringBuilder();
								wordCountIncrementMessage.append(key);
								wordCountIncrementMessage.append(" count incremented in word store");
								logger.info(wordCountIncrementMessage.toString());
								return new AtomicLong(val.incrementAndGet());
							}) == null) {
								wordStore.computeIfAbsent(translatedWord, (key) -> {
									StringBuilder wordCountAdditionMessage = new StringBuilder();
									wordCountAdditionMessage.append(key);
									wordCountAdditionMessage.append(" added to word store");
									logger.info(wordCountAdditionMessage.toString());
									return new AtomicLong(1);
								});
							}
						continue;
					} else {
						StringBuilder nonTranslatedWordMessage = new StringBuilder();
						nonTranslatedWordMessage.append(currentWord);
						nonTranslatedWordMessage.append(" not translated");
						logger.info(nonTranslatedWordMessage.toString());
					}
				}
			}
			// Assumption - Requirements do not state what to do in case of null or empty List of words, 
			//              an empty word, non-valid word or unrecognised word.
			// 			  - Here we assume an empty word should result in an empty translation.
			StringBuilder nonWordCountMessage = new StringBuilder();
			nonWordCountMessage.append(currentWord);
			nonWordCountMessage.append(" not added to wordcount");
			logger.info(nonWordCountMessage.toString());
			
			translatedWords.add("");
		}
		
		return translatedWords;
	}

	@Override
	public long countWord(String word) {
		
		StringBuilder wordCountMessage = new StringBuilder();
		
		if ((word != null) && (word.length() > 0)) {
			AtomicLong wordCount = wordStore.get(word.trim().toLowerCase());
			if (wordCount == null) {
				wordCountMessage.append("No word count found for : ");
				wordCountMessage.append(word);
				logger.info(wordCountMessage.toString());
				return 0l;
			}
			
			wordCountMessage.append("Word count for : ");
			wordCountMessage.append(word);
			wordCountMessage.append(" = ");
			wordCountMessage.append(word);
			logger.info(wordCount.toString());
			
			return wordCount.longValue();
		} else {
			logger.info("countWord invoked for null or empty word");
			return 0l;
		}
	}
	
	@Override
	public void clearWordStore() {
		// Provided for internal housekeeping purposes.
		wordStore.clear();
		logger.info("word store cleared");
	}
	
	private boolean isNullOrEmptyList(List<String> words) {
		return (words == null || words.size() < 1) ? true : false;
	}
	
	private boolean isNullOrEmptyWord(String word) {
		// TODO can use a third party library for this check instead
		return (word == null || word.isEmpty() || word.trim().isEmpty()) ? true : false;
	}
	
	private boolean validateWord(String word) {
		// TODO can use a third party library for this check instead
		char[] charArr = word.toCharArray();
		for(char c : charArr) {
	        if(!Character.isLetter(c)) {
	        	return false;
	        }
	    }
		return true;
	}

}
