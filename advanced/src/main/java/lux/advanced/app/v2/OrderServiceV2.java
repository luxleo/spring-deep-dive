package lux.advanced.app.v2;

import lombok.RequiredArgsConstructor;
import lux.advanced.app.trace.HelloTraceV1;
import lux.advanced.app.trace.HelloTraceV2;
import lux.advanced.app.trace.TraceId;
import lux.advanced.app.trace.TraceStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceV2 {
    private final OrderRepositoryV2 orderRepository;
    private final HelloTraceV2 trace;
    public void orderItem(TraceId traceId, String itemId) {
        TraceStatus status = null;
        try {
            status = trace.beginSync(traceId, "OrderService.orderItem()");
            orderRepository.save(status.getTraceId(), itemId);
            trace.end(status);
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
