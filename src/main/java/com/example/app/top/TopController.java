package com.example.app.top;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.domain.a3rt.A3rtService;
import com.example.domain.polly.PollyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TopController {
	
	private final PollyService pollyService;
	
	private final A3rtService a3rtService;
	
	@Autowired
	public TopController(PollyService pollyService, A3rtService a3rtService) {
		this.pollyService = pollyService;
		this.a3rtService = a3rtService;
	}
	
	@RequestMapping(value = "/")
	public String index() {
		return "top";
	}
	
	@GetMapping(value = "/speech")
	public ResponseEntity<byte[]> getVoiceData(@RequestParam (value = "text", required = true) String text) {
		HttpHeaders headers = new HttpHeaders();
		try {
			return new ResponseEntity<>(
							pollyService.synthesize(text),
							headers,
							HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(
							null,
							headers,
							HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ResponseBody
	@GetMapping(value = "/question")
	public String fetchAnswer(@RequestParam (value = "text", required = true) String text) {
		try {
			return this.a3rtService.getReply(text);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return "もう一度よろしいですか？";
	}
}
