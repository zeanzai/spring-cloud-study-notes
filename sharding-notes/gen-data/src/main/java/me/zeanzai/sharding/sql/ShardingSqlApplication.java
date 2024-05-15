package me.zeanzai.sharding.sql;

import me.zeanzai.sharding.sql.gendata.util.InsertDataUtils;

public class ShardingSqlApplication {


    public static void main(String[] args) {
        createTable();
    }

    public static void createTable(){
        InsertDataUtils insertDataUtils = new InsertDataUtils();
        insertDataUtils.createTableWithoutIndex();
    }


    public static void genData1(){
        InsertDataUtils insertDataUtils = new InsertDataUtils();
        insertDataUtils.insertBigData3();
    }



}
