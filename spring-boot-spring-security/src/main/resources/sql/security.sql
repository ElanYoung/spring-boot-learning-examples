/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : security

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 17/02/2023 16:22:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `parent_id`   bigint       NULL     DEFAULT NULL COMMENT '父级菜单ID',
    `name`        varchar(32)  NOT NULL COMMENT '菜单名称',
    `path`        varchar(128) NULL     DEFAULT NULL COMMENT '路由地址',
    `permission`  varchar(32)  NULL     DEFAULT NULL COMMENT '权限标识',
    `icon`        varchar(32)  NULL     DEFAULT NULL COMMENT '菜单图标',
    `component`   varchar(255) NULL     DEFAULT NULL COMMENT '组件路径',
    `type`        tinyint      NOT NULL COMMENT '菜单类型（0-菜单；1-按钮）',
    `visible`     tinyint(1)   NOT NULL DEFAULT 0 COMMENT '菜单状态（0-显示；1-隐藏）',
    `status`      tinyint(1)   NOT NULL DEFAULT 0 COMMENT '菜单状态（0-正常；1-停用）',
    `keep_alive`  tinyint      NOT NULL DEFAULT 0 COMMENT '缓存页面（0-开启；1- 关闭）',
    `sort_order`  int          NOT NULL DEFAULT 0 COMMENT '排序值',
    `create_by`   varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `create_time` datetime(3)  NULL     DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_by`   varchar(64)  NULL     DEFAULT NULL COMMENT '更新人',
    `update_time` datetime(3)  NULL     DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `is_deleted`  int          NULL     DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu`
VALUES (1, 0, '权限管理', '/admin', NULL, NULL, NULL, 0, 0, 0, 0, 1, NULL, '2023-02-17 11:18:51.794', NULL,
        '2023-02-17 11:29:55.070', 0);
INSERT INTO `sys_menu`
VALUES (2, 1, '用户管理', '/admin/user', NULL, NULL, NULL, 0, 0, 0, 0, 0, NULL, '2023-02-17 11:21:24.954', NULL,
        '2023-02-17 11:30:05.591', 0);
INSERT INTO `sys_menu`
VALUES (3, 1, '用户新增', NULL, 'sys:user:add', NULL, NULL, 1, 0, 0, 0, 0, NULL, '2023-02-17 11:30:18.783', NULL,
        '2023-02-17 11:31:05.613', 0);
INSERT INTO `sys_menu`
VALUES (4, 1, '用户修改', NULL, 'sys:user:edit', NULL, NULL, 1, 0, 0, 0, 0, NULL, '2023-02-17 11:31:48.434', NULL,
        '2023-02-17 11:31:48.434', 0);
INSERT INTO `sys_menu`
VALUES (5, 1, '用户删除', NULL, 'sys:user:delete', NULL, NULL, 1, 0, 0, 0, 0, NULL, '2023-02-17 11:32:03.733', NULL,
        '2023-02-17 11:32:03.733', 0);
INSERT INTO `sys_menu`
VALUES (6, 1, '导入导出', NULL, 'sys:user:import:export', NULL, NULL, 1, 0, 0, 0, 0, NULL, '2023-02-17 11:32:43.763',
        NULL, '2023-02-17 11:32:43.763', 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `name`        varchar(64)  NULL DEFAULT NULL COMMENT '角色名称',
    `code`        varchar(64)  NULL DEFAULT NULL COMMENT '角色编码',
    `description` varchar(255) NULL DEFAULT NULL COMMENT '角色描述',
    `status`      tinyint(1)   NULL DEFAULT 0 COMMENT '角色状态（0-正常；1-停用）',
    `create_by`   varchar(64)  NULL DEFAULT NULL COMMENT '创建人',
    `create_time` datetime(3)  NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_by`   varchar(64)  NULL DEFAULT NULL COMMENT '更新人',
    `update_time` datetime(3)  NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `is_deleted`  int          NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role`
VALUES (1, '管理员', 'ADMIN', '拥有一切权限', 0, NULL, '2023-02-17 11:12:28.781', NULL, '2023-02-17 11:14:35.122', 0);
INSERT INTO `sys_role`
VALUES (2, '普通用户', 'USER', '拥有普通权限', 0, NULL, '2023-02-17 11:14:25.273', NULL, '2023-02-17 11:14:37.885', 0);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `id`      int    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id` bigint NOT NULL COMMENT '角色编号',
    `menu_id` bigint NULL DEFAULT NULL COMMENT '菜单编号',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色菜单表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu`
VALUES (1, 1, 1);
INSERT INTO `sys_role_menu`
VALUES (2, 1, 2);
INSERT INTO `sys_role_menu`
VALUES (3, 1, 3);
INSERT INTO `sys_role_menu`
VALUES (4, 1, 4);
INSERT INTO `sys_role_menu`
VALUES (5, 1, 5);
INSERT INTO `sys_role_menu`
VALUES (6, 1, 6);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    varchar(64)  NOT NULL DEFAULT 'NULL' COMMENT '用户名',
    `nickname`    varchar(64)  NOT NULL DEFAULT 'NULL' COMMENT '昵称',
    `password`    varchar(64)  NOT NULL DEFAULT 'NULL' COMMENT '密码',
    `email`       varchar(64)  NULL     DEFAULT NULL COMMENT '邮箱',
    `phone`       char(11)     NULL     DEFAULT NULL COMMENT '手机号',
    `gender`      tinyint(1)   NULL     DEFAULT NULL COMMENT '用户性别（0-男；1-女；2-未知）',
    `avatar`      varchar(128) NULL     DEFAULT NULL COMMENT '头像',
    `status`      tinyint(1)   NULL     DEFAULT 0 COMMENT '账号状态（0-正常；1-停用）',
    `create_by`   varchar(64)  NULL     DEFAULT NULL COMMENT '创建人',
    `create_time` datetime(3)  NULL     DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_by`   varchar(64)  NULL     DEFAULT NULL COMMENT '更新人',
    `update_time` datetime(3)  NULL     DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    `is_deleted`  int          NULL     DEFAULT 0 COMMENT '删除标志（0-未删除；时间戳-已删除）',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user`
VALUES (1, 'admin', '威廉王子', '$2a$10$SD/NeFToLnnpWky0S4KFZuzkUKA1WZt3YZh8KDqlr2pPHcvyBlPSu', NULL, NULL, 1, NULL, 0,
        NULL, '2022-10-19 17:32:33.184', NULL, '2023-02-17 15:08:01.639', 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `id`      int    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` bigint NOT NULL COMMENT '用户编号',
    `role_id` bigint NOT NULL COMMENT '角色编号',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role`
VALUES (1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
