package lux.advanced.app.trace;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloTraceV2Test {

    @Test
    void begin_sync() {
        var trace = new HelloTraceV2();
        TraceStatus status1 = trace.begin("Hello");
        TraceStatus status2 = trace.beginSync(status1.getTraceId(), "Hello2");
        trace.end(status2);
        trace.end(status1);
    }


    @Test
    void begin_ex() {
        var trace = new HelloTraceV2();
        TraceStatus status1 = trace.begin("ex");
        TraceStatus status2 = trace.beginSync(status1.getTraceId(), "ex2");
        trace.exception(status2, new IllegalStateException("ex2"));
        trace.exception(status1, new IllegalStateException("ex1"));
    }
}