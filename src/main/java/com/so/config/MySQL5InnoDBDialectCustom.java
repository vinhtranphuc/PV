package com.so.config;

import org.hibernate.dialect.MySQL5InnoDBDialect;

@SuppressWarnings("deprecation")
public class MySQL5InnoDBDialectCustom extends MySQL5InnoDBDialect {
    @Override
    public String getTableTypeString() {
        return "ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";
    }
}
