package org.realworld.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(RestDocumentationExtension.class)
@Import(RestDocsConfig.class)
public class RestDocsSupportTest extends BaseControllerTest {

  @Autowired
  RestDocumentationResultHandler restdocs;

  @BeforeEach
  void setup(WebApplicationContext context, RestDocumentationContextProvider provider) {
    this.mvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
        .apply(SecurityMockMvcConfigurers.springSecurity())
        .alwaysDo(MockMvcResultHandlers.print())
        .alwaysDo(restdocs)
        .addFilters(new CharacterEncodingFilter("UTF-8", true))
        .build();
  }

}
