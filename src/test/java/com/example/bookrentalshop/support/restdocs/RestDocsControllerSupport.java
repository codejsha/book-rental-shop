package com.example.bookrentalshop.support.restdocs;

import com.example.bookrentalshop.config.RestDocsConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@TestPropertySource(locations = "classpath:application.yaml")
@Import(RestDocsConfig.class)
public abstract class RestDocsControllerSupport {

    protected static final String OPENAPI_DOCUMENT_IDENTIFIER = "openapi/{class-name}/{method-name}";

    @Autowired
    protected RestDocsConfig restDocsConfig;

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    protected String serverUrl;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
        this.serverUrl = String.format("%s://%s:%d", this.restDocsConfig.getScheme(),
                this.restDocsConfig.getHost(), this.restDocsConfig.getPort());
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider)
                        .uris()
                        .withScheme(this.restDocsConfig.getScheme())
                        .withHost(this.restDocsConfig.getHost())
                        .withPort(this.restDocsConfig.getPort())
                        .and()
                        .snippets().withEncoding("UTF-8"))
                .build();
    }
}
