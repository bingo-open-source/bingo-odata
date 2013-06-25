package bingo.odata.consumer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import bingo.odata.consumer.base.ServerTestBase;
import bingo.odata.consumer.requests.behaviors.ClientBehaviorTest;

@RunWith(Suite.class)
@SuiteClasses({ODataConsumerImplTest.class, ClientBehaviorTest.class})
public class TestSuite extends ServerTestBase{

}
