package noctem.storeService.store.service;

import noctem.storeService.store.dto.response.*;

import java.util.List;

public interface OrderService {
    List<OrderRequestResDto> getNotConfirmOrders();

    List<OrderRequestResDto> getMakingOrders();

    List<OrderRequestResDto> getCompletedOrders();

    OrderStatusResDto getOrderStatus(Long purchaseId);

    Boolean progressToMakingOrderStatus(Long purchaseId);

    Boolean progressToCompletedOrderStatus(Long purchaseId);

    Boolean cancelOrderByUser(Long purchaseId);

    Boolean cancelOrderByStore(Long purchaseId);

    WaitingTimeStoreResDto getStoreWaitingTime(Long storeId);

    WaitingTimeUserResDto getUserWaitingTime();

    OrderProgressResDto getOrderInProgress();

    List<OrderMenuInProgressResDto> getOrderMenuInProgress();

    void orderBatchProcessing();
}
