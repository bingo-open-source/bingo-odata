package bingo.odata.consumer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import bingo.odata.consumer.base.ServerTestBase;

@RunWith(Suite.class)
@SuiteClasses({ODataConsumerImplTest.class})
public class TestSuite extends ServerTestBase{

}
