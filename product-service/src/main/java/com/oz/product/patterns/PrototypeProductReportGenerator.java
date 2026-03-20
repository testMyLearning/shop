package com.oz.product.patterns;

import com.oz.product.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PrototypeProductReportGenerator {
    private List<Product> products;
    private StringBuilder reportContent = new StringBuilder();

    public void setProducts(List<Product> products) {
        this.products = products;
    }
    public byte[] generateCsvReport(){
        log.info("Начало генерации отчета в объекте: {}", this.hashCode());

        // Формируем "шапку"
        reportContent.append("ID;Name;Price;Count\n");

        // Наполняем данными
        for (Product p : products) {
            reportContent.append(p.getId()).append(";")
                    .append(p.getName()).append(";")
                    .append(p.getPrice()).append(";")
                    .append(p.getCount()).append("\n");
        }

        return reportContent.toString().getBytes();
    }
}
