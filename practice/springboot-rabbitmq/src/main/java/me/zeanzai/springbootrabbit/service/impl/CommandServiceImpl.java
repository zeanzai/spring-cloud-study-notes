package me.zeanzai.springbootrabbit.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.zeanzai.springbootrabbit.entity.DeviceCommondInfoEntity;
import me.zeanzai.springbootrabbit.entity.EquipmentEntity;
import me.zeanzai.springbootrabbit.service.CommandService;
import me.zeanzai.springbootrabbit.utils.RedisUtil;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/11
 */
@Service
@Slf4j
public class CommandServiceImpl implements CommandService {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    private Executor THREADPOOL = Executors.newFixedThreadPool(10);

    @Override
    public Long genDevice(int num) {
        List<DeviceCommondInfoEntity> deviceCmd = createDeviceCmd(num);
        ListOperations listOperations = redisTemplate.opsForList();
        Long devicecmds = listOperations.rightPushAll("devicecmds", deviceCmd);
        return devicecmds;
    }

    private List<DeviceCommondInfoEntity> createDeviceCmd(int num){
        List<DeviceCommondInfoEntity> deviceCommondInfoEntities = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            DeviceCommondInfoEntity deviceCommondInfoEntity = new DeviceCommondInfoEntity();
            deviceCommondInfoEntity.setId(String.valueOf(i));
            deviceCommondInfoEntity.setDeviceTypeNo("typeNo: "+i);
            deviceCommondInfoEntity.setDeviceTypeName("typeName: "+i);
            deviceCommondInfoEntity.setDeviceNo("deviceNo: "+i);
            deviceCommondInfoEntity.setDeviceName("deviceName: "+i);

            deviceCommondInfoEntity.setCmdInfo("id="+i+"&typeNo="+i+"&typeName="+i+"&deviceNo="+i+"&deviceName="+i);
            deviceCommondInfoEntities.add(deviceCommondInfoEntity);
        }
        return deviceCommondInfoEntities;
    }

    @Override
    public void sendSingleMsg() {
        DeviceCommondInfoEntity deviceCommondInfoEntity = (DeviceCommondInfoEntity) redisTemplate.opsForList().rightPop("devicecmds");
        rabbitTemplate.convertAndSend("deviceCmdExchange", "deviceBinding", deviceCommondInfoEntity);
    }

    @Override
    public void sendPatchCmd(){
        List equipmentlist = redisTemplate.opsForList().range("equipmentlist", 0, 500);
        for (int i = 0; i < equipmentlist.size(); i++) {
            EquipmentEntity equipment = (EquipmentEntity)equipmentlist.get(i);
            sendMessageToDevice(equipment.getIpcNum(), equipment);
        }


//        // 去数据库中找到要抄表的所有设备的设备id
//        List<String> equipmentId = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            equipmentId.add("alldevicelist:equipmentlist:equipment_" + String.format("%09d", i % 10));
//        }
//
//        // 去redis中找到要抄表的设备列表
//        String keyPattern = "alldevicelist:equipmentlist:equipment_"+ "*";
//        ScanOptions options = ScanOptions.scanOptions().match(keyPattern).build();
//        redisTemplate.executeWithStickyConnection(new RedisCallback<EquipmentEntity>() {
//            @Override
//            public EquipmentEntity doInRedis(RedisConnection redisConnection) throws DataAccessException {
//                Cursor<byte[]> cursor = redisConnection.scan(options);
//                while (cursor.hasNext()) {
//                    Object o = redisTemplate.opsForValue().get(cursor.next());
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    try {
//                        EquipmentEntity equipment = objectMapper.readValue((JsonParser) o, EquipmentEntity.class);
//                        // 发送消息给rabbitmq
////                        rabbitTemplate.convertAndSend("device.exchange", equipment.getIpcNum(), equipment);
//                        sendMessageToDevice(equipment.getIpcNum(), equipment);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                return null;
//            }
//        });

    }

    public void sendMessageToDevice(String ipcid, Object message) {
        // 动态声明队列和绑定
        Queue queue = new Queue(ipcid, true);
        DirectExchange exchange = new DirectExchange("device.exchange");
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(ipcid);

        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareBinding(binding);

        // 发送消息
        rabbitTemplate.convertAndSend("device.exchange", ipcid, message);
    }

    @Override
    public void syncChaobiao() {
        // 1. 获取所有设备信息，之后构造抄表命令，等待不同时间后进行返回抄表结果；
        long startT = System.currentTimeMillis();
        log.info("---------> pharse 00, {}", startT);

        List<DeviceCommondInfoEntity> deviceBinding = redisTemplate.opsForList().range("devicecmds", 1, 1000);

        Map<String, Long> resultMap = new ConcurrentHashMap<>();
//        List<CompletableFuture<Void>> completableFutures = new ArrayList<>();
        CompletableFuture[] tempF = new CompletableFuture[deviceBinding.size()];

//        CompletableFuture[] completableFutures1 = deviceBinding.stream()
//                .map(deviceCommondInfoEntity -> CompletableFuture.completedFuture(resultMap)
//                        .thenApply(map -> {
//                            Random random = new Random();
//                            double v = random.nextDouble();
//                            long sleeptime = (long) (v * 1000);
//                            resultMap.put(deviceCommondInfoEntity.getId(), sleeptime);
//                            return resultMap;
//                        })).toArray(CompletableFuture[]::new);
//        CompletableFuture.allOf(completableFutures1)
//                .whenCompleteAsync((rmap, throwable)-> Arrays.stream(completableFutures1).forEach(completableFuture-> {
//                    try {
//                        System.out.println(completableFuture.get());
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    } catch (ExecutionException e) {
//                        throw new RuntimeException(e);
//                    }
//                }));
        for (int i=0; i<deviceBinding.size();i++) {
            DeviceCommondInfoEntity temp = deviceBinding.get(i);
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(new SyncTask(temp.getId(), resultMap), THREADPOOL);
            tempF[i] = voidCompletableFuture;
//            completableFutures.add(voidCompletableFuture);
        }
//        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(tempF);
        CompletableFuture.allOf(tempF).join();
//        try {
////            voidCompletableFuture.get(3, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        } catch (TimeoutException e) {
//            throw new RuntimeException(e);
//        }
        log.info("---------> pharse 01, {}", startT-System.currentTimeMillis());


        List<Map<String, Long>> mapList = new ArrayList<>();
        Iterator<Map.Entry<String, Long>> iterator = resultMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> next = iterator.next();
            Map<String, Long> map = new HashMap<>();
            map.put(next.getKey(), next.getValue());
            mapList.add(map);
        }

        redisTemplate.opsForList().rightPushAll("result", mapList);

        log.info("---------> pharse 02, {}", startT-System.currentTimeMillis());

    }

    private class SyncTask implements Runnable{
        private Map<String, Long> map;
        private String deviceId;



        public SyncTask(String deviceId, Map<String, Long> map) {
            this.deviceId = deviceId;
            this.map = map;
        }

        @Override
        public void run() {
            Random random = new Random();
            double v = random.nextDouble();
            long sleeptime = (long) (v * 1000);
            map.put(this.deviceId, sleeptime);

            try {
                Thread.sleep(sleeptime);
                log.info("threadname: {}, sleeptime: {}ms", Thread.currentThread().getName(), sleeptime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Autowired
    private RedisUtil redisUtils;


    @Override
    public void syncChaobiao1() {
        long startT = System.currentTimeMillis();
        log.info("---------> pharse 00, {}", startT);

        List<DeviceCommondInfoEntity> deviceBinding = redisTemplate.opsForList().range("devicecmds", 1, 1000);
        CompletableFuture<Void>[] completableFutureArr = new CompletableFuture[deviceBinding.size()];

        Map<String, Long> resultMap = new ConcurrentHashMap<>();
        for (int i = 0; i < deviceBinding.size(); i++) {
            DeviceCommondInfoEntity deviceCommondInfoEntity = deviceBinding.get(i);
            CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(new SyncTask(deviceCommondInfoEntity.getId(), resultMap), Executors.newFixedThreadPool(10));
            completableFutureArr[i] = completableFuture;
        }
        CompletableFuture.allOf(completableFutureArr).join();

        log.info("---------> pharse 01, {}", System.currentTimeMillis()-startT);
        String uuid = UUID.randomUUID().toString();
        redisTemplate.opsForHash().putAll(uuid, resultMap);
//        redisUtils.hashMSet(UUID.randomUUID(), resultMap);
        List<Map<String, Long>> mapListA = new ArrayList<>();
        Iterator<Map.Entry<String, Long>> iterator = resultMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> next = iterator.next();
            Map<String, Long> map = new HashMap<>();
            map.put(next.getKey(), next.getValue());
            mapListA.add(map);
        }

//        redisTemplate.opsForList().rightPushAll("result", mapListA);

        log.info("---------> pharse 02, {}", System.currentTimeMillis()-startT);

    }



    @Override
    public void asyncChaobiao() {
        // 1. 获取所有设备信息，构造抄表命令，之后发送给mq，发送完成后返回确认信息
    }

    @Override
    public void sendCmdResultToQueue() {
        // 动态声明队列和绑定
        Queue queue = new Queue("cmdresponse", true);
        DirectExchange exchange = new DirectExchange("ipc.exchange");
        Binding binding = BindingBuilder.bind(queue).to(exchange).with("response");

        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareBinding(binding);

        List equipmentlist = redisTemplate.opsForList().range("equipmentlist", 0, 500);
        for (int i = 0; i < equipmentlist.size(); i++) {
            EquipmentEntity equipment = (EquipmentEntity)equipmentlist.get(i);// 发送消息
            rabbitTemplate.convertAndSend("ipc.exchange", "response", equipment);
        }
    }

    @Override
    public void dataReportToQueue() {
        // 动态声明队列和绑定
        Queue queue = new Queue("datareport", true);
        DirectExchange exchange = new DirectExchange("ipc.exchange");
        Binding binding = BindingBuilder.bind(queue).to(exchange).with("report");

        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareBinding(binding);

        List equipmentlist = redisTemplate.opsForList().range("equipmentlist", 0, 500);
        for (int i = 0; i < equipmentlist.size(); i++) {
            EquipmentEntity equipment = (EquipmentEntity)equipmentlist.get(i);// 发送消息
            rabbitTemplate.convertAndSend("ipc.exchange", "report", equipment);
        }
    }
}
