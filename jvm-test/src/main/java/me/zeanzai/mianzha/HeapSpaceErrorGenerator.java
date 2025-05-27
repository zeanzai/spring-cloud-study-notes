package me.zeanzai.mianzha;

import java.util.ArrayList;
import java.util.List;

public class HeapSpaceErrorGenerator {
    public static void main(String[] args) {
        // 第一步，创建一个大的容器
        List<byte[]> bigObjects = new ArrayList<>();
        try {
            // 第二步，循环写入数据
            while (true) {
                // 第三步，创建一个大对象，一个大约 10M 的数组
                byte[] bigObject = new byte[10 * 1024 * 1024];
                // 第四步，将大对象添加到容器中
                bigObjects.add(bigObject);
            }
        } catch (OutOfMemoryError e) {
            System.out.println("OutOfMemoryError 发生在 " + bigObjects.size() + " 对象后");
            throw e;
        }
    }
}
