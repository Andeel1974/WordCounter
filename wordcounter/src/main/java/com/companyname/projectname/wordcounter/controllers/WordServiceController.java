package com.companyname.projectname.wordcounter.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.companyname.projectname.wordcounter.service.WordCounterService;

/**
 * Restful controller class for word translation and count functionality.
 */
@RestController
public class WordServiceController {
	
	public static final String WORD_COUNT_URL = "/wordcounter/{word}";
	public static final String WORD_ADD_URL = "/wordcounter/";
	
	@Autowired
	private WordCounterService wordCounterService;

	@GetMapping(WordServiceController.WORD_COUNT_URL)
	public ResponseEntity<Long> countWord(@PathVariable String word) {
		long wordCount = wordCounterService.countWord(word);
		return new ResponseEntity<>(wordCount, HttpStatus.OK);
	}
	
	// Although this returns a List of String objects in the response it cannot be a 'GET'
	// operation because it is not 'safe' or 'idempotent', hence the decision to use 'POST'.
	@PostMapping(WordServiceController.WORD_ADD_URL)
	public ResponseEntity<List<String>> addWords(@RequestBody List<String> words) {
		List<String> translatedWords = wordCounterService.addWords(words);
		return new ResponseEntity<>(translatedWords, HttpStatus.CREATED);
	}
}
