package org.wmc.integrated;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DuplicateKeyException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.wmc.integrated.builder.Builder;
import org.wmc.integrated.mongo.entity.*;
import org.wmc.integrated.mongo.repository.LogRecordRepository;
import org.wmc.integrated.mongo.repository.UserRepository;
import org.wmc.integrated.util.JsonUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MongoDBTest extends BaseTest {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LogRecordRepository logRecordRepository;

    // -------------------------------MongoTemplate---------------------------------------
    @Test
    public void saveDemoTest() {
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setId(1L);
        demoEntity.setTitle("Spring Boot 中使用 MongoDB");
        demoEntity.setDescription("关注公众号，搜云库，专注于开发技术的研究与知识分享");
        demoEntity.setBy("souyunku");
        demoEntity.setUrl("http://www.souyunku.com");
        mongoTemplate.save(demoEntity);

        demoEntity = new DemoEntity();
        demoEntity.setId(2L);
        demoEntity.setTitle("Spring Boot 中使用 MongoDB");
        demoEntity.setDescription("关注公众号，搜云库，专注于开发技术的研究与知识分享");
        demoEntity.setBy("souyunku");
        demoEntity.setUrl("http://www.souyunku.com");
        mongoTemplate.save(demoEntity);
    }

    @Test
    public void removeDemoTest() {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is("60614e613e2c026217e3ef6e"));
        mongoTemplate.remove(query, DemoEntity.class);
    }

    @Test
    public void updateDemoTest() {

        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setId(1L);
        demoEntity.setTitle("Spring Boot 中使用 MongoDB 更新数据");
        demoEntity.setDescription("关注公众号，搜云库，专注于开发技术的研究与知识分享");
        demoEntity.setBy("souyunku");
        demoEntity.setUrl("http://www.souyunku.com");

        Update update = new Update();
        update.set("title", demoEntity.getTitle());
        update.set("description", demoEntity.getDescription());
        update.set("by", demoEntity.getBy());
        update.set("url", demoEntity.getUrl());
        mongoTemplate.updateFirst(new Query(Criteria.where("id").is(demoEntity.getId())), update, DemoEntity.class);
    }

    @Test
    public void findDemoByIdTest() {
        List<DemoEntity> demoEntities = mongoTemplate.find(new Query(Criteria.where("id").is(1L)), DemoEntity.class);
        System.out.println(JsonUtils.serialize(demoEntities));
    }

    // --------------------------------------MongoRepository----------------------------------------
    //插入文档中嵌入一个文档
    @Test
    public void insertMany() {
        User user = Builder.of(User::new)
                .with(User::setId, "1")
                .with(User::setUserName, "xiaobai")
                .with(User::setPassword, "123456")
                .with(User::setAge, 88)
                .with(User::setCreateTime, 888888L)
                .build();

        DemoEntity demoEntity = Builder.of(DemoEntity::new)
                .with(DemoEntity::setId, 1L)
                .with(DemoEntity::setTitle, "Spring Boot 中使用 MongoDB")
                .with(DemoEntity::setDescription, "mongo")
                .with(DemoEntity::setBy, "bybyby")
                .with(DemoEntity::setUrl, "www.www.www")
                .build();
        user.setDemoEntity(demoEntity);
        try {
            User insert = userRepository.insert(user);
            System.out.println(insert);
        } catch (DuplicateKeyException ex) {
            System.out.println("ex:" + ex.getClass());
            System.out.println("ex:" + "主键冲突");
        } catch (Exception e) {
            System.out.println("e:" + e.getClass());
            System.out.println("e:" + "主键冲突");
        }
        // e:class org.springframework.dao.DuplicateKeyException
        // e:主键冲突
    }

    //批量插入
    @Test
    public void insertList() {
        List<User> userList = IntStream.rangeClosed(11, 20).mapToObj(i -> {
            User user = Builder.of(User::new)
                    .with(User::setId, i + "")
                    .with(User::setUserName, "xiaobai")
                    .with(User::setPassword, "123456")
                    .with(User::setAge, 88)
                    .with(User::setCreateTime, 888888L)
                    .with(User::addSports, "run")
                    .with(User::addSports, "basketball")
                    .with(User::addSports, "football")
                    .build();

            DemoEntity demoEntity = Builder.of(DemoEntity::new)
                    .with(DemoEntity::setId, Long.valueOf(i))
                    .with(DemoEntity::setTitle, "Spring Boot 中使用 MongoDB")
                    .with(DemoEntity::setDescription, "mongo")
                    .with(DemoEntity::setBy, "bybyby")
                    .with(DemoEntity::setUrl, "www.www.www")
                    .build();
            user.setDemoEntity(demoEntity);
            return user;
        }).collect(Collectors.toList());
        List<User> insert = userRepository.insert(userList);
        System.out.println("insert:" + insert);

    }

    //查询全部
    @Test
    public void findALL() {
        List<User> all = userRepository.findAll();
        all.forEach(System.out::println);

    }

    //查询条数
    @Test
    public void count() {
        long count = userRepository.count();
        System.out.println(count);
    }

    //条件统计条数
    @Test
    public void countByExample() {
        User user = Builder.of(User::new)
                .with(User::setUserName, "xiaobai")
                .build();
        System.out.println("user:" + user);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnorePaths("age", "createTime");
        Example<User> userExample = Example.of(user, exampleMatcher);
        long count = userRepository.count(userExample);
        System.out.println(count);
    }

    @Test
    public void exists() {
        User user = Builder.of(User::new)
                .with(User::setUserName, "xiaobai")
                .build();
        System.out.println("user:" + user);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnorePaths("age", "createTime");
        Example<User> userExample = Example.of(user, exampleMatcher);
        boolean exists = userRepository.exists(userExample);
        System.out.println(exists);
    }

    @Test
    public void existsById() {
        String id = "1";
        boolean exists = userRepository.existsById(id);
        System.out.println(exists);
    }

    //查询喜欢运动中有跑步,和篮球的用户
    @Test
    public void findExample() {
        User user = Builder.of(User::new)
                .with(User::setUserName, "xiaobai")
                .with(User::addSports, "run")
                .with(User::addSports, "basketball")
                //.with(User::addSports, "football")
                .build();
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnorePaths("age", "createTime");
        Example example = Example.of(user, exampleMatcher);
        Optional one = userRepository.findOne(example);
        if (one.isPresent()) {
            System.out.println(one.get());
        }
    }

    //排序查询，这里查找姓名按照一个个字段逐一比较
    @Test
    public void findBySort() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<User> all = userRepository.findAll(sort);
        for (User user : all) {
            System.out.println(user);
        }
    }

    //获取表中所有的数据,分页查询 pageAble
    @Test
    public void findByPageable() {
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<User> all = userRepository.findAll(pageable);
        System.out.println(all.getTotalPages());
        System.out.println(all.getTotalElements());
        all.get().forEach(System.out::println);
    }

    //批量查询
    @Test
    public void findByList() {
        List<String> ids = new ArrayList<>();
        ids.add("1");
        ids.add("20");
        Iterable<User> allById = userRepository.findAllById(ids);
        for (User users : allById) {
            System.out.println(users);
        }
    }

    //
    //利用save更新
    @Test
    public void save() {
        User user = new User();
        user.setId("5d36d565bde542361407adfb");
        user.setUserName("shawn");
        userRepository.save(user);
    }

    //添加一个新字段，(user其他字段不能传空，否则将只有这个字段)
    @Test
    public void saveNew1() {
        User user = new User();
        user.setId("5d36d565bde542361407adfb");
        user.setUserName("shawn");
        user.setAge(22);
        userRepository.save(user);
    }

    //添加一个新字段，其他的不修改可以先查询
    @Test
    public void saveNew2() {
        String id = "5d36d565bde542361407adfb";
        Optional<User> byId = userRepository.findById(id);
        User user = byId.get();
        user.setUserName("shawn1");
        System.out.println(user);
        userRepository.save(user);
    }

    // ---------------------------------聚合查询--------------------------------------
    @Test
    public void aggregate() {
        // 统计各个年级人数
        System.out.println("------------------------------统计各个年级人数------------------------------");
        Aggregation aggregation = Aggregation.newAggregation(Aggregation.group("grade_name").count().as("count"));
        AggregationResults<AggregationResult> aggregationResults = mongoTemplate.aggregate(aggregation, "pt", AggregationResult.class);
        for (Iterator<AggregationResult> iterator = aggregationResults.iterator(); iterator.hasNext(); ) {
            AggregationResult obj = iterator.next();
            System.out.println(JsonUtils.serialize(obj));
        }
        // 统计某个年级某一项测试在某个范围的人数
        System.out.println("------------------------------统计某个年级某一项测试在某个范围的人数------------------------------");
        Aggregation aggregation4 =
                Aggregation.newAggregation(
                        Aggregation.unwind("items"),
                        Aggregation.match(Criteria.where("items.item_name").is("BM1").and("items.score").gt(40).lt(80)),
                        Aggregation.group("grade_name").count().as("BMI正常人数"));
        AggregationResults<BasicDBObject> aggregationResults1 =
                mongoTemplate.aggregate(aggregation4, "pt", BasicDBObject.class);
        for (Iterator<BasicDBObject> iterator = aggregationResults1.iterator(); iterator.hasNext(); ) {
            DBObject obj = iterator.next();
            System.out.println(JsonUtils.serialize(obj));
        }
        // 统计某个年级某一项测试不在某个范围的人数
        System.out.println("------------------------------统计某个年级某一项测试不在某个范围的人数------------------------------");
        Aggregation aggregation3 =
                Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("grade_name").is("一年级")),
                        Aggregation.unwind("items"),
                        Aggregation.match(Criteria.where("items.item_name").is("BM1").orOperator(
                                Criteria.where("items.score").lte(60),
                                Criteria.where("items.score").gte(70))),
                        Aggregation.group("grade_name").count().as("BMI不正常人数"));
        AggregationResults<BasicDBObject> outputTypeCount3 =
                mongoTemplate.aggregate(aggregation3, "pt", BasicDBObject.class);

        for (Iterator<BasicDBObject> iterator = outputTypeCount3.iterator(); iterator.hasNext(); ) {
            DBObject obj = iterator.next();
            System.out.println(JsonUtils.serialize(obj));
        }

        // 统计各个测试项目得分的最大值，最小值，平均值
        System.out.println("------------------------------统计各个测试项目得分的最大值，最小值，平均值------------------------------");
        Aggregation aggregation5 =
                Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("grade_name").is("一年级")),
                        Aggregation.unwind("items"),
                        Aggregation.group("$items.item_name").avg("$items.score").as("平均分").min("$items.score").as
                                ("最小值").max("$items.score").as("最大值"));
        AggregationResults<BasicDBObject> outputTypeCount5 =
                mongoTemplate.aggregate(aggregation5, "pt", BasicDBObject.class);

        for (Iterator<BasicDBObject> iterator = outputTypeCount5.iterator(); iterator.hasNext(); ) {
            DBObject obj = iterator.next();
            System.out.println(JsonUtils.serialize(obj));
        }
        System.out.println("----------------------使用原生语句的写法-----------------------------------");
        //展开数组
        BasicDBObject queryUnwind = new BasicDBObject("$unwind", "$items");
        //分组统计
        BasicDBObject groupObject = new BasicDBObject("_id", "$items.item_name");
        groupObject.put("min", new BasicDBObject("$min", "$items.score"));
        groupObject.put("max", new BasicDBObject("$max", "$items.score"));
        groupObject.put("avg", new BasicDBObject("$avg", "$items.score"));
        BasicDBObject queryGroup = new BasicDBObject("$group", groupObject);
        MongoCollection<Document> mongoCollection = mongoTemplate.getCollection("pt");
        AggregateIterable<Document> pt = mongoCollection.aggregate(Arrays.asList(queryUnwind, queryGroup));
        for (MongoCursor<Document> iterator = pt.iterator(); iterator.hasNext(); ) {
            System.out.println(JsonUtils.serialize(iterator.next()));
        }

        // 统计各个年级每一个项目得分的最大值，最小值，平均值
        System.out.println("------------------------------统计各个年级每一个项目得分的最大值，最小值，平均值------------------------------");
        /* 创建 $unwind 操作, 用于切分数组*/
        BasicDBObject unwind = new BasicDBObject("$unwind", "$items");
        /* Group操作*/
        BasicDBObject groupFields = new BasicDBObject("_id", new BasicDBObject("grade_name", "$grade_name").append("item_name", "$items.item_name"));
        groupFields.put("min_score", new BasicDBObject("$min", "$items.score"));
        groupFields.put("max_score", new BasicDBObject("$max", "$items.score"));
        groupFields.put("avg_score", new BasicDBObject("$avg", "$items.score"));
        BasicDBObject group = new BasicDBObject("$group", groupFields);
        AggregateIterable<Document> pt0 = mongoTemplate.getCollection("pt").aggregate(Arrays.asList(unwind, group));
        /**
         * {"_id":{"grade_name":"一年级","item_name":"BM1"},"min_score":34.41,"max_score":1668.27,"avg_score":190.9676923076923}
         * {"_id":{"grade_name":"三年级","item_name":"BM1"},"min_score":70.9,"max_score":70.9,"avg_score":70.9}
         * {"_id":{"grade_name":"四年级","item_name":"肺活量"},"min_score":1668.27,"max_score":1668.27,"avg_score":1668.27}
         * {"_id":{"grade_name":"四年级","item_name":"原地纵跳"},"min_score":34.41,"max_score":34.41,"avg_score":34.41}
         * {"_id":{"grade_name":"二年级","item_name":"肺活量"},"min_score":1668.27,"max_score":1668.27,"avg_score":1668.27}
         * {"_id":{"grade_name":"二年级","item_name":"BM1"},"min_score":70.9,"max_score":70.9,"avg_score":70.9}
         * {"_id":{"grade_name":"六年级","item_name":"原地纵跳"},"min_score":34.41,"max_score":34.41,"avg_score":34.41}
         * {"_id":{"grade_name":"四年级","item_name":"BM1"},"min_score":70.9,"max_score":70.9,"avg_score":70.9}
         * {"_id":{"grade_name":"三年级","item_name":"肺活量"},"min_score":1668.27,"max_score":1668.27,"avg_score":1668.27}
         * {"_id":{"grade_name":"一年级","item_name":"原地纵跳"},"min_score":34.41,"max_score":34.41,"avg_score":34.41}
         * {"_id":{"grade_name":"六年级","item_name":"肺活量"},"min_score":1668.27,"max_score":1668.27,"avg_score":1668.27}
         * {"_id":{"grade_name":"三年级","item_name":"原地纵跳"},"min_score":34.41,"max_score":34.41,"avg_score":34.41}
         * {"_id":{"grade_name":"一年级","item_name":"肺活量"},"min_score":1668.27,"max_score":1668.27,"avg_score":1668.27}
         * {"_id":{"grade_name":"二年级","item_name":"原地纵跳"},"min_score":34.41,"max_score":34.41,"avg_score":34.41}
         * {"_id":{"grade_name":"六年级","item_name":"BM1"},"min_score":70.9,"max_score":70.9,"avg_score":70.9}
         */
        for (MongoCursor<Document> iterator = pt0.iterator(); iterator.hasNext(); ) {
            Document obj = iterator.next();
            System.out.println(JsonUtils.serialize(obj));
        }

        /* Reshape Group Result*/
        BasicDBObject projectFields = new BasicDBObject();
        projectFields.put("grade_name", "$_id.grade_name");
        BasicDBObject subProjects = new BasicDBObject("item_name", "$_id.item_name");
        subProjects.put("min", "$min_score");
        subProjects.put("max", "$max_score");
        subProjects.put("avg", "$avg_score");
        projectFields.put("item_info", subProjects);
        BasicDBObject project = new BasicDBObject("$project", projectFields);
        /**
         * {"_id":{"grade_name":"一年级","item_name":"BM1"},"grade_name":"一年级","item_info":{"item_name":"BM1","min":34.41,"max":1668.27,"avg":190.9676923076923}}
         * {"_id":{"grade_name":"三年级","item_name":"BM1"},"grade_name":"三年级","item_info":{"item_name":"BM1","min":70.9,"max":70.9,"avg":70.9}}
         * {"_id":{"grade_name":"四年级","item_name":"肺活量"},"grade_name":"四年级","item_info":{"item_name":"肺活量","min":1668.27,"max":1668.27,"avg":1668.27}}
         * {"_id":{"grade_name":"四年级","item_name":"原地纵跳"},"grade_name":"四年级","item_info":{"item_name":"原地纵跳","min":34.41,"max":34.41,"avg":34.41}}
         * {"_id":{"grade_name":"二年级","item_name":"肺活量"},"grade_name":"二年级","item_info":{"item_name":"肺活量","min":1668.27,"max":1668.27,"avg":1668.27}}
         * {"_id":{"grade_name":"二年级","item_name":"BM1"},"grade_name":"二年级","item_info":{"item_name":"BM1","min":70.9,"max":70.9,"avg":70.9}}
         * {"_id":{"grade_name":"六年级","item_name":"原地纵跳"},"grade_name":"六年级","item_info":{"item_name":"原地纵跳","min":34.41,"max":34.41,"avg":34.41}}
         * {"_id":{"grade_name":"四年级","item_name":"BM1"},"grade_name":"四年级","item_info":{"item_name":"BM1","min":70.9,"max":70.9,"avg":70.9}}
         * {"_id":{"grade_name":"三年级","item_name":"肺活量"},"grade_name":"三年级","item_info":{"item_name":"肺活量","min":1668.27,"max":1668.27,"avg":1668.27}}
         * {"_id":{"grade_name":"一年级","item_name":"原地纵跳"},"grade_name":"一年级","item_info":{"item_name":"原地纵跳","min":34.41,"max":34.41,"avg":34.41}}
         * {"_id":{"grade_name":"六年级","item_name":"肺活量"},"grade_name":"六年级","item_info":{"item_name":"肺活量","min":1668.27,"max":1668.27,"avg":1668.27}}
         * {"_id":{"grade_name":"三年级","item_name":"原地纵跳"},"grade_name":"三年级","item_info":{"item_name":"原地纵跳","min":34.41,"max":34.41,"avg":34.41}}
         * {"_id":{"grade_name":"一年级","item_name":"肺活量"},"grade_name":"一年级","item_info":{"item_name":"肺活量","min":1668.27,"max":1668.27,"avg":1668.27}}
         * {"_id":{"grade_name":"二年级","item_name":"原地纵跳"},"grade_name":"二年级","item_info":{"item_name":"原地纵跳","min":34.41,"max":34.41,"avg":34.41}}
         * {"_id":{"grade_name":"六年级","item_name":"BM1"},"grade_name":"六年级","item_info":{"item_name":"BM1","min":70.9,"max":70.9,"avg":70.9}}
         */
        AggregateIterable<Document> pt2 = mongoTemplate.getCollection("pt").aggregate(Arrays.asList(unwind, group, project));
        for (MongoCursor<Document> iterator = pt2.iterator(); iterator.hasNext(); ) {
            Document obj = iterator.next();
            System.out.println(JsonUtils.serialize(obj));
        }

        /* 将结果push到一起*/
        BasicDBObject groupAgainFields = new BasicDBObject("_id", "$grade_name");
        groupAgainFields.put("item_info", new BasicDBObject("$push", "$item_info"));
        BasicDBObject reshapeGroup = new BasicDBObject("$group", groupAgainFields);

        /* 查看Group结果 */
        AggregateIterable<Document> pt1 = mongoTemplate.getCollection("pt").aggregate(Arrays.asList(unwind, group, project, reshapeGroup));
        /**
         * {"_id":"三年级","item_info":[{"item_name":"BM1","min":70.9,"max":70.9,"avg":70.9},{"item_name":"肺活量","min":1668.27,"max":1668.27,"avg":1668.27},{"item_name":"原地纵跳","min":34.41,"max":34.41,"avg":34.41}]}
         * {"_id":"四年级","item_info":[{"item_name":"肺活量","min":1668.27,"max":1668.27,"avg":1668.27},{"item_name":"原地纵跳","min":34.41,"max":34.41,"avg":34.41},{"item_name":"BM1","min":70.9,"max":70.9,"avg":70.9}]}
         * {"_id":"六年级","item_info":[{"item_name":"原地纵跳","min":34.41,"max":34.41,"avg":34.41},{"item_name":"肺活量","min":1668.27,"max":1668.27,"avg":1668.27},{"item_name":"BM1","min":70.9,"max":70.9,"avg":70.9}]}
         * {"_id":"一年级","item_info":[{"item_name":"BM1","min":34.41,"max":1668.27,"avg":190.9676923076923},{"item_name":"原地纵跳","min":34.41,"max":34.41,"avg":34.41},{"item_name":"肺活量","min":1668.27,"max":1668.27,"avg":1668.27}]}
         * {"_id":"二年级","item_info":[{"item_name":"肺活量","min":1668.27,"max":1668.27,"avg":1668.27},{"item_name":"BM1","min":70.9,"max":70.9,"avg":70.9},{"item_name":"原地纵跳","min":34.41,"max":34.41,"avg":34.41}]}
         */
        for (MongoCursor<Document> iterator = pt1.iterator(); iterator.hasNext(); ) {
            Document obj = iterator.next();
            System.out.println(JsonUtils.serialize(obj));
        }
    }

    @Test
    public void updatePT() {
        Query query = new Query(new Criteria().orOperator(Criteria.where("student_Id").in(Arrays.asList("2", "3")), Criteria.where("student_Id").in(Arrays.asList("8", "9"))));
        List<PT> pts = mongoTemplate.find(query, PT.class);
        pts.forEach(System.out::println);
//        Update update = new Update();
//        update.set("grade_name", "五年级");
//        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, PT.class);
//        System.out.println(updateResult.getMatchedCount());
//        System.out.println(updateResult.getModifiedCount());
//        System.out.println(updateResult.wasAcknowledged());
//        System.out.println(updateResult.getUpsertedId());
//        List<PT> pts1 = mongoTemplate.find(query, PT.class);
//        pts1.forEach(System.out::println);
    }

    @Test
    public void insertPT() {
        List<PT> pts = new ArrayList<>();
        IntStream.rangeClosed(1, 100).forEach(i -> {
            PT pt = new PT();
            pt.setStudent_id("" + i);
            pt.setStudent_name("学生1");
            pt.setSchool_id("000002");
            pt.setSchool_name("向阳中学");
            pt.setGrade_id("00000201");
            pt.setGrade_name("六年级");
            pt.setClass_id("0000020101");
            pt.setClass_name("一班");
            pt.setBatch(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
            pt.setTest_time(new Date());
            Item item1 = new Item();
            item1.setItem_name("BM1");
            item1.setScore(70.9);
            item1.setUnit("bmi");
            Item item2 = new Item();
            item2.setItem_name("肺活量");
            item2.setScore(1668.27);
            item2.setUnit("cc");
            Item item3 = new Item();
            item3.setItem_name("原地纵跳");
            item3.setScore(34.41);
            item3.setUnit("cm");
            pts.add(pt);
            pt.setItems(Arrays.asList(item1, item2, item3));
        });
        mongoTemplate.insertAll(pts);
    }


}
