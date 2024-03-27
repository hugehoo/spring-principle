![img.png](img.png)

``` java
public class PrincipleApplication {
    public static void main(String[] args) {
        // spring container 를 만들어 보자.
        GenericApplicationContext applicationContext = new GenericApplicationContext();// 얘가 결국 스프링컨테이너가 된다.
        // 스프링 컨테이너는 오브젝트를 직접 생성하여 넣어 줄 수 도 있지만,
        // 어떤 클래스를 이용해서 빈 오브젝트를 생성할 것인가 라는 메타정보를 넣어 주는 방식으로 구성할 수 있다.
        applicationContext.registerBean(HelloController.class); // object 를 넘기는 것이 아니라 클래스 정보만 넘긴다.
        applicationContext.refresh(); // container 를 초기화

        ServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
        WebServer webServer = tomcatServletWebServerFactory.getWebServer(servletContext -> {
            servletContext.addServlet("frontController", new HttpServlet() {
                @Override
                public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
                    if (req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())) {
                        String name = req.getParameter("name");
                        HelloController helloController = applicationContext.getBean(HelloController.class);
                        String hello = helloController.hello(name);
                        // application context 는 HelloController 라는 타입의 빈이 어떻게 생성됐는지는 알지 못해도 상관없다. 그저 가져다 사용만 할 뿐이다.
                        res.setContentType(MediaType.TEXT_PLAIN_VALUE);
                        res.getWriter().println(hello);
                    } else {
                        res.setStatus(HttpStatus.NOT_FOUND.value());
                    }

                }
            }).addMapping("/*"); // "/hello" 로 들어오는 요청이 있으면 여기서 익명클래스로 만든 오브젝트가 처리하겠다는 의미.
        });
        webServer.start(); // tomcat servlet container 가 실행된다.
    }
}
```
- 굳이 spring container 를 통해 HelloController 를 만들어야 하는 이유가 뭘까? 이전 처럼 new 연산자로 HelloController 오브젝트를 생성하면 되는 것 아닐까?
- 이점 : spring container 는 어떤 타입의 오브젝트를 만들 때 딱 한번만 만든다.
- Servlet Container 외에 다른 곳에서도 HelloController 를 사용하고 싶을 때, Spring Container 는 최초 한번 만들어둔 HelloContainer 를 그대로 반환한다.
- 즉 HelloController 를 필요하는 모든 곳에서는 결국 동일한 object 를 가져다 쓰는 격이다.
- 스프링 컨테이너는 싱글톤 패턴을 사용하지 않고도 객체를 딱 한번만 만들어 재사용할 수 있게 한다. 그래서 스프링 컨테이너를 싱글톤 레지스트리 라고도 부른다.
 
### Dependency Injection
- HelloController -> SimpleHelloService : HelloController 가 SimpleHelloService 를 의존한다.
- 이말이 무슨 말이냐, SimpleHelloService 가 변경되면 HellocController 는 영향을 받는다. 이걸 의존관계가 있다고 표현한다.

![img_1.png](img_1.png)
- 코드상으로 보면 HelloController 는 HelloService Interface 에 의존하는 것 처럼 보인다.
- 런타임에는 실제로 HelloController 가 의존할 구현 클래스(SimpleHelloService or ComplexHelloService)를 지정해줘야 한다.
- 내가 어느 클래스의 오브젝트를 사용할 것인지 정해져야 한다.
- HelloController 와 SimpleHelloService 의 연관관계를 만드는 것, 이 작업을 `Dependency Injection` 이라 부른다.
- `Dependency Injection` 에는 `Assembler` 라는 제 3의 존재가 필요하다.
- HelloController 가 사용할 Service 를 new 연산자로 내부에서 직접 생성하는 것이 아닌 외부에서 주입하도록 도와주는 역할을 Assembler 가 맡는다.
- 이 Assembler 를 우리는 `Spring Container` 라 부른다.
![img_2.png](img_2.png)
- 주입을 해준다 -> SimpleHelloService 의 reference 를 넘겨준다 라는 의미.
- 인터페이스를 통해 간접적으로 의존관계를 맺고 코드상의 명시적인 의존관계는 지운다.