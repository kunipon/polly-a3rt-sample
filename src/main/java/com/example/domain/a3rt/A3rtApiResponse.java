package com.example.domain.a3rt;

import lombok.Data;

@Data
public class A3rtApiResponse {
	int status;
	String message;
	A3rtApiResults[] results;
}
