package es.educastur.oms.configuracion;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * Configuración de internacionalización (i18n) para la aplicación Spring Boot.
 * Esta clase define la resolución del locale predeterminado y permite cambiarlo
 * dinámicamente a través de un interceptor.
 */
@Configuration
public class I18nConfiguration implements WebMvcConfigurer {
    
    /**
     * Define un {@link LocaleResolver} que almacena el locale en la sesión del usuario.
     * Se establece el idioma predeterminado a español de España (es_ES).
     * 
     * @return una instancia de {@link SessionLocaleResolver} con la configuración establecida.
     */
	
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(new Locale("es", "ES"));
        return sessionLocaleResolver;
    }
    
    /**
     * Define un {@link LocaleChangeInterceptor} que permite cambiar el idioma
     * de la aplicación mediante un parámetro en la URL.
     * 
     * @return una instancia de {@link LocaleChangeInterceptor} con el parámetro "lang".
     */
    
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }
    
    /**
     * Agrega el interceptor de cambio de idioma a la configuración de la aplicación.
     * 
     * @param registry el registro de interceptores donde se agrega el interceptor de cambio de idioma.
     */
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
