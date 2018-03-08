package net.consensys.orion.impl.http.handler.resend;

import net.consensys.orion.api.enclave.Enclave;
import net.consensys.orion.api.storage.Storage;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * ask to resend a single transaction or all transactions. Useful in situations where a
 * constellation node has lost its database and wants to recover lost transactions.
 */
public class ResendHandler implements Handler<RoutingContext> {
  private final Enclave enclave;
  private final Storage storage;

  public ResendHandler(Enclave enclave, Storage storage) {
    this.enclave = enclave;
    this.storage = storage;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    throw new UnsupportedOperationException("This handler has not been implemented yet");
  }
}
