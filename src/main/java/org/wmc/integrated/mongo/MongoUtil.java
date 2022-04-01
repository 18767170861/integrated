package org.wmc.integrated.mongo;//package org.wmc.integrated.mongo;
//
//import com.mongodb.*;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoCursor;
//import com.mongodb.client.MongoDatabase;
//import com.mongodb.client.MongoIterable;
//import com.mongodb.client.model.Filters;
//import com.mongodb.client.result.DeleteResult;
//import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
//import org.bson.Document;
//import org.bson.conversions.Bson;
//import org.bson.types.ObjectId;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public enum MongoUtil {
//    /**
//     * 定义一个枚举元素，它代表此类的一个实例
//     */
//    INSTANCE;
//
//    private static MongoClient mongoClient;
//
//    static {
//        System.out.println("===============MongoDBUtil初始化========================");
//        String ip = "localhost";
//        int port =27017;
//        List<ServerAddress> adds = new ArrayList<>();
//        //ServerAddress()两个参数分别为 服务器地址 和 端口
//        ServerAddress serverAddress = new ServerAddress(ip, port);
//        adds.add(serverAddress);
//
//        List<MongoCredential> credentials = new ArrayList<>();
//        //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
//        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential("root", "admin", "root".toCharArray());
//        credentials.add(mongoCredential);
//
//
//        // 大部分用户使用mongodb都在安全内网下，但如果将mongodb设为安全验证模式，就需要在客户端提供用户名和密码：
//        // boolean auth = db.authenticate(myUserName, myPassword);
//        MongoClientOptions.Builder options = new MongoClientOptions.Builder();
//        options.cursorFinalizerEnabled(true);
//        // options.autoConnectBasicDBObjectRetry(true);// 自动重连true
//        // options.maxAutoConnectRetryTime(10); // the maximum auto connect retry time
//        options.connectionsPerHost(300);// 连接池设置为300个连接,默认为100
//        options.connectTimeout(30000);// 连接超时，推荐>3000毫秒
//        options.maxWaitTime(5000); //
//        options.socketTimeout(0);// 套接字超时时间，0无限制
//        options.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
//        /**
//         * write concern描述mongodb对单示例，副本集或分片集写入操作的确认请求级别。在分片集mongos实例会传递write concern到其它实例
//         * w=1（默认）:则要求得到写操作已经传播到独立的Mongod实例或副本集的primary成员的确认
//         */
//        options.writeConcern(WriteConcern.W1);
//        MongoClientOptions build = options.build();
//        INSTANCE.mongoClient = new MongoClient(serverAddress, credentials , build);
//    }
//
//// ------------------------------------共用方法---------------------------------------------------
//    /**
//     * 获取DB实例 - 指定DB
//     *
//     * @param dbName
//     * @return
//     */
//    public MongoDatabase getDB(String dbName){
//        if (dbName != null && !"".equals(dbName)) {
//            MongoDatabase database = mongoClient.getDatabase(dbName);
//            return database;
//        }
//        return null;
//    }
//
//    /**
//     * 获取collection对象 - 指定Collection
//     *
//     * @param collName
//     * @return
//     */
//    public MongoCollection<Document> getCollection(String dbName, String collName) {
//        if (null == collName || "".equals(collName)) {
//            return null;
//        }
//        if (null == dbName || "".equals(dbName)) {
//            return null;
//        }
//        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
//        return collection;
//    }
//
//    /**
//     * 查询DB下的所有表名
//     */
//    public List<String> getAllCollections(String dbName) {
//        MongoIterable<String> colls = getDB(dbName).listCollectionNames();
//        List<String> _list = new ArrayList<String>();
//        for (String s : colls) {
//            _list.add(s);
//        }
//        return _list;
//    }
//
//    /**
//     * 获取所有数据库名称列表
//     *
//     * @return
//     */
//    public MongoIterable<String> getAllDBNames() {
//        MongoIterable<String> s = mongoClient.listDatabaseNames();
//        return s;
//    }
//
//    /**
//     * 查找对象 - 根据主键_id
//     *
//     * @param coll
//     * @param id
//     * @return
//     */
//    public Document findById(MongoCollection<Document> coll, String id) {
//        ObjectId _idobj = null;
//        try {
//            _idobj = new ObjectId(id);
//        } catch (Exception e) {
//            return null;
//        }
//        Document myDoc = coll.find(Filters.eq("_id", _idobj)).first();
//        return myDoc;
//    }
//
//    /** 统计数 */
//    public int getCount(MongoCollection<Document> coll) {
//        int count = (int) coll.count();
//        return count;
//    }
//
//    /** 条件查询 */
//    public MongoCursor<Document> find(MongoCollection<Document> coll, Bson filter) {
//        return coll.find(filter).iterator();
//    }
//
//    /** 分页查询 */
//    public MongoCursor<Document> findByPage(MongoCollection<Document> coll, Bson filter, int pageNo, int pageSize) {
//        Bson orderBy = new BasicDBObject("_id", -1);
//        return coll.find(filter).sort(orderBy).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
//    }
//
//    /**
//     * 通过ID删除
//     *
//     * @param coll
//     * @param id
//     * @return
//     */
//    public int deleteById(MongoCollection<Document> coll, String id) {
//        int count = 0;
//        ObjectId _id = null;
//        try {
//            _id = new ObjectId(id);
//        } catch (Exception e) {
//            return 0;
//        }
//        Bson filter = Filters.eq("_id", _id);
//        DeleteResult deleteResult = coll.deleteOne(filter);
//        count = (int) deleteResult.getDeletedCount();
//        return count;
//    }
//
//    /**
//     * FIXME
//     *
//     * @param coll
//     * @param id
//     * @param newdoc
//     * @return
//     */
//    public Document updateById(MongoCollection<Document> coll, String id, Document newdoc) {
//        ObjectId _idobj = null;
//        try {
//            _idobj = new ObjectId(id);
//        } catch (Exception e) {
//            return null;
//        }
//        Bson filter = Filters.eq("_id", _idobj);
//        //coll.replaceOne(filter, newdoc); // 完全替代
//        coll.updateOne(filter, new Document("$set", newdoc));
//        return newdoc;
//    }
//
//    public void dropCollection(String dbName, String collName) {
//        getDB(dbName).getCollection(collName).drop();
//    }
//
//    /**
//     * 关闭Mongodb
//     */
//    public void close() {
//        if (mongoClient != null) {
//            mongoClient.close();
//            mongoClient = null;
//        }
//    }
//    public static void main(String[] args) {
//        MongoUtil instance = MongoUtil.INSTANCE;
//        MongoCollection<Document> collection = instance.getCollection("mymongo", "test");
//        Bson age = Filters.eq("age", 38);
//        MongoCursor<Document> documentMongoCursor = instance.findByPage(collection, age, 1, 5);
//        while(documentMongoCursor.hasNext()){
//            System.out.println(documentMongoCursor.next());
//        }
//        Document document = new Document();
//        document.put("name", "xiaoming");
//        document.put("age", 38);
//        System.out.println("document:" + document);
//        Document document1 = instance.updateById(collection, "605d5a105ecc5b7daeb36a5f", document);
//        System.out.println("document1:" + document1);
//    }
//
//
//
//
//
//
//
//
//
//
//}
