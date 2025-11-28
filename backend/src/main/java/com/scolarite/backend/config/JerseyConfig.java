package com.scolarite.backend.config;

import com.scolarite.backend.controllers.AuthResource;
import com.scolarite.backend.controllers.ClasseResource;
import com.scolarite.backend.controllers.EtudiantResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.ApplicationPath;

import java.io.IOException;

@Configuration
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(JacksonFeature.class);
        register(AuthResource.class);
        register(ClasseResource.class);
        register(EtudiantResource.class);
        register(OpenApiResource.class);
    }

}
