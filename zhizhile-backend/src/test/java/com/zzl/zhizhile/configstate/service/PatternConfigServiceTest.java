package com.zzl.zhizhile.configstate.service;

import com.zzl.zhizhile.configstate.model.dto.SavePatternConfigRequest;
import com.zzl.zhizhile.configstate.model.entity.PatternConfigEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PatternConfigServiceTest {

    @Autowired
    private PatternConfigService service;

    @Test
    void shouldSaveFirstTime() {
        SavePatternConfigRequest req = new SavePatternConfigRequest();
        req.setCurrentPage(3);
        req.setMaskTopOffset(10);
        req.setMaskHeight(200);

        PatternConfigEntity entity = service.save(1L, 1L, req);

        assertNotNull(entity.getId());
        assertEquals(3, entity.getCurrentPage());
    }

    @Test
    void shouldReturnDefaultValue() {
        PatternConfigEntity entity = service.get(11L, 22L);

        assertEquals(1, entity.getCurrentPage());
        assertEquals(0, entity.getMaskTopOffset());
    }

    @Test
    void shouldIsolateByPattern() {
        SavePatternConfigRequest a = new SavePatternConfigRequest();
        a.setCurrentPage(2);
        service.save(100L, 1000L, a);

        SavePatternConfigRequest b = new SavePatternConfigRequest();
        b.setCurrentPage(8);
        service.save(100L, 1001L, b);

        assertEquals(2, service.get(100L, 1000L).getCurrentPage());
        assertEquals(8, service.get(100L, 1001L).getCurrentPage());
    }
}
