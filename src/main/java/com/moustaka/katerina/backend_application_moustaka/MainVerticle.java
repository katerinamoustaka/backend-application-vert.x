package com.moustaka.katerina.backend_application_moustaka;

import com.moustaka.katerina.backend_application_moustaka.Repositories.BookRepository;
import com.moustaka.katerina.backend_application_moustaka.Repositories.LendingRepository;
import com.moustaka.katerina.backend_application_moustaka.Repositories.PeopleRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.openapi.RouterBuilder;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;

public class MainVerticle extends AbstractVerticle {

  private HttpServer server;
  // DB connection
  private MySQLConnectOptions connectOptions = new MySQLConnectOptions().setPort(3306).setHost("127.0.0.1").setDatabase("library").setUser("root").setPassword("0000");

  private PoolOptions poolOptions = new PoolOptions();

  //Create the client
  private MySQLPool client = MySQLPool.pool(Vertx.vertx(), connectOptions, poolOptions);
  private BookRepository bookRepository = new BookRepository(client);
  private PeopleRepository peopleRepository = new PeopleRepository(client);
  private LendingRepository lendingRepository = new LendingRepository(client);


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    RouterBuilder.create(vertx, "library.yaml")
      .onSuccess(routerBuilder -> {

        // Add handlers
        routerBuilder.operation("listBooks").handler(bookRepository.listBooksHandler());
        routerBuilder.operation("addBook").handler(bookRepository.addBookHandler());
        routerBuilder.operation("getBookByISBN").handler(bookRepository.searchBookByISBN());
        routerBuilder.operation("listPeople").handler(peopleRepository.listPeopleHandler());
        routerBuilder.operation("addPeople").handler(peopleRepository.addPeopleHandler());
        routerBuilder.operation("getPeopleById").handler(peopleRepository.searchPeopleById());
        routerBuilder.operation("listRentals").handler(lendingRepository.listRentalsHandler());
        routerBuilder.operation("addRental").handler(lendingRepository.addRentalHandler());

        // Generate router - start

        Router router = routerBuilder.createRouter();
        router.errorHandler(404, routingContext -> {
          JsonObject errorObj = new JsonObject().put("code", 404).put("message",
            (routingContext.failure() != null) ?
              routingContext.failure().getMessage() : "Not Found");

          routingContext.response().setStatusCode(404)
            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .end(errorObj.encode());

        });

        router.errorHandler(400, routingContext -> {
          JsonObject errorObject = new JsonObject().put("code", 400).put("message",
            (routingContext.failure().getMessage() != null) ?
              routingContext.failure().getMessage() : "Bad Request");

          routingContext.response().setStatusCode(400)
            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .end(errorObject.encode());
        });

        server = vertx.createHttpServer(new HttpServerOptions().setPort(8080).setHost("localhost"));
        server.requestHandler(router).listen();

        // Generate router - end

        startPromise.complete();

      }).onFailure(startPromise::fail);
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    client.close();
    server.close();
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }
}
