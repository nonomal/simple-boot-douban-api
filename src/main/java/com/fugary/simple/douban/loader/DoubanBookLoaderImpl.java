package com.fugary.simple.douban.loader;

import com.fugary.simple.douban.provider.BookHtmlParseProvider;
import com.fugary.simple.douban.util.HttpRequestUtils;
import com.fugary.simple.douban.vo.BookVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author Gary Fu
 * @date 2021/8/28 11:02
 */
@Component
public class DoubanBookLoaderImpl implements BookLoader {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BookHtmlParseProvider bookHtmlParseProvider;

    @Cacheable(cacheNames = "dobanBook", sync = true)
    @Override
    public BookVo loadBook(String bookUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT, HttpRequestUtils.getHeader(HttpHeaders.USER_AGENT));
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        String bookStr = restTemplate.exchange(bookUrl, HttpMethod.GET, entity, String.class).getBody();
        return bookHtmlParseProvider.parse(bookUrl, bookStr);
    }

}
