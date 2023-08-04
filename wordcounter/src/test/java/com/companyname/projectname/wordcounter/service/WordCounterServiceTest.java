package com.companyname.projectname.wordcounter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.companyname.projectname.wordcounter.exception.EmptyWordListException;

@ExtendWith(MockitoExtension.class)
public class WordCounterServiceTest {
	
	@Mock
	TranslatorService transltorServivce;
	
	@InjectMocks
    private WordCounterServiceImpl service;
	
	@BeforeEach
	void setup() {
		service.clearWordStore();
	}
	
	@Test
	void shouldAddOneWord() {
		
		String firstWordTranslated = "FirstWord";
		
		when(transltorServivce.translate(any(String.class))).thenReturn(firstWordTranslated);
		
		List<String> words = new ArrayList<String>();
		words.add("PremierMot ");
		
		List<String> addedWords = service.addWords(words);
		
		assertTrue(addedWords.size() == 1);
		assertEquals(firstWordTranslated.toLowerCase(), addedWords.get(0));
		
		assertEquals(1l, service.countWord(firstWordTranslated.toLowerCase()));
	}
	
	@Test
	void shouldAddMultipleWords() {
		
		String FirstWordFrench = "PremierMot";
		String SecondWordGerman = "ZweitesWort";
		String ThirdWordItalian = "SecondaParola";
				
		String firstWordTranslated = "FirstWord";
		String secondWordTranslated = "SecondWord";
		String thirdWordTranslated = "ThirdWord";
		
		when(transltorServivce.translate(FirstWordFrench)).thenReturn(firstWordTranslated);
		when(transltorServivce.translate(SecondWordGerman)).thenReturn(secondWordTranslated);
		when(transltorServivce.translate(ThirdWordItalian)).thenReturn(thirdWordTranslated);
		
		List<String> words = new ArrayList<String>();
		words.add(FirstWordFrench);
		words.add(SecondWordGerman);
		words.add(ThirdWordItalian);
		
		List<String> addedWords = service.addWords(words);
		
		assertTrue(addedWords.size() == 3);
		assertEquals(firstWordTranslated.toLowerCase(), addedWords.get(0));
		assertEquals(secondWordTranslated.toLowerCase(), addedWords.get(1));
		assertEquals(thirdWordTranslated.toLowerCase(), addedWords.get(2));
		
		assertEquals(1l, service.countWord(firstWordTranslated.toLowerCase()));
		assertEquals(1l, service.countWord(secondWordTranslated.toLowerCase()));
		assertEquals(1l, service.countWord(thirdWordTranslated.toLowerCase()));
	}
	
	@Test
	void addWords_shouldAddMultipleWordsWithBlankEntry() {
		
		String FirstWordFrench = "PremierMot";
		String SecondWordEmpty = " ";
		String ThirdWordItalian = "SecondaParola";
		
		String firstWordTranslated = "FirstWord";
		String secondWordTranslated = "";
		String thirdWordTranslated = "ThirdWord";
		
		when(transltorServivce.translate(FirstWordFrench)).thenReturn(firstWordTranslated);
		when(transltorServivce.translate(ThirdWordItalian)).thenReturn(thirdWordTranslated);
		
		List<String> words = new ArrayList<String>();
		words.add(FirstWordFrench);
		words.add(SecondWordEmpty);
		words.add(ThirdWordItalian);
		
		List<String> addedWords = service.addWords(words);
		
		assertTrue(addedWords.size() == 3);
		assertEquals(firstWordTranslated.toLowerCase(), addedWords.get(0));
		assertEquals(secondWordTranslated, addedWords.get(1));
		assertEquals(thirdWordTranslated.toLowerCase(), addedWords.get(2));
	}
	
	@Test
	void addWords_shouldNotAddEmptyWordsList() {
		List<String> wordsEmpty = new ArrayList<String>();
		assertThrows(EmptyWordListException.class, () -> service.addWords(wordsEmpty));
		assertThrows(EmptyWordListException.class, () -> service.addWords(null));
	}
	
