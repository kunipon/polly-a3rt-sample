package com.example.domain.polly;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.DescribeVoicesRequest;
import com.amazonaws.services.polly.model.DescribeVoicesResult;
import com.amazonaws.services.polly.model.LanguageCode;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.Voice;
import com.amazonaws.util.IOUtils;

@Service
public class PollyService {
	private AmazonPollyClient polly;
	private Voice voice;
	
	@PostConstruct
	public void pollyInitialize() {
		// create an Amazon Polly client
		createPolly(Region.getRegion(Regions.US_WEST_2));
		// Create describe voices request.
		describeVoice();
	}
	
	/**
	 * create an Amazon Polly client in a specific region
	 * @param region
	 */
	private void createPolly(Region region) {
		polly = new AmazonPollyClient(
				new DefaultAWSCredentialsProviderChain(), 
				new ClientConfiguration());
		polly.setRegion(region);
	}
	
	/**
	 * 
	 */
	private void describeVoice() {
		// Create describe voices request.
		DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest().withLanguageCode(LanguageCode.JaJP);

		// Synchronously ask Amazon Polly to describe available TTS voices.
		DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
		voice = describeVoicesResult.getVoices().get(0);
	}
	
	/**
	 * 
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public byte[] synthesize(String text) throws IOException {
		SynthesizeSpeechRequest synthReq = 
			new SynthesizeSpeechRequest().withText(text).withVoiceId(voice.getId())
				.withOutputFormat(OutputFormat.Mp3);
		SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);
		InputStream in = synthRes.getAudioStream();
		
		return  IOUtils.toByteArray(in);
	}
}
