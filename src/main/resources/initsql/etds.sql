/*
 Navicat Premium Data Transfer

 Source Server         : 阿里云
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : 47.100.162.114:13306
 Source Schema         : etds

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 23/11/2021 13:44:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `account` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '账户',
  `password` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `status` int NOT NULL COMMENT '账户状态[0:正常状态 1:禁用状态]',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户/账户表';

-- ----------------------------
-- Table structure for data_switch
-- ----------------------------
DROP TABLE IF EXISTS `data_switch`;
CREATE TABLE `data_switch` (
  `id` int NOT NULL AUTO_INCREMENT,
  `flag` int DEFAULT NULL COMMENT '0: 开启状态   1：关闭状态',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据开关表，标志etds能否发送数据';

-- ----------------------------
-- Table structure for etds
-- ----------------------------
DROP TABLE IF EXISTS `etds`;
CREATE TABLE `etds` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `license` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT 'license',
  `create_time` bigint DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint DEFAULT NULL COMMENT '更新时间',
  `company_name` varchar(50) NOT NULL DEFAULT '' COMMENT '公司名称',
  `company_dtid` varchar(40) NOT NULL DEFAULT '' COMMENT '企业dtid',
  `state` int unsigned DEFAULT '0' COMMENT '状态[0 : 正常  1:暂停]',
  `etds_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT 'etds url（ip+port）',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '描述',
  `activation_code` varchar(255) DEFAULT '' COMMENT '激活码',
  `etds_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT 'Etds唯一标识码',
  `app_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '应用key(用于etds校验来自tdass的请求)',
  `app_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密钥(用于etds校验来自tdass的请求)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='etds相关信息';

-- ----------------------------
-- Table structure for etds_status_record
-- ----------------------------
DROP TABLE IF EXISTS `etds_status_record`;
CREATE TABLE `etds_status_record` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '记录主键',
  `type` int NOT NULL COMMENT '类型[0:启动  1:暂停]',
  `company_dtid` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'tdaas对应的数字身份',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `etds_code` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'etds的唯一code',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='tdaas对etds暂停/恢复的操作记录表';

-- ----------------------------
-- Table structure for tdaas_private_key
-- ----------------------------
DROP TABLE IF EXISTS `tdaas_private_key`;
CREATE TABLE `tdaas_private_key` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_dtid` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数字身份',
  `private_key` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '私钥',
  `public_key` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公钥',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='tdaas的publicKey和privateKey\n';

SET FOREIGN_KEY_CHECKS = 1;
