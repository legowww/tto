package com.tto.core;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

@SpringBootTest
class CoreApplicationTests {

    @Test
    void contextLoads() {






    }
}

/**
 * https://namu.wiki/w/URL%20escape%20code
 * Encode URL: X4cMPRltQhalSiXM8QgHsuAOK1%2FasF494602CvtfRMOEOyTmY1h9UOxgzYax5T1oPy%2Bq1m9BtXlsHzznuJFxew%3D%3D
 *
 * Decode URL: X4cMPRltQhalSiXM8QgHsuAOK1%252FasF494602CvtfRMOEOyTmY1h9UOxgzYax5T1oPy%252Bq1m9BtXlsHzznuJFxew%253D%253
 *      RestClient Encoded URL: X4cMPRltQhalSiXM8QgHsuAOK1/asF494602CvtfRMOEOyTmY1h9UOxgzYax5T1oPy+q1m9BtXlsHzznuJFxew%3D%3D
 *
 *      - Encode URL 는 암호화된 상태를 의미.
 *      - Decode URL 는 암호화 이전 상태
 *      RestClient 는 URL 을 기본적으로 디코드 상태라 가정하기때문에 인코딩을 항상 해준다.
 *      암호화 이전 -> 암호화 후
 *      %2F        /
 *      %2B        +
 *      %25        %
 *
 *      https://meyerweb.com/eric/tools/dencoder/
 *      에서 X4cMPRltQhalSiXM8QgHsuAOK1%252FasF494602CvtfRMOEOyTmY1h9UOxgzYax5T1oPy%252Bq1m9BtXlsHzznuJFxew%253D%253 를 넣고 decode 하면
 *      X4cMPRltQhalSiXM8QgHsuAOK1%2FasF494602CvtfRMOEOyTmY1h9UOxgzYax5T1oPy%2Bq1m9BtXlsHzznuJFxew%3D%3D 이 나온다.
 *
 *
 *  RestClient 는 URL 을 기본적으로 인코딩하여 보낸다. WebClient 는 인코딩을 build()에서 끌 수 있지만 얘는 찾아봐야한다.
 *
 *
 *
 *  URL 을
 */
