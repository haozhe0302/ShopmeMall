package com.shopme.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String userImageDirName = "user-photos";
        Path userPhotosDir = Paths.get(userImageDirName);
        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + userImageDirName + "/**").addResourceLocations("file:/" + userPhotosPath + "/");

        String categoryImageDirName = "category-images";
        Path categoryImagesDir = Paths.get(categoryImageDirName);
        String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + categoryImageDirName + "/**").addResourceLocations("file:/" + categoryImagesPath + "/");

        String brandLogosDirName = "brand-logos";
        Path brandLogosDir = Paths.get(brandLogosDirName);
        String brandLogosPath = brandLogosDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + brandLogosDirName + "/**").addResourceLocations("file:/" + brandLogosPath + "/");
    }
}
