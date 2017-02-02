package com.nfl.graphql.mediator;


import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;

@SuppressWarnings("WeakerAccess")
public class BaseBeanTest {

    protected String loadFromFile(String fileName) {
        ClassPathResource cpr = new ClassPathResource(fileName);
        try {
            return IOUtils.toString(cpr.getInputStream(), Charset.defaultCharset());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
