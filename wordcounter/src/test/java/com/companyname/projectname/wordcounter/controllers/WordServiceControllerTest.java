package com.companyname.projectname.wordcounter.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.companyname.projectname.wordcounter.WordCounterApplication;
import com.companyname.projectname.wordcounter.exception.EmptyWordListException;
import com.companyname.projectname.wordcounter.exception.ExceptionResponse;
import com.companyname.projectname.wordcounter.exception.GlobalExceptionHandler;
import com.companyname.projectname.wordcounter.service.WordCounterService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = WordCounterApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource
public class WordServiceControllerTest {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	TestRestTemplate restTemplate = new TestRestTemplate();
	
	HttpHeaders headers = new HttpHeaders();
	
	@MockBean
	WordCounterService wordCounterService;
	
	@Test
	public void countWord_nonExistentWord() throws JSONException {
		
		when(wordCounterService.countWord(any(String.class))).thenReturn(0l);
		
		String url1 = WordServiceController.WORD_COUNT_URL.replace("{word}", "first");
		
		ResponseEntity<Long> response1 = restTemplate
                .getForEntity(url1, Long.class);
		
		assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertTrue(response1.getBody().longValue() == 0l);
	}
	
	@Test
	public void shouldAddOneWord() throws JSONException, URISyntaxException {
		
		String translatedWord = "firstword";
		
		when(wordCounterService.addWords(anyList())).thenReturn(Stream.of(translatedWord).collect(Collectors.toList()));
		
		URI url1 = new URI(WordServiceController.WORD_ADD_URL);
		
		List<String> bodyList = Stream.of("PremierMot").collect(Collectors.toList());
		HttpEntity<List<String>> entity1 = new HttpEntity<List<String>>(bodyList, headers);
		
		ResponseEntity<List<String>> response1 = 
			  restTemplate.exchange(
			    url1,
			    HttpMethod.POST,
			    entity1,
			    new ParameterizedTypeReference<List<String>>() {}
			  );
		
		assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertTrue(response1.getBody().size() == 1);
		assertThat(response1.getBody().get(0).equals(translatedWord));
		
		when(wordCounterService.countWord(any(String.class))).thenReturn(1l);

		URI url2 = new URI(WordServiceController.WORD_COUNT_URL.replace("{word}", translatedWord));
		
		ResponseEntity<Long> response2 = restTemplate
                .getForEntity(url2, Long.class);
		
		assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertTrue(response2.getBody().longValue() == 1l);
	}
	
	@Test
	public void addWords_shouldNotAddEmptyWordsList() throws JSONException, URISyntaxException {
		
		when(wordCounterService.addWords(anyList())).thenThrow(EmptyWordListException.class);
		
		URI url1 = new URI(WordServiceController.WORD_ADD_URL);
		
		HttpEntity<List<String>> entity1 = new HttpEntity<List<String>>(new ArrayList<String>(), headers);
		
		ResponseEntity<ExceptionResponse> response1 = 
				  restTemplate.exchange(
				    url1,
				    HttpMethod.POST,
				    entity1,
				    new ParameterizedTypeReference<ExceptionResponse>() {}
				  );
		
		assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertTrue(response1.getBody().getMessageCode().equals(GlobalExceptionHandler.EMPTY_WORD_LIST_MESSAGE_CODE));
		assertTrue(response1.getBody().getMessage().equals(WordCounterService.EMPTYWORDLIST_MESSAGE));
	}
}
