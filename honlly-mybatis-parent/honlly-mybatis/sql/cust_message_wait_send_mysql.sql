/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50626
 Source Host           : localhost
 Source Database       : sms

 Target Server Type    : MySQL
 Target Server Version : 50626
 File Encoding         : utf-8

 Date: 04/05/2016 18:41:57 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `CUST_MESSAGE_WAIT_SEND`
-- ----------------------------
DROP TABLE IF EXISTS `CUST_MESSAGE_WAIT_SEND`;
CREATE TABLE `CUST_MESSAGE_WAIT_SEND` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONTENT` varchar(1050) DEFAULT NULL,
  `MOBILE_PHONES` varchar(1200) DEFAULT NULL,
  `SOURCE` varchar(20) DEFAULT NULL,
  `ACCEPT_TIME` date DEFAULT NULL,
  `STATUS` int(1) DEFAULT NULL,
  `SEND_NUM` int(2) DEFAULT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `CHECKED` int(1) DEFAULT NULL,
  `TOTAL_PHONE_NUMBER` int(5) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=123101 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `CUST_MESSAGE_WAIT_SEND`
-- ----------------------------
BEGIN;
INSERT INTO `CUST_MESSAGE_WAIT_SEND` VALUES ('1', 'test', null, null, null, null, null, null, null, null), ('5', 'aa1', null, null, null, null, null, null, null, null), ('6', 'updates', null, null, null, null, null, null, null, null), ('7', 'updates', null, null, null, null, null, null, null, null), ('8', 'aa', null, null, null, null, null, null, null, null), ('9', 'aa1', null, null, null, null, null, null, null, null), ('10', 'aa', null, null, null, null, null, null, null, null), ('11', 'aa1', null, null, null, null, null, null, null, null), ('12', 'aa', null, null, null, null, null, null, null, null), ('13', 'aa1', null, null, null, null, null, null, null, null), ('14', 'aa', null, null, null, null, null, null, null, null), ('15', 'aa1', null, null, null, null, null, null, null, null), ('16', 'test2', '13646874892', 'nfsq', null, null, null, null, null, null), ('17', 'test2forupdate', '13646874892', 'nfsq', null, null, null, null, null, null), ('18', 'test2', '13646874892', 'nfsq', null, null, null, null, null, null), ('19', 'test2', '13646874892', 'nfsq', null, null, null, null, null, null), ('20', 'test1', '13646874892', 'nfsq', null, null, null, null, null, null), ('21', 'test2', '13646874892', 'nfsq', null, null, null, null, null, null), ('22', 'test2forupdate', '13646874892', 'nfsq', null, null, null, null, null, null), ('23', 'test2', '13646874892', 'nfsq', null, null, null, null, null, null), ('24', 'test2', '13646874892', 'nfsq', null, null, null, null, null, null), ('25', 'test1', '13646874892', 'nfsq', null, null, null, null, null, null), ('26', 'updateAsMap - 哈哈,测试,傻白甜！！！', '13646874892', 'nfsq', '2016-04-05', null, null, null, null, null), ('27', '大家好，我是update方法的测试内容！！', '13646874892', 'nfsq', null, null, null, null, null, null), ('28', 'test2', '13646874892', 'nfsq', null, null, null, null, null, null), ('29', 'test2', '13646874892', 'nfsq', null, null, null, null, null, null), ('30', 'test1', '13646874892', 'nfsq', null, null, null, null, null, null), ('31', '哈哈,测试,傻白甜！！！', null, null, '2016-03-25', null, null, null, null, null), ('32', '哈哈,测试,傻白甜！！！', '13646874892', '110', '2016-04-05', '1', '1', '备注', '1', '1'), ('123098', '哈哈,测试,傻白甜！！！', null, null, '2016-04-05', null, null, null, null, null), ('123099', '哈哈,测试,傻白甜！！！', '13646874892', '1100', '2016-04-05', '1', '1', '备注', '1', '1'), ('123100', '哈哈,测试,傻白甜！！！', '13646874892', 'abc', '2016-04-05', '1', '1', '备注', '1', '1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
