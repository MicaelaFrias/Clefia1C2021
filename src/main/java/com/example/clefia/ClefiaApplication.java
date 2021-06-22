package com.example.clefia;

import com.example.clefia.imageTest.TestImage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClefiaApplication {

	public static void main(String[] args) {
		//SpringApplication.run(ClefiaApplication.class, args);
		TestImage ti = new TestImage();
	}

}
