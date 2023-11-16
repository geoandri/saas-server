package org.geoandri.saas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "companies")
public class Company {

  @Id private UUID id;

  private String name;

  @OneToMany(mappedBy = "company")
  private Set<User> users;

  public Company(UUID id, String name) {
    this.id = id;
    this.name = name;
  }

  public Company() {}

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Company company = (Company) o;
    return Objects.equals(id, company.id) && Objects.equals(name, company.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
