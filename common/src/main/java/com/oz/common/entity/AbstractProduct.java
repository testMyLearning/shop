package com.oz.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.oz.common.enums.Color;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
//@Audited
@EqualsAndHashCode
public abstract class AbstractProduct {
    protected String name;
    protected Long count;
    protected BigDecimal price;
    @Column(name = "date_of_entry", updatable = false)
    @CreationTimestamp
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    protected LocalDate dateOfEntry;
    @Enumerated(EnumType.STRING)
    protected Color color;


    @JsonSerialize(using = LocalDateTimeSerializer.class) // Не LocalDate!
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    protected AbstractProduct(){}
    protected AbstractProduct(String name, Long count, BigDecimal price, LocalDate dateOfEntry, Color c) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.dateOfEntry = dateOfEntry;
        this.color=c;
    }

}