	@Test
	void nonAlphabeticCharacters() {
		
		String FirstWordFrench = "PremierMot";
		String SecondWordNonAlphabetic = "Zweites#Wort";
		String ThirdWordItalian = "SecondaParola";
		
		String firstWordTranslated = "FirstWord";
		String secondWordTranslated = "";
		String thirdWordTranslated = "ThirdWord";
		
		when(transltorServivce.translate(FirstWordFrench)).thenReturn(firstWordTranslated);
		when(transltorServivce.translate(ThirdWordItalian)).thenReturn(thirdWordTranslated);
		
		List<String> words = new ArrayList<String>();
		words.add(FirstWordFrench);
		words.add(SecondWordNonAlphabetic);
		words.add(ThirdWordItalian);
		
		List<String> addedWords = service.addWords(words);
		
		assertTrue(addedWords.size() == 3);
		assertEquals(firstWordTranslated.toLowerCase(), addedWords.get(0));
		assertEquals(secondWordTranslated, addedWords.get(1));
		assertEquals(thirdWordTranslated.toLowerCase(), addedWords.get(2));
		
		assertEquals(0l, service.countWord(secondWordTranslated.toLowerCase()));
	}
	
	@Test
	void addWords_nonTranslatedWord() {
		
		String FirstWordFrench = "PremierMot";
		String SecondWordNotTranslatable = "ajhdfkjadshjka";
		String ThirdWordItalian = "SecondaParola";
				
		String firstWordTranslated = "FirstWord";
		String secondWordTranslated = "";
		String thirdWordTranslated = "ThirdWord";
		
		when(transltorServivce.translate(FirstWordFrench)).thenReturn(firstWordTranslated);
		when(transltorServivce.translate(SecondWordNotTranslatable)).thenReturn(null);
		when(transltorServivce.translate(ThirdWordItalian)).thenReturn(thirdWordTranslated);
		
		List<String> words = new ArrayList<String>();
		words.add(FirstWordFrench);
		words.add(SecondWordNotTranslatable);
		words.add(ThirdWordItalian);
		
		List<String> addedWords = service.addWords(words);
		
		assertTrue(addedWords.size() == 3);
		assertEquals(firstWordTranslated.toLowerCase(), addedWords.get(0));
		assertEquals(secondWordTranslated, addedWords.get(1));
		assertEquals(thirdWordTranslated.toLowerCase(), addedWords.get(2));
	}
	
	@Test
	void countWord_emptyWord() {
		assertEquals(0l, service.countWord(null));
		assertEquals(0l, service.countWord(" "));
	}
	
	@Test
	void countWord_nonExistentWord() {
		
		String FirstWordFrench = "PremierMot";
		String firstWordTranslated = "FirstWord";
		
		when(transltorServivce.translate(FirstWordFrench)).thenReturn(firstWordTranslated);
		
		List<String> words = new ArrayList<String>();
		words.add(FirstWordFrench);
		
		service.addWords(words);
		
		assertEquals(0l, service.countWord("nonExistentWord"));
	}
	
	@Test
	void countWord_multipleSameEntries() {
		
		String FirstWordFrench = "PremierMot";
		String SecondWordGerman = "ZweitesWort";
		
		String firstWordTranslated = "FirstWord";
		String secondWordTranslated = "SecondWord";
		
		when(transltorServivce.translate(FirstWordFrench)).thenReturn(firstWordTranslated);
		when(transltorServivce.translate(SecondWordGerman)).thenReturn(secondWordTranslated);
		
		List<String> words = new ArrayList<String>();
		words.add(FirstWordFrench);
		words.add(FirstWordFrench);
		words.add(FirstWordFrench);
		words.add(SecondWordGerman);
		words.add(SecondWordGerman);
		
		service.addWords(words);
		
		assertEquals(3l, service.countWord(firstWordTranslated.toLowerCase()));
		assertEquals(2l, service.countWord(secondWordTranslated.toLowerCase()));
	}

}
