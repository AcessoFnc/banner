
### Administracao do Middleware

Funcoes de request de middleware estao no namespace `banner.middleware`.
O request de logging helper called `log-request` esta definido nesse namespace.

Este namespace tambem define os vetores `development-middleware` e `production-middleware` para manter a organizacao do namespace.
Qualquer middleware para se executar em desenvolvimento como `log-request` deve seradicionado ao vetor `dev...`.

### Seguem alguns links para consulta:

1. [HTML templating](http://www.luminusweb.net/docs/html_templating.md)
2. [Accessing the database](http://www.luminusweb.net/docs/database.md)
3. [Serving static resources](http://www.luminusweb.net/docs/static_resources.md)
4. [Setting response types](http://www.luminusweb.net/docs/responses.md)
5. [Defining routes](http://www.luminusweb.net/docs/routes.md)
6. [Adding middleware](http://www.luminusweb.net/docs/middleware.md)
7. [Sessions and cookies](http://www.luminusweb.net/docs/sessions_cookies.md)
8. [Security](http://www.luminusweb.net/docs/security.md)
9. [Deploying the application](http://www.luminusweb.net/docs/deployment.md)
