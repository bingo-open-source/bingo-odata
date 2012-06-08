package odata.test.unit.producer.inmemory;

import java.util.List;

import junit.framework.Assert;

import odata.OAtomStreamEntity;
import odata.OEntityKey;
import odata.edm.EdmEntitySet;
import odata.edm.EdmProperty;
import odata.expression.BoolCommonExpression;
import odata.expression.Expression;
import odata.producer.CountResponse;
import odata.producer.EntitiesResponse;
import odata.producer.EntityQueryInfo;
import odata.producer.InlineCount;
import odata.producer.QueryInfo;
import odata.producer.inmemory.InMemoryProducer;
import odata.producer.resources.OptionsQueryParser;
import odata.zinternal.Funcs;

import org.junit.Test;

import bingo.lang.Func;
import bingo.lang.Func1;
import bingo.lang.enumerable.EnumerableImpl;

@SuppressWarnings("unused")
public class InMemoryProducerTest {
    private final QueryInfo       NULL_QUERY        = new QueryInfo(InlineCount.ALLPAGES, null, null, null, null, null, null, null, null);
    private final EntityQueryInfo NULL_ENTITY_QUERY = new EntityQueryInfo(null, null, null, null);

    @Test
    public void inlineCountWithOneShotIterable() {
        InMemoryProducer producer = new InMemoryProducer("inlineCountWithOneShotIterable");
        final List<String> testData = EnumerableImpl.of("one", "two", "three").toList();
        Func<Iterable<String>> getTestData = new Func<Iterable<String>>() {
            
            public Iterable<String> apply() {
                // worst case - a one shot iterable
                return EnumerableImpl.of(Funcs.constant(testData.iterator()));
            }
        };
        producer.register(String.class, String.class, "TestData", getTestData, Funcs.identity(String.class));

        EntitiesResponse response = producer.getEntities("TestData", null);
        Assert.assertEquals(3, response.getEntities().size());
        Assert.assertNull(response.getInlineCount());

        response = producer.getEntities("TestData", NULL_QUERY);
        Assert.assertEquals(3, response.getEntities().size());
        Assert.assertEquals(Integer.valueOf(3), response.getInlineCount());
    }

    @Test
    public void testStreamEntity() {
        final InMemoryProducer p = new InMemoryProducer("testStreamEntity");
        p.register(StreamEntity.class, "setName", new Func<Iterable<StreamEntity>>() {
            
            public Iterable<StreamEntity> apply() {
                return EnumerableImpl.of(new StreamEntity());
            }
        }, "Id");
        p.register(String.class, String.class, "ss", new Func<Iterable<String>>() {
            
            public Iterable<String> apply() {
                return EnumerableImpl.of("aaa");
            }
        }, Funcs.identity(String.class));

        final EdmEntitySet setName = p.getMetadata().findEdmEntitySet("setName");
        Assert.assertNotNull(setName);
        Assert.assertTrue(setName.getType().getHasStream());

        final EdmEntitySet ss = p.getMetadata().findEdmEntitySet("ss");
        Assert.assertNotNull(ss);
        Assert.assertFalse(ss.getType().getHasStream());
    }

    @Test
    public void testSetNameAndType() {
        final SimpleEntity e1 = new SimpleEntity();
        String namespace = "testSetNameAndType";
        InMemoryProducer p = new InMemoryProducer(namespace);
        p.register(SimpleEntity.class, "setName", "typeName", new Func<Iterable<SimpleEntity>>() {
            
            public Iterable<SimpleEntity> apply() {
                return EnumerableImpl.of(e1, new SimpleEntity());
            }
        }, "Id");

        Assert.assertEquals(2, p.getEntities("setName", NULL_QUERY).getEntities().size());
        Assert.assertNotNull(p.getEntity("setName", OEntityKey.create(e1.getId()), NULL_ENTITY_QUERY).getEntity());

        Assert.assertNotNull(p.getMetadata().findEdmEntitySet("setName"));
        Assert.assertNotNull(p.getMetadata().findEdmEntityType(namespace + ".typeName"));
    }

    @Test
    public void complexQuery() {
        class Entry {
            public String getFoo() {
                return "322COMMON333";
            }

            public int getId() {
                return System.identityHashCode(this);
            }
        }

        InMemoryProducer producer = new InMemoryProducer("complexQuery");
        final List<Entry> testData = EnumerableImpl.of(new Entry(), new Entry()).toList();
        Func<Iterable<Entry>> getTestData = new Func<Iterable<Entry>>() {
            
            public Iterable<Entry> apply() {
                // worst case - a one shot iterable
                return EnumerableImpl.of(Funcs.constant(testData.iterator()));
            }
        };
        producer.register(Entry.class, Integer.class, "TestData", getTestData, new Func1<Entry, Integer>() {
            
            public Integer apply(Entry entry) {
                return entry.getId();
            }
        });

        QueryInfo qi = new QueryInfo(InlineCount.ALLPAGES, null, null, OptionsQueryParser.parseFilter("(Foo ne null) and substringof('common',tolower(Foo))"), null, null, null, null, null);
        EntitiesResponse data = producer.getEntities("TestData", qi);
        Assert.assertEquals(data.getEntities().size(), 2);
    }

