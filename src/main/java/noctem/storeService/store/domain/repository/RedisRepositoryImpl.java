package noctem.storeService.store.domain.repository;

import lombok.RequiredArgsConstructor;
import noctem.storeService.global.enumeration.OrderStatus;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/***
 * waiting time 단위: second
 */
@Repository
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {
    private final String WAITING_TIME_KEY_PREFIX = "waitingTime";
    private final String ORDER_STATUS_KEY_PREFIX = "orderStatus";
    private final String ORDER_REQUEST_TIME_KEY_PREFIX = "orderRequestTime";
    private final String ORDER_IN_PROGRESS_KEY_PREFIX = "orderInProgress";
    private final RedisTemplate<String, Long> redisLongTemplate;
    private final RedisTemplate<String, String> redisStringTemplate;

    public String getOrderStatus(Long purchaseId) {
        String key = String.format("%s:%d", ORDER_STATUS_KEY_PREFIX, purchaseId);
        return redisStringTemplate.opsForValue().get(key);
    }

    @Override
    public void setOrderStatus(Long purchaseId, OrderStatus orderStatus) {
        String key = String.format("%s:%d", ORDER_STATUS_KEY_PREFIX, purchaseId);
        redisStringTemplate.opsForValue().set(key, orderStatus.getValue());
    }

    public String getSetOrderStatus(Long purchaseId, OrderStatus orderStatus) {
        String key = String.format("%s:%d", ORDER_STATUS_KEY_PREFIX, purchaseId);
        return redisStringTemplate.opsForValue().getAndSet(key, orderStatus.getValue()); // getSet deprecated됨
    }

    // redis 최초 주문요청된 시간 저장
    @Override
    public void setOrderRequestTime(Long purchaseId, LocalDateTime dateTime) {
        String key = String.format("%s:%d", ORDER_REQUEST_TIME_KEY_PREFIX, purchaseId);
        String time = dateTime.toString();
        redisStringTemplate.opsForValue().set(key, time);
    }

    @Override
    public String getOrderRequestTime(Long purchaseId) {
        String key = String.format("%s:%d", ORDER_REQUEST_TIME_KEY_PREFIX, purchaseId);
        return redisStringTemplate.opsForValue().get(key);
    }

    @Override
    public Long getPurchaseIdOrderInProgress(Long userAccountId) {
        String key = String.format("%s:%d", ORDER_IN_PROGRESS_KEY_PREFIX, userAccountId);
        return redisLongTemplate.opsForValue().get(key);
    }

    @Override
    public void delOrderInProgress(Long userAccountId) {
        String key = String.format("%s:%d", ORDER_IN_PROGRESS_KEY_PREFIX, userAccountId);
        redisLongTemplate.opsForValue().getAndDelete(key);
    }
}
