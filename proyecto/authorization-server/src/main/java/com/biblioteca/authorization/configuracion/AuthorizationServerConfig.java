package com.biblioteca.authorization.configuracion;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

/**
 * Servidor de autorizacion OAuth2 (Capitulo 11).
 * Dos cadenas de seguridad: la primera (@Order(1))
 * protege los endpoints propios del protocolo
 * (/oauth2/authorize, /oauth2/token, /oauth2/jwks);
 * la segunda (SeguridadWebConfig) da el formulario
 * de login para el usuario final. El cliente
 * "biblioteca-web" usa el flujo authorization_code
 * (con PKCE) -- no hay grant de contrasenia: Spring
 * Authorization Server no lo ofrece de fabrica desde
 * que sustituyo a Spring Security OAuth (informe
 * SS3.4-27: RSA, nunca HS256/JWT_SECRET).
 */
@Configuration
public class AuthorizationServerConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain
            cadenaProtocoloOAuth2(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfiguration
            .applyDefaultSecurity(http);
        http.getConfigurer(
            OAuth2AuthorizationServerConfigurer.class)
            .oidc(Customizer.withDefaults());
        http.exceptionHandling(excepciones ->
            excepciones
                .defaultAuthenticationEntryPointFor(
                    new LoginUrlAuthenticationEntryPoint(
                        "/login"),
                    new MediaTypeRequestMatcher(
                        MediaType.TEXT_HTML)));
        return http.build();
    }

    @Bean
    public RegisteredClientRepository
            repositorioClientes() {
        RegisteredClient bibliotecaWeb =
            RegisteredClient
                .withId(UUID.randomUUID()
                    .toString())
                .clientId("biblioteca-web")
                .clientSecret(
                    "{noop}secreto-biblioteca-web")
                .clientAuthenticationMethod(
                    ClientAuthenticationMethod
                        .CLIENT_SECRET_BASIC)
                .authorizationGrantType(
                    AuthorizationGrantType
                        .AUTHORIZATION_CODE)
                .authorizationGrantType(
                    AuthorizationGrantType
                        .REFRESH_TOKEN)
                .redirectUri(
                    "http://127.0.0.1:9000/authorized")
                .scope(OidcScopes.OPENID)
                .scope("biblioteca.api")
                .clientSettings(ClientSettings
                    .builder()
                    .requireAuthorizationConsent(
                        false)
                    .build())
                .build();
        return new InMemoryRegisteredClientRepository(
            bibliotecaWeb);
    }

    @Bean
    public JWKSource<SecurityContext> fuenteClaves() {
        KeyPair par = generarParRsa();
        RSAPublicKey publica =
            (RSAPublicKey) par.getPublic();
        RSAPrivateKey privada =
            (RSAPrivateKey) par.getPrivate();
        RSAKey clave = new RSAKey.Builder(publica)
            .privateKey(privada)
            .keyID(UUID.randomUUID().toString())
            .build();
        return new ImmutableJWKSet<>(
            new JWKSet(clave));
    }

    private static KeyPair generarParRsa() {
        try {
            KeyPairGenerator generador =
                KeyPairGenerator
                    .getInstance("RSA");
            generador.initialize(2048);
            return generador.generateKeyPair();
        } catch (Exception excepcion) {
            throw new IllegalStateException(
                excepcion);
        }
    }

    @Bean
    public JwtDecoder decodificadorJwt(
            JWKSource<SecurityContext> fuente) {
        return OAuth2AuthorizationServerConfiguration
            .jwtDecoder(fuente);
    }

    @Bean
    public AuthorizationServerSettings
            ajustesServidor() {
        return AuthorizationServerSettings
            .builder()
            .build();
    }
}
