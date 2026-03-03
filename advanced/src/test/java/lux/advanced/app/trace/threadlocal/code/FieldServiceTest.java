package lux.advanced.app.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;


@Slf4j
class FieldServiceTest {
    FieldService service = new FieldService();

    @Test
    void field() {
        Runnable runnableA = () -> {
            service.logic("userA");
        };
        Runnable runnableB = () -> {
            service.logic("userB");
        };

        Thread threadA = new Thread(runnableA);
        threadA.setName("threadA");
        Thread threadB = new Thread(runnableB);
        threadB.setName("threadB");

        threadA.start();
        sleep(2000); // 동시성 문제 없음
        // sleep(100); // 동시성 문제 발생
        threadB.start();
        sleep(3000); // main 종료 대기
        log.info("main exit");
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}