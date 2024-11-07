package com.spring.app.urlshorter.repository;

import com.spring.app.urlshorter.entity.UrlEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomUrlRepositoryImpl implements CustomUrlRepository {
    private static final String query = """
                UPDATE url 
                SET 
                    access_qty = access_qty + 1 
                WHERE 
                    short_code = :shortcode 
                AND
                    expiration_at > now()
                RETURNING *
            """;

    private final NamedParameterJdbcTemplate jdbc;
    //  A different way to pass params as Array and not by hash map named params
    //    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<UrlEntity> updateAndFindByCode(String shortcode) {
        try {
            Map<String, Object> params = new HashMap<>();

            params.put("shortcode", shortcode);

            UrlEntity url = jdbc.query(query, params, (row) -> {
                if (row.next()) {
                    return UrlEntity.builder()
                            .id(row.getLong("id"))
                            .shortCode(row.getString("short_code"))
                            .originalAddress(row.getString("original_address"))
                            .accessQty(row.getInt("access_qty"))
                            .expirationAt(row.getTimestamp("expiration_at").toLocalDateTime().atZone(ZoneId.systemDefault()))
                            .updatedAt(row.getTimestamp("updated_at").toLocalDateTime().atZone(ZoneId.systemDefault()))
                            .createdAt(row.getTimestamp("created_at").toLocalDateTime().atZone(ZoneId.systemDefault()))
                            .build();
                }

                return null;
            });

            return Optional.ofNullable(url);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
