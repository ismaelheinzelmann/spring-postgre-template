# Template Project Spring/Postgre/Docker
Este projeto é um template com estrutura básica de CRUD utilizando as tecnologias mencionadas.

## Como executar este projeto

### Docker
Utilizando Docker, você pode executar o projeto apenas rodando `docker compose up` e ele estará funcionando em poucos minutos.

### Java 21
Utilizando Maven, execute `mvn clean package` e depois `cd target && java -jar demo-0.0.1-SNAPSHOT.jar`.

Fique atento sobre a necessidade de um banco de dados da aplicação, que pode ser inicializado com `docker compose up postgres`, que estará configurado de acordo com o requisitado pelo template.

## Desenvolvimento
Este projeto implementa um CRUD simples de clientes utilizando Java 21 (Spring Boot) com banco de dados PostgreSQL.

A estrutura de pastas está definida da seguinte forma:

### 📁 `config`
Contém classes de configuração do Spring (por exemplo, segurança, web, beans)
- `SecurityConfig.java` - Configuração do Spring Security
- `WebMvcConfig.java` - Configuração do MVC e CORS
- `SwaggerConfig.java` - Configuração da documentação da API
- `CustomExeptionHandler.java` - Classe que lida com exceções lançadas pelos serviços

### 📁 `constants`
Contém valores constantes utilizados em toda a aplicação.
- `ApiConstants.java` - Endpoints e versionamento da API
- `ErrorMessages.java` - Mensagens de erro padronizadas
- `AppConstants.java` - Configurações da aplicação (roles, paginação, etc.)

### 📁 `controller`
Endpoints da API REST (camada de apresentação).
- `UserController.java` - Endpoints relacionados a usuários
- `ProductController.java` - APIs para gerenciamento de produtos
- `AuthController.java` - APIs de autenticação

### 📁 `dto` (Data Transfer Objects)
Objetos utilizados para requisições/respostas da API (diferentes das entidades).
- `requests/` - Estruturas de payload para requisições
    - `LoginRequest.java`
    - `CreateUserRequest.java`
- `responses/` - Estruturas de resposta da API
    - `UserResponse.java`
    - `ApiResponse.java` - Wrapper padrão para respostas da API

### 📁 `model`
Entidades do banco de dados (classes JPA/Hibernate).
- `User.java` - Entidade de usuário
- `Product.java` - Entidade de produto
- `AuditModel.java` - Entidade base com campos createdAt/updatedAt

### 📁 `repository`
Camada de acesso ao banco de dados (interfaces do Spring Data JPA).
- `UserRepository.java` - Operações CRUD de usuários
- `ProductRepository.java` - Consultas de produtos
- Consultas personalizadas utilizando `@Query`

### 📁 `service`
Camada de lógica de negócio.
- `ClientService.java` - Serviço que lida com a lógica de cliente

### 📁 `util`
Classes utilitárias e helpers.
- `DateTimeUtils.java` - Helpers para formatação de datas
- `JwtUtils.java` - Operações com tokens JWT
- `ValidationUtils.java` - Validações customizadas

### Arquivos adicionais
- `Application.java` - Classe principal do Spring Boot
- `application.properties` - Arquivo de configuração da aplicação
- `pom.xml`/`build.gradle` - Gerenciamento de dependências

## Estilo de Código
Spotless é utilizado para manter um padrão no estilo de código ao longo do projeto. Para rodar, basta executar `mvn spotless:apply`.

## Padrões de Código
Seguem-se boas práticas para manter consistência, legibilidade e manutenibilidade em todo o código-fonte.

### DTO de entrada e saída
Utilizar DTO de request e response.

### Classe de Strings
A classe `SystemMessages.java` possui declarações de Strings para serem utilizadas como mensagens:
```java
public class SystemMessages {
    public static final String EMPTY_CPF = "CPF cannot be empty";
    ...
}
```
E para utilizá-los, apenas é necessário importá-las, unificando assim as constantes gerais:
```java
import static com.template.demo.constants.SystemMessages.*;
...
public class ClientRequest {
    ...
    @NotBlank(message = EMPTY_CPF)
    @Pattern(regexp = "\\d{11}", message = INVALID_CPF)
    private String cpf;
    ...
}

```
### Services disparam exceções
Services devem tratar as exceções de lógica e retornar `ResponseEntity` quando for necessário retornar um erro:
```java
public ResponseEntity<ClientResponse> createClient(ClientRequest request) {
        if (clientRepository.findByCpfOrEmail(request.getCpf(), request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CLIENT_ALREADY_EXISTS);
        }
        // Code
        return new ResponseEntity<>(
                new ClientResponse(request.getCpf(), request.getName(), request.getEmail()),
                HttpStatus.CREATED);
    }
```
Caso seja uma exceção não prevista pelo service, deve-se ser tratada então no `ExceptionHandler`.

### Exception Handler
Um **Exception Handler** é uma classe que lida com exceções dentro dos fluxos do Spring as quais a forem definidas tratamento.

Por exemplo, a classe `ClienteRequest.java` implementa uma requisição de cliente:
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequest {
    @NotBlank(message = EMPTY_NAME)
    private String name;

    @NotBlank(message = EMPTY_CPF)
    @Pattern(regexp = "\\d{11}", message = INVALID_CPF)
    private String cpf;

    @Email(message = INVALID_EMAIL)
    private String email;
}
```
Percebe-se que os campos possuem validações. No campo CPF possuímos duas validações: o campo não pode ser vazio, e tem que ser composto de 11 dígitos.
Estas validações serão feitas em cima do _body_ de uma instanciação desta classe, o que ocorre ao receber uma requisição no endpoint de criação de usuário.

Quando uma validação falha, por exemplo um CPF vazio, uma exceção do tipo `MethodArgumentNotValidException.java` é disparada.
Para tratar esta validação e informar o problema ao usuário para que o mesmo possa saber o que houve de errado, a classe `CustomExceptionHandler.java` implementa um tratamento customizado:
```java
@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}
```
O efeito deste tratamento é que para cada campo problemático, uma resposta customizada será exibida.
Para exemplificar, vamos realizar uma chamada de criação passando um CPF com letras e um email inválido:
```json
{
    "email":"joao.com",
    "cpf":"12345678asd",
    "name":"Joao"
}
```
A resposta resultante é um 400 - Bad Request e a seguinte resposta:
```json
{
    "cpf": "CPF must be 11 numeric digits",
    "email": "Email must be valid"
}
```
Agora podemos corrigir os erros informados e então cadastrar o novo usuário:
```json
{
    "email":"joao@gmail.com",
    "cpf":"12345678910",
    "name":"Joao"
}
```
E recebemos um 201 - Created.
