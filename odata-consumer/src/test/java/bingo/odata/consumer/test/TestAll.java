package bingo.odata.consumer.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import bingo.odata.consumer.ODataConsumerImplTest;
import bingo.odata.consumer.requests.behaviors.BehaviorTest;

@RunWith(Suite.class)
@SuiteClasses({ODataConsumerImplTest.class, BehaviorTest.class})
public class TestAll extends TestWithServerRunning{

}
