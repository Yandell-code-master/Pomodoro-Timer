package com.backend.code.pomodoro_timer.util;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class CloudinaryConfiguration {

    // cuando hacemos un value lo que hace spring es inyectar los valores cuando ya se ha creado la variable, por eso la variable no puede ser final ya que no tiene sentido
    // porque se estaría modificando la variable algo que no es posible cuando es final
    @Value("${CLOUDINARY_URL}")
    private String cloudinaryCredential;

    @Bean
    Cloudinary cloudinaryBean() {
        return new Cloudinary(cloudinaryCredential);
    }
}
