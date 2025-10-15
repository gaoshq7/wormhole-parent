# Wormhole 多模块项目

这是一个基于Java 8和Maven的多模块项目，包含5个核心模块。

## 项目结构

```
wormhole-parent/
├── pom.xml                 # 父级POM文件
├── w-common/              # 公共组件模块
│   ├── pom.xml
│   └── src/
├── w-protocol/            # 协议定义模块
│   ├── pom.xml
│   └── src/
├── w-client/              # 客户端模块
│   ├── pom.xml
│   └── src/
├── w-executor/            # 执行器模块
│   ├── pom.xml
│   └── src/
└── w-audit/               # 审计模块
    ├── pom.xml
    └── src/
```

## 模块说明

### w-common (公共组件)
- 提供通用的工具类和公共功能
- 包含JSON处理、字符串工具等
- 被其他模块依赖

### w-protocol (协议定义)
- 定义消息协议和数据结构
- 包含RequestMessage、ResponseMessage等
- 依赖w-common模块

### w-client (客户端)
- 提供客户端SDK
- 支持同步和异步请求
- 依赖w-common和w-protocol模块

### w-executor (执行器)
- 基于Spring Boot的Web服务
- 提供命令执行功能
- 依赖w-common和w-protocol模块

### w-audit (审计)
- 提供审计日志功能
- 基于Spring Boot和JPA
- 支持审计日志的存储和查询
- 依赖w-common和w-protocol模块

## 技术栈

- **Java版本**: JDK 8
- **构建工具**: Maven 3.x
- **框架**: Spring Boot 2.7.18
- **数据库**: H2 (测试环境)
- **日志**: SLF4J + Logback
- **测试**: JUnit 4 + Mockito

## 快速开始

### 1. 编译项目
```bash
mvn clean compile
```

### 2. 运行测试
```bash
mvn test
```

### 3. 打包项目
```bash
mvn clean package
```

### 4. 运行执行器服务
```bash
cd w-executor
mvn spring-boot:run
```

### 5. 运行审计服务
```bash
cd w-audit
mvn spring-boot:run
```

## 使用示例

### 客户端使用示例
```java
// 创建客户端
WormholeClient client = new WormholeClient("http://localhost:8080");

// 同步执行命令
ResponseMessage response = client.execute("echo 'Hello World'");

// 异步执行命令
CompletableFuture<ResponseMessage> future = client.executeAsync("ls -la");

// 关闭客户端
client.close();
```

### 审计服务使用示例
```java
@Autowired
private AuditService auditService;

// 记录审计日志
auditService.logAudit("user001", "execute", "command", "req-001", 
                     "127.0.0.1", "browser", "SUCCESS", "SUCCESS", 
                     100L, "command executed successfully");

// 查询审计日志
List<AuditLog> logs = auditService.queryAuditLogs("user001", null, null, null);
```

## 开发说明

1. **模块依赖关系**: w-protocol → w-common, w-client → w-protocol + w-common, w-executor → w-protocol + w-common, w-audit → w-protocol + w-common

2. **版本管理**: 所有模块版本在父POM中统一管理

3. **测试**: 每个模块都包含相应的单元测试

4. **配置**: Spring Boot应用可以通过application.properties进行配置

## 扩展建议

1. 添加配置文件支持
2. 集成Redis缓存
3. 添加安全认证
4. 集成监控和指标收集
5. 添加API文档(Swagger)
6. 支持Docker容器化部署
