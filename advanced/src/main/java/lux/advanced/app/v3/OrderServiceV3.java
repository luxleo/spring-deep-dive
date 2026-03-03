package lux.advanced.app.v3;

import lombok.RequiredArgsConstructor;
import lux.advanced.app.trace.HelloTraceV2;
import lux.advanced.app.trace.TraceId;
import lux.advanced.app.trace.TraceStatus;
import lux.advanced.app.trace.logtrace.LogTrace;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceV3 {
    private final OrderRepositoryV3 orderRepository;
    private final LogTrace trace;
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderService.orderItem()");
            orderRepository.save(itemId);
            trace.end(status);
        } catch (Exception e) {

            trace.exception(status, e);
            throw e;
        }
    }
}
