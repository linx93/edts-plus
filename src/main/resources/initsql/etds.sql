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

 Date: 07/12/2021 19:17:01
*/

CREATE DATABASE IF NOT EXISTS etds DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

use etds;
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
-- 初始化管理员账户 admin 000000
INSERT INTO `account` ( `account`, `password`, `status`, `created_time`, `updated_time` ) VALUES('admin','$2a$10$Hs4YJT0ZPB3iaHwNLmJrHuV957.TVaY6ysDFEekij1G/hmcC3kKP6',0,NOW(),NOW());
-- ----------------------------
-- Table structure for data_result_apply_11
-- ----------------------------
DROP TABLE IF EXISTS `data_result_apply_11`;
CREATE TABLE `data_result_apply_11` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `notice_id` bigint DEFAULT NULL COMMENT '授权通知id',
  `data_id` bigint DEFAULT NULL COMMENT '拉取数据通知id',
  `apply_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发起etds唯一编码',
  `to_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业dtid',
  `to_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业名称',
  `to_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业etds唯一编号',
  `grant_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业dtid',
  `grant_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业名称',
  `data_type` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '被授权的数据类型',
  `data_amount` int DEFAULT NULL COMMENT '被拉取的数量',
  `data_document` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '数据的凭证',
  `serial_number` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统一业务标识号',
  `operated_time` bigint DEFAULT NULL COMMENT '操作时间',
  `created_time` bigint DEFAULT NULL COMMENT '创建时间',
  `claim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凭证id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `claim_id` (`claim_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2835 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='拉取数据统计日志(请求方)<11>';

-- ----------------------------
-- Table structure for data_statistics_provide_10
-- ----------------------------
DROP TABLE IF EXISTS `data_statistics_provide_10`;
CREATE TABLE `data_statistics_provide_10` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `notice_id` bigint DEFAULT NULL COMMENT '授权通知id',
  `data_id` bigint DEFAULT NULL COMMENT '拉取数据通知id',
  `apply_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发起etds唯一编码',
  `to_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业dtid',
  `to_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业名称',
  `to_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业etds唯一编号',
  `grant_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业dtid',
  `grant_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业名称',
  `data_type` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '被授权的数据类型',
  `data_amount` int DEFAULT NULL COMMENT '被拉取的数量',
  `statistics_document` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统计分析的凭证',
  `serial_number` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统一业务标识号',
  `operated_time` bigint DEFAULT NULL COMMENT '操作时间',
  `created_time` bigint DEFAULT NULL COMMENT '创建时间',
  `claim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凭证id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `claim_id` (`claim_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='拉取数据统计日志(提供方)<10>';

-- ----------------------------
-- Table structure for data_statistics_provide_11
-- ----------------------------
DROP TABLE IF EXISTS `data_statistics_provide_11`;
CREATE TABLE `data_statistics_provide_11` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `notice_id` bigint DEFAULT NULL COMMENT '授权通知id',
  `data_id` bigint DEFAULT NULL COMMENT '拉取数据通知id',
  `apply_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发起etds唯一编码',
  `to_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业dtid',
  `to_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业名称',
  `to_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业etds唯一编号',
  `grant_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业dtid',
  `grant_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业名称',
  `data_type` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '被授权的数据类型',
  `data_amount` int DEFAULT NULL COMMENT '被拉取的数量',
  `statistics_document` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统计分析的凭证',
  `serial_number` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统一业务标识号',
  `operated_time` bigint DEFAULT NULL COMMENT '操作时间',
  `created_time` bigint DEFAULT NULL COMMENT '创建时间',
  `claim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凭证id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `claim_id` (`claim_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='拉取数据统计日志(提供方)<11>';

-- ----------------------------
-- Table structure for data_statistics_provide_12
-- ----------------------------
DROP TABLE IF EXISTS `data_statistics_provide_12`;
CREATE TABLE `data_statistics_provide_12` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `notice_id` bigint DEFAULT NULL COMMENT '授权通知id',
  `data_id` bigint DEFAULT NULL COMMENT '拉取数据通知id',
  `apply_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发起etds唯一编码',
  `to_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业dtid',
  `to_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业名称',
  `to_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业etds唯一编号',
  `grant_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业dtid',
  `grant_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业名称',
  `data_type` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '被授权的数据类型',
  `data_amount` int DEFAULT NULL COMMENT '被拉取的数量',
  `statistics_document` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统计分析的凭证',
  `serial_number` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统一业务标识号',
  `operated_time` bigint DEFAULT NULL COMMENT '操作时间',
  `created_time` bigint DEFAULT NULL COMMENT '创建时间',
  `claim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凭证id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `claim_id` (`claim_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='拉取数据统计日志(提供方)<12>';

