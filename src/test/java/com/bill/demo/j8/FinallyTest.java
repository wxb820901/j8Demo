package com.bill.demo.j8;

import org.junit.Test;
import org.testng.Assert;

public class FinallyTest {

    public int testMethodWIthFianlly(int param){
        if( param <= 3) {

            try {
                if(param == 1){
                    System.exit(1);
                }
                return 1;
            } catch (Exception w) {
                if(param == 2){
                    System.exit(2);
                }
                return 2;
            } finally {
                return 3;
            }
        }else{
            return 4;
        }
    }
    @Test
    public void testFinally(){
//        testMethodWIthFianlly(1); ==> Process finished with exit code 1
//        testMethodWIthFianlly(2); ==> Process finished with exit code 2
        Assert.assertEquals(3, testMethodWIthFianlly(3));//finally impact return in try/catch
        Assert.assertEquals(4, testMethodWIthFianlly(4));//not finally if not join try
    }
}
