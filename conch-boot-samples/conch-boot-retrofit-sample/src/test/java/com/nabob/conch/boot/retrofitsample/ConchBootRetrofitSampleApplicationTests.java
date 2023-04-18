package com.nabob.conch.boot.retrofitsample;

import com.nabob.conch.boot.retrofitsample.sample.RetrofitTestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConchBootRetrofitSampleApplicationTests {

    @Autowired
    private RetrofitTestService retrofitTestService;

    @Test
    public void testSearch() {
        try {
            retrofitTestService.testSearch();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testString() {
        try {
            retrofitTestService.testString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testString2() {
        try {
            retrofitTestService.testString2();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testString4() {
        try {
            retrofitTestService.testString4();
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
