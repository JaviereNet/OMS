package es.educastur.oms.configuracion;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

/**
 * Configuración de internacionalización (i18n) para la aplicación Spring Boot.
 * Esta clase define la resolución del locale (ahora en cookie) y permite cambiarlo
 * dinámicamente a través de un interceptor.
 */
@Configuration
public class I18nConfiguration implements WebMvcConfigurer {

    /**
     * Define un {@link LocaleResolver} basado en cookie que guarda la preferencia
     * de idioma en el navegador del cliente. Se establece el idioma predeterminado a español de España (es_ES).
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(new Locale("es", "ES"));
        cookieLocaleResolver.setCookieName("LOCALE");
        cookieLocaleResolver.setCookieMaxAge(60 * 60 * 24 * 30); // 30 dias
        return cookieLocaleResolver;
    }

    /**
     * Define un {@link LocaleChangeInterceptor} que permite cambiar el idioma
     * de la aplicación mediante un parámetro en la URL.
     *
     * @return una instancia de {@link LocaleChangeInterceptor} con la configuración para el parámetro "lang".
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
