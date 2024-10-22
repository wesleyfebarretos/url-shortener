package com.spring.app.urlshorter.repository;

import com.spring.app.urlshorter.entity.UrlEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomUrlRepositoryImpl implements CustomUrlRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UrlEntity> updateAndFindByCode(String shortcode) {
        try {
            String sql = """
                        UPDATE url 
                        SET 
                            access_qty = access_qty + 1 
                        WHERE 
                            short_code = :shortcode 
                        AND
                            expiration_at > now()
                        RETURNING *
                    """;

            Query query = entityManager.createNativeQuery(sql, UrlEntity.class);
            query.setParameter("shortcode", shortcode);

            return Optional.ofNullable((UrlEntity) query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
