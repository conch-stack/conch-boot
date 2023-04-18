/*
 Navicat Premium Data Transfer

 Source Server         : Haina-Dev-Mysql
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : 192.168.2.44:3307
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 02/08/2019 10:08:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for iot_system_user
-- ----------------------------
DROP TABLE IF EXISTS `iot_system_user`;
CREATE TABLE `iot_system_user` (
  `SU_ID` varchar(128) NOT NULL,
  `SU_USER_NAME` varchar(256) DEFAULT NULL,
  `SU_NICK_NAME` varchar(256) DEFAULT NULL,
  `SU_AGE` int(3) DEFAULT NULL,
  `SU_PASSWORD` varchar(256) DEFAULT NULL,
  `SU_STATUS` int(1) DEFAULT NULL,
  `SU_CREATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `SU_MARK` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`SU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of iot_system_user
-- ----------------------------
BEGIN;
INSERT INTO `iot_system_user` VALUES ('2222222222', 'b', 'bb', 20, '222', 1, '2019-08-02 01:30:59', 'bbb');
INSERT INTO `iot_system_user` VALUES ('3333333333', 'c', 'cc', 30, '333', 1, '2019-08-02 01:31:44', 'ccc');
INSERT INTO `iot_system_user` VALUES ('4444444444', 'b', 'bbb', 40, '2222', 1, '2019-08-02 01:32:34', 'bbbb');
INSERT INTO `iot_system_user` VALUES ('55555555555', 'admin', 'admin', 58, 'admin', 1, '2019-08-02 01:34:11', 'admin');
INSERT INTO `iot_system_user` VALUES ('58eea57e-a1f1-11e9-9b7e-3417eb9c0f80', 'a', 'aa', 20, '111', 1, '2019-08-02 01:30:09', 'aaa');
COMMIT;

-- ----------------------------
-- Table structure for iot_system_user_role
-- ----------------------------
DROP TABLE IF EXISTS `iot_system_user_role`;
CREATE TABLE `iot_system_user_role` (
  `SUR_ID` int(11) NOT NULL,
  `SUR_ROLE_ID` varchar(256) DEFAULT NULL,
  `SUR_USER_ID` varchar(256) DEFAULT NULL,
  `SUR_CREATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`SUR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of iot_system_user_role
-- ----------------------------
BEGIN;
INSERT INTO `iot_system_user_role` VALUES (1, '367c8078-a1f1-11e9-9b7e-3417eb9c0f80', '58eea57e-a1f1-11e9-9b7e-3417eb9c0f80', '2019-08-02 01:33:08');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
