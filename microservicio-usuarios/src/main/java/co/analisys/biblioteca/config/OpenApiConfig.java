package co.analisys.biblioteca.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Usuarios - API")
                        .description("API para la gestión de usuarios de la biblioteca. " +
                                "Permite consultar información de usuarios y actualizar sus datos de contacto." +
                                "\n\n**Autenticación:** JWT Bearer Token obtenido desde Keycloak." +
                                "\n\n**Roles:**\n- `ROLE_USER`: consulta de su propio perfil\n- `ROLE_LIBRARIAN`: consulta y edición\n- `ROLE_ADMIN`: acceso completo")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Analisys")
                                .email("soporte@analisys.co")))
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Servidor local")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT obtenido desde Keycloak. " +
                                                "Obtén el token en: POST http://localhost:8080/realms/biblioteca/protocol/openid-connect/token")));
    }
}
