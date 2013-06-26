package bingo.odata.consumer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import bingo.odata.consumer.base.ServerTestBase;
import bingo.odata.consumer.requests.behaviors.BehaviorTest;

@RunWith(Suite.class)
@SuiteClasses({ODataConsumerImplTest.class, BehaviorTest.class})
public class TestAll extends ServerTestBase{

}
