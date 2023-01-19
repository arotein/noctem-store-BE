package noctem.storeService;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        entityManagerFactoryRef = "purchaseEntityManagerFactory",
        transactionManagerRef = "purchaseTransactionManager",
        basePackages = "noctem.storeService.purchase.domain.repository"
)
public class PurchaseDbConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-purchase")
    public DataSourceProperties purchaseDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-purchase.jpa")
    public JpaProperties purchaseJpaProperties() {
        return new JpaProperties();
    }

    @Bean
    public DataSource purchaseDataSource() {
        return purchaseDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Qualifier("purchaseEntityManagerFactory")
    @Bean
    public LocalContainerEntityManagerFactoryBean purchaseEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(purchaseDataSource())
                .packages("noctem.storeService.purchase.domain.entity")
                .persistenceUnit("purchaseEntityManager")
                .properties(purchaseJpaProperties().getProperties())
                .build();
    }

    @Bean
    public PlatformTransactionManager purchaseTransactionManager(@Qualifier("purchaseEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
