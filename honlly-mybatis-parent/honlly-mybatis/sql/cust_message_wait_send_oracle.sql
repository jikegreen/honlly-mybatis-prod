--------------------------------------------------------
--  文件已创建 - 星期二-四月-05-2016   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table CUST_MESSAGE_WAIT_SEND
--------------------------------------------------------

  CREATE TABLE "YSTSMG"."CUST_MESSAGE_WAIT_SEND" 
   (	"ID" NUMBER(20,0), 
	"CONTENT" VARCHAR2(1050 BYTE), 
	"MOBILE_PHONES" VARCHAR2(1200 BYTE), 
	"SOURCE" VARCHAR2(48 BYTE), 
	"ACCEPT_TIME" TIMESTAMP (6), 
	"STATUS" NUMBER(1,0), 
	"SEND_NUM" NUMBER(2,0), 
	"REMARK" VARCHAR2(255 BYTE), 
	"CHECKED" NUMBER(1,0), 
	"TOTAL_PHONE_NUMBER" NUMBER(5,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SMG_DATE" ;

   COMMENT ON COLUMN "YSTSMG"."CUST_MESSAGE_WAIT_SEND"."ID" IS '主键';
   COMMENT ON COLUMN "YSTSMG"."CUST_MESSAGE_WAIT_SEND"."CONTENT" IS '短信内容';
   COMMENT ON COLUMN "YSTSMG"."CUST_MESSAGE_WAIT_SEND"."MOBILE_PHONES" IS '短信号码组';
   COMMENT ON COLUMN "YSTSMG"."CUST_MESSAGE_WAIT_SEND"."SOURCE" IS '发送方';
   COMMENT ON COLUMN "YSTSMG"."CUST_MESSAGE_WAIT_SEND"."ACCEPT_TIME" IS '请求时间点';
   COMMENT ON COLUMN "YSTSMG"."CUST_MESSAGE_WAIT_SEND"."STATUS" IS '处理状态';
   COMMENT ON COLUMN "YSTSMG"."CUST_MESSAGE_WAIT_SEND"."SEND_NUM" IS '补发次数';
   COMMENT ON COLUMN "YSTSMG"."CUST_MESSAGE_WAIT_SEND"."REMARK" IS '备注';
   COMMENT ON COLUMN "YSTSMG"."CUST_MESSAGE_WAIT_SEND"."CHECKED" IS '是否检查';
   COMMENT ON COLUMN "YSTSMG"."CUST_MESSAGE_WAIT_SEND"."TOTAL_PHONE_NUMBER" IS '号码个数';
REM INSERTING into YSTSMG.CUST_MESSAGE_WAIT_SEND
SET DEFINE OFF;
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (284,'大家好，我是update方法的测试内容！！','13646874892','nfsq',to_timestamp('05-4月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (285,'test1','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (321,'哈哈,测试,傻白甜！！！','13646874892','110',to_timestamp('17-3月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),1,1,'备注',1,1);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (290,'updateAsMap - 哈哈,测试,傻白甜！！！','13646874892','nfsq',to_timestamp('05-4月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (291,'test2forupdate','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (292,'test2','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (293,'test1','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (294,'test2','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (333,'哈哈,测试,傻白甜！！！','13646874892','110',to_timestamp('18-3月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),1,1,'备注',1,1);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (334,'哈哈,测试,傻白甜！！！','13646874892','110',to_timestamp('18-3月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),1,1,'备注',1,1);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (281,'updates','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (282,'test1','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (325,'哈哈,测试,傻白甜！！！','13646874892','110',to_timestamp('18-3月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),1,1,'备注',1,1);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (303,'test2','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (326,'哈哈,测试,傻白甜！！！','13646874892','110',to_timestamp('18-3月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),1,1,'备注',1,1);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (286,'test2','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (287,'test1','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (328,'哈哈,测试,傻白甜！！！','13646874892','110',to_timestamp('18-3月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),1,1,'备注',1,1);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (329,'哈哈,测试,傻白甜！！！','13646874892','110',to_timestamp('18-3月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),1,1,'备注',1,1);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (339,null,null,null,null,null,null,null,null,2);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (340,null,null,null,null,null,null,null,null,2);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (335,'哈哈,测试,傻白甜！！！','13646874892','110',to_timestamp('23-3月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),1,1,'备注',1,1);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (297,'test2','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (295,'test2','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (296,'test2forupdate','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (298,'test1','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (332,'哈哈,测试,傻白甜！！！','13646874892','110',to_timestamp('18-3月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),1,1,'备注',1,1);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (301,'test2','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (299,'test2','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (300,'test2forupdate','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (302,'test1','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (304,'test2','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (305,'test2forupdate','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (306,'test2','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (307,'test2','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (308,'test1','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (322,'哈哈,测试,傻白甜！！！','13646874892','110',to_timestamp('17-3月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),1,1,'备注',1,1);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (327,'哈哈,测试,傻白甜！！！','13646874892','110',to_timestamp('18-3月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),1,1,'备注',1,1);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (331,'哈哈,测试,傻白甜！！！','13646874892','110',to_timestamp('18-3月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),1,1,'备注',1,1);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (323,'哈哈,测试,傻白甜！！！','13646874892','110',to_timestamp('18-3月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),1,1,'备注',1,1);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (324,'哈哈,测试,傻白甜！！！','13646874892','110',to_timestamp('18-3月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),1,1,'备注',1,1);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (288,'test2','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (289,'test1','13646874892','nfsq',null,null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (343,'哈哈,测试,傻白甜！！！',null,null,to_timestamp('24-3月 -16 12.00.00.000000000 上午','DD-MON-RR HH.MI.SSXFF AM'),null,null,null,null,null);
Insert into YSTSMG.CUST_MESSAGE_WAIT_SEND (ID,CONTENT,MOBILE_PHONES,SOURCE,ACCEPT_TIME,STATUS,SEND_NUM,REMARK,CHECKED,TOTAL_PHONE_NUMBER) values (246,'test2','13646874892','nfsq',null,null,null,null,null,null);
--------------------------------------------------------
--  DDL for Index CUST_MESSAGE_WAIT_SEND_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "YSTSMG"."CUST_MESSAGE_WAIT_SEND_PK" ON "YSTSMG"."CUST_MESSAGE_WAIT_SEND" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SMG_INDX" ;
--------------------------------------------------------
--  Constraints for Table CUST_MESSAGE_WAIT_SEND
--------------------------------------------------------

  ALTER TABLE "YSTSMG"."CUST_MESSAGE_WAIT_SEND" ADD CONSTRAINT "CUST_MESSAGE_SEND_PK" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SMG_INDX"  ENABLE;
  ALTER TABLE "YSTSMG"."CUST_MESSAGE_WAIT_SEND" MODIFY ("ID" NOT NULL ENABLE);