    @Test
    public void testSimpleCount() {
        InMemoryProducer p = new InMemoryProducer("testSimpleCount");
        p.register(SimpleEntity.class, "setName", "typeName", new Func<Iterable<SimpleEntity>>() {
            
            public Iterable<SimpleEntity> apply() {
                return EnumerableImpl.of(new SimpleEntity(), new SimpleEntity());
            }
        }, "Id");

        CountResponse response = p.getEntitiesCount("setName", null);
        Assert.assertEquals(2L, response.getCount());
    }

    @Test
    public void testFilteredCount() {
        InMemoryProducer p = new InMemoryProducer("testFilteredCount");
        p.register(SimpleEntity.class, "setName", "typeName", new Func<Iterable<SimpleEntity>>() {
            
            public Iterable<SimpleEntity> apply() {
                return EnumerableImpl.of(new SimpleEntity(1), new SimpleEntity(2));
            }
        }, "Id");

        BoolCommonExpression filter = Expression.gt(Expression.simpleProperty("Integer"), Expression.integral(1));
        CountResponse response = p.getEntitiesCount("setName", new QueryInfo(InlineCount.NONE, null, null, filter, null, null, null, null, null));
        Assert.assertEquals(1L, response.getCount());
    }

    @Test
    public void testTopCount() {
        InMemoryProducer p = new InMemoryProducer("testTopCount");
        p.register(SimpleEntity.class, "setName", "typeName", new Func<Iterable<SimpleEntity>>() {
            
            public Iterable<SimpleEntity> apply() {
                return EnumerableImpl.of(new SimpleEntity(1), new SimpleEntity(2), new SimpleEntity(3), new SimpleEntity(4), new SimpleEntity(5));
            }
        }, "Id");

        CountResponse response = p.getEntitiesCount("setName", new QueryInfo(InlineCount.NONE, 3, null, null, null, null, null, null, null));
        Assert.assertEquals(3L, response.getCount());
    }

    @Test
    public void testSkipCount() {
        InMemoryProducer p = new InMemoryProducer("testSkipCount");
        p.register(SimpleEntity.class, "setName", "typeName", new Func<Iterable<SimpleEntity>>() {
            
            public Iterable<SimpleEntity> apply() {
                return EnumerableImpl.of(new SimpleEntity(1), new SimpleEntity(2), new SimpleEntity(3), new SimpleEntity(4), new SimpleEntity(5));
            }
        }, "Id");

        CountResponse response = p.getEntitiesCount("setName", new QueryInfo(InlineCount.NONE, null, 3, null, null, null, null, null, null));
        Assert.assertEquals(2L, response.getCount());
    }

    @Test
    public void testMetadataContainerName() {
        InMemoryProducer p = new InMemoryProducer("testMetadataContainerName", "Foo", 20, null, null);
        String name = p.getMetadata().getSchemas().iterator().next().getEntityContainers().iterator().next().getName();
        Assert.assertEquals("Foo", name);
    }

    @Test
    public void testKeysAreNotNull() {
        InMemoryProducer p = new InMemoryProducer("testKeysAreNotNull");
        p.register(SimpleEntity.class, "QQ", new Func<Iterable<SimpleEntity>>() {
            
            public Iterable<SimpleEntity> apply() {
                return EnumerableImpl.of(new SimpleEntity());
            }
        }, "Id", "String");

        EnumerableImpl<EdmProperty> properties = EnumerableImpl.of(p.getMetadata().getEdmEntitySet("QQ").getType().getProperties());
        boolean found = false;
        for (EdmProperty property : properties) {
            if (property.getName().equals("Id") || property.getName().equals("String")) {
                Assert.assertFalse(property.isNullable());
                found = true;
            }
        }
        Assert.assertTrue("There should be keys", found);
    }

    private static class SimpleEntity {
        private final int integer;

        public SimpleEntity() {
            this(0);
        }

        public SimpleEntity(int integer) {
            this.integer = integer;
        }

        public String getId() {
            return String.valueOf(System.identityHashCode(this));
        }

        public String getString() {
            return "string-" + getId();
        }

        public boolean getBool() {
            return false;
        }

        public int getInteger() {
            return integer;
        }
    }

    private static class StreamEntity extends SimpleEntity implements OAtomStreamEntity {
        
        public String getAtomEntityType() {
            return "application/zip";
        }

        
        public String getAtomEntitySource() {
            return "somewhere";
        }
    }

}
