package com.moustaka.katerina.backend_application_moustaka.Repositories;

import com.moustaka.katerina.backend_application_moustaka.Models.Book;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.RequestParameters;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.ArrayList;
import java.util.List;

public class BookRepository {
  private MySQLPool client;

  public BookRepository(MySQLPool client) {
    this.client = client;
  }

  // Handlers

  public Handler<RoutingContext> listBooksHandler() {
    return routingContext -> {

      List<Book> books = new ArrayList<>();
      client.query("SELECT * FROM book").execute(ar -> {
        if (ar.succeeded()) {
          RowSet<Row> rows = ar.result();
          for (Row row : rows) {
            books.add(new Book(row.getString("title"), row.getString("author"), row.getString("isbn")));
          }

        }
        routingContext.response().setStatusCode(200)
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(new JsonArray(books).encode()
          );

      });


    };
  }

  public Handler<RoutingContext> addBookHandler() {
    return routingContext -> {
      JsonObject book = routingContext.getBodyAsJson();
      client.preparedQuery("INSERT INTO book (title,author,isbn) VALUES (?,?,?)")
        .execute(Tuple.of(book.getString("title"), book.getString("author"), book.getString("isbn")),
          ar -> {
            if (ar.succeeded()) {
              routingContext.response().setStatusCode(200)
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .end();
            }
          });
    };
  }

  public Handler<RoutingContext> searchBookByISBN() {
    return routingContext -> {
      RequestParameters parameters = routingContext.get("parsedParameters");
      String isbnNo = parameters.pathParameter("isbnNo").getString();
      List<Book> book = new ArrayList<>();
      client.preparedQuery("SELECT * FROM book WHERE isbn=?").execute(Tuple.of(isbnNo), ar -> {
        if (ar.succeeded()) {
          RowSet<Row> rows = ar.result();
          for (Row row : rows) {
            book.add(new Book(row.getString("title"), row.getString("author"), row.getString("isbn")));
          }
        }
        routingContext.response().setStatusCode(200)
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(new JsonArray(book).encode()
          );

      });


    };
  }

}
