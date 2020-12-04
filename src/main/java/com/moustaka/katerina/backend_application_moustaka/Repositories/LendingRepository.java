package com.moustaka.katerina.backend_application_moustaka.Repositories;

import com.moustaka.katerina.backend_application_moustaka.Models.Rentals;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.ArrayList;
import java.util.List;

public class LendingRepository {
  private MySQLPool client;

  public LendingRepository(MySQLPool client) {
    this.client = client;
  }

  public Handler<RoutingContext> listRentalsHandler() {
    return routingContext -> {

      List<Rentals> rentals = new ArrayList<>();
      client.query("SELECT * FROM rentals INNER JOIN book ON rentals.bookID = book.isbn INNER JOIN people ON rentals.peopleId = people.id").execute(ar -> {
        if (ar.succeeded()) {
          RowSet<Row> rows = ar.result();
          for (Row row : rows) {
            rentals.add(new Rentals(row.getString("title"), row.getString("author"), row.getString("name"), row.getString("lastName"), row.getString("bookId"), row.getString("peopleId")));
          }
        }
        routingContext.response().setStatusCode(200)
          .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
          .end(new JsonArray(rentals).encode()
          );
      });
    };
  }

  public Handler<RoutingContext> addRentalHandler() {
    return routingContext -> {
      JsonObject rental = routingContext.getBodyAsJson();
      client.preparedQuery("INSERT INTO rentals (bookId,peopleId) VALUES (?,?)")
        .execute(Tuple.of(rental.getString("bookId"), rental.getString("peopleId")),
          ar -> {
            if (ar.succeeded()) {
              routingContext.response().setStatusCode(200)
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .end();
            }
          });
    };
  }
}
