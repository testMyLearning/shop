package com.oz.product.entity;

import jakarta.persistence.*;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@Table(name = "revinfo") // Hibernate сам её создаст или обновит
@RevisionEntity
public class AuditRevisionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "revision_seq")
    @SequenceGenerator(name = "revision_seq", sequenceName = "revinfo_seq", allocationSize = 1)
    @RevisionNumber
    private int id;

    @RevisionTimestamp
    private long timestamp;

    // Геттеры и сеттеры обязательно
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}