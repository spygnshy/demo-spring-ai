# 🤖 Spring AI + Ollama 流式对话 Demo

一个基于 Spring Boot + Spring AI 构建的本地大模型对话应用，支持与 Ollama 部署的模型进行实时流式交互。

---

## ✨ 项目亮点
- 🔌 无缝集成 Ollama 本地大模型，保护数据隐私
- 📡 流式响应：打字机效果实时返回 AI 回复
- ☕ 纯 Java 后端实现，无需额外前端依赖
- 🚀 开箱即用，快速验证本地 AI 服务能力

---

## 🛠️ 技术栈
- **后端框架**: Spring Boot 3.x
- **AI 集成**: Spring AI
- **本地模型**: Ollama
- **构建工具**: Maven
- **JDK 版本**: 17+

---

## 🚀 快速启动

### 1. 前置准备
1.  安装并启动 Ollama
    ```bash
    # 以 Llama3 模型为例
    ollama pull llama3
    ollama run llama3
2.克隆项目
git clone https://github.com/spygnshy/demo-spring-ai.git
cd demo-spring-ai

配置文件修改
在 application.yml 中配置你的 Ollama 地址和模型
spring:
ai:
ollama:
base-url: http://localhost:11434
chat:
options:
model: llama3

启动应用
./mvnw spring-boot:run

接口示例
流式对话接口
http
GET /api/chat/stream?message=你好，请介绍一下自己

响应效果：
以 text/event-stream 格式返回数据
前端可直接实现打字机式实时渲染

项目结构
demo-spring-ai/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── controller/  # 接口层
│   │   │   ├── service/     # 业务逻辑层
│   │   │   └── DemoApplication.java
│   │   └── resources/
│   │       └── application.yml  # 配置文件
├── pom.xml
└── README.md


后续扩展
支持多模型切换
增加对话历史上下文
接入 Web 前端实现可视化界面
plaintext

---

### 使用方法：
1.  复制上面的全部内容
2.  回到你的 GitHub 仓库页面，点击 `Add file` → `Create new file`
3.  文件名填 `README.md`
4.  粘贴内容，拉到最下面点 `Commit new file`
