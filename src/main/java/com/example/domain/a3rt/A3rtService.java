package com.example.domain.a3rt;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class A3rtService {
	
	// テストなので~/.spring-boot-devtools.propertiesに用意
	@Value("${a3rt.apikey}")
	private String API_KEY;
	private static final String TALK_API_URL = "https://api.a3rt.recruit-tech.co.jp/talk/v1/smalltalk";
	
	public String getReply(String question) throws Exception {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost( TALK_API_URL );
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("apikey", API_KEY));
		params.add(new BasicNameValuePair("query", question));
		post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		
		CloseableHttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		String responseJson = EntityUtils.toString( entity, "UTF-8" );
		ObjectMapper mapper = new ObjectMapper();
		A3rtApiResponse a3rtApiResponse = mapper.readValue(responseJson, A3rtApiResponse.class);
		if(response.getStatusLine().getStatusCode()!=200) {
			throw new RuntimeException(response.getStatusLine().getReasonPhrase());
		}
		
		return a3rtApiResponse.getResults()[0].getReply();
	}
}
