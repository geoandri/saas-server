package org.geoandri.saas.exception;

public class CompanyNotFoundException extends RuntimeException {
  public CompanyNotFoundException(String message) {
    super(message);
  }
}
