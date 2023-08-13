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
	
	private static final String EMPTY_SPACE = " ";
	private static final String TRANSLATED_INTO = "translated into";
	private static final String COUNT_INCREMENTED = "count incremented in word store";
	private static final String ADDED_TO_WORD_STORE = "added to word store";
	private static final String NOT_TRANSLATED = "not translated";
	private static final String NOT_ADDED_TO_WORD_STORE = "not added to word store";
	private static final String NO_WORD_COUNT = "No word count found for : ";
	private static final String WORD_COUNT = "Word count for : ";
	private static final String NULL_EMPTY_WORD = "countWord invoked for null or empty word";
	private static final String WORD_STORE_CLEARED = "word store cleared";

	// TODO - If the words and their counts were to be stored in a database instead then 
		// the RDBMS in conjunction with an application-tier @Transactional annotation 
		// (with the necessary 'isolation' level set as an attribute) then that together 
		// with a DAO (e.g. implementing a typed Spring JpaRepository interface) would 
		// preferably provide the necessary multi-user environment protection instead.
	// TODO - Functionality will need to be specified & developed to housekeep this 
		// data-structure or else a Memory Leak will ensue.
		// This is outside of the scope of the stated requirements but nonetheless 
		// a public clearWordStore() method is provided
	// Choice of 'static', 'ConcurrentMap' & 'AtomicLong' are to keep a consistent & minimal 
	// state in a multi-user environment where each request must work with the same 
	// 'wordStore' instance. Refer to comments further above regarding refactoring 
	// this solution to use a RDBMS instead.
	private static ConcurrentMap<String, AtomicLong> wordStore = new ConcurrentHashMap<>();
	
	private static Supplier<EmptyWordListException> exceptionHandler = () -> {
		throw new EmptyWordListException(EMPTYWORDLIST_MESSAGE);
	};
	
	@Autowired
	private TranslatorService translatorService;
	
	private final Logger logger = LoggerFactory.getLogger(WordCounterServiceImpl.class);

	@Override
	public List<String> addWords(List<String> words) throws EmptyWordListException {
		
		if (this.isNullOrEmptyList(words)) {
			logger.error(EMPTYWORDLIST_MESSAGE);
			exceptionHandler.get();
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
						tranlatedWordMessage.append(EMPTY_SPACE);
						tranlatedWordMessage.append(TRANSLATED_INTO);
						tranlatedWordMessage.append(EMPTY_SPACE);
						tranlatedWordMessage.append(translatedWord);
						logger.info(tranlatedWordMessage.toString());
						
						if (wordStore.computeIfPresent(translatedWord, (key, val) -> {
								StringBuilder wordCountIncrementMessage = new StringBuilder();
								wordCountIncrementMessage.append(key);
								wordCountIncrementMessage.append(EMPTY_SPACE);
								wordCountIncrementMessage.append(COUNT_INCREMENTED);
								logger.info(wordCountIncrementMessage.toString());
								return new AtomicLong(val.incrementAndGet());
							}) == null) {
								wordStore.computeIfAbsent(translatedWord, (key) -> {
									StringBuilder wordCountAdditionMessage = new StringBuilder();
									wordCountAdditionMessage.append(key);
									wordCountAdditionMessage.append(EMPTY_SPACE);
									wordCountAdditionMessage.append(ADDED_TO_WORD_STORE);
									logger.info(wordCountAdditionMessage.toString());
									return new AtomicLong(1);
								});
							}
						continue;
					} else {
						StringBuilder nonTranslatedWordMessage = new StringBuilder();
						nonTranslatedWordMessage.append(currentWord);
						nonTranslatedWordMessage.append(EMPTY_SPACE);
						nonTranslatedWordMessage.append(NOT_TRANSLATED);
						logger.info(nonTranslatedWordMessage.toString());
					}
				}
			}
			// Assumption - Requirements do not state what to do in case of 
			//              an empty word, non-valid word or unrecognised word.
			// 			  - Here we assume an empty word should result in an empty translation.
			StringBuilder nonWordCountMessage = new StringBuilder();
			nonWordCountMessage.append(currentWord);
			nonWordCountMessage.append(EMPTY_SPACE);
			nonWordCountMessage.append(NOT_ADDED_TO_WORD_STORE);
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
				wordCountMessage.append(NO_WORD_COUNT);
				wordCountMessage.append(word);
				logger.info(wordCountMessage.toString());
				return 0l;
			}
			
			wordCountMessage.append(WORD_COUNT);
			wordCountMessage.append(word);
			wordCountMessage.append(" = ");
			wordCountMessage.append(word);
			logger.info(wordCount.toString());
			
			return wordCount.longValue();
		} else {
			logger.info(NULL_EMPTY_WORD);
			return 0l;
		}
	}
	
	@Override
	public void clearWordStore() {
		// Provided for internal housekeeping purposes.
		wordStore.clear();
		logger.info(WORD_STORE_CLEARED);
	}
	
	private boolean isNullOrEmptyList(List<String> words) {
		return (words == null || words.isEmpty()) ? true : false;
	}
	
	private boolean isNullOrEmptyWord(String word) {
		return (StringUtils.isBlank(word)) ? true : false;
	}
	
	private boolean validateWord(String word) {
		char[] charArr = word.toCharArray();
		for(char c : charArr) {
	        if(!Character.isLetter(c)) {
	        	return false;
	        }
	    }
		return true;
	}

}
