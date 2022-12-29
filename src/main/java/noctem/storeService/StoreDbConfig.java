package noctem.storeService;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "storeEntityManagerFactory",
        transactionManagerRef = "storeTransactionManager",
        basePackages = "noctem.storeService.store.domain.repository"
)
public class StoreDbConfig {
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-store")
    public DataSourceProperties storeDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-store.jpa")
    public JpaProperties storeJpaProperties() {
        return new JpaProperties();
    }

    @Primary
    @Bean
    public DataSource storeDataSource(@Qualifier("storeDataSourceProperties") DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean storeEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                            @Qualifier("storeDataSource") DataSource dataSource,
                                                                            @Qualifier("storeJpaProperties") JpaProperties jpaProperties) {
        return builder.dataSource(dataSource)
                .packages("noctem.storeService.store.domain.entity")
                .persistenceUnit("storeEntityManager")
                .properties(jpaProperties.getProperties())
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager storeTransactionManager(@Qualifier("storeEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
