package com.genersoft.iot.vmp.conf.redis;

import com.alibaba.fastjson2.JSON;
import com.genersoft.iot.vmp.common.CommonCallback;
import com.genersoft.iot.vmp.conf.UserSetting;
import com.genersoft.iot.vmp.conf.redis.bean.RedisRpcClassHandler;
import com.genersoft.iot.vmp.conf.redis.bean.RedisRpcMessage;
import com.genersoft.iot.vmp.conf.redis.bean.RedisRpcRequest;
import com.genersoft.iot.vmp.conf.redis.bean.RedisRpcResponse;
import com.genersoft.iot.vmp.service.redisMsg.dto.RpcController;
import com.genersoft.iot.vmp.vmanager.bean.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class RedisRpcConfig implements MessageListener {

    public final static String REDIS_REQUEST_CHANNEL_KEY = "WVP_REDIS_REQUEST_CHANNEL_KEY";

    private static final long ASYNC_CALLBACK_TIMEOUT_MILLIS = TimeUnit.MINUTES.toMillis(5);

    private final AtomicLong requestSequence = new AtomicLong(ThreadLocalRandom.current().nextLong());

    @Autowired
    private UserSetting userSetting;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final int MESSAGE_QUEUE_CAPACITY = 100_000;

    private final BlockingQueue<Message> taskQueue = new LinkedBlockingQueue<>(MESSAGE_QUEUE_CAPACITY);
    private final AtomicBoolean consumerRunning = new AtomicBoolean();

    @Autowired
    private TaskExecutor taskExecutor;

    private final static Map<String, RedisRpcClassHandler> protocolHash = new ConcurrentHashMap<>();

    public void addHandler(String path, RedisRpcClassHandler handler) {
        protocolHash.put(path, handler);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        List<Class<?>> classList = ClassUtil.getClassList("com.genersoft.iot.vmp.service.redisMsg.control", RedisRpcController.class);
//        for (Class<?> handlerClass : classList) {
//            String controllerPath = handlerClass.getAnnotation(RedisRpcController.class).value();
//            Object bean = ClassUtil.getBean(controllerPath, handlerClass);
//            // 扫描其下的方法
//            Method[] methods = handlerClass.getDeclaredMethods();
//            for (Method method : methods) {
//                RedisRpcMapping annotation = method.getAnnotation(RedisRpcMapping.class);
//                if (annotation != null) {
//                    String methodPath =  annotation.value();
//                    if (methodPath != null) {
//                        protocolHash.put(controllerPath + "/" + methodPath, new RedisRpcClassHandler(bean, method));
//                    }
//                }
//
//            }
//
//        }
//        for (String s : protocolHash.keySet()) {
//            System.out.println(s);
//        }
//        if (log.isDebugEnabled()) {
//            log.debug("消息ID缓存表 protocolHash:{}", protocolHash);
//        }
//    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        if (!taskQueue.offer(message)) {
            log.error("[redis-rpc]消息队列已满，丢弃消息");
            return;
        }
        startConsumer();
    }

    private void startConsumer() {
        if (consumerRunning.compareAndSet(false, true)) {
            taskExecutor.execute(this::consumeMessages);
        }
    }

    private void consumeMessages() {
        try {
            Message msg;
            while ((msg = taskQueue.poll()) != null) {
                try {
                    RedisRpcMessage redisRpcMessage = JSON.parseObject(new String(msg.getBody()), RedisRpcMessage.class);
                    if (redisRpcMessage.getRequest() != null) {
                        handlerRequest(redisRpcMessage.getRequest());
                    } else if (redisRpcMessage.getResponse() != null){
                        handlerResponse(redisRpcMessage.getResponse());
                    } else {
                        log.error("[redis-rpc]解析失败 {}", JSON.toJSONString(redisRpcMessage));
                    }
                } catch (Exception e) {
                    log.error("[redis-rpc]解析异常 {}", new String(msg.getBody()), e);
                }
            }
        } finally {
            consumerRunning.set(false);
            if (!taskQueue.isEmpty()) {
                startConsumer();
            }
        }
    }

    private void handlerResponse(RedisRpcResponse response) {
        if (!userSetting.getServerId().equals(response.getToId())) {
            return;
        }
        log.info("[redis-rpc] << {}", response);
        response(response);
    }

    private void handlerRequest(RedisRpcRequest request) {
        try {
            if (userSetting.getServerId().equals(request.getFromId())) {
                return;
            }
            if (request.getToId() != null && !request.getToId().isBlank()
                    && !userSetting.getServerId().equals(request.getToId())) {
                return;
            }
            log.info("[redis-rpc] << {}", request);
            RedisRpcClassHandler redisRpcClassHandler = protocolHash.get(request.getUri());
            if (redisRpcClassHandler == null) {
                log.error("[redis-rpc] 路径: {}不存在", request.getUri());
                RedisRpcResponse response = request.getResponse();
                response.setStatusCode(ErrorCode.ERROR404.getCode());
                sendResponse(response);
                return;
            }
            RpcController controller = redisRpcClassHandler.getController();
            Method method = redisRpcClassHandler.getMethod();
            if (method == null) {
                RedisRpcResponse response = request.getResponse();
                response.setStatusCode(ErrorCode.ERROR404.getCode());
                sendResponse(response);
                return;
            }
            RedisRpcResponse response = (RedisRpcResponse)method.invoke(controller, request);
            if (response != null) {
                sendResponse(response);
            }
        }catch (Exception e) {
            log.error("[redis-rpc ] 处理请求失败 ", e);
            RedisRpcResponse response = request.getResponse();
            response.setStatusCode(ErrorCode.ERROR100.getCode());
            sendResponse(response);
        }
    }

    private void sendResponse(RedisRpcResponse response){
        log.info("[redis-rpc] >> {}", response);
        response.setFromId(userSetting.getServerId());
        RedisRpcMessage message = new RedisRpcMessage();
        message.setResponse(response);
        redisTemplate.convertAndSend(REDIS_REQUEST_CHANNEL_KEY, message);
    }

    private void sendRequest(RedisRpcRequest request){
        request.setFromId(userSetting.getServerId());
        log.info("[redis-rpc] >> {}", request);
        RedisRpcMessage message = new RedisRpcMessage();
        message.setRequest(request);
        redisTemplate.convertAndSend(REDIS_REQUEST_CHANNEL_KEY, message);
    }

    private final Map<Long, SynchronousQueue<RedisRpcResponse>> topicSubscribers = new ConcurrentHashMap<>();
    private final Map<Long, PendingCallback> callbacks = new ConcurrentHashMap<>();

    public RedisRpcResponse request(RedisRpcRequest request, long timeOut) {
        return request(request, timeOut, TimeUnit.SECONDS);
    }

    public RedisRpcResponse request(RedisRpcRequest request, long timeOut, TimeUnit timeUnit) {
        SynchronousQueue<RedisRpcResponse> subscribe = subscribe(request);

        try {
            sendRequest(request);
            return subscribe.poll(timeOut, timeUnit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("[redis rpc timeout] uri: {}, sn: {}", request.getUri(), request.getSn(), e);
            RedisRpcResponse redisRpcResponse = new RedisRpcResponse();
            redisRpcResponse.setStatusCode(ErrorCode.ERROR486.getCode());
            return redisRpcResponse;
        } finally {
            this.unsubscribe(request.getSn());
        }
    }

    public void request(RedisRpcRequest request, CommonCallback<RedisRpcResponse> callback) {
        setCallback(request, callback);
        sendRequest(request);
    }

    public Boolean response(RedisRpcResponse response) {
        SynchronousQueue<RedisRpcResponse> queue = topicSubscribers.get(response.getSn());
        PendingCallback pendingCallback = callbacks.get(response.getSn());
        if (queue != null) {
            try {
                return queue.offer(response, 2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("{}", e.getMessage(), e);
            }
        }else if (pendingCallback != null && callbacks.remove(response.getSn(), pendingCallback)) {
            pendingCallback.callback().run(response);
        }
        return false;
    }

    private void unsubscribe(long key) {
        topicSubscribers.remove(key);
    }


    private SynchronousQueue<RedisRpcResponse> subscribe(RedisRpcRequest request) {
        while (true) {
            long requestId = nextRequestId();
            SynchronousQueue<RedisRpcResponse> queue = new SynchronousQueue<>();
            if (topicSubscribers.putIfAbsent(requestId, queue) == null) {
                request.setSn(requestId);
                return queue;
            }
        }
    }

    private void setCallback(RedisRpcRequest request, CommonCallback<RedisRpcResponse> callback)  {
        while (true) {
            long requestId = nextRequestId();
            PendingCallback pendingCallback = new PendingCallback(callback,
                    System.currentTimeMillis() + ASYNC_CALLBACK_TIMEOUT_MILLIS);
            if (callbacks.putIfAbsent(requestId, pendingCallback) == null) {
                request.setSn(requestId);
                return;
            }
        }
    }

    private long nextRequestId() {
        long serverHash = Integer.toUnsignedLong(userSetting.getServerId().hashCode());
        return requestSequence.incrementAndGet() ^ (serverHash * 0x9E3779B97F4A7C15L);
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void cleanExpiredCallbacks() {
        long now = System.currentTimeMillis();
        callbacks.entrySet().removeIf(entry -> entry.getValue().expiresAt() <= now);
    }

    public void removeCallback(long key)  {
        callbacks.remove(key);
    }


    public int getCallbackCount(){
        return callbacks.size();
    }

    private record PendingCallback(CommonCallback<RedisRpcResponse> callback, long expiresAt) {
    }




//    @Scheduled(fixedRate = 1000)   //每1秒执行一次
//    public void execute(){
//        logger.info("callbacks的长度: " + callbacks.size());
//        logger.info("队列的长度: " + topicSubscribers.size());
//        logger.info("HOOK监听的长度: " + hookSubscribe.size());
//        logger.info("");
//    }
}