-- ----------------------------
-- Table structure for data_statistics_provide_13
-- ----------------------------
DROP TABLE IF EXISTS `data_statistics_provide_13`;
CREATE TABLE `data_statistics_provide_13` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `notice_id` bigint DEFAULT NULL COMMENT '授权通知id',
  `data_id` bigint DEFAULT NULL COMMENT '拉取数据通知id',
  `apply_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发起etds唯一编码',
  `to_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业dtid',
  `to_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业名称',
  `to_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业etds唯一编号',
  `grant_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业dtid',
  `grant_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业名称',
  `data_type` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '被授权的数据类型',
  `data_amount` int DEFAULT NULL COMMENT '被拉取的数量',
  `statistics_document` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统计分析的凭证',
  `serial_number` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统一业务标识号',
  `operated_time` bigint DEFAULT NULL COMMENT '操作时间',
  `created_time` bigint DEFAULT NULL COMMENT '创建时间',
  `claim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凭证id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `claim_id` (`claim_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='拉取数据统计日志(提供方)<13>';

-- ----------------------------
-- Table structure for data_switch
-- ----------------------------
DROP TABLE IF EXISTS `data_switch`;
CREATE TABLE `data_switch` (
  `id` int NOT NULL AUTO_INCREMENT,
  `flag` int DEFAULT NULL COMMENT '0: 开启状态   1：关闭状态',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据开关表，标志etds能否发送数据';

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
  `etds_name` varchar(255) DEFAULT NULL COMMENT 'etds名字',
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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='tdaas对etds暂停/恢复的操作记录表';

-- ----------------------------
-- Table structure for grant_result_apply_4
-- ----------------------------
DROP TABLE IF EXISTS `grant_result_apply_4`;
CREATE TABLE `grant_result_apply_4` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `notice_id` bigint DEFAULT NULL COMMENT '授权通知id',
  `apply_etds_uuid` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发起方企业etds唯一编号',
  `grant_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业dtid',
  `grant_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业名称',
  `to_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业dtid',
  `to_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业名称',
  `to_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业etds唯一编号',
  `grant_details` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权详情',
  `grant_status` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权状态',
  `grant_document` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权的凭证',
  `serial_number` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统一业务标识号',
  `operated_time` bigint DEFAULT NULL COMMENT '操作时间',
  `created_time` bigint DEFAULT NULL COMMENT '创建时间',
  `claim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凭证id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `claim_id` (`claim_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='授权结果返回表(请求方) <4>';

-- ----------------------------
-- Table structure for grant_result_provide_6
-- ----------------------------
DROP TABLE IF EXISTS `grant_result_provide_6`;
CREATE TABLE `grant_result_provide_6` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `notice_id` bigint DEFAULT NULL COMMENT '授权通知id',
  `apply_etds_uuid` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发起方企业etds唯一编号',
  `grant_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业dtid',
  `grant_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业名称',
  `to_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业dtid',
  `to_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业名称',
  `to_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业etds唯一编号',
  `grant_details` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权详情',
  `grant_status` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权状态',
  `claim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凭证id',
  `grant_document` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权的凭证',
  `use_status` int DEFAULT NULL COMMENT '用于标记tdaas控制此授权凭证的状态（0:正常 1:暂停）',
  `serial_number` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统一业务标识号',
  `operated_time` bigint DEFAULT NULL COMMENT '操作时间',
  `created_time` bigint DEFAULT NULL COMMENT '创建时间',
  `apply_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据请求方企业数字身份',
  `apply_name` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据请求方企业企业名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `claim_id` (`claim_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='授权结果返回表(提供方) <6>';

-- ----------------------------
-- Table structure for re_auth_notice_apply_1
-- ----------------------------
DROP TABLE IF EXISTS `re_auth_notice_apply_1`;
CREATE TABLE `re_auth_notice_apply_1` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `apply_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发起etds唯一编码',
  `apply_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发起etds所属的tdaas的数字身份',
  `to_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业dtid',
  `to_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业名称',
  `to_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业etds唯一编号',
  `grant_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业dtid',
  `grant_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业名称',
  `notice_details` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '通知详情',
  `dtc_document` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '凭证详情',
  `serial_number` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统一业务标识号',
  `apply_time` bigint DEFAULT NULL COMMENT '发起时间',
  `created_time` bigint DEFAULT NULL COMMENT '创建时间',
  `claim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凭证id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='请求授权通知(请求方)<1>';

-- ----------------------------
-- Table structure for re_auth_notice_apply_2
-- ----------------------------
DROP TABLE IF EXISTS `re_auth_notice_apply_2`;
CREATE TABLE `re_auth_notice_apply_2` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `from_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发起etds唯一编码',
  `apply_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据发起方企业dtid',
  `apply_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据发起方企业名称',
  `to_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业etds唯一编号',
  `to_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业dtid',
  `to_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业名称',
  `grant_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '凭证状态',
  `notice_details` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '通知详情',
  `dtc_document` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '凭证详情',
  `serial_number` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统一业务标识号',
  `operated_time` bigint DEFAULT NULL COMMENT '操作时间',
  `created_time` bigint DEFAULT NULL COMMENT '创建时间',
  `claim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凭证id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='请求授权通知(请求方)<2>';

-- ----------------------------
-- Table structure for re_data_notice_apply_7
-- ----------------------------
DROP TABLE IF EXISTS `re_data_notice_apply_7`;
CREATE TABLE `re_data_notice_apply_7` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `result_id` bigint DEFAULT NULL COMMENT '授权结果返回主键',
  `apply_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发起etds唯一编码',
  `to_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业dtid',
  `to_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业名称',
  `to_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业etds唯一编号',
  `grant_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业dtid',
  `grant_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业名称',
  `notice_details` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '通知详情',
  `dtc_document` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '凭证详情',
  `serial_number` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统一业务标识号',
  `operated_time` bigint DEFAULT NULL COMMENT '操作时间',
  `created_time` bigint DEFAULT NULL COMMENT '创建时间',
  `claim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凭证id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `claim_id` (`claim_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='请求拉取数据通知(请求方)<7>';

-- ----------------------------
-- Table structure for re_data_notice_apply_8
-- ----------------------------
DROP TABLE IF EXISTS `re_data_notice_apply_8`;
CREATE TABLE `re_data_notice_apply_8` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `result_id` bigint DEFAULT NULL COMMENT '授权结果返回主键',
  `apply_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发起etds唯一编码',
  `to_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业dtid',
  `to_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业名称',
  `to_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业etds唯一编号',
  `grant_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业dtid',
  `grant_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业名称',
  `notice_details` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '通知详情',
  `dtc_document` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '凭证详情',
  `serial_number` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统一业务标识号',
  `operated_time` bigint DEFAULT NULL COMMENT '操作时间',
  `created_time` bigint DEFAULT NULL COMMENT '创建时间',
  `claim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凭证id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `claim_id` (`claim_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='请求拉取数据通知(请求方)<8>';

-- ----------------------------
-- Table structure for re_data_notice_apply_9
-- ----------------------------
DROP TABLE IF EXISTS `re_data_notice_apply_9`;
CREATE TABLE `re_data_notice_apply_9` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `result_id` bigint DEFAULT NULL COMMENT '授权结果返回主键',
  `apply_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发起etds唯一编码',
  `to_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业dtid',
  `to_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业名称',
  `to_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业etds唯一编号',
  `grant_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业dtid',
  `grant_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业名称',
  `notice_details` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '通知详情',
  `dtc_document` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '凭证详情',
  `serial_number` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统一业务标识号',
  `operated_time` bigint DEFAULT NULL COMMENT '操作时间',
  `created_time` bigint DEFAULT NULL COMMENT '创建时间',
  `claim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凭证id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `claim_id` (`claim_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='请求拉取数据通知(请求方)<9>';

-- ----------------------------
-- Table structure for re_data_notice_provide_9
-- ----------------------------
DROP TABLE IF EXISTS `re_data_notice_provide_9`;
CREATE TABLE `re_data_notice_provide_9` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `result_id` bigint DEFAULT NULL COMMENT '授权结果返回主键',
  `apply_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发起etds唯一编码',
  `to_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业dtid',
  `to_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业名称',
  `to_etds_uuid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据提供方企业etds唯一编号',
  `grant_dtid` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业dtid',
  `grant_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据授权方企业名称',
  `notice_details` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '通知详情',
  `dtc_document` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '凭证详情',
  `serial_number` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '统一业务标识号',
  `operated_time` bigint DEFAULT NULL COMMENT '操作时间',
  `created_time` bigint DEFAULT NULL COMMENT '创建时间',
  `claim_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '凭证id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `claim_id` (`claim_id`)
) ENGINE=InnoDB AUTO_INCREMENT=802 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='请求拉取数据通知(提供方)<9>';

-- ----------------------------
-- Table structure for report_apply_11
-- ----------------------------
DROP TABLE IF EXISTS `report_apply_11`;
CREATE TABLE `report_apply_11` (
  `id` int NOT NULL AUTO_INCREMENT,
  `claim_id` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '凭证id',
  `requested_at` bigint DEFAULT NULL COMMENT '请求时间',
  `first_response` bigint DEFAULT NULL COMMENT '首次响应时间\n',
  `last_response` bigint DEFAULT NULL COMMENT '最后响应时间',
  `serial_number` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求序列号',
  `from_tdaas` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方数字身份',
  `from_etds` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方etds唯一码',
  `to_tdaas` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '响应方数字身份',
  `to_etds` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '响应方etds唯一码',
  `request_http_meta_path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求Path',
  `request_http_meta_query` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求Query',
  `request_http_meta_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方法，只允许GET/POST',
  `request_http_meta_header` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求头',
  `request_http_meta_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求body的数据',
  `auth_dtc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权凭证id',
  `retries` int DEFAULT NULL COMMENT '重试次数',
  `auth_status` int DEFAULT NULL COMMENT '授权状态码',
  `auth_status_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '授权状态描述',
  `response_http_meta_header` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '响应头',
  `response_http_meta_status` int DEFAULT NULL COMMENT '响应状态码',
  `response_http_meta_contentLength` bigint DEFAULT NULL COMMENT '响应数据大小,单位是byte',
  `chunk_size` bigint DEFAULT NULL COMMENT '分片大小,每片的大小单位b',
  `chunk_length` int DEFAULT NULL COMMENT '分片的数量',
  `create_time` datetime DEFAULT NULL COMMENT '插入时间',
  `from_tdaas_name` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方企业名称',
  `to_tdaas_name` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '提供方企业名称',
  `auth_tdaas` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权方数字身份',
  `auth_tdaas_name` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权方企业名称',
  PRIMARY KEY (`id`),
  KEY `auth_dtc_index` (`auth_dtc`)
) ENGINE=InnoDB AUTO_INCREMENT=208 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for report_provide_11
-- ----------------------------
DROP TABLE IF EXISTS `report_provide_11`;
CREATE TABLE `report_provide_11` (
  `id` int NOT NULL AUTO_INCREMENT,
  `claim_id` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '凭证id',
  `requested_at` bigint DEFAULT NULL COMMENT '请求时间',
  `first_response` bigint DEFAULT NULL COMMENT '首次响应时间\n',
  `last_response` bigint DEFAULT NULL COMMENT '最后响应时间',
  `serial_number` varchar(225) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求序列号',
  `from_tdaas` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方数字身份',
  `from_etds` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方etds唯一码',
  `to_tdaas` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '响应方数字身份',
  `to_etds` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '响应方etds唯一码',
  `request_http_meta_path` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求Path',
  `request_http_meta_query` text COLLATE utf8mb4_general_ci COMMENT '请求Query',
  `request_http_meta_method` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方法，只允许GET/POST',
  `request_http_meta_header` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求头',
  `request_http_meta_body` text COLLATE utf8mb4_general_ci COMMENT '请求body的数据',
  `auth_dtc` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权凭证id',
  `retries` int DEFAULT NULL COMMENT '重试次数',
  `auth_status` int DEFAULT NULL COMMENT '授权状态码',
  `auth_status_desc` text COLLATE utf8mb4_general_ci COMMENT '授权状态描述',
  `response_http_meta_header` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '响应头',
  `response_http_meta_status` int DEFAULT NULL COMMENT '响应状态码',
  `response_http_meta_contentLength` bigint DEFAULT NULL COMMENT '响应数据大小,单位是byte',
  `chunk_size` bigint DEFAULT NULL COMMENT '分片大小,每片的大小单位b',
  `chunk_length` int DEFAULT NULL COMMENT '分片的数量',
  `create_time` datetime DEFAULT NULL COMMENT '插入时间',
  `from_tdaas_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方企业名称',
  `to_tdaas_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '提供方企业名称',
  `auth_tdaas` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权方数字身份',
  `auth_tdaas_name` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权方企业名称',
  PRIMARY KEY (`id`),
  KEY `auth_dtc_index` (`auth_dtc`)
) ENGINE=InnoDB AUTO_INCREMENT=208 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
  `safe_code` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '安全码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='tdaas的publicKey和privateKey\n';

SET FOREIGN_KEY_CHECKS = 1;
