package com.zzl.zhizhile.progress.service;

import com.zzl.zhizhile.progress.model.dto.SaveRowProgressRequest;
import com.zzl.zhizhile.progress.model.dto.SaveTimeProgressRequest;
import com.zzl.zhizhile.progress.model.entity.ProjectProgressEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ProjectProgressServiceTest {

    @Autowired
    private ProjectProgressService service;

    @Test
    void shouldOverwriteSaveRow() {
        SaveRowProgressRequest req1 = new SaveRowProgressRequest();
        req1.setCurrentRowIndex(1);
        req1.setTotalCount(10);
        service.saveRowProgress(1L, req1);

        SaveRowProgressRequest req2 = new SaveRowProgressRequest();
        req2.setCurrentRowIndex(2);
        req2.setTotalCount(20);
        ProjectProgressEntity entity = service.saveRowProgress(1L, req2);

        assertEquals(2, entity.getCurrentRowIndex());
        assertEquals(20, entity.getTotalCount());
    }

    @Test
    void shouldOverwriteSaveTime() {
        SaveTimeProgressRequest req = new SaveTimeProgressRequest();
        req.setTotalSeconds(120L);
        service.saveTimeProgress(2L, req);

        req.setTotalSeconds(240L);
        ProjectProgressEntity entity = service.saveTimeProgress(2L, req);

        assertEquals(240L, entity.getTotalSeconds());
    }

    @Test
    void shouldGetProgress() {
        SaveRowProgressRequest row = new SaveRowProgressRequest();
        row.setCurrentRowIndex(4);
        row.setTotalCount(40);
        service.saveRowProgress(3L, row);

        SaveTimeProgressRequest time = new SaveTimeProgressRequest();
        time.setTotalSeconds(400L);
        service.saveTimeProgress(3L, time);

        ProjectProgressEntity entity = service.get(3L);
        assertEquals(4, entity.getCurrentRowIndex());
        assertEquals(40, entity.getTotalCount());
        assertEquals(400L, entity.getTotalSeconds());
    }
}
