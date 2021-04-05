package song.pan.toolkit.redis.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * @author Song Pan
 * @version 1.0.0
 */
public class JedisTest {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);

        String ping = jedis.ping();
        System.out.println(ping);

        Transaction transaction = jedis.multi();

        try {
            transaction.set("", "");
            transaction.exec();
        } catch (Exception e) {
            transaction.discard();
            throw e;
        } finally {
            jedis.close();
        }
    }
}
